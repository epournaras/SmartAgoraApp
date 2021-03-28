package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

import java.util.LinkedHashMap;

public class SensorState 
{
	public final Integer stateID;			// number of the state, E {0; num.possible.states-1}
	
	public final LinkedHashMap<String,Object> stateValues;		// optional: the values that represent the state, e.g from the means from the k-means, or the medians from k-medoids, etc
																			// stateValues is a LinkedHashMap because a sensor can generate more than 1 value, eg a GPS sensor products lat/lon and altitude 
																			// can be null -> not available
	
	public SensorState(Integer stateID, LinkedHashMap<String,Object> stateValues )
	{
		this.stateID = stateID;
		this.stateValues = stateValues;
	}
		
	@Override
	public String toString()
	{
		return "SensorState: stateID=" + stateID.toString() + (stateValues == null ? "" : "(" + stateValues.toString() + ")" );
	}
	

}
