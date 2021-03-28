package diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.apiinterface;

import java.util.HashMap;

import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.AggregationType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.AggregateResult;


interface IMappingWrapper {

    void setReading(String dataSourceID, HashMap<String, Double> sensorreading);

    AggregateResult getAggregate(String dataSourceID, String aggregationType);

    AggregateResult getAggregate(String dataSourceID, AggregationType aggregationType);

    void leave(String dataSourceID);

    int cleanclose();

    boolean isOnline();

    void setPossibleStatesRange(String dataSourceID, int n);

    void setPossibleStatesRange(String dataSourceID, int lower, int upper);

    void setPossibleStatesRange(String dataSourceID, double lower, double upper, double steps);

    void setPossibleStates(String dataSourceID, double[] states);

    void sendReading(String dataSourceID, HashMap<String, Double> sensorreading);

    void sendReading(String dataSourceID, double... readings);

    void reset();

    void saveLogs();
}