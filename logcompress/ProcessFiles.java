import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Calendar;

public final class ProcessFiles
{
  public static final void main( String[] args ) throws IOException
  {
    String processdirectory = null;
    int keeptime = -1;
    if ( args.length >= 2 )
    {
      processdirectory = args[ 0 ];
      keeptime = Integer.parseInt( args[ 1 ] );
    }
    else
    {
      System.out.println( "Usage: java ProcessFiles [directory] [keeptime]" );
      System.exit( 1 );
    }

    Calendar now = Calendar.getInstance();
    Calendar keepdate = Calendar.getInstance();
    keepdate.add( Calendar.DATE, -keeptime );
    Calendar lastmodified = Calendar.getInstance();

System.out.println( "Process directory: " + processdirectory );
System.out.println( "Today: " + UtilityBean.getDate( now ) + " " + UtilityBean.getTime( now ));
System.out.println( "Keep Date: " + UtilityBean.getDate( keepdate ));

    File files[] = FileIO.getFileList( processdirectory );
    files = FileIO.sortByAlpha( files );
    for ( int i = 0; i < ( files.length - 1 ); i++ )
    {
      lastmodified.setTimeInMillis( files[ i ].lastModified());
      if ( lastmodified.before( keepdate ))
      {
        System.out.println( "Deleting file " + files[ i ].getName());
        files[ i ].delete();
      }
      else
      {
        if ( files[ i ].getName().indexOf( "bz2" ) == -1 )
        {
          FileIO.zipFile( processdirectory + "/" + files[ i ].getName());
        }
      }
    }
  }
}
