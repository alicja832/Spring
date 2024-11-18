package com.example.pythonapp.controller;
import com.example.pythonapp.dto.VerificationRequest;
import com.example.pythonapp.mapper.UserMapper;
import com.example.pythonapp.model.*;
import com.example.pythonapp.model.enums.Role;
import com.example.pythonapp.service.SolutionService;
import com.example.pythonapp.service.StudentService;
import com.example.pythonapp.service.TeacherService;
import com.example.pythonapp.service.UserService;
import com.example.pythonapp.dto.UserCreationDto;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.util.Pair;
import com.example.pythonapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.lang.Math;
import java.util.*;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JWTToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;


@RestController
@CrossOrigin(origins = "http://localhost:3000",maxAge=360000)
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    private List<Pair<String,String>> EmailCode = new ArrayList<>();
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private JWTToken jwt;
    @Autowired
    private AuthenticationManager authenticationManager;
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
    
    @GetMapping("/")
    public List<UserEntity> getUser(){
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
    @PutMapping("/changePassword/")
    public synchronized ResponseEntity<String> changePassword(@RequestBody VerificationRequest request)
    {
        userService.updateUser(request.email,request.password);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
   /**
    * metoda generująca kod do zmiany hasła 
    * zostaje wysłana na podany adres  
   */
    @PostMapping("/code")
    public synchronized ResponseEntity<String> getCode(@RequestBody String email)
    {
        if(userService.findByEmail(email).isEmpty())
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

            EmailCode.add(new Pair<>(email,Integer.toString(code)));
        }
        catch (Exception exception)
        {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }
    
    /**
    * weryfikacja kodu do zmiany hasła
    */
    @PostMapping("/CodeVerification")
    public ResponseEntity<String> codeVerification(@RequestBody VerificationRequest data)
    {
        Pair<String,String> emailFounded = EmailCode.stream()
                .filter(pair -> data.email.equals(pair.getKey()))
                .findAny()
                .orElse(null);
        if(emailFounded==null)
        {
            return new ResponseEntity<>("Nieprawidłowy email",HttpStatus.BAD_REQUEST);
        }
        if(emailFounded.getValue().equals(data.code))
        {
            EmailCode.remove(emailFounded);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Nieprawidłowy kod",HttpStatus.BAD_REQUEST);
    }

    /**
    * ranking użytkowników
    */
    @GetMapping("/ranking")
    public List<Student> getStudentRanking()
    {
        List<Student> list = studentService.listStudents();
        list.sort((Student a,Student b)->a.getScore()-b.getScore());
        return list;
    }

    /**
    * lista zadan stworzonych przez zalogowanego nauczyciela
    */
    @GetMapping("/exercises")
    public List<Map<String,String>> getExercises(){

        Teacher loginTeacher = teacherService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(UserNotFoundException::new);
        List<Exercise> exercises = loginTeacher.getExercises();
        List<Map<String, String>> exercisesWithInfo = new ArrayList<>();

            for (Exercise exercise : exercises) {
                Map<Integer, Integer> withScores = new HashMap<>();
                List<Solution> listSolutions = solutionService.getAllSolutionsByExercise(exercise);
                for (Solution solution : listSolutions) {
                    int score = solution.getScore();
                    if (!withScores.containsKey(score))
                        withScores.put(score, 1);
                    else
                        withScores.replace(score, withScores.get(score) + 1);
                }
                int mostpopularScore = 0;
                int scorecount = 0;
                Iterator<Map.Entry<Integer, Integer>> it = withScores.entrySet().iterator();
                
                while (it.hasNext()) {
                    Map.Entry<Integer, Integer> element = it.next();
                    if (element.getValue() > scorecount) {
                        mostpopularScore = element.getKey();
                        scorecount = element.getValue();
                    }
                }
                
                Map<String,String> info = new HashMap<>();
                info.put("id",Integer.toString(exercise.getId()));
                info.put("name",exercise.getName());
                info.put("content",exercise.getContent());
                info.put("introduction",exercise.getIntroduction());
                info.put("maxPoints",Integer.toString(exercise.getMaxPoints()));
                info.put("score",Integer.toString(mostpopularScore));
                info.put("quantity",Integer.toString(scorecount));
                if(exercise instanceof LongExercise) 
                	info.put("correctSolution",((LongExercise)exercise).getCorrectSolution());
                if(exercise instanceof ShortExercise) 
                {	
                	info.put("firstOption",((ShortExercise)exercise).getFirstOption());
                	info.put("secondOption",((ShortExercise)exercise).getSecondOption());
                	info.put("thirdOption",((ShortExercise)exercise).getThirdOption());
                	info.put("fourthOption",((ShortExercise)exercise).getFourthOption());
                	info.put("correctAnswer",Character.toString(((ShortExercise)exercise).getCorrectAnswer()));
                }
               
                exercisesWithInfo.add(info);
            }

        return exercisesWithInfo;
    }
    /**
     * function which describe the position of student in ranking
     */
    @GetMapping("/position")
    public Pair<Integer,Integer> studentPosition(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Student loginStudent = studentService.findByName(name).get();
        List<Student> ranking = getStudentRanking();

        return new Pair<Integer,Integer>(ranking.indexOf(loginStudent),ranking.size());
    }
    /**
     * @return
     * @throws Exception
     * autentykacja uzytkownika, generowanie tokenu oraz refresh-tokenu
     */
   @PostMapping("/authenticate")
   public JWTResponse authenticate(@RequestBody UserEntity userEntity)throws Exception{

     return primaryAuthentication(userEntity,true);

   }
    /**
     * @return
     * @throws Exception
     * generowanie nowego tokenu
     */
    @GetMapping("/refresh-token")
    public JWTResponse authenticate() throws Exception{

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userService.findByName(userName).orElseThrow(UserNotFoundException::new);
        System.out.println(userEntity.getName());
        return primaryAuthentication(userEntity,false);
    }
    private  JWTResponse primaryAuthentication(UserEntity userEntity,Boolean haveToGenerateRefreshToken)throws RuntimeException
    {
        String refreshToken;
        String token;
        String role;
        UserDetails userDetails;
        UserEntity loginUser;

        String password = userEntity.getPassword();
        String name = userEntity.getName();

        if(haveToGenerateRefreshToken)
        {
             loginUser = userService.findByName(name).orElseThrow(UserNotFoundException::new);
            if(!encoder.matches(password,loginUser.getPassword()))
            {
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
        role = teacherService.findByName(name).isPresent() ? "ROLE_"+Role.TEACHER : "ROLE_"+Role.STUDENT;
        userDetails = new User(name, password, List.of(new SimpleGrantedAuthority(role)));
        token = jwt.generateToken(userDetails);
        refreshToken = haveToGenerateRefreshToken? jwt.generateRefreshToken(userDetails) : null;
       
        return  new JWTResponse(token,refreshToken,jwt.getExpirationDateFromToken(token));
    }

}
