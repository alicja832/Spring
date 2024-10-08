package com.example.demoggggg.controller;
import com.example.demoggggg.dto.VerificationRequest;
import com.example.demoggggg.model.*;
import com.example.demoggggg.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import javax.naming.InitialContext;
import java.lang.Math;
import java.sql.SQLException;
import java.util.*;

//import com.example.demoggggg.jwt.JWTRequest;
//import com.example.demoggggg.jwt.JWTResponse;
//import com.example.demoggggg.jwt.MyJwt;
//
//import com.example.demoggggg.config.SecurityConfiguration.*;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import com.example.demoggggg.model.enums.RoleEnum;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.User;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    private List<Pair<String,String>> EmailCode;
//    @Autowired
//    private MyJwt jwt;
//    @Autowired
//    private AuthenticationManager authenticationManager;

    @PostMapping("/teacher")
    public ResponseEntity<String> add(@RequestBody Teacher teacher){

        teacher.setRole("TEACHER");
        Teacher savedTeacher = userService.findTeacherByEmail(teacher.getEmail());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);

        if(savedTeacher!=null)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik o podanym adresie email istnieje");
        }
        savedTeacher = userService.findTeacherByName(teacher.getName());

        if(savedTeacher!=null)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik o podanym loginie istnieje");
        }
        userService.saveTeacher(teacher);
        return responseEntity;
    }

    @PostMapping("/student")
    public ResponseEntity<String> add(@RequestBody Student student){

        student.setRole("STUDENT");
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
        if(userService.findbyEmail(email) == null)
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
            return  new ResponseEntity<String>(HttpStatus.CONFLICT);
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
//    @PostMapping("/authenticate")
//    public JWTResponse authenticate(@RequestBody UserEntity userEntity)throws Exception{
//        String token = null;
//
//
//
//        String password = userEntity.getPassword();
//        String name = userEntity.getName();
//        System.out.println(name);
//        System.out.println(password);
//        UserEntity entity = userService.findUserByNameAndPassword(name,password);
//        if(entity!=null)
//            System.out.println(entity.getRole());
//        UserDetails userDetails=new User(name,password,List.of(new SimpleGrantedAuthority("ROLE_" +entity.getRole()))) ;
//        token = jwt.generateToken(userDetails);
//
//        try {
//            System.out.println(userDetails.getAuthorities());
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(userEntity.getName(), userEntity.getPassword())
//            );
//
//            System.out.println("Authentication successful: " + authentication.getName());
//        }
//        catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//        catch(Exception ex)
//        {
//            System.out.println(ex.getMessage());
//        }
//        System.out.println("OK");
//        return new JWTResponse(token);
//    }

    @GetMapping("/teacher/{email}")
    public List<Teacher> getTeacher(@PathVariable String email){

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

    @PostMapping("/login")
    //  @Operation(summary = "Login based on user role after authentication", security = @SecurityRequirement(name = "bearerAuth"))
    public String login(@RequestBody UserEntity user) {

        System.out.println(user.getPassword());
        UserEntity userByUsername = this.userService.findByName(user.getName());

        switch(userByUsername.getRole()) {
            case "STUDENT":
                return "STUDENT";
            case "TEACHER":
                return "TEACHER";
            case "ADMIN":
                return "ADMIN";
        }
        return null;
    }
}
