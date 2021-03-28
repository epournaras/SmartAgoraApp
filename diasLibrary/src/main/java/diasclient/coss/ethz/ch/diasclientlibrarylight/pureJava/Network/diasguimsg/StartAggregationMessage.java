package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class StartAggregationMessage extends GUIMessage {

    public StartAggregationMessage(SensorAgentDescription SensorAgent, SensorDescription sensorDescription) {
        // initialise the parent class
        super("StartAggregation", SensorAgent, sensorDescription);

    }


}
