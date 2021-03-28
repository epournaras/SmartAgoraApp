package diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.apiinterface;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements.NOKError;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements.TimeoutReport;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.AggregationType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.AggregateResult;

public class MappingWrapper implements IMappingWrapper {
    /**
     * This class is specifically designed to link smartagora with the dias library. It therefore cuts down on functionalities in order to make usage easier.
     */

    private DIASInterface diasinterface;

    public MappingWrapper(Context appContext, String gatewayIP) {
        diasinterface = new DIASInterface(appContext, gatewayIP); //create DIAS interface, which will be used to communicate to the dias library
    }

    /**
     * Returns if, after start of application, a connection to the server could be established
     *
     * @return true, if one was established (anyone)
     */
    @Override
    public boolean isOnline() {
        return diasinterface.isOnline();
    }

    /**
     * Sets possible states from 0 to (and with) n, step size is 1
     *
     * @param dataSourceID
     * @param n
     */
    @Override
    public void setPossibleStatesRange(String dataSourceID, int n) {
        setPossibleStatesRange(dataSourceID, 0, n);
    }

    @Override
    public void setPossibleStates(String dataSourceID, double[] states){
        diasinterface.setPossibleStates(dataSourceID, states);
    }

    /**
     * Sets possible states from lower to (and with) upper, step size is 1
     *
     * @param dataSourceID
     * @param lower
     * @param upper
     */
    @Override
    public void setPossibleStatesRange(String dataSourceID, int lower, int upper) {
        int n = upper - lower + 1;
        double[] PS = new double[n];
        for (int i = 0; i < n; i++) {
            PS[i] = lower + i;
        }
        diasinterface.setPossibleStates(dataSourceID, PS);
    }

    @Override
    public void setPossibleStatesRange(String dataSourceID, double lower, double upper, double steps){
        int n = (int) Math.round((upper - lower)/steps) + 1;
        double[] PS = new double[n];
        int index = 0;
        for(double i = lower; i<=upper; i+=steps){
            PS[index++] = i;
        }
        diasinterface.setPossibleStates(dataSourceID, PS);
    }


    /**
     * sets readings of hashmap
     *
     * @param dataSourceID
     * @param sensorreading
     */
    @Override
    public void setReading(String dataSourceID, HashMap<String, Double> sensorreading) {
        diasinterface.setReading(dataSourceID, sensorreading);
        diasinterface.startAgg(dataSourceID);
    }

    /**
     * sets all the readings, which were inputted, computes the average and sends it to the server
     *
     * @param dataSourceID
     * @param sensorreading
     */
    @Override
    public void sendReading(String dataSourceID, HashMap<String, Double> sensorreading) {
        setReading(dataSourceID, sensorreading);
    }

    /**
     * sets all the readings, which were inputted, computes the average and sends it to the server
     *
     * @param dataSourceID
     * @param readings
     */
    @Override
    public void sendReading(String dataSourceID, double... readings) {
        double sum = 0;
        for (double i : readings)
            sum += i;
        sum /= readings.length;
        diasinterface.setReading(dataSourceID, sum);
        diasinterface.startAgg(dataSourceID);
    }

    /**
     * Gets aggregate from server
     *
     * @deprecated use AggregationType, rather than String (method below)
     *
     * @param dataSourceID
     * @param aggregationType
     * @return
     */
    @Deprecated
    @Override
    public AggregateResult getAggregate(String dataSourceID, String aggregationType) {
        return diasinterface.getAggregate(dataSourceID, aggregationType);
    }

    /**
     * Gets aggregate from server
     *
     * @param dataSourceID
     * @param aggregationType
     * @return
     */
    @Override
    public AggregateResult getAggregate(String dataSourceID, AggregationType aggregationType) {
        return diasinterface.getAggregate(dataSourceID, aggregationType);
    }

    /**
     * leave sensor
     *
     * @param dataSourceID
     */
    @Override
    public void leave(String dataSourceID) {
        //disconnect sensor from dias peer. Will only close sockets and send leave message to peer.
        //as nodes can not really leave a network completely and a specific clients sensor should always reconnect to the same peer the information stored in persistent storage is not removed..
        diasinterface.leave(dataSourceID);
    }

    /**
     * Resets all connection informaiton. It is important that this method is called BEFORE contacting the server.
     */
    @Override
    public void reset() {
        //resets persistent storage. Equivalent to deleting all data in the settings menu.
        diasinterface.reset();
    }

    /**
     * closes the MappingWrapper class correctly
     *
     * @return
     */
    @Override
    public int cleanclose() {
        return diasinterface.cleanclose(); //shuts down the whole library. Please use the service to prevent faulty use.
    }


    public ArrayList<NOKError> getNOKs() {
        return diasinterface.getNOKs();
    }

    public ArrayList<TimeoutReport> getTimeoutReports() {
        return diasinterface.getTimeoutReports();
    }

    /**
     * Manually backs up the logs
     */
    @Override
    public void saveLogs(){
        diasinterface.saveLogs();
    }
}
