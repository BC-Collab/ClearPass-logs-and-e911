import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class sendHTMLemail 
{
    public static void main(String[] args) throws Exception
    {

        if ( args.length < 3 )
        {
           System.out.println( "Error:" );
           System.out.println( "Usage:" );
           System.out.println( "java sendmail <from> <to> <subject> <file>" );
           System.exit( 0 );
        }
        // Replace with your sender email address
        String from = args[ 0 ];
        // Replace with your recipient email address
        String to = args[ 1 ];
        // Email subject
        String subject = args[ 2 ];
        // file
        String messagefile = args[ 3 ];

        // HTML formatted email content
        String body = new String( "" );

        try
        {
          BufferedReader emailfile = new BufferedReader( new InputStreamReader( new FileInputStream( messagefile )));
          String linevar = emailfile.readLine();
          while ( linevar != null )
          {
            body += linevar;
            linevar = emailfile.readLine();
          }
          emailfile.close();
        }
        catch ( FileNotFoundException fnfe ) { fnfe.printStackTrace(); }

        // Get system properties
        Properties props = new Properties();
//        Properties props = System.getProperties();

        // Set mail server properties
        props.put("mail.smtp.host", "relay.bc.edu"); // Replace with your mail server

        // Get a session object
        Session session = Session.getInstance( props, null );

        try {
            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set from address
            message.setFrom(new InternetAddress(from));

            // Set recipient address
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set email subject
            message.setSubject(subject);

            // Set email content with HTML format
            message.setContent(body, "text/html; charset=utf-8");

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

