import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.EOFException;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilityBean
{
  public UtilityBean() {}

  public static Calendar convertDate( String d, String t )
  {
    Calendar a = convertNumeric( d );
    a = addTime( a, t );
    return a;
  }

  public static Calendar convertNumeric( String string )
  {
    Calendar thedate = Calendar.getInstance();
    thedate.clear();
    thedate.set( Integer.parseInt( string.substring( 6, 10 )),
                 Integer.parseInt( string.substring( 0, 2 )) - 1, 
                 Integer.parseInt( string.substring( 3, 5 )));
    return thedate;
  }

  public static Calendar addTime( Calendar calendar, String string )
  {
    int hour = Integer.parseInt( string.substring( 0, 2 ).trim());
    int minutes = Integer.parseInt( string.substring( 3, 5 ));
    int seconds = Integer.parseInt( string.substring( 6, 8 ));
    calendar.set( Calendar.HOUR_OF_DAY, hour );
    calendar.set( Calendar.MINUTE, minutes );
    calendar.set( Calendar.SECOND, seconds );
    return calendar;
  }

  public static String getDateNumeric( long date )
  {
    Calendar tempdate = Calendar.getInstance();
    tempdate.setTimeInMillis( date );
    return getDateNumeric( tempdate );
  }

  public static String getDateNumeric( Calendar date )
  {
    if ( date != null )
    {
      String string = new String( "" );
      String time = new String( "" );
      string = Integer.toString( date.get( Calendar.YEAR ));
      string += "-";
      if ( date.get( Calendar.MONTH ) < 9 ) string += "0" + Integer.toString( date.get( Calendar.MONTH ) + 1 );
      else string += Integer.toString( date.get( Calendar.MONTH ) + 1 );
      string += "-";
      if ( date.get( Calendar.DATE ) < 10 ) string += "0";
      string += date.get( Calendar.DATE );
  
      if ( date.get( Calendar.HOUR_OF_DAY ) < 10 ) time = "0" + date.get( Calendar.HOUR_OF_DAY );
      else time = Integer.toString( date.get( Calendar.HOUR_OF_DAY ));
      if ( date.get( Calendar.MINUTE ) < 10 ) time += "0" + date.get( Calendar.MINUTE );
      else time += date.get( Calendar.MINUTE );
      if ( date.get( Calendar.SECOND ) < 10 ) time += "0" + date.get( Calendar.SECOND );
      else time += date.get( Calendar.SECOND );

      return string + "-" + time;
    }
    else return "--";
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

  public static String getDate( Calendar date )
  {
    return getDayName( date.get( Calendar.DAY_OF_WEEK )) + ", " + getMonthName( date.get( Calendar.MONTH )) + " " +
                       date.get( Calendar.DATE ) + getPostScript( date.get( Calendar.DATE )) + ", " + date.get( Calendar.YEAR );
  }

  public static String getTime()
  {
    Calendar now = Calendar.getInstance();
    return getTime( now );
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

  public static String getShortMonth( int month )
  {
    String string = new String( "" );
    switch ( month )
    {
      case  1: string = "JAN";
      case  2: string = "FEB";
      case  3: string = "MAR";
      case  4: string = "APR";
      case  5: string = "MAY";
      case  6: string = "JUN";
      case  7: string = "JUL";
      case  8: string = "AUG";
      case  9: string = "SEP";
      case 10: string = "OCT";
      case 11: string = "NOV";
      case 12: string = "DEC";
    }
    return string;
  }

  public static final String getOracleDate( Calendar date )
  {
    String string = new String( "" );
    string = Integer.toString( date.get( Calendar.YEAR ));
    string += "-" + getShortMonth( date.get( Calendar.MONTH ) + 1 );
    if ( date.get( Calendar.DATE ) < 10 ) string += "0";
    string += "-" + date.get( Calendar.DATE );
    return string;
  }
}
