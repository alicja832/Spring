package com.example.pythonapp.controller;
import com.example.pythonapp.dto.VerificationRequest;
import com.example.pythonapp.mapper.UserMapper;
import com.example.pythonapp.model.*;
import com.example.pythonapp.model.enums.Role;
import com.example.pythonapp.service.*;
import com.example.pythonapp.dto.UserCreationDto;
import com.example.pythonapp.PythonApplication;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.util.Pair;
import com.example.pythonapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;
import java.lang.Math;
import java.util.*;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JWTToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;


@RestController
@CrossOrigin (origins=PythonApplication.frontend,allowCredentials = "true",maxAge = 36000)
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
  
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private JWTToken jwt;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ExerciseService exerciseService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserMapper userMapper = new UserMapper();
   
  
    /**
     * Dodawanie nowego uzytkownika
     */
    @PostMapping("/")
    public ResponseEntity<String> add(@RequestBody UserCreationDto userCreationDto){

        userCreationDto.setPassword( encoder.encode(userCreationDto.getPassword()));
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);

        if(userService.findByEmail(userCreationDto.getEmail()).isPresent())
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik o podanym adresie email istnieje");
        }

        if(userService.findByName(userCreationDto.getName()).isPresent())
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik o podanym loginie istnieje");
        }

        UserEntity createdEntity = userMapper.createDto(userCreationDto);
        if(createdEntity instanceof Teacher) teacherService.save((Teacher) createdEntity);
        if(createdEntity instanceof Student) studentService.save((Student) createdEntity);
        return responseEntity;
    }

    /**
     * podaje informacje o użytkowniku
     * @return
     */
    @GetMapping("/")
    public List<UserEntity> getUserInformation(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userService.findByName(name).orElseThrow(UserNotFoundException::new);
        if(userService.findByName(name).isPresent())
            return List.of(userService.findByName(name).get());
        else if(studentService.findByName(name).isPresent())
            return List.of(userService.findByName(name).get());
        return List.of(userEntity);
    }
    /**
     * zmiana hasła
     */
    @PutMapping("/changepassword")
    public ResponseEntity<String> changePassword(@RequestBody VerificationRequest request)
    {
        userService.updateUser(request.email,encoder.encode(request.password));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
   /**
    * metoda generująca kod do zmiany hasła 
    * zostaje wysłana na podany adres  
   */
    @PostMapping("/code")
    public ResponseEntity<String> getCode(@RequestBody String email)
    {

        if(userService.findByEmail(email).isEmpty())
        {
            return new ResponseEntity<String>("Nieprawidlowy adres email",HttpStatus.NOT_FOUND);
        }
        try{
            userService.generateCode(email);
        }
        catch (Exception exception)
        {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @GetMapping("/token")
    public JWTResponse GetNewToken(@CookieValue( name = "refreshToken") String refreshToken)
    {
        String userName="";

        try{
            
            userName = jwt.getUsernameFromToken(refreshToken);
           
        }catch(Exception ex)
        {
            throw new UserNotFoundException();
        }
      
        if (userName!=null) {
            UserEntity userEntity = userService.findByName(userName).orElseThrow(UserNotFoundException::new);
            String role = teacherService.findByName(userName).isPresent() ? "ROLE_"+ Role.TEACHER : "ROLE_"+Role.STUDENT;
            UserDetails userDetails
                    = new User(userEntity.getName(),userEntity.getPassword(), List.of(new SimpleGrantedAuthority(role))) ;
            if (jwt.validateToken(refreshToken, userDetails)) {
               
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        userEntity.getPassword(), List.of(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            else
            {
                throw new UserNotFoundException();
            }
        }
    
      
        return generateNewToken();
    }
    /**
    * weryfikacja kodu do zmiany hasła
    */
    @PostMapping("/codeverification")
    public ResponseEntity<String> codeVerification(@RequestBody VerificationRequest data)
    {
        return userService.codeVerify(data) ? new ResponseEntity<>("Prawidłowy Kod",HttpStatus.OK) : new ResponseEntity<>("Nieprawidłowy kod",HttpStatus.BAD_REQUEST);
    }

    /**
    * ranking użytkowników
    */
    @GetMapping("/ranking")
    public List<Student> getStudentRanking()
    {
        List<Student> list = studentService.listStudents();
        list.sort((Student a,Student b)->b.getScore()-a.getScore());
        return list;
    }

    /**
    * lista zadan stworzonych przez zalogowanego nauczyciela
    */
    @GetMapping("/exercises")
    public List<Map<String,String>> getTeacherExercises(){

        Teacher loginTeacher = teacherService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(UserNotFoundException::new);
        return solutionService.getAllTeacherExercises(loginTeacher);
    }
    /**
     * function which describe the position of student in ranking
     */
    @GetMapping("/position")
    public Pair<Integer,Integer> getStudentPosition(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Student loginStudent = studentService.findByName(name).get();
        List<Student> ranking = getStudentRanking();

        return new Pair<Integer,Integer>(ranking.indexOf(loginStudent),ranking.size());
    }
   
   /**
     * @return
     * @throws Exception
     * autentykacja uzytkownika, generowanie tokenu
     */
   @PostMapping("/authenticate")
   public JWTResponse authenticate(@RequestBody UserEntity userEntity){

        String name, password;
       if(userEntity!=null) {
           name = userEntity.getName();
           UserEntity findUser = userService.findByName(userEntity.getName()).orElseThrow(UserNotFoundException::new);
           password = userEntity.getPassword();
           userEntity.getPassword();
           if (!encoder.matches(password, findUser.getPassword())) {
               throw new UserNotFoundException();
           }
           try {
               authenticationManager.authenticate(
                       new UsernamePasswordAuthenticationToken(userEntity.getName(), userEntity.getPassword())
               );
           } catch (BadCredentialsException e) {
               throw new UserNotFoundException();
           } catch (Exception ex) {
               System.out.println(ex.getMessage());
           }
       }
        else{
            name = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(name);
            password = userService.findByName(name).get().getPassword();
            System.out.println(password);
       }
       String role = teacherService.findByName(name).isPresent() ? "ROLE_"+Role.TEACHER : "ROLE_"+Role.STUDENT;
       UserDetails userDetails = new User(name, password, List.of(new SimpleGrantedAuthority(role)));
       String token = jwt.generateToken(userDetails);
        return new JWTResponse(token,jwt.getExpirationDateFromToken(token));

   }
    /**
     * @return Response Entity with HttpOnlyCookie
     * generowanie refresh tokenu
     */
    @GetMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken() {

        String refreshToken=""; 
        UserDetails userDetails;
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userService.findByName(name).orElseThrow(UserNotFoundException::new);
        String role;
        try{
            
            role = teacherService.findByName(name).isPresent() ? "ROLE_"+Role.TEACHER : "ROLE_"+Role.STUDENT;
            userDetails = new User(name, userEntity.getPassword(), List.of(new SimpleGrantedAuthority(role)));
            refreshToken = jwt.generateRefreshToken(userDetails);
        
        }catch(Exception exception)
        {
            System.out.println(exception.getMessage());
        }
        
        ResponseCookie springCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(90000)
                .domain("pythfront.azurewebsites.net")
                .build();   

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .build();
    }


    public JWTResponse generateNewToken(){
        return authenticate(null);
    }


}
