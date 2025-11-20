import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Calendar;

/**
 * Handle a multitude of File manipulation for the EAHistory application.
 */
public final class FileIO
{

  /** The full path of the program that will compress processed log files */
  public static final String ZIP_PROGRAM = "/usr/bin/bzip2"; 

  /** The buffer size during file io */
  public static final int BUFFER_SIZE = 16 * 1024;


  public static final File[] getFileList( String path )
  {
    LinkedList 	list = new LinkedList();
    File 	directory = new File( path );
    File 	files[] = directory.listFiles();

    boolean exact = false;
System.out.println( "FileIO:getFileList( " + path );
    if ( files == null ) return null;

    for ( int x = 0; x < files.length; x++ )
    {
      list.add( files[ x ] );
    }

    files = new File[ list.size() ];
    list.toArray( files );

    return files;
  }

  public static File[] sortByDate( File[] files )
  {
    boolean done = false;
    while ( !done )
    {
      done = true;
      for ( int x = 0; x < files.length - 1; x++ )
      {
        if ( files[ x ].lastModified() > files[ x + 1 ].lastModified())
        {
          done = false;
          File temp = files[ x ];
          files[ x ] = files[ x + 1 ];
          files[ x + 1 ] = temp;
        }
      }
    }
    return files;
  }

  public static File[] sortByAlpha( File[] files )
  {
    boolean done = false;
    while ( !done )
    {
      done = true;
      for ( int x = 0; x < files.length - 1; x++ )
      {
        if ( files[ x ].getName().compareToIgnoreCase( files[ x + 1 ].getName()) > 0 )
        {
          done = false;
          File temp = files[ x ];
          files[ x ] = files[ x + 1 ];
          files[ x + 1 ] = temp;
        }
      }
    }
    return files;
  }

  /**
   * Copy the <code>sourceFile</code> to the destination.
   * Sets the lastmod time of dest file to that of source file.
   */
  public static final File copyFile( File sourceFile, String destination ) throws IOException
  { 
    return( copyFile( sourceFile, new File( destination )));
  }

  /**
   * Copy the <code>sourceFile</code> to the destination.
   * Sets the lastmod time of dest file to that of source file.
   */
  public static final File copyFile( File sourceFile, File destFile ) throws IOException
  {
    FileInputStream fis = new FileInputStream( sourceFile );
    FileOutputStream fos = new FileOutputStream( destFile );
    byte b[] = new byte[ BUFFER_SIZE ];
System.out.println( "Coping file '" + sourceFile.getName() + "' to '" + destFile.getName() + "'." );

    for ( int rd = 0; ( rd= fis.read( b )) > 0; fos.write( b, 0, rd ));

System.out.println( "Original date: " + UtilityBean.getDateNumeric( sourceFile.lastModified()));
    System.out.println( destFile.setLastModified( sourceFile.lastModified()));
System.out.println( "Copied date: " + UtilityBean.getDateNumeric( destFile.lastModified()));
    fis.close();
    fos.close();
    return destFile;
  }

  public static final void zipFile( String filename )
  {
    System.out.println( "FileIO.zipFile(): START zipping file = " + filename );

    String commandLine = ZIP_PROGRAM + " " + filename;

    try 
    {
      Process process = ( Runtime.getRuntime()).exec( commandLine );
      process.waitFor();
    }
    catch ( InterruptedException ie ) { System.out.println( "FileIO.zipFile(): cmd = " + commandLine ); }
    catch ( IOException ioe ) { ioe.printStackTrace(); }
  }
}
