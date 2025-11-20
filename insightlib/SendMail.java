import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * msgmultisendsample creates a simple multipart/mixed message and sends
 * it.  Both body parts are text/plain.
 *
 * usage: java msgmultisendsample to from smtp true|false
 * where to and from are the destination and
 * origin email addresses, respectively, and smtp
 * is the hostname of the machine that has smtp server
 * running.  The last parameter either turns on or turns off
 * debugging during sending.
 */

public class SendMail
{
  
  private static String to = null;
  private static String from = null;
  private static String subject = null;
  private static String smtphost = "relay.bc.edu";
  private static StringBuffer message = new StringBuffer();

  public static void main( String args[] ) throws Exception
  {
    BufferedReader input = null;
    from = args[ 0 ];
    to = args[ 1 ];
    subject = args[ 2 ];
    String messagetext = args[ 3 ];

    try
    {
      input = new BufferedReader( new InputStreamReader( new FileInputStream( messagetext )));
    }
    catch ( FileNotFoundException e ) { input = null; }

    if ( input != null )
    {
      String linevar = input.readLine();
      while ( linevar != null )
      {
        message.append( linevar + "\r\n" );
        linevar = input.readLine();
      }
      input.close();
    }
    else message.append( args[ 3 ] );

    sendMessage();
  }

  public static void sendMessage()
  {
    // create some properties and get the default Session
    Properties props = new Properties();
    props.put( "mail.smtp.class", "smtp" );
    props.put( "mail.smtp.host", smtphost );

    Session session = Session.getDefaultInstance( props, null );

    try 
    {
      // create message
      MimeMessage msg = new MimeMessage( session );
      msg.setFrom( new InternetAddress( from ));
      msg.addRecipients( Message.RecipientType.TO, to );
      msg.setSubject( subject );
      msg.setSentDate( new Date());

      // crate and fill the first message part

      MimeBodyPart mbp1 = new MimeBodyPart();
      mbp1.setText( message.toString());

/*
      // create and fill the second message part
      MimeBodyPart mbp2 = new MimeBodyPart();
      // Use setText( text, charset), to show it off !
      mbp2.setText( msgText2, "us-ascii" );
*/

      // create the Mutlipart and its parts to it
      Multipart mp = new MimeMultipart();
      mp.addBodyPart( mbp1 );
//      mp.addBodyPart( mbp2 );

      // add the Mutlipart to the message
      msg.setContent( mp );

      // send the message
      Transport.send( msg );
    }
    catch ( MessagingException mex )
    {
      mex.printStackTrace();
      Exception ex = null;
      if (( ex = mex.getNextException()) != null )
        ex.printStackTrace();
    }
  }
}
