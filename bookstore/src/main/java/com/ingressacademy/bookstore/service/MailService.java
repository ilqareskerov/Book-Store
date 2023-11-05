package com.ingressacademy.bookstore.service;

import com.ingressacademy.bookstore.dto.Mail;
import com.ingressacademy.bookstore.dto.MailDetailsDto;
import com.ingressacademy.bookstore.model.Student;
import com.ingressacademy.bookstore.repo.MailRepository;
import com.ingressacademy.bookstore.model.Author;
import com.ingressacademy.bookstore.model.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import java.util.Properties;

@Service
public class MailService {
    private final AuthorService authorService;
    @Value("${EMAIL_ID}")
    private String emailId;

    @Value("${EMAIL_PASSWORD}")
    private String password;
    @Autowired
    private JavaMailSender mailSender;
    private final MailRepository emailRepository;

    public MailService(AuthorService authorService, MailRepository emailRepository) {
        this.authorService = authorService;
        this.emailRepository = emailRepository;
    }

    public void sendMail(Mail mail, Author author) {
        for (Student student : author.getSubscribedStudents()) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom("ilqar.eskerov@mail.ru");
            mailMessage.setTo("eskerov.2002@mail.ru");
            mailMessage.setSubject(mail.getSubject());
            mailMessage.setText(mail.getMessage());

            mailSender.send(mailMessage);
        }
    }

    public void addMailToAuthor(String username , MailDetailsDto emailDetailsDto) {
        Author author = authorService.getAuthorByEmail(username);
        EmailDetails mail = new EmailDetails();
        mail.setAuthor(author);
        author.setEmailDetails(mail);
        emailRepository.save(mail);
        authorService.saveAuthor(author);
    }


    public JavaMailSender getJavaMailSender(String emailUser, String password) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailUser);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
