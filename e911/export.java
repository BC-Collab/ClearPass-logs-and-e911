// Here is a program
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class export
{
  public static void main( String args[] ) throws IOException
  {
    boolean fullreport = true;
    if ( args.length > 0 )
    {
      fullreport = false;
    }

    Calendar now = Calendar.getInstance();

    try
    {
      boolean sendemail = false;

      Class.forName( "org.postgresql.Driver" );
      String metricspass = postgresPassClass.getPassword( "bcprintdb.bc.edu", "5432", "metrics", "metrics" );
      Connection connection = DriverManager.getConnection( "jdbc:postgresql://bcprintdb.bc.edu:5432/metrics?user=metrics&password=" + metricspass );
      Statement statement = connection.createStatement();

      Timestamp starttime = null;
      Timestamp endtime = null;
      int count = 0;
      if ( !fullreport )
      {
        ResultSet results = statement.executeQuery( "select count( voip_mac ) from e911 where location_count > 1" );
        if ( results.next()) count = results.getInt( 1 );
        if ( count > 0 ) sendemail = true;
        results = statement.executeQuery( "select previous_check_in, current_check_in from e911 where voip_mac = '-1'" );
        if ( results.next())
        {
          starttime = results.getTimestamp( 1 );
          endtime = results.getTimestamp( 2 );
        }
      }

      PrintWriter exportfile = null;
      if ( fullreport ) exportfile = new PrintWriter( new BufferedWriter( new FileWriter( "fullexport.csv" )));
      else exportfile = new PrintWriter( new BufferedWriter( new FileWriter( "export.csv" )));
      PrintWriter emailfile = null;
      exportfile.println( "MAC,Number of switch ports seen,First switch port seen,Last switch port seen,Number of Auths,hostname" );
      if ( sendemail ) 
      { 
        emailfile = new PrintWriter( new BufferedWriter( new FileWriter( "email.eml" )));
        emailfile.println( "<HTML><Body><Table border=1 cellpadding=5 cellspacing=0>" );
        emailfile.println( "<TR><TH colspan=6>VoIP Phones That Have Changed Switch Ports</TH></TR>" );
        emailfile.println( "<TR><TH>MAC</TH><TH>Number of Switch Ports Seen</TH><TH>First Switch Port seen</TH><TH>Last Switch Port Seen</TH><TH>Number of Auths</TH><TH>Hostname</TH></TR>" );
      }

      ResultSet results = statement.executeQuery( "select voip_mac, location_count, previous_nas_ip, previous_nas_id, previous_nas_port, voip_ip_address, current_nas_id, current_nas_port, auth_count, voip_dns from e911 where voip_mac != '-1' order by location_count desc, voip_mac" );
      while ( results.next())
      {
        int x = 1;
        String mac = results.getString( x++ );
        int location_count = results.getInt( x++ );
        String previous_nas_ip = results.getString( x++ );
        String previous_nas_id = results.getString( x++ );
        String previous_nas_port = results.getString( x++ );
        String current_nas_ip = results.getString( x++ );
        String current_nas_id = results.getString( x++ );
        String current_nas_port = results.getString( x++ );
        int auth_count = results.getInt( x++ );
        String hostname = results.getString( x++ );

        if ( fullreport )
          exportfile.println( "\"" + mac + "\"," + location_count + ",\"" + previous_nas_ip + ":" + previous_nas_id + ":" + previous_nas_port + "\",\"" + current_nas_ip + ":" + 
                               current_nas_id + ":" + current_nas_port + "\"," + auth_count + ",\"" + hostname + "\"" );
        else
        {
          if ( location_count > 1 ) 
          {
            exportfile.println( "\"" + mac + "\", " + location_count + ", \"" + previous_nas_ip + ":" + previous_nas_id + ":" + previous_nas_port + "\", \"" + current_nas_ip + ":" + 
                               current_nas_id + ":" + current_nas_port + "\", " + auth_count + ", \"" + hostname + "\"" );
            if ( sendemail ) emailfile.println( "<TR><TD>" + mac + "</TD><TD align=right>" + location_count + "</TD><TD>" + previous_nas_ip + ":" + previous_nas_id + ":" + previous_nas_port + "</TD><TD>" + 
                             current_nas_ip + ":" + current_nas_id + ":" + current_nas_port + "</TD><TD align=right>" + auth_count + "</TD><TD>" + hostname + "</TD></TR>" );
          }
        }
      }
      statement.close();
      connection.close();

      exportfile.close();
      if ( sendemail ) 
      {
        emailfile.println( "</Table></Body></HTML>" );
        emailfile.println( "This is an updated report for VoIP Phone moves.  Please email itssstaff.collab@bc.edu if you have any questions or issues." );
        emailfile.close();
      }
    }
    catch ( SQLException e ) { e.printStackTrace(); }
    catch ( ClassNotFoundException cnfe ) { cnfe.printStackTrace(); }
  }
}
