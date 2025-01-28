package com.example.pythonapp.service;
import com.example.pythonapp.model.UserEntity;
import com.example.pythonapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.util.Pair;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.lang.Math;
import java.util.Properties;
import com.example.pythonapp.dto.VerificationRequest;
import java.util.Random;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    private List<Pair<String,String>> EmailCode = new ArrayList<>();

    @Override
    public Optional<UserEntity> findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<UserEntity> findByName(String name)
    {
        return userRepository.findByName(name);
    }
    @Override
    public void updateUser(String email,String password)
    {
        userRepository.updateByEmail(email, password);
    }

    /**
     *
     * @param email - email of User for who have to be generated code
     * @throws Exception
     */
    @Override
    public void generateCode(String email) throws Exception{  
        
        int code = 0;
        
        for(int i=0; i<6; i++) {
            
            Random generator = new Random();
            code += Math.abs(generator.nextInt())*Math.pow(10,i);
        }
    

            String host = "smtp.gmail.com";  
            final String username = "alicja.zosia.k@gmail.com"; 
            final String password = "ogrl kvbf utms njjs"; 

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

    /**
     * @param data - data to verify - email and code
     * @return boolean if data are correct
     */
    @Override
        public boolean codeVerify(VerificationRequest data)
        {
           Pair<String,String> emailFounded = EmailCode.stream()
                .filter(pair -> data.email.equals(pair.getKey()))
                .findAny()
                .orElse(null);
    
            if(emailFounded.getValue().equals(data.code))
            {
                EmailCode.remove(emailFounded);
                return true;
            }
           
            return false;
            
        }


    
}