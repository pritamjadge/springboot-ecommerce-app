package com.project.ecommerce.utility;

import com.project.ecommerce.models.ConfirmationToken;
import com.project.ecommerce.models.User;
import com.project.ecommerce.repository.ConfirmationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserVerificationUtility {

    private static final Logger logger = LoggerFactory.getLogger(UserVerificationUtility.class);

    private static final String MAIL_SUBJECT = "Complete Registration!";
    private static String MAIL_TEXT = "To confirm your account, please click here : " + "http://localhost:8080/api/auth/confirm-account?token=";


    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public UserVerificationUtility(ConfirmationTokenRepository confirmationTokenRepository, JavaMailSender javaMailSender) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.javaMailSender = javaMailSender;
    }

    public String emailTokenConfirmation(User user) { //throws MailException

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        String mailText = "To confirm your account, please click here: " + "http://ecom-app-env.eba-zyk4rrdx.ap-south-1.elasticbeanstalk.com/ecommerce-app/api/auth/confirm-account?token=" + confirmationToken.getConfirmationToken();
        logger.info("mailText {} :" + mailText);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(MAIL_SUBJECT);
        mailMessage.setText(mailText);

        logger.info("mailMessage {} :" + mailMessage.toString());

        javaMailSender.send(mailMessage);
        logger.info("Confirmation Token {} :" + confirmationToken.getConfirmationToken());
        return "Verify email by the link sent on your email address";

    }


}
