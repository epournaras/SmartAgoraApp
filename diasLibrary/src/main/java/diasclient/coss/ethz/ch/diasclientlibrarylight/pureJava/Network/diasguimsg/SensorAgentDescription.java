package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

import java.util.StringTokenizer;

public class SensorAgentDescription 
{
	final public String Host;

	final public int		Port;
	
	public SensorAgentDescription(String Host, int Port )
	{
		this.Host = Host;
		this.Port = Port;
	}
	
	public SensorAgentDescription( SensorAgentDescription other )
	{
		this.Host = other.Host;
		this.Port = other.Port;
	}
	
	public SensorAgentDescription( String ConnectionString )	// e.g: localhost:9000
	{
		StringTokenizer tokens = new StringTokenizer(ConnectionString, ":");
 	    String[] splited = new String[tokens.countTokens()];
 	    int index = 0;
 	    while(tokens.hasMoreTokens())
 	        splited[index++] = tokens.nextToken();
 	    
 	    
		this.Host = splited[0];
		this.Port = Integer.parseInt(splited[1]);
	}
	
	public boolean compareTo(SensorAgentDescription other)
	{
		return( (this.Host.compareTo(other.Host) == 0) && (this.Port == other.Port) ); 
	}
	
	public String toString()
	{
		return Host + ":" + Port;
	}

}
