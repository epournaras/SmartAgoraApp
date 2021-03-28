package diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.Network;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedList;

import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.AggregationType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.MessageType;

/**
 * This class locally saves the values of the last sent Possible and Selected States
 * and returns them when needed.
 * <p>
 * Author: Renato Kunz
 */

public class MessageBackupSystem implements IMessageBackupSystem {
    private Gson json;
    private SharedPreferences sp;
    private HashMap<String, double[]> possibleStates;
    private HashMap<String, SelectedState> selectedState;
    private HashMap<String, LinkedList<Integer>> lastAggregateRequest;
    private HashMap<String, LinkedList<AggregationType>> lastArType;

    public MessageBackupSystem(Context context) {
        json = new Gson();
        sp = context.getSharedPreferences("MessageBackupSystem", Context.MODE_PRIVATE);

        lastAggregateRequest = new HashMap<>();
        lastArType = new HashMap<>();

        String serializedPossibleStates = sp.getString("PossibleStates", null);
        if (serializedPossibleStates == null) {
            possibleStates = new HashMap<>();
        } else {
            possibleStates = json.fromJson(serializedPossibleStates, new TypeToken<HashMap<String, double[]>>() {
            }.getType());
        }

        String serializedSelectedStates = sp.getString("SelectedStates", null);
        if (serializedSelectedStates == null) {
            selectedState = new HashMap<>();
        } else {
            selectedState = json.fromJson(serializedSelectedStates, new TypeToken<HashMap<String, SelectedState>>() {
            }.getType());
        }
    }

    /**
     * Save Selected states in form of a double value and an index of which possible state is used;
     *
     * @param dataSourceID
     * @param value
     * @param index
     */
    @Override
    public void saveSelectedState(String dataSourceID, double value, int index) {
        selectedState.put(dataSourceID, new SelectedState(value, index));
    }

    /**
     * Save Possible states in form of an array of doubles
     *
     * @param dataSourceID
     * @param values
     */
    @Override
    public void savePossibleStates(String dataSourceID, double[] values) {
        possibleStates.put(dataSourceID, values);
    }

    /**
     * Get last saved SelectedState of requested ID
     *
     * @param dataSourceID
     * @return
     */
    @Override
    public SelectedState getSelectedState(String dataSourceID) {
        return selectedState.get(dataSourceID);
    }

    /**
     * Sets the last requested AggregationType
     *
     * @param dataSourceID
     * @param type
     */
    @Override
    public synchronized void addNewArType(String dataSourceID, AggregationType type) {
        if (!lastArType.containsKey(dataSourceID))
            lastArType.put(dataSourceID, new LinkedList<AggregationType>());
        lastArType.get(dataSourceID).addLast(type);
    }

    /**
     * Gets the last requested AggregationType
     *
     * @param dataSourceID
     * @return
     */
    @Override
    public synchronized AggregationType removeLastArType(String dataSourceID) {
        if (!lastArType.containsKey(dataSourceID))
            lastArType.put(dataSourceID, new LinkedList<AggregationType>());
        return lastArType.get(dataSourceID).removeFirst();
    }

    /**
     * Saves the last sent aggregateRequest-ID
     *
     * @param dataSourceId
     * @param id
     */
    @Override
    public void appendAggregateRequestMessageID(String dataSourceId, int id) {
        if(!lastAggregateRequest.containsKey(dataSourceId))
            lastAggregateRequest.put(dataSourceId, new LinkedList<Integer>());
        lastAggregateRequest.get(dataSourceId).addLast(id);
    }

    /**
     * Retrieves last sent aggregateRequest-ID, null if nothing sent before
     *
     * @param dataSourceId
     * @return
     */
    @Override
    public Integer retrieveRequestMessageId(String dataSourceId) {
        if(!lastAggregateRequest.containsKey(dataSourceId))
            lastAggregateRequest.put(dataSourceId, new LinkedList<Integer>());
        return lastAggregateRequest.get(dataSourceId).removeFirst();
    }

    /**
     * get last saved PossibleState of requested ID
     *
     * @param dataSourceID
     * @return
     */
    @Override
    public double[] getPossibleStates(String dataSourceID) {
        return possibleStates.get(dataSourceID);
    }

    /**
     * Save the values persistently in memory
     */
    private void backupData() {
        String serialzedPossibleStates = json.toJson(possibleStates);
        String serializedSelectedStates = json.toJson(selectedState);

        sp.edit()
                .putString("PossibleStates", serialzedPossibleStates)
                .putString("SelectedStates", serializedSelectedStates)
                .apply();
    }

    /**
     * Close class, backups the data handled
     */
    @Override
    public void close() {
        backupData();
    }

}
