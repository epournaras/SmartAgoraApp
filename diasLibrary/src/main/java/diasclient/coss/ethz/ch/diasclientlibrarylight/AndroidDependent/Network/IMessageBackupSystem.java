package diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.Network;

import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.AggregationType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.MessageType;

interface IMessageBackupSystem {
    void  saveSelectedState(String dataSourceID, double value, int index);
    void savePossibleStates(String dataSourceID, double[] values);
    SelectedState getSelectedState(String dataSourceID);
    double[] getPossibleStates(String dataSourceID);
    void close();
    void appendAggregateRequestMessageID(String dataSourceId, int id);
    Integer retrieveRequestMessageId(String dataSourceId);
    void addNewArType(String dataSourceID, AggregationType type);
    AggregationType removeLastArType(String dataSourceID);
}
