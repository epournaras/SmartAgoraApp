package diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.apiinterface;

import java.util.ArrayList;
import java.util.HashMap;

import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.AggregationType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.AggregateResult;

public interface IDIASInterface {


    //should be called on application start a complete Arraylist of HashMap
    void setPossibleStates(String dataSourceID, ArrayList<HashMap<String, Double>> possiblestates);
    void setPossibleStates(String dataSourceID, double[] possibleStates);

    void setSelectedState(String dataSourceID, HashMap<String, Double> selectedstate);

    //should be called whenever the track is finished
    void setReading(String dataSourceID, HashMap<String, Double> sensorreading);

    void setReading(String dataSourceID, double reading);

    //should be called on start of application/ start of walk
    AggregateResult getAggregate(String dataSourceID, String aggregationType);

    AggregateResult getAggregate(String dataSourceID, AggregationType aggregationType);

    //should be called when leaving a sensor
    void leave(String dataSourceID);

    void reset(); //reset states should only be called if user wishes to
    int cleanclose(); //close whole DIASClient

    boolean isOnline();

    void startAgg(String dataSourceID);

    void saveLogs();
}
