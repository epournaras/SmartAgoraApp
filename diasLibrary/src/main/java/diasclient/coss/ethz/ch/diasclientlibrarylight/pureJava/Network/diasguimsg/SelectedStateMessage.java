package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class SelectedStateMessage extends GUIMessage
{
	public final SensorState			sensorState;
	
	public SelectedStateMessage( SensorState sensorState, SensorAgentDescription 	SensorAgent, SensorDescription Sensor )
	{
		// initialise the parent class
		super("SelectedState", SensorAgent, Sensor);
				
		this.sensorState = sensorState;
	}

	@Override
	public String toString()
	{
		return super.toString() + "|" + this.sensorState.toString();
	}
}
