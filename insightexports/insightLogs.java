// Here is a program
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Properties;
import java.util.Calendar;
import org.postgresql.util.PSQLException;

public class insightLogs
{
  public static void main( String args[] ) throws IOException
  {
    String postgressDriver = "org.postgresql.Driver";

    Calendar now = Calendar.getInstance();
    now.set( Calendar.SECOND, 0 );
    Calendar startdate = (Calendar) now.clone();
    Calendar enddate = (Calendar) now.clone();
    startdate.add( Calendar.MINUTE, -7 );
    enddate.add( Calendar.MINUTE, -5 );
    Calendar finaldate = (Calendar) enddate.clone();
    String logfilename = null;

    if ( args.length > 1 )
    {
      logfilename = args[ 0 ];
System.out.println( ">" + logfilename + "<" );
      if (( args.length > 1 ) && ( args.length < 2 ))
      {
        System.out.println( "Not enough paramenters.  Date example: '2016-10-11' '13:00'" );
        System.out.println( "java Insight [insightlogs] <date> <time>" );
        System.exit( 0 );
      }
      if ( args.length > 2 )
      {
System.out.println( args[ 1 ] + " " + args[ 2 ] );
        startdate = UtilityBean.convertNumericWithTime( args[ 1 ] + " " + args[ 2 ] );
        enddate = (Calendar) startdate.clone();
        enddate.add( Calendar.MINUTE, 2 );
      }
    }
    else
    {
      System.out.println( "Please specify the directories where the output logs are to be written:" );
      System.out.println( "java Insight [insightlogs] <date> <time>" );
      System.out.println( );
      System.exit( 0 );
    }

    while ( !enddate.after( finaldate ))
    {
System.out.println( "Startdate = " + UtilityBean.getDateNumeric( startdate ));
System.out.println( "  Enddate = " + UtilityBean.getDateNumeric( enddate ));

      String filename = logfilename + "/" + UtilityBean.getDatedFilename( startdate ) + "-syslog.log";
System.out.println( "File        = " + logfilename );
      PrintWriter logfile = new PrintWriter( new BufferedWriter( new FileWriter( filename, true )));
 
      Connection cpconnection = null;

      try
      {
        Class.forName( postgressDriver );

        String password = postgresPassClass.getPassword( "xena.bc.edu", "5433", "insightdb", "appexternal" );
        Properties postgressproperties = new Properties();
        postgressproperties.setProperty( "user", "appexternal" );
        postgressproperties.setProperty( "password", password );
        postgressproperties.setProperty( "ssl", "true" );
        postgressproperties.setProperty( "sslmode", "require" );
        postgressproperties.setProperty( "sslfactory", "org.postgresql.ssl.NonValidatingFactory" );
        cpconnection = DriverManager.getConnection( "jdbc:postgresql://xena.bc.edu:5433/insightdb", postgressproperties );
      }
      catch ( ClassNotFoundException cnfe ) { cnfe.printStackTrace(); }
      catch ( SQLException se ) { se.printStackTrace(); }

      try
      {
        Statement statement2 = cpconnection.createStatement();
        Statement statement3 = cpconnection.createStatement();
        PreparedStatement statement = cpconnection.prepareStatement( "select a.timestamp, a.session_id, a.username, a.mac, a.protocol, a.nad_ip, " +
                          " a.service, a.auth_src, a.roles, a.enfprofiles, a.ap_name, a.auth_username, c.hostname, a.nas_port_id, " +
                          " a.nas_identifier, a.ssid, a.error_code, a.auth_method from auth a, cppm_cluster c " +
                          " where a.timestamp >= ? and a.timestamp < ? " + 
//                          " and a.service != '[AirGroup Authorization Service]' " +
                          " and a.cppm_uuid = c.uuid" );
        statement.setQueryTimeout( 45 );
        statement.setTimestamp( 1, new Timestamp( startdate.getTimeInMillis()));
        statement.setTimestamp( 2, new Timestamp( enddate.getTimeInMillis()));
        ResultSet results = statement.executeQuery();
        while ( results.next())
        {
          int x = 1;
          Timestamp timestamp = results.getTimestamp( x++ );
          String sessionid = results.getString( x++ );
          String username = results.getString( x++ );
          if ( username != null ) username = username.toLowerCase();
          String macaddress = results.getString( x++ );
          String mac = macaddress;
          if ( mac != null ) mac = UtilityBean.getMAC( mac, UtilityBean.MAC_COLONS );
          String protocol = results.getString( x++ );
          String nasip = results.getString( x++ );
          String service = results.getString( x++ );
          String authsrc = results.getString( x++ );
          String roles = results.getString( x++ );
          String profiles = results.getString( x++ );
          String ap = results.getString( x++ );
          String authusername = results.getString( x++ );
          if ( authusername != null ) authusername = authusername.toLowerCase();
          String cppmhost = results.getString( x++ );
          String nasport = results.getString( x++ );
          String nasid = results.getString( x++ );
          String ssid = results.getString( x++ );
          String errorcode = results.getString( x++ );
          String authmethod = results.getString( x++ );
          if ( username != null ) 
            if ( UtilityBean.getMAC( username, UtilityBean.MAC_COLONS ).equals( mac )) 
              username = UtilityBean.getMAC( username, UtilityBean.MAC_COLONS );
          if ( authusername != null ) 
          if ( UtilityBean.getMAC( authusername, UtilityBean.MAC_COLONS ).equals( mac )) 
            authusername = UtilityBean.getMAC( authusername, UtilityBean.MAC_COLONS );
          if ( roles != null ) roles = roles.replace( "\"", "" );
          if ( profiles != null ) profiles = profiles.replace( "\"", "" );
  
          logfile.print( "timestamp=\"" + timestamp + "\"" );
          if ( sessionid != null ) logfile.print( ",session_id=\"" + sessionid + "\"" );
          if ( username != null ) logfile.print( ",username=\"" + username + "\"" );
          if ( mac != null ) logfile.print( ",mac=\"" + mac + "\"" );
          if ( protocol != null ) logfile.print( ",protocol=\"" + protocol + "\"" );
          if ( service != null ) logfile.print( ",service=\"" + service + "\"" );
          if ( authsrc != null ) logfile.print( ",authsrc=\"" + authsrc + "\"" );
          if ( roles != null ) logfile.print( ",roles=\"" + roles + "\"" );
          if ( profiles != null ) logfile.print( ",profiles=\"" + profiles + "\"" );
          if ( authusername != null ) logfile.print( ",auth-username=\"" + authusername + "\"" );
          if ( cppmhost != null ) logfile.print( ",cppm-node=\"" + cppmhost + "\"" );
          if ( ssid != null ) logfile.print( ",ssid=\"" + ssid + "\"" );
          if ( errorcode != null ) logfile.print( ",error-code=\"" + errorcode + "\"" );
          if ( authmethod != null ) logfile.print( ",auth-method=\"" + authmethod + "\"" );
          if ( profiles != null ) profiles = profiles.replace ( "\"", "" );
         
          ResultSet results2 = statement2.executeQuery( "select device_category, device_family, device_name, hostname, ip from endpoints where mac = '" + macaddress + "'" );
          while ( results2.next())
          {
            int y = 1;
            String devicecategory = results2.getString( y++ );
            String devicefamily = results2.getString( y++ );
            String devicename = results2.getString( y++ );
            String hostname = results2.getString( y++ );
            String ip = results2.getString( y++ );
  
            if ( devicecategory != null ) logfile.print( ",device-category=\"" + devicecategory + "\"" );
            if ( devicefamily != null ) logfile.print( ",device-family=\"" + devicefamily + "\"" );
            if ( devicename != null ) logfile.print( ",device-name=\"" + devicename + "\"" );
            if ( hostname != null ) logfile.print( ",hostname=\"" + hostname + "\"" );
            if ( ip != null ) logfile.print( ",ip-address=\"" + ip + "\"" );
          }

          if ( nasip != null ) logfile.print( ",nas-ip=\"" + nasip + "\"" );
          if ( nasport != null ) logfile.print( ",nas-port=\"" + nasport + "\"" );
          if ( nasid != null ) logfile.print( ",nas-id=\"" + nasid + "\"" );
          if ( ap != null ) logfile.print( ",ap-name=\"" + ap + "\"" );

          if ( mac != null )
          {
            ResultSet results3 = statement3.executeQuery( "select sponsor_name from guests where mac = '" + UtilityBean.getMAC( mac, UtilityBean.MAC_DASHES ) + "' and " +
                                 " ( expires_at > '" + UtilityBean.getDateNumeric( now ) + "' or expires_at is null )" );
            if ( results3.next())
            {
              String sponsor = results3.getString( 1 );
              if ( sponsor != null ) logfile.print( ",sponsor-name=" + sponsor );
            }
          }

          logfile.println();
        }
        statement.close();
        statement2.close();
      }
      catch ( SQLException se ) { se.printStackTrace(); }
      logfile.close();

      startdate.add( Calendar.MINUTE, 2 );
      enddate.add( Calendar.MINUTE, 2 );
    }
  }
}
