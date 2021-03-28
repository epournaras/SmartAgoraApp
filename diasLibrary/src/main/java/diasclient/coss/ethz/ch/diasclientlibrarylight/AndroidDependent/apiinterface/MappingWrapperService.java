package diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.apiinterface;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;

import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.AggregationType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.AggregateResult;

public class MappingWrapperService extends Service implements IMappingWrapper {
    private IBinder mBinder = new LocalBinder();
    MappingWrapper mappingWrapper;

    public MappingWrapperService(){
        this("tcp:0.0.0.0:0000");
    }

    public MappingWrapperService(String gatewayIP){
        mappingWrapper = new MappingWrapper(this, gatewayIP);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //close the mapping wrapper automatically when the service is terminated.
        mappingWrapper.cleanclose();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Binding is the way to use this service
        return mBinder;
    }

    /**
     * Returns if, after start of application, a connection to the server could be established,
     * if there is no message received for more than 5 seconds, online status gets set to false again
     *
     * @return true, if one was established (anyone)
     */
    @Override
    public boolean isOnline() {
        return mappingWrapper.isOnline();
    }

    /**
     * Sets possible states from 0 to (and with) n, step size is 1
     *
     * @param dataSourceID
     * @param n
     */
    @Override
    public void setPossibleStatesRange(String dataSourceID, int n) {
        mappingWrapper.setPossibleStatesRange(dataSourceID, n);
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
        mappingWrapper.setPossibleStatesRange(dataSourceID, lower, upper);
    }

    @Override
    public void setPossibleStatesRange(String dataSourceID, double lower, double upper, double steps){
        mappingWrapper.setPossibleStatesRange(dataSourceID, lower, upper, steps);
    }

    @Override
    public void setPossibleStates(String dataSourceID, double[] states){
        mappingWrapper.setPossibleStates(dataSourceID, states);
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
        return mappingWrapper.getAggregate(dataSourceID, aggregationType);
    }

    /**
     * Gets aggregate from server
     * @param dataSourceID
     * @param aggregationType
     * @return
     */
    @Override
    public AggregateResult getAggregate(String dataSourceID, AggregationType aggregationType) {
        return mappingWrapper.getAggregate(dataSourceID, aggregationType);
    }

    /**
     * leave sensor
     * @param dataSourceID
     */
    @Override
    public void leave(String dataSourceID) {
        mappingWrapper.leave(dataSourceID);
    }


    /**
     * Don't use this as onDestroy() handles everything
     */
    @Deprecated
    @Override
    public int cleanclose() {
        Log.e("MappingWrapperService", "cleanclose() was called, but this method doesn't do anything");
        return 0;
    }

    public class LocalBinder extends Binder {
        public MappingWrapperService getServerInstance() {
            return MappingWrapperService.this;
        }
    }
    /**
     * sets all the readings, which were inputted, computes the average and sends it to the server
     * @param dataSourceID
     * @param sensorreading
     */
    @Override
    public void sendReading(String dataSourceID, HashMap<String, Double> sensorreading) {
        mappingWrapper.sendReading(dataSourceID, sensorreading);
    }

    /**
     * sets all the readings, which were inputted, computes the average and sends it to the server
     * @param dataSourceID
     * @param readings
     */
    @Override
    public void sendReading(String dataSourceID, double... readings) {
        mappingWrapper.sendReading(dataSourceID, readings);
    }

    /**
     * sets redings of hashmap
     * @param dataSourceID
     * @param sensorreading
     */
    @Override
    public void setReading(String dataSourceID, HashMap<String, Double> sensorreading) {
        mappingWrapper.setReading(dataSourceID, sensorreading);
    }

    /**
     * Resets all connection information other than the gateway. It is important that this method is called BEFORE contacting the server.
     */
    @Override
    public void reset(){
        mappingWrapper.reset();
    }

    /**
     * Manually backs up the logs
     */
    @Override
    public void saveLogs(){
        mappingWrapper.saveLogs();
    }
}
