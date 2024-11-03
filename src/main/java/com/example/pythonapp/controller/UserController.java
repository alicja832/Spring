package com.example.pythonapp.controller;
import com.example.pythonapp.dto.VerificationRequest;
import com.example.pythonapp.model.*;
import com.example.pythonapp.service.UserService;
import com.sun.net.httpserver.Authenticator;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.util.Pair;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.naming.InitialContext;
import java.lang.Math;
import java.sql.SQLException;
import java.util.*;
import com.example.pythonapp.jwt.JWTRequest;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JwtToken;
import com.example.pythonapp.config.SecurityConfiguration.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.pythonapp.model.enums.RoleEnum;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;


@RestController
@CrossOrigin(origins = "http://localhost:3000",maxAge=360000)
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    private List<Pair<String,String>> EmailCode;
    @Autowired
    private JwtToken jwt;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

  
    @PostMapping("/")
    public ResponseEntity<String> add(@RequestBody UserEntity userEntity){


        userEntity.setPassword( encoder.encode(userEntity.getPassword()));
        UserEntity savedUser = userService.findByEmail(userEntity.getEmail());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);

        if(savedUser!=null)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik o podanym adresie email istnieje");
        }
        savedUser = userService.findByName(userEntity.getName());

        if(savedUser!=null)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik o podanym loginie istnieje");
        }
        if(userEntity.getRole().equals("TEACHER")) {
            Teacher teacher = new Teacher(userEntity);
            userService.saveTeacher(teacher);
        }
        if(userEntity.getRole().equals("STUDENT")) {
            Teacher teacher = new Teacher(userEntity);
                userService.saveTeacher(teacher);
        }
        return responseEntity;
    }
 
    @PostMapping("/student")
    public ResponseEntity<String> add(@RequestBody Student student){

        student.setRole("STUDENT");
        student.setPassword( encoder.encode(student.getPassword()));

        Student savedStudent = userService.findStudentByEmail(student.getEmail());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);

        if(savedStudent!=null)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik o podanym adresie email istnieje");
        }
        savedStudent = userService.findStudentByEmail(student.getName());

        if(savedStudent!=null)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik o podanym loginie istnieje");
        }
        userService.saveStudent(student);
        return responseEntity;
    }
 
    @PutMapping("/changePassword/")
    public synchronized ResponseEntity<String> changePassword(@RequestBody VerificationRequest request)
    {
        userService.updateUser(request.email,request.password);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
  
    @PostMapping("/remindPassword/")
    public synchronized ResponseEntity<String> getCode(@RequestBody String email)
    {
        if(userService.findByEmail(email) == null)
        {
            return new ResponseEntity<String>("Nieprawidlowy adres email",HttpStatus.NOT_FOUND);
        }
        Random generator = new Random();
        int code = 0;
        for(int i=0; i<6; i++) {
            code += generator.nextInt()*Math.pow(10,i);
        }

        try {

            String host = "smtp.gmail.com";  
            final String username = "alicja.zosia.k@gmail.com"; 
            final String password = "uurc jbbw dqeo uvmp"; 

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("alicja.zosia.k@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your Passcode");

            String msg = "Twój kod to:\n" +
                    "\n" +
                    code +
                    "." +
                    "\n" +
                    "Ten email jest odpowiedzią na prośbę o odzyskanie zapomnianego hasła. Możesz zignorować tę wiadomość, jeśli nie złożyłeś takiej prośby.\n" +
                    "Użyj tego kodu, aby zresetować swoje hasło.\n" +
                    "Nie odpowiadaj na tę wiadomość.";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            if(EmailCode==null)
                EmailCode = new ArrayList<>();

            EmailCode.add(new Pair<>(email,Integer.toString(code)));
        }
        catch (Exception exception)
        {
            return  new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }


    @PostMapping("/CodeVerification")
    public ResponseEntity<String> codeVerification(@RequestBody VerificationRequest data)
    {
        Pair<String,String> pairfounded = EmailCode.stream()
                .filter(pair -> data.email.equals(pair.getKey()))
                .findAny()
                .orElse(null);
        if(pairfounded == null)
        {
            return new ResponseEntity<>("Nieprawidłowy email",HttpStatus.BAD_REQUEST);
        }
        if(pairfounded.getValue().equals(data.code))
        {
            EmailCode.remove(pairfounded);
            System.out.println(EmailCode.size());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Nieprawidłowy kod",HttpStatus.BAD_REQUEST);
    }
  
    @GetMapping("/ranking")
    public List<Student> getStudentRanking()
    {
        List<Student> list = userService.listStudents();
        list.sort((Student a,Student b)->a.getScore()-b.getScore());
        System.out.println(list);
        return list;
    }
    @GetMapping("/exercises/{email}")
    public List<Exercise> getExercises(@PathVariable String email){

        Teacher loginTeacher = userService.findTeacherByEmail(email);
        if(loginTeacher!=null) {
            loginTeacher = userService.findTeacherByName(loginTeacher.getName());
            System.out.printf(loginTeacher.getExercises().toString());
            return loginTeacher.getExercises();

        }
        return new ArrayList<>();
    }
    /**
     * Narazie niech bedzie tak
     * @return
     * @throws Exception
     */
   @PostMapping("/authenticate")
   public JWTResponse authenticate(@RequestBody UserEntity userEntity)throws Exception{
       String token = null;
       String password = userEntity.getPassword();
       String name = userEntity.getName();
       System.out.println(name);
       System.out.println(password);
       UserEntity entity = userService.findByName(name);
       if(entity!=null)
           System.out.println(entity.getRole());
           UserDetails userDetails=new User(name,password,List.of(new SimpleGrantedAuthority("ROLE_" +entity.getRole()))) ;
           token = jwt.generateToken(userDetails);

       try {
           System.out.println(userDetails.getAuthorities());
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(userEntity.getName(), userEntity.getPassword())
           );

           System.out.println("Authentication successful: " + authentication.getName());
       }
       catch (BadCredentialsException e) {
           throw new Exception("INVALID_CREDENTIALS", e);
       }
       catch(Exception ex)
       {
           System.out.println(ex.getMessage());
       }
       System.out.println("OK");
       return new JWTResponse(token);
   }

    @GetMapping("/teacher/{email}")
    public List<Teacher> getTeacher(@PathVariable String email){
        System.out.println(userService.findTeacherByEmail(email));
        return List.of(userService.findTeacherByEmail(email));
    }
    
    @GetMapping("/student/{email}")
    public List<Student> getStudent(@PathVariable String email){
        
        return List.of(userService.findStudentByEmail(email));

    }
    @GetMapping("/users")
    public List<UserEntity> list(){

        return userService.listAll();
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Long> delete(@PathVariable long id){

        long deleted = userService.deleteUser(id);
        return ResponseEntity.ok(deleted);
    }
}
