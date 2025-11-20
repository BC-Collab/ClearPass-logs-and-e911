// phoneClass v2
import java.sql.Timestamp;

public final class phoneClass
{
  int counter = 0;
  int nascounter = 0;
  Timestamp timestamp = null;
  String dns = null;
  String ip = null;
  String mac = null;
  String nasid = null;
  String nasport = null;

  public phoneClass() {};

  public phoneClass( Timestamp t, String m, String d, String i, String id, String p )
  {
    counter = 1;
    nascounter = 1;
    timestamp = t;
    mac = m;
    dns = d;
    ip = i;
    nasid = id;
    nasport = p;
  }

  public void count() { counter++; }
  public void NAScount() { nascounter++; }
  public void setTimestamp( Timestamp t ) { timestamp = t; }
  public void setNASid( String s ) { nasid = s; }
  public void setNASport( String s ) { nasport = s; }

  public int getCounter() { return counter; }
  public int getNASCounter() { return nascounter; }
  public Timestamp getTimestamp() { return timestamp; }
  public String getMAC() { return mac; }
  public String getIP() { return ip; }
  public String getDNS() { return dns; }
  public String getNASid() { return nasid; }
  public String getNASport() { return nasport; }
}
