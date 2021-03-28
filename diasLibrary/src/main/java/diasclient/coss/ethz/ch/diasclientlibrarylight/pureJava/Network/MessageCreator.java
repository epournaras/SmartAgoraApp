package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.Network.PersistentStorage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.AggregationType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.AggregateRequestMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.HeartbeatMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.LeaveMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.PeerAddressRequestMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.PossibleStatesMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.SelectedStateMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.SensorAgentDescription;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.SensorDescription;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.SensorSetControlMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.SensorState;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.StartAggregationMessage;

import static diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.NetworkConstants.SensorPrefix;
import static diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.NetworkConstants.UUIDPORT;

/**
 * This Class creates the Messages required from the server
 * <p>
 * Author: Renato Kunz
 */
class MessageCreator {
    private String UUID;

    /**
     * Instanciates the class
     *
     * @param persistentStorage
     */
    public MessageCreator(PersistentStorage persistentStorage) {
        UUID = persistentStorage.getUUID();
    }

    /**
     * Creates a possibleStates Message
     *
     * @param dataSourceID       id of sensor
     * @param possibleStates values
     * @return
     */
    public PossibleStatesMessage createPSMessage1D(String dataSourceID, double[] possibleStates) {
        SensorAgentDescription sensorAgent = new SensorAgentDescription(UUID, UUIDPORT); //TODO: change UUID

        SensorDescription sensorDescription = new SensorDescription(SensorPrefix + dataSourceID, null); //TODO: change SensorType

        ArrayList<SensorState> clusters = new ArrayList<>();

        for (int i = 0; i < possibleStates.length; i++) {
            LinkedHashMap<String, Object> state = new LinkedHashMap<>();
            state.put("x.1", possibleStates[i]);
            clusters.add(new SensorState(i, state));
        }

        return new PossibleStatesMessage(clusters, sensorAgent, sensorDescription);
    }

    /**
     * Creates a SelectedStates Message
     *
     * @param dataSourceID      id of sensor
     * @param selectedState values
     * @param stateID       index of SS in PS
     * @return
     */
    public SelectedStateMessage cresteSSMessage1D(String dataSourceID, double selectedState, int stateID) {
        SensorAgentDescription sensorAgent = new SensorAgentDescription(UUID, UUIDPORT); //TODO: change UUID

        SensorDescription sensorDescription = new SensorDescription(SensorPrefix + dataSourceID, null); //TODO: change SensorType

        LinkedHashMap<String, Object> state = new LinkedHashMap<>();
        state.put("x.1", selectedState);
        SensorState sensorState = new SensorState(stateID, state);

        return new SelectedStateMessage(sensorState, sensorAgent, sensorDescription);

    }

    /**
     * Creates a Leave Message
     *
     * @param dataSourceID id of sensor
     * @return
     */
    public LeaveMessage createLeaveMessage(String dataSourceID) {
        SensorAgentDescription sensorAgent = new SensorAgentDescription(UUID, UUIDPORT); //TODO: change UUID

        SensorDescription sensorDescription = new SensorDescription(SensorPrefix + dataSourceID, null); //TODO: change SensorType

        return new LeaveMessage(sensorAgent, sensorDescription);
    }

    /**
     * Creates an AggregationRequest Message, returns null if sensorType is unknown
     *
     * @param dataSourceID id of sensor
     * @param type     aggregationTyp
     * @return
     */
    public AggregateRequestMessage createAggregateRequestMessage(String dataSourceID, AggregationType type) {
        String stringType = getStringType(type);
        if (stringType == null)
            return null;

        SensorAgentDescription sensorAgent = new SensorAgentDescription(UUID, UUIDPORT); //TODO: change UUID

        SensorDescription sensorDescription = new SensorDescription(SensorPrefix + dataSourceID, null); //TODO: change SensorType

        return new AggregateRequestMessage(sensorAgent, sensorDescription, "topic", stringType); //TODO: change topic
    }

    /**
     * Create a PeerAddressRequestMessage
     *
     * @param dataSourceID id of sensor
     * @return
     */
    public PeerAddressRequestMessage createPeerAddressRequestMessage(String dataSourceID) {
        SensorAgentDescription sensorAgent = new SensorAgentDescription(UUID, UUIDPORT); //TODO: change UUID

        SensorDescription sensorDescription = new SensorDescription(SensorPrefix + dataSourceID, null);

        return new PeerAddressRequestMessage(sensorAgent, sensorDescription);
    }

    /**
     * Creates timeoutMessage for DIAS server
     *
     * @param dataSourceID
     * @param timeout
     * @return
     */
    public SensorSetControlMessage createTimeOutMessage(String dataSourceID, long timeout) {
        SensorAgentDescription sensorAgent = new SensorAgentDescription(UUID, UUIDPORT); //TODO: change UUID

        SensorDescription sensorDescription = new SensorDescription(SensorPrefix + dataSourceID, null);

        return new SensorSetControlMessage(sensorAgent, sensorDescription, "TimeoutMs", String.valueOf(timeout));
    }

    /**
     * Creates StartAggregationMessage for DIAS server
     *
     * @return
     */
    public StartAggregationMessage createStartAggregationMessage(String dataSourceID) {
        SensorAgentDescription sensorAgent = new SensorAgentDescription(UUID, UUIDPORT); //TODO: change UUID

        SensorDescription sensorDescription = new SensorDescription(SensorPrefix + dataSourceID, null);

        return new StartAggregationMessage(sensorAgent, sensorDescription);
    }

    /**
     * creates Heartbeat message for DIAS server
     *
     * @param dataSourceID
     * @return
     */
    public HeartbeatMessage createHeartBeatMessage(String dataSourceID) {
        SensorAgentDescription sensorAgent = new SensorAgentDescription(UUID, UUIDPORT); //TODO: change UUID

        SensorDescription sensorDescription = new SensorDescription(SensorPrefix + dataSourceID, null);

        return new HeartbeatMessage(sensorAgent, sensorDescription);
    }

    /**
     * Returns String associated with enum
     *
     * @param type enum of type
     * @return
     */
    private String getStringType(AggregationType type) {
        switch (type) {
            case avg:
                return "avg";

            case sum:
                return "sum";

            case stdev:
                return "stddev";

            case max:
                return "max";

            case min:
                return "min";

            case count:
                return "count";

            default:
                Log.e("MessageCreator", "Unknown Message tpye: " + type.toString());
                return null;
        }
    }

}
