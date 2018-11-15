package utils;

import model.Appointment;
import model.Setting;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.UUID;

public class EmailManager {
    private static EmailManager instance;

    private Session session;
    private String email;

    private DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("yyyyMMddTHHmmssZ");

    private EmailManager(){

    }

    public static synchronized EmailManager getInstance(){
        if(instance==null){
            instance = new EmailManager();
        }

        return instance;
    }

    public void setSession(String host, int port, String user, String email, String password) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);

        this.email = email;

        this.session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
    }

    public String sendAppointmentNotification(Appointment appointment){
        if(appointment.getPatient().getEmail().length()==0){
            return "No se ha definido el email del paciente";
        }

        if(session==null){
            return "No se ha configurado el inicio de sesión de email";
        }

        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(email));
        } catch (Exception e) {
            e.printStackTrace();
            return "Email inválido de origen";
        }

        try {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(appointment.getPatient().getEmail()));
        } catch (Exception e) {
            e.printStackTrace();
            return "Email inválido de paciente";
        }

        try {
            message.setSubject("Recordatorio de cita médica");

            StringBuilder sb = new StringBuilder();
            sb.append("BEGIN:VCALENDAR\n" +
                    "PRODID:-//Airmed//1.0//ES\n" +
                    "VERSION:2.0\n" +
                    "METHOD:REQUEST\n" +
                    "BEGIN:VEVENT\n");
            Setting medicName = Setting.find.byId("medic_name");
            if(medicName==null){
                sb.append("SUMMARY:Consulta el día ").append(appointment.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
                sb.append("DESCRIPTION:Consulta el día ").append(appointment.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
            }
            else {
                sb.append("SUMMARY:Consulta con el Dr.").append(medicName.getValue()).append(" el día ").append(appointment.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
                sb.append("DESCRIPTION:Consulta con el Dr.").append(medicName.getValue()).append(" el día ").append(appointment.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
            }
            Setting medicAddress = Setting.find.byId("medic_address");
            if(medicAddress!=null){
                sb.append("LOCATION:").append(medicAddress.getValue()).append("\n");
            }
            sb.append("UID:Airmed").append(appointment.getId()).append("\n");
            sb.append("ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:").append(appointment.getPatient().getEmail()).append("\n");
            sb.append("ORGANIZER:MAILTO:").append(email).append("\n");
            sb.append("DTSTAMP:").append(dFormatter.format(LocalDateTime.now())).append("\n");
            sb.append("DTSTART:").append(dFormatter.format(appointment.getDateTime())).append("\n");
            sb.append("DTEND:").append(dFormatter.format(appointment.getDateTime().plusMinutes(Long.parseLong(Setting.find.byId("cons_length").getValue())))).append("\n");
            sb.append("TRANSP:OPAQUE\n" +
                    "SEQUENCE:0\n" +
                    "CATEGORIES:APPOINTMENT\n" +
                    "PRIORITY:5\n" +
                    "CLASS:PUBLIC\n" +
                    "BEGIN:VALARM\n" +
                    "TRIGGER;RELATED=START:-PT00H15M00S\n" +
                    "ACTION:DISPLAY\n" +
                    "DESCRIPTION:REMINDER\n" +
                    "END:VALARM\n" +
                    "END:VEVENT\n" +
                    "END:VCALENDAR");

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
            messageBodyPart.setHeader("Content-ID", "calendar_message");
            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(sb.toString(), "text/calendar")));
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            //Send message
            Transport.send(message);
        }
        catch (Exception e){
            e.printStackTrace();
            return "Error";
        }

        return null;
    }
}
