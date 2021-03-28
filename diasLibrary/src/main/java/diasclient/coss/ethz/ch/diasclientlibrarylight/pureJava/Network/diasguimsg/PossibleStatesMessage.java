package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;


import java.util.ArrayList;

public class PossibleStatesMessage extends GUIMessage
{
	public final ArrayList<SensorState> sensorStates;
	
	public PossibleStatesMessage(ArrayList<SensorState> sensorStates, SensorAgentDescription 	SensorAgent, SensorDescription Sensor )
	{
		// initialise the parent class
		super("PossibleStates", SensorAgent, Sensor);
				
		this.sensorStates = sensorStates;
	}

	@Override
	public String toString()
	{
		return super.toString() + "|" + this.sensorStates.toString();
	}
	
	public ArrayList<Double> asDIASArrayList()
	{
		ArrayList<Double> ret = new ArrayList<Double>();
		
		for( SensorState s : sensorStates )
		{
			if( s.stateValues.size() != 1 )
			{	
				System.err.printf( "asDIASArrayList: attempting to convert %d dimensional sensor date to a single Double\n", s.stateValues.size() );
				return null;
			}
			else
			{
				for( String key : s.stateValues.keySet() )
				{
					ret.add( (double) s.stateValues.get(key) );
				}
			}
			
		}
		
		return ret;
		
		
	}
}
