package diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.apiinterface;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.Network.PersistentStorage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements.Logger;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements.NOKError;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements.TimeoutReport;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.AggregationType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.AggregateResult;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.Network;

public class DIASInterface implements IDIASInterface {

    final private String LOGNAME = "DIASInterface";
    private int index;
    private SharedPreferences sp;

    private Context appContext;

    private Gson gson = new Gson();

    private Network network;

    private Logger logger;

    /**
     * Initialises DIAS Client
     *
     * @param appContext context of App
     */
    public DIASInterface(Context appContext, String gatewayIP) {
        logger = new Logger();
        network = new Network(appContext, gatewayIP, logger);
        network.start();
        this.appContext = appContext;

        sp = appContext.getSharedPreferences(LOGNAME, Context.MODE_PRIVATE);
        index = sp.getInt("INDEX", 0);
    }

    private String createAcceptedID(String id) {
        return "SmartAgora_" + id;
    }

    /**
     * sets possisblestates message on server
     *
     * @param dataSourceID
     * @param possiblestates
     * @deprecated please prefer the usage of a array of doubles, instead of an ArrayList of HashMaps
     */
    @Deprecated
    @Override
    public void setPossibleStates(String dataSourceID, ArrayList<HashMap<String, Double>> possiblestates) {
        double[] PS = new double[possiblestates.size()];

        for (int i = 0; i < PS.length; i++) PS[i] = possiblestates.get(i).get("x.1");

        this.setPossibleStates(dataSourceID, PS);

    }

    /**
     * sets possisblestates message in memory
     *
     * @param dataSourceID
     * @param possibleStates
     */
    @Override
    public void setPossibleStates(String dataSourceID, double[] possibleStates) {
        network.appendPSMessage1D(createAcceptedID(dataSourceID), possibleStates);
        logger.log("Possible States set: " + Arrays.toString(possibleStates), dataSourceID);
    }

    /**
     * sets selectedstate in memory
     *
     * @param dataSourceID
     * @param selectedstate
     * @deprecated prefer usage of setReading with double
     */
    @Deprecated
    @Override
    public void setSelectedState(String dataSourceID, HashMap<String, Double> selectedstate) {
        double selectedState = selectedstate.get("x.1");

        network.appendSSMessage1D(createAcceptedID(dataSourceID), selectedState);
        logger.log("Selected State set: " + selectedState, dataSourceID);
    }

    /**
     * Sets reading to virtual sensor
     *
     * @param dataSourceID
     * @param sensorreading
     * @deprecated prefer usage of double value, instead of HashMap
     */
    @Deprecated
    @Override
    public void setReading(String dataSourceID, HashMap<String, Double> sensorreading) {
        double avg = 0;
        for (Double value : sensorreading.values())
            avg += value;
        avg /= sensorreading.size();

        this.setReading(dataSourceID, avg);
    }

    /**
     * sets single reading point and sends it to vitual sensor
     *
     * @param dataSourceID
     * @param reading
     */
    @Override
    public void setReading(String dataSourceID, double reading) {
        network.appendSSMessage1D(createAcceptedID(dataSourceID), reading);
        logger.log("Selected State set: " + reading, dataSourceID);
    }

    /**
     * Tries to get Aggregate from server,
     *
     * @param dataSourceID
     * @param aggregationType
     * @return
     */
    @Deprecated
    @Override
    public AggregateResult getAggregate(String dataSourceID, String aggregationType) {
        return network.getAggregate(createAcceptedID(dataSourceID), AggregationType.valueOf(aggregationType));
    }

    /**
     * Tries to get Aggregate from server,
     *
     * @param dataSourceID
     * @param aggregationType
     * @return
     */
    @Override
    public AggregateResult getAggregate(String dataSourceID, AggregationType aggregationType) {
        return network.getAggregate(createAcceptedID(dataSourceID), aggregationType);
    }

    @Override
    public void leave(String dataSourceID) {
        network.appendLeaveMessage(createAcceptedID(dataSourceID));
        logger.log("Leave message sent", dataSourceID);
    }


    /**
     * reset all IP addresses
     */
    @Override
    public void reset() {
        appContext.getSharedPreferences(PersistentStorage.PERSISTENT_STORAGE, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
        network.reset();
    }

    /**
     * Closes all sockets, please call method before nulling the object
     *
     * @return
     */
    @Override
    public int cleanclose() {
        network.closeAll();
        logger.log("Closed network, possibly sent leave messages", "All");
        saveLogs();

        return 1;
    }

    /**
     * backs up the logs manually
     */
    @Override
    public void saveLogs(){
        logger.log("TEST", "TEST");
        Logger.LogTuple tuple = logger.getLogTuple();
        LinkedList<String> logFile = tuple.logFile;
        LinkedList<String> timeStamps = tuple.timestamp;

        if (logFile != null && timeStamps != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("LOGGING" + index, gson.toJson(logFile));
            editor.putString("TIMESTAMPS" + index, gson.toJson(timeStamps));
            editor.putInt("INDEX", index + 1);
            index++;
            editor.apply();
            Log.e("LOGGER", "saved logs");
        }else
            Log.e("LOGGER", "DIDN't save logs");
    }

    @Override
    public boolean isOnline() {
        return network.isOnline();
    }

    @Override
    public void startAgg(String dataSourceID) {
        network.appendStartAggregationMessage(createAcceptedID(dataSourceID));
        logger.log("Startaggregation message sent", dataSourceID);
    }

    public ArrayList<NOKError> getNOKs() {
        return network.getReceivedNOKs();
    }

    public ArrayList<TimeoutReport> getTimeoutReports() {
        return network.getSavedTimeoutReports();
    }
}
