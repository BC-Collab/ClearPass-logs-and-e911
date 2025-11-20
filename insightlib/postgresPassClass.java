/* Program to get a postgres password from the Postgres password file if it exists.
 *
 * The default location of the file is ~/.pgpass.  If there is an
 * environment variable of PGPASSFILE, it can contain a different path.
 *
 * The file should contain the following information:
 * #hostname:port:database:username:password
 * 
 * Any object can be replaced by a wild card "*" which would match that field.
 *
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;

public class postgresPassClass
{
  public postgresPassClass() {}

  public static String getPassword( String hostname, String port, String username, String databasename )
  {
    String password = null;

    String passfilepath = System.getenv( "PGPASSFILE" );
    if ( passfilepath == null ) passfilepath = "~/.pgpass";

    try
    {
      BufferedReader passfile = new BufferedReader( new InputStreamReader( new FileInputStream( passfilepath )));

      String linevar = passfile.readLine();
      while ( linevar != null ) 
      {
        if ( linevar.length() > 0 )
        {
          if ( !linevar.substring( 0, 1 ).equals( "#" ))
          {
            String[] parameters = linevar.split( ":" );
            if ( parameters.length == 5 )
            {
              if (( parameters[ 0 ].equals( "*" ) || ( parameters[ 0 ].equals( hostname ))))
              {
                if (( parameters[ 1 ].equals( "*" ) || ( parameters[ 1 ].equals( port ))))
                {
                  if (( parameters[ 2 ].equals( "*" ) || ( parameters[ 2 ].equals( username ))))
                  {
                    if (( parameters[ 3 ].equals( "*" ) || ( parameters[ 3 ].equals( databasename )))) password = parameters[ 4 ];
                  }
                }
              }
            }
          }
        }
        linevar = passfile.readLine();
      }
      passfile.close();
    }
    catch ( IOException ioe ) { ioe.printStackTrace(); }

    return password;
  }
}
