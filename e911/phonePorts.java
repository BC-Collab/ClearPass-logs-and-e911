// phonePorts v2
// This program reads log events from a database table
// The resulting report is built by writing the information
// to another database table.
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Calendar;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class phonePorts
{
  public static void main( String args[] ) throws IOException
  {
    int hours = -1;
    if ( args.length > 0 ) hours = Integer.parseInt(  args[ 0 ] );
    
    try
    {
      Calendar now = Calendar.getInstance();
      Calendar start = Calendar.getInstance();
      Calendar end = Calendar.getInstance();
      if ( hours == -1 )
      {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add( Calendar.DATE, -1 );
        yesterday.set( Calendar.HOUR, 0 );
        yesterday.set( Calendar.MINUTE, 0 );
        yesterday.set( Calendar.SECOND, 0 );
        Calendar endofyesterday = (Calendar) yesterday.clone();
        endofyesterday.add( Calendar.DATE, 1 );
        start = yesterday;
        end = endofyesterday;
      }
      else
      {
        Calendar before = Calendar.getInstance();
        before.add( Calendar.HOUR, -hours );
        start = before;
      }

System.out.println( "Now: " + UtilityBean.getDate( now ) + " " + UtilityBean.getTime( now ));
System.out.println( "Start: " + UtilityBean.getDate( start ) + " " + UtilityBean.getTime( start ) + ": " + UtilityBean.getMysqlDate( start ) + " " + UtilityBean.getTime24( start ));
System.out.println( "End: " + UtilityBean.getDate( end ) + " " + UtilityBean.getTime( end ) + ": " + UtilityBean.getMysqlDate( end ) + " " + UtilityBean.getTime24( end ));

      HashMap phones = new HashMap();

      String metricspass = postgresPassClass.getPassword( "bcprintdb.bc.edu", "5432", "metrics", "metrics" );
      Class.forName( "org.postgresql.Driver" );
      Connection connection = DriverManager.getConnection( "jdbc:postgresql://bcprintdb.bc.edu:5432/metrics?user=metrics&password=" + metricspass );

      Statement statement = connection.createStatement();
      Statement statement3 = connection.createStatement();
      statement.executeUpdate( "truncate table e911" );
      statement.executeUpdate( "insert into e911 ( voip_mac, previous_check_in, current_check_in ) values ( '-1', '" + UtilityBean.getMysqlDate( start ) + " " + UtilityBean.getTime24( start ) + "', '" + UtilityBean.getMysqlDate( end ) + " " + UtilityBean.getTime24( end ) + "' )" );
      PreparedStatement statement2 = connection.prepareStatement( "insert into e911 ( voip_dns, voip_ip_address, voip_mac, location_count, auth_count, current_check_in, current_nas_id, current_nas_port, previous_check_in, previous_nas_id, previous_nas_ip, previous_nas_port ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )" );
      PreparedStatement statement4 = connection.prepareStatement( "update e911 set location_count = ?, auth_count = ?, current_check_in = ?, voip_ip_address = ?, current_nas_id = ?, current_nas_port = ? where voip_mac = ?" );

      ResultSet results = statement.executeQuery( "select thedate, mac, hostname, nasip, nasid, nasport from clearpasslogs where profiles = '{BC-VoIP-Profile}' and thedate >= '" + UtilityBean.getMysqlDate( start ) + " " + UtilityBean.getTime24( start ) + "' and thedate < '" + UtilityBean.getMysqlDate( end ) + " " + UtilityBean.getTime24( end ) + "' order by thedate" );
      while ( results.next())
      {
        int x = 1;
        Timestamp timestamp = results.getTimestamp( x++ );
        String mac = results.getString( x++ );
        String hostname = results.getString( x++ );
        if ( hostname == null ) hostname = "";
        String ip = results.getString( x++ );
        String nasid = results.getString( x++ );
        String nasport = results.getString( x++ );
	if ( nasport == null ) nasport = "-";
        if ( phones.get( mac ) == null )
        {
          phoneClass p = new phoneClass( timestamp, mac, hostname, ip, nasid, nasport );
          phones.put( mac, p );

          int y = 1;
          statement2.setString( y++, hostname );
          statement2.setString( y++, ip );
          statement2.setString( y++, mac );
          statement2.setInt( y++, 1 );
          statement2.setInt( y++, 1 );
          statement2.setTimestamp( y++, timestamp );
          statement2.setString( y++, nasid );
          statement2.setString( y++, nasport );
          statement2.setTimestamp( y++, timestamp );
          statement2.setString( y++, nasid );
          statement2.setString( y++, ip );
          statement2.setString( y++, nasport );
          statement2.executeUpdate();
        }
        else
        {
          boolean change = false;
          phoneClass p = (phoneClass) phones.get( mac );
          p.count();
          if ( !p.getNASid().equals( nasid )) 
          {
System.out.println( p.getMAC() + " (" + hostname + ") NAS change from '" + p.getNASid() + ":" + p.getNASport() + "' to '" + nasid + ":" + nasport + "' at " + timestamp );
            p.setTimestamp( timestamp );
            p.setNASid( nasid );
            change = true;
          }
          if ( !p.getNASport().equals( nasport )) 
          {
System.out.println( p.getMAC() + " (" + hostname + ") NAS port change from '" + p.getNASid() + ":" + p.getNASport() + "' to '" + nasid + ":" + nasport + "' at " + timestamp );
            p.setTimestamp( timestamp );
            p.setNASport( nasport );
            change = true;
          }
          if ( change ) p.NAScount();
          int previouscount = 0;
          Timestamp previous_check_in = null;
          String previous_voip_ip = null;
          String previous_nas_id = null;
          String previous_nas_port = null;
          ResultSet r3 = statement3.executeQuery( "select location_count, current_check_in, voip_ip_address, current_nas_id, current_nas_port from e911 where voip_mac = '" + mac + "'" );
          if ( r3.next()) 
          {
            previouscount = r3.getInt( 1 );
            if ( change )previouscount++;
            previous_check_in = r3.getTimestamp( 2 );
            previous_voip_ip = r3.getString( 3 );
            previous_nas_id = r3.getString( 4 );
            previous_nas_port = r3.getString( 5 );
          }

          int y = 1;
          statement4.setInt( y++, previouscount );
          statement4.setInt( y++, p.getCounter() );
          statement4.setTimestamp( y++, timestamp );
          statement4.setString( y++, ip );
          statement4.setString( y++, nasid );
          statement4.setString( y++, nasport );
          statement4.setString( y++, mac );
          statement4.executeUpdate();
        }
      }
      statement.close();
      statement2.close();
      statement3.close();
      statement4.close();
      connection.close();
    }
    catch ( SQLException e ) { e.printStackTrace(); }
    catch ( ClassNotFoundException cnfe ) { cnfe.printStackTrace(); }
  }
}
