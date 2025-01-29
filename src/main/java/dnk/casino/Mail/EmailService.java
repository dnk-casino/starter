package dnk.casino.Mail;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio de correo electrónico para enviar mensajes a los usuarios.
 * 
 * @author Danikileitor
 */
@Service
public class EmailService {

    /**
     * Servicio de correo electrónico de JavaMail.
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Envía un correo electrónico simple a un destinatario.
     * 
     * @param destinatario el correo electrónico del destinatario
     * @param asunto       el asunto del correo electrónico
     * @param mensaje      el mensaje del correo electrónico
     */
    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(System.getenv().get("MAIL_USERNAME"));
        mailMessage.setTo(destinatario);
        mailMessage.setSubject(asunto);
        mailMessage.setText(mensaje);

        javaMailSender.send(mailMessage);
    }

    /**
     * Envía un correo electrónico con un adjunto a un destinatario.
     * 
     * @param destinatario el correo electrónico del destinatario
     * @param asunto       el asunto del correo electrónico
     * @param mensaje      el mensaje del correo electrónico
     * @param adjunto      la ruta del archivo adjunto
     */
    public void enviarCorreo(String destinatario, String asunto, String mensaje, String adjunto) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(System.getenv().get("MAIL_USERNAME"));
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(mensaje);
            FileSystemResource file = new FileSystemResource(new File(adjunto));
            helper.addAttachment(file.getFilename(), file);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
