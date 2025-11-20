import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UtilityBean
{
  public UtilityBean() {}

  public static Calendar convertNumericWithTime( String string )
  {
    Calendar thedate = Calendar.getInstance();
    thedate.clear();
    thedate.set( Integer.parseInt( string.substring( 0, 4 )),
                 Integer.parseInt( string.substring( 5, 7 )) -1,
                 Integer.parseInt( string.substring( 8, 10 )),
                 Integer.parseInt( string.substring( 11, 13 )),
                 Integer.parseInt( string.substring( 14, 16 )),
                 Integer.parseInt( string.substring( 17, 19 )));
    return thedate;
  }

  public static String getDateNumeric( Calendar date )
  {
    String string = new String( "" );
    string = Integer.toString( date.get( Calendar.YEAR ));
    string += "-";
    if ( date.get( Calendar.MONTH ) < 9 ) string += "0" + Integer.toString( date.get( Calendar.MONTH ) + 1 );
    else string += Integer.toString( date.get( Calendar.MONTH ) + 1 );
    string += "-";
    if ( date.get( Calendar.DATE ) < 10 ) string += "0";
    string += date.get( Calendar.DATE );

    string += " ";
    if ( date.get( Calendar.HOUR_OF_DAY ) < 10 ) string += "0" + Integer.toString( date.get( Calendar.HOUR_OF_DAY ));
    else string += Integer.toString( date.get( Calendar.HOUR_OF_DAY ));
    string += ":";
    if ( date.get( Calendar.MINUTE ) < 10 ) string += "0" + Integer.toString( date.get( Calendar.MINUTE ));
    else string += Integer.toString( date.get( Calendar.MINUTE ));
    string += ":";
    if ( date.get( Calendar.SECOND ) < 10 ) string += "0" + Integer.toString( date.get( Calendar.SECOND ));
    else string += Integer.toString( date.get( Calendar.SECOND ));
    return string;
  }

  public static String getDatedFilename( Calendar date )
  {
    String string = new String( "" );
    string = Integer.toString( date.get( Calendar.YEAR ));
    string += "-";
    if ( date.get( Calendar.MONTH ) < 9 ) string += "0" + Integer.toString( date.get( Calendar.MONTH ) + 1 );
    else string += Integer.toString( date.get( Calendar.MONTH ) + 1 );
    string += "-";
    if ( date.get( Calendar.DATE ) < 10 ) string += "0";
    string += date.get( Calendar.DATE );

    string += "-";
    if ( date.get( Calendar.HOUR_OF_DAY ) < 10 ) string += "0" + Integer.toString( date.get( Calendar.HOUR_OF_DAY ));
    else string += Integer.toString( date.get( Calendar.HOUR_OF_DAY ));
    return string;
  }

  public static String getTime24( Calendar date )
  {
    String time = new String( "" );
    if ( date.get( Calendar.HOUR_OF_DAY ) < 10 ) time = "0" + date.get( Calendar.HOUR_OF_DAY );
    else time = "" + date.get( Calendar.HOUR_OF_DAY );
    if ( date.get( Calendar.MINUTE ) < 10 ) time += ":0" + date.get( Calendar.MINUTE );
    else time += ":" + date.get( Calendar.MINUTE );
    if ( date.get( Calendar.SECOND ) < 10 ) time += ":0" + date.get( Calendar.SECOND );
    else time += ":" + date.get( Calendar.SECOND );

    return time;
  }

  public static Calendar addTime( Calendar calendar, String string )
  {
    int hour = Integer.parseInt( string.substring( 0, 2 ));
    int minutes = Integer.parseInt( string.substring( 3, 5 ));
    int seconds = 0;
    if ( string.length() > 6 ) seconds = Integer.parseInt( string.substring( 6, 8 ));
    calendar.set( Calendar.HOUR_OF_DAY, hour );
    calendar.set( Calendar.MINUTE, minutes );
    calendar.set( Calendar.SECOND, seconds );
    return calendar;
  }

  public static String getDateNumeric( Timestamp date )
  {
    Calendar newtime = Calendar.getInstance();
    newtime.setTimeInMillis( date.getTime());
    return getDateNumeric( newtime );
  }

  public static String getMysqlDate( Timestamp date )
  {
    Calendar thedate = Calendar.getInstance();
    thedate.setTimeInMillis( date.getTime());
    return getMysqlDate( thedate );
  }

  public static String getMysqlDate( Calendar date )
  {
    String string = new String( "" );
    string = Integer.toString( date.get( Calendar.YEAR ));
    string += "-";
    if ( date.get( Calendar.MONTH ) < 9 ) string += "0" + Integer.toString( date.get( Calendar.MONTH ) + 1 );
    else string += Integer.toString( date.get( Calendar.MONTH ) + 1 );
    string += "-";
    if ( date.get( Calendar.DATE ) < 10 ) string += "0";
    string += date.get( Calendar.DATE );
    return string;
  }

  public static String getOracleDate( Calendar date )
  {
    String hour = new String();
    String minute = new String();
    String seconds = new String();
    if ( date.get( Calendar.HOUR_OF_DAY ) < 10 ) hour = "0" + date.get( Calendar.HOUR_OF_DAY );
    else hour = Integer.toString( date.get( Calendar.HOUR_OF_DAY ));
    if ( date.get( Calendar.MINUTE ) < 10 ) minute = "0" + date.get( Calendar.MINUTE );
    else minute = Integer.toString( date.get( Calendar.MINUTE ));
    if ( date.get( Calendar.SECOND ) < 10 ) seconds = "0" + date.get( Calendar.SECOND );
    else seconds = Integer.toString( date.get( Calendar.SECOND ));
//    return date.get( Calendar.DATE ) + "-" + getMonthShortName( date.get( Calendar.MONTH )) + "-" + date.get( Calendar.YEAR ) + " " + hour + ":" + minute + ":" + seconds;
    return date.get( Calendar.DATE ) + "-" + getMonthShortName( date.get( Calendar.MONTH )) + "-" + date.get( Calendar.YEAR );
    }

  public static String getMonthName( int m )
  {
    switch ( m )
    {
      case  0: return "January";
      case  1: return "February";
      case  2: return "March";
      case  3: return "April";
      case  4: return "May";
      case  5: return "June";
      case  6: return "July";
      case  7: return "August";
      case  8: return "September";
      case  9: return "October";
      case 10: return "November";
      case 11: return "December";
    }
    return null;
  }

  public static String getMonthShortName( int m )
  {
    switch ( m )
    {
      case  0: return "JAN";
      case  1: return "FEB";
      case  2: return "MAR";
      case  3: return "APR";
      case  4: return "MAY";
      case  5: return "JUN";
      case  6: return "JUL";
      case  7: return "AUG";
      case  8: return "SEP";
      case  9: return "OCT";
      case 10: return "NOV";
      case 11: return "DEC";
    }
    return null;
  }

  public static String getDayName( int d )
  {
    switch ( d )
    {
      case 1: return "Sunday";
      case 2: return "Monday";
      case 3: return "Tuesday";
      case 4: return "Wednesday";
      case 5: return "Thursday";
      case 6: return "Friday";
      case 7: return "Saturday";
    }
    return null;
  }

  public static int getMonthNum( String month )
  {
    int monthnum = 0;
    if ( month.toUpperCase().equals( "JAN" )) monthnum = 0;
    if ( month.toUpperCase().equals( "FEB" )) monthnum = 1;
    if ( month.toUpperCase().equals( "MAR" )) monthnum = 2;
    if ( month.toUpperCase().equals( "APR" )) monthnum = 3;
    if ( month.toUpperCase().equals( "MAY" )) monthnum = 4;
    if ( month.toUpperCase().equals( "JUN" )) monthnum = 5;
    if ( month.toUpperCase().equals( "JUL" )) monthnum = 6;
    if ( month.toUpperCase().equals( "AUG" )) monthnum = 7;
    if ( month.toUpperCase().equals( "SEP" )) monthnum = 8;
    if ( month.toUpperCase().equals( "OCT" )) monthnum = 9;
    if ( month.toUpperCase().equals( "NOV" )) monthnum = 10;
    if ( month.toUpperCase().equals( "DEC" )) monthnum = 11;
    return monthnum;
  }

  public static String getPostScript( int d )
  {
  String string;

    switch ( d )
    {
      case 1: 
      case 21:
      case 31: 
        string = new String( "st" );
        break;
      case 2:
      case 22: 
        string = new String( "nd" );
        break;
      case 3:
      case 23: 
        string = new String( "rd" );
        break;
      default: 
        string = new String( "th" );
        break;
    }
    return string;
  }

  public static String getDate( Date date )
  {
    Calendar newtime = Calendar.getInstance();
    newtime.setTimeInMillis( date.getTime());
    return getDate( newtime ) + " " + getTime( newtime );
  }

  public static String getDate( Calendar date )
  {
    return getDayName( date.get( Calendar.DAY_OF_WEEK )) + ", " + getMonthName( date.get( Calendar.MONTH )) + " " +
                       date.get( Calendar.DATE ) + getPostScript( date.get( Calendar.DATE )) + ", " + date.get( Calendar.YEAR );
  }

  public static String getTime( Calendar date )
  {
    String time;
    if ( date.get( Calendar.HOUR ) == 0 ) time = new String( "12" );
    else time = Integer.toString( date.get( Calendar.HOUR ));
    if ( date.get( Calendar.MINUTE ) < 10 ) 
      time += ":0" + date.get( Calendar.MINUTE );
    else
      time += ":" + date.get( Calendar.MINUTE );
    if ( date.get( Calendar.SECOND ) < 10 )
      time += ":0" + date.get( Calendar.SECOND );
    else
      time += ":" + date.get( Calendar.SECOND );
    switch ( date.get( Calendar.AM_PM ))
    {
      case Calendar.AM:
        time += "am";
        break;
      case Calendar.PM:
        time += "pm";
        break;
    }
    
    return time;
  }

  public static int getTotalDaysInMonth( int month )
  {
    int totaldays = 0;

    switch ( month )
    {
      case 0:
        totaldays = 31;
        break;
      case 1:
        totaldays = 28;
        break;
      case 2:
        totaldays = 31;
        break;
      case 3:
        totaldays = 30;
        break;
      case 4:
        totaldays = 31;
        break;
      case 5:
        totaldays = 30;
        break;
      case 6:
        totaldays = 31;
        break;
      case 7:
        totaldays = 31;
        break;
      case 8:
        totaldays = 30;
        break;
      case 9:
        totaldays = 31;
        break;
      case 10:
        totaldays = 30;
        break;
      case 11:
        totaldays = 31;
        break;
    }
    return totaldays;
  }

  public static String convertTime( int totalminutes )
  {
    String time = null;
    int hour = totalminutes / 60;
    int minutes = totalminutes - ( hour * 60 );
    boolean am = true;
    if ( hour > 11 ) am = false;
    if ( hour == 0 ) hour = 12;
    if ( hour > 12 ) hour -= 12;
    time = hour + ":";
    if ( minutes < 10 ) time += "0" + minutes;
    else time += minutes;
    if ( am ) time += "am";
    else time += "pm";

    return time;
  }

  public static String Capitalize( String s )
  {
    char chararray[] = s.toCharArray();
    for ( int x = 1; x < s.length(); x++ )
    {
      boolean changecase = true;
      if ( chararray[ x - 1 ] == ' ' ) changecase = false;
      if ( chararray[ x - 1 ] == '\'' ) changecase = false;
      if ( chararray[ x - 1 ] == '-' ) changecase = false;
      if (( chararray[ x - 1 ] == '\'' ) && ( chararray[ x ] == 'S' )) changecase = true;
      if ( changecase )
      {
        switch ( chararray[ x ] )
        {
          case 'A':
            chararray[ x ] = 'a';
            break;
          case 'B':
            chararray[ x ] = 'b';
            break;
          case 'C':
            chararray[ x ] = 'c';
            break;
          case 'D':
            chararray[ x ] = 'd';
            break;
          case 'E':
            chararray[ x ] = 'e';
            break;
          case 'F':
            chararray[ x ] = 'f';
            break;
          case 'G':
            chararray[ x ] = 'g';
            break;
          case 'H':
            chararray[ x ] = 'h';
            break;
          case 'I':
            chararray[ x ] = 'i';
            break;
          case 'J':
            chararray[ x ] = 'j';
            break;
          case 'K':
            chararray[ x ] = 'k';
            break;
          case 'L':
            chararray[ x ] = 'l';
            break;
          case 'M':
            chararray[ x ] = 'm';
            break;
          case 'N':
            chararray[ x ] = 'n';
            break;
          case 'O':
            chararray[ x ] = 'o';
            break;
          case 'P':
            chararray[ x ] = 'p';
            break;
          case 'Q':
            chararray[ x ] = 'q';
            break;
          case 'R':
            chararray[ x ] = 'r';
            break;
          case 'S':
            chararray[ x ] = 's';
           break;
          case 'T':
            chararray[ x ] = 't';
            break;
          case 'U':
            chararray[ x ] = 'u';
            break;
          case 'V':
            chararray[ x ] = 'v';
            break;
          case 'W':
            chararray[ x ] = 'w';
            break;
          case 'X':
            chararray[ x ] = 'x';
            break;
          case 'Y':
            chararray[ x ] = 'y';
            break;
          case 'Z':
            chararray[ x ] = 'z';
            break;
        }
      }
    }

    String newstring = String.copyValueOf( chararray );
    return newstring;
  }

  public static Calendar fromOracleDate( String string )
  {
    Calendar thedate = Calendar.getInstance();
    thedate.clear();
    int x = 0;
    if ( string.substring( 1, 2 ).equals( "-" )) x = 1;
    else x = 2;
    String month = string.substring( 1 + x, 4 + x );
    int monthnum = 0;
    monthnum = getMonthNum( month );
    thedate.set( Integer.parseInt( string.substring( 5 + x, 9 + x )), monthnum, Integer.parseInt( string.substring( 0, x )));
    return thedate;
  }
  
  public void Include( String filename )
  {
    BufferedReader input = null;

    try
    {
      input = new BufferedReader( new InputStreamReader( new FileInputStream( filename )));
    }
    catch ( IOException e ) { System.out.println( "Error opening file " + e.toString()); }
   
    while ( true )
    {
      try
      {
        String linevar = input.readLine();
        System.out.println( linevar );
      }
      catch ( EOFException eof )
      { 
        System.out.println( "EOF" );
      }
      catch ( IOException e ) { System.out.println( e.toString()); e.printStackTrace(); }
    }
  }

  /**
   * Return the current date and time in String format 
   *  'Thursday, November 24, 2004 9:38am'
   */
  public static final String getCurrentDateTimeFormal()
  {
    String s = "whoops";
    String t = "whoops";
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM, dd, yyyy h:mm");
    SimpleDateFormat am = new SimpleDateFormat("a");
    try {
      s = sdf.format(new Date()); 
      t = am.format(new Date()); 
    } catch ( Throwable thr ) { ; }
    return(s + t.toLowerCase()); 
  }

  
  /***** CUSTOM MOD - ea 1/31/2005 - shortcut for window status link ****/
  public static final String windowStatus(String message)
  {
    // failsafe
    if ( message == null || message.length() == 0 ) { return(""); }

    StringBuffer d = new StringBuffer(256);
    char         c[] = message.toCharArray();

    d.append("onMouseOver=\"window.status='");

    for ( int i=0; i < c.length; i++ ) {
      if ( c[i] == '\'' ) { d.append("\\\'"); }
      else if ( c[i] == '\"' ) { d.append("&quot;"); }
      else { d.append(c[i]); }
    }
    d.append("';return(true);\" onMouseOut='window.status=\"\";return(true);'");
    return(d.toString());
  }

  /**
   * Return a non-zero length trimmed string or null
   */
  public static final String valOrNull( String s )
  {
    if ( s == null ) { return( s ); }
    s = s.trim();
    if ( s.length() == 0 ) { return( null ); }
    else { return( s ); }
  }
    
  /**
   * Return a non-null string
   */
  public static final String valOrEmptyString( String s )
  {
    if ( s == null ) { return( "" ); }
    else { return( s.trim() ); }
  }

  public static final int MAC_DASHES = 1;
  public static final int MAC_COLONS = 2;
  public static final int MAC_NOTHING = 3;

  public static final String getMAC( String mac, int conversion )
  {
    String seperator = "";
    String newmac = null;
    mac = mac.toUpperCase();

    if ( conversion == MAC_DASHES ) seperator = "-";
    if ( conversion == MAC_COLONS ) seperator = ":";
    if ( mac.length() == 12 )
    {
      newmac = mac.substring( 0, 2 ) + seperator + mac.substring( 2, 4 ) + seperator + mac.substring( 4, 6 ) + seperator + mac.substring( 6, 8 ) + seperator + mac.substring( 8, 10 ) + seperator + mac.substring( 10, 12 );
    }
    else 
    {
      if ( conversion == MAC_DASHES ) newmac = mac.replace( ":", "-" );
      if ( conversion == MAC_COLONS ) newmac = mac.replace( "-", ":" );
      if ( conversion == MAC_NOTHING )
      {
        if ( mac.indexOf( ":" ) != -1 ) newmac = mac.replace( ":", "" );
        if ( mac.indexOf( "-" ) != -1 ) newmac = mac.replace( "-", "" );
        newmac = newmac.toLowerCase();
      }
    }
    return newmac;
  }

  public static String formatNumber( double thenumber )
  {
    String sNumber = Double.toString( thenumber );
    if ( sNumber.indexOf( "E" ) != -1 )
    {
      int x = sNumber.indexOf( "E" ) + 1;
      int y = Integer.parseInt( sNumber.substring( x ));
      sNumber = sNumber.substring( 0, 1 ) + sNumber.substring( 2, y + 2 );
    }
    if ( sNumber.indexOf( "." ) != -1 ) sNumber = sNumber.substring( 0, sNumber.indexOf( "." ));
    return formatNumber( sNumber );
  }

  public static String formatNumber( int thenumber )
  {
    String sNumber = Integer.toString( thenumber );
    return formatNumber( sNumber );
  }

  public static String formatNumber( String sNumber )
  {
    if ( sNumber.length() < 4 )
    {
      return sNumber;
    }
    if ( sNumber.length() < 7 )
    {
      sNumber = sNumber.substring( 0, sNumber.length() - 3 ) + "," + sNumber.substring( sNumber.length() - 3 );
      return sNumber;
    }
    sNumber = sNumber.substring( 0, sNumber.length() - 6 ) + "," + sNumber.substring( sNumber.length() - 6, sNumber.length() - 3 ) + "," + sNumber.substring( sNumber.length() -3 );
    return sNumber;
  }

  public static String formatFloat( Float thenumber, int decimal )
  {
    String sNumber = thenumber.toString();
    int x = sNumber.indexOf( "." );
    int len = sNumber.length();
    if (( x + decimal ) < len )
     if ( x != -1 ) sNumber = sNumber.substring( 0, x + decimal + 1 );
    return sNumber;
  }

  public static String fixUsername( String username )
  {
    int x = username.indexOf( "@" );
    
    if ( x != -1 ) username = username.substring( 0, x );
 
    return username;
  }

  public static String fixMac( String macaddress )
  {
    return macaddress.replace( "-", ":" ).toUpperCase();
  }

  public static String cleanMac( String macaddress )
  {
    return macaddress.replace( "-", "" ).toLowerCase();
  }
}
