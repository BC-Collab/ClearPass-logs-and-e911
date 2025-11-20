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
import java.sql.SQLIntegrityConstraintViolationException;

public class insightToDB
{
  public static void main( String args[] ) throws IOException
  {
    Calendar now = Calendar.getInstance();
    now.set( Calendar.SECOND, 0 );
    Calendar keepdate = Calendar.getInstance();
    keepdate.add( Calendar.DATE, -14 );
    keepdate.set( Calendar.HOUR, 0 );
    keepdate.set( Calendar.MINUTE, 0 );
    keepdate.set( Calendar.SECOND, 0 );
    Calendar startdate = (Calendar) now.clone();
    Calendar enddate = (Calendar) now.clone();
    startdate.add( Calendar.MINUTE, -7 );
    enddate.add( Calendar.MINUTE, -5 );
    Calendar finaldate = (Calendar) enddate.clone();

    if ( args.length > 1 )
    {
System.out.println( args[ 0 ] + " " + args[ 1 ] );
      startdate = UtilityBean.convertNumericWithTime( args[ 0 ] + " " + args[ 1 ] + ":00" );
      enddate = (Calendar) startdate.clone();
      enddate.add( Calendar.MINUTE, 2 );
    }

    Connection cpconnection = null;
    Connection mconnection = null;

    try
    {
      Class.forName( "org.postgresql.Driver" );

      String clearpass = postgresPassClass.getPassword( "xena.bc.edu", "5433", "insightdb", "appexternal" );
      String metricspass = postgresPassClass.getPassword( "bcprintdb.bc.edu", "5432", "metrics", "metrics" );
 
      Properties postgressproperties = new Properties();
      postgressproperties.setProperty( "user", "appexternal" );
      postgressproperties.setProperty( "password", clearpass );
      postgressproperties.setProperty( "ssl", "true" );
      postgressproperties.setProperty( "sslmode", "require" );
      postgressproperties.setProperty( "sslfactory", "org.postgresql.ssl.NonValidatingFactory" );
      cpconnection = DriverManager.getConnection( "jdbc:postgresql://xena.bc.edu:5433/insightdb", postgressproperties );
      mconnection = DriverManager.getConnection( "jdbc:postgresql://bcprintdb.bc.edu:5432/metrics?user=metrics&password=" + metricspass );

System.out.println( "Purging data before " + UtilityBean.getDate( keepdate ) + "..." );
      Statement statement = mconnection.createStatement();
      statement.executeUpdate( "delete from clearpasslogs where thedate < '" + UtilityBean.getMysqlDate( keepdate ) + "'" );
      statement.close();
    }
    catch ( ClassNotFoundException cnfe ) { cnfe.printStackTrace(); }
    catch ( SQLException se ) { se.printStackTrace(); }

    while ( !enddate.after( finaldate ))
    {
System.out.println( "Extracting log data..." );
System.out.println( "Startdate = " + UtilityBean.getDateNumeric( startdate ));
System.out.println( "  Enddate = " + UtilityBean.getDateNumeric( enddate ));

      try
      {
        Statement statement2 = cpconnection.createStatement();
        Statement statement3 = cpconnection.createStatement();
        PreparedStatement statement4 = mconnection.prepareStatement( "insert into clearpasslogs ( thedate, sessionid, username, mac, protocol, service, authsrc, roles, profiles, authusername, cppmhost, ssid, errorcode, devicecategory, devicefamily, devicename, hostname, ip, sponsor, nasip, nasport, nasid, ap ) " +
                          " values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )" );
        PreparedStatement statement = cpconnection.prepareStatement( "select a.timestamp, a.session_id, a.username, a.mac, a.protocol, a.nad_ip, a.service, a.auth_src, a.roles, a.enfprofiles, a.ap_name, a.auth_username, c.hostname, a.nas_port_id, " +
                          " a.nas_identifier, a.ssid, a.error_code from auth a, cppm_cluster c where a.timestamp >= ? and a.timestamp < ? and a.cppm_uuid = c.uuid" );
        statement.setQueryTimeout( 45 );
        statement.setTimestamp( 1, new Timestamp( startdate.getTimeInMillis()));
        statement.setTimestamp( 2, new Timestamp( enddate.getTimeInMillis()));
        ResultSet results = statement.executeQuery();
        while ( results.next())
        {
          try 
          {
            int x = 1;
            Timestamp thedate = results.getTimestamp( x++ );
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
            int errorcode = results.getInt( x++ );
            if ( username != null ) 
              if ( UtilityBean.getMAC( username, UtilityBean.MAC_COLONS ).equals( mac )) 
                username = UtilityBean.getMAC( username, UtilityBean.MAC_COLONS );
            if ( authusername != null ) 
            if ( UtilityBean.getMAC( authusername, UtilityBean.MAC_COLONS ).equals( mac )) 
              authusername = UtilityBean.getMAC( authusername, UtilityBean.MAC_COLONS );
            if ( roles != null ) roles = roles.replace( "\"", "" );
            if ( profiles != null ) profiles = profiles.replace( "\"", "" );
    
            String devicecategory = null;
            String devicefamily = null;
            String devicename = null;
            String hostname = null;
            String ip = null;
  
            ResultSet results2 = statement2.executeQuery( "select device_category, device_family, device_name, hostname, ip from endpoints where mac = '" + macaddress + "'" );
            while ( results2.next())
            {
              int y = 1;
              devicecategory = results2.getString( y++ );
              devicefamily = results2.getString( y++ );
              devicename = results2.getString( y++ );
              hostname = results2.getString( y++ );
              if ( hostname != null ) hostname = hostname.replaceAll( "\\n", "" );
              ip = results2.getString( y++ );
            }
  
            String sponsor = new String( " " );
            if ( mac != null )
            {
              ResultSet results3 = statement3.executeQuery( "select sponsor_name from guests where mac = '" + UtilityBean.getMAC( mac, UtilityBean.MAC_DASHES ) + "' and ( expires_at > '" + UtilityBean.getDateNumeric( now ) + "' or expires_at is null )" );
              if ( results3.next()) sponsor = results3.getString( 1 );
            }
  
            int z = 1;
            statement4.setTimestamp( z++, thedate );
            statement4.setString( z++, sessionid );
            statement4.setString( z++, username );
            statement4.setString( z++, mac );
            statement4.setString( z++, protocol );
            statement4.setString( z++, service );
            statement4.setString( z++, authsrc );
            statement4.setString( z++, roles );
            statement4.setString( z++, profiles );
            statement4.setString( z++, authusername );
            statement4.setString( z++, cppmhost );
            statement4.setString( z++, ssid );
            statement4.setInt( z++, errorcode );
            statement4.setString( z++, devicecategory );
            statement4.setString( z++, devicefamily );
            statement4.setString( z++, devicename );
            statement4.setString( z++, hostname );
            statement4.setString( z++, ip );
            statement4.setString( z++, sponsor );
            statement4.setString( z++, nasip );
            statement4.setString( z++, nasport );
            statement4.setString( z++, nasid );
            statement4.setString( z++, ap );
            statement4.executeUpdate();
//System.out.println( thedate + ", " + sessionid + ", " + username + ", " + mac + ", " + protocol + ", " + service + ", " + authsrc + ", " + roles + ", " + profiles + ", " + authusername + ", " + cppmhost + ", " + ssid + ", " + errorcode + ", " + 
//                    devicecategory + "," + devicefamily + ", " + devicename + ", " + hostname + ", " + ip + ", " + sponsor + ", " + nasip + ", " + nasport + ", " + nasid + ", " + ap );
          } 
          catch ( SQLIntegrityConstraintViolationException e ) { e.printStackTrace(); }
          catch ( PSQLException pe ) { pe.printStackTrace(); }
        }
        statement.close();
        statement2.close();
        statement3.close();
        statement4.close();
      }
      catch ( SQLException se ) { se.printStackTrace(); }
//      catch ( PSQLException pe ) { pe.printStackTrace(); }

      startdate.add( Calendar.MINUTE, 2 );
      enddate.add( Calendar.MINUTE, 2 );
    }
  }
}
