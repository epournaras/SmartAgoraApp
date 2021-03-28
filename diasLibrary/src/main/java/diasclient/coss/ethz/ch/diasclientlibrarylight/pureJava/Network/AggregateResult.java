package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network;

import android.util.Log;

import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements.Logger;

/**
 * AggregateResult gets the Result as soon as ready, or when timeOut is reached
 */
public class AggregateResult {

    private final String LOGTAG = "AggregateResult:";

    private final int UUID;
    private double value = 0.0;
    private volatile boolean isSet = false, isCancelled = false;
    private Logger logger;
    private String sensorID;

    AggregateResult(int UUID, Logger logger, String sensorID) {
        this.UUID = UUID;
        this.logger = logger;
        this.sensorID = sensorID;
    }

    /**
     * true if calculation was finished
     *
     * @return
     */
    public synchronized boolean isReceived() {
        return isSet;
    }

    /**
     * true if calculation was canceled
     *
     * @return
     */
    public synchronized boolean isCancelled() {
        return isCancelled;
    }

    /**
     * true, if get() will not block anymore -> result didn't come back and result wasn't cancelled
     * false -> get() will block
     *
     * @return
     */
    public synchronized boolean isFinished() {
        return isSet || isCancelled;
    }

    /**
     * if true, calculation is still ongoing, if false, calculation finished
     * false -> get() will not block
     *
     * @return
     */
    public synchronized boolean isStillRunning() {
        return !isCancelled && !isSet;
    }

    /**
     * waits until the value was received or action was canceled
     *
     * @return value if received, null if canceled
     * @throws InterruptedException
     */
    public synchronized Double get() throws InterruptedException {
        while (!isSet) {
            if (isCancelled) {
                Log.e(LOGTAG, "Aggregate was cancelled");
                return null;
            }
            wait();
        }
        Log.d(LOGTAG, "Value received: " + value);
        return value;
    }

    /**
     * Waits for inputted amount of milliseconds
     *
     * @param waitingPeriod
     * @return value if received within waitingPeriod, null elsewhise
     * @throws InterruptedException if interrupted
     */
    public synchronized Double get(long waitingPeriod) throws InterruptedException {
        if (isCancelled) { //if cancelled, return null
            Log.e(LOGTAG, "Aggregate was cancelled");
            return null;
        }
        if (!isSet) //if not yet set and not cancelled, wait
            wait(waitingPeriod);
        if (isSet) { //if now set, return
            Log.d(LOGTAG, "Value received: " + value);
            return value;
        }
        Log.d(LOGTAG, "Value after waiting still not ready, isCancelled: " + isCancelled);
        return null; //either cancelled or still not ready -> return null
    }

    /**
     * don't wait at all and immediately return
     *
     * @return
     */
    public synchronized Double getImmediately() {
        if (isSet) {
            Log.d(LOGTAG, "Value immediately received: " + value);
            return value;
        }
        Log.d(LOGTAG, "Value not immediately received");
        return null;
    }

    /**
     * internal, should not be visible for library user
     * sets value of AggregateResult, notifies waiting threads, sets isSet to true
     *
     * @param value
     */
    synchronized boolean set(double value) {
        if (isSet || isCancelled) {
            Log.e(LOGTAG, "Tried to set value, but " + (isSet ? "value was already set" : "process was already cancelled"));
            return false;
        }
        this.value = value;
        isSet = true;
        notifyAll();
        Log.i(LOGTAG, "Value set to " + value);

        if (logger != null)
            logger.log("Value from server received: " + value, sensorID);

        return true;
    }

    /**
     * internal, should not be visible for library user
     * cancels action, notifies waiting threads
     */
    synchronized void cancel() {
        if (isSet) { //if already set, should not be cancelled
            Log.e(LOGTAG, "Tried to cancel already finished process");
            return;
        }
        Log.i(LOGTAG, "Process was cancelled");
        isCancelled = true;
        if (logger != null)
            logger.log("Value receiving cancelled: null", sensorID);
        notifyAll();
    }

    boolean matchesID(int id) {
        return this.UUID == id;
    }

    @Override
    public String toString() {
        return "AggregateValue: {isSet: " + isSet + ",isCancelled: " + isCancelled + ", UUID: " + UUID + ",value: " + value + "}";
    }
}
