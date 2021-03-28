package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network;

import android.util.Log;

import org.zeromq.ZMQ;

import java.util.concurrent.LinkedBlockingDeque;

import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.GUIMessage;

/**
 * Each SocketHolder has its own context due to past crashing of whole ZMQ System
 * <p>
 * edited by Renato Kunz
 */
public class Socketholder {
    private final String LOGTAG = "Socketholder";

    ZMQ.Socket socket; // REQREO socket to internet
    long lastsent = 0; // last action send or receive for timeout
    boolean sent = false; // msg was sent if true receive on this socket until timeout
    private LinkedBlockingDeque<String> buffer = new LinkedBlockingDeque<>();
    private ZMQ.Context context;
    private String target;
    private String lastSentMessage = null;
    private Network mother;
    private String dataSourceID;

    public Socketholder(String connectionString, Network mother, String dataSourceID) {
        target = connectionString;
        context = ZMQ.context(1);
        socket = context.socket(ZMQ.REQ);
        socket.setLinger(10);
        socket.setReqRelaxed(true);
        socket.connect(target);
        Log.d(LOGTAG, "connecting to: " + target);
        this.mother = mother;
        this.dataSourceID = dataSourceID;
    }

    /**
     * Adds message to buffer
     *
     * @param msg
     * @return
     */
    void putmsg(String msg) {// used to put message in buffer will be checked with sendmsg command

        if (buffer.size() >= NetworkConstants.BUFFERSIZE) {
            String remMSG = buffer.removeFirst();
            mother.removeIfAggregateRequest(dataSourceID, remMSG);
            Log.e(LOGTAG, "Buffer overflow!");
        }
        buffer.addLast(msg);
    }

    /**
     * Adds message to head of buffer
     *
     * @param msg
     */
    void putOnFront(String msg) {
        if (buffer.size() >= NetworkConstants.BUFFERSIZE) {
            buffer.removeFirst();
            Log.e(LOGTAG, "Buffer overflow!");
        }
        buffer.addFirst(msg);
    }

    /**
     * Sends next message to server
     *
     * @return
     */
    boolean sendmsg() { // only used in loop otherwise the message will be lost, use putmsg method

        boolean out = false;
        if (buffer.size() > 0) {
            String bufferEl = buffer.pollFirst();

            out = socket.send(bufferEl.getBytes(), ZMQ.NOBLOCK); // send non blocking messages

            Log.d("Socket sent: ", bufferEl);
            lastsent = System.currentTimeMillis();
            sent = true;
            lastSentMessage = bufferEl;
            buffer.remove(0);
        }
        return out;
    }

    String getLastSentMessage(){
        return lastSentMessage;
    }

    /**
     * resets socket (closes socket, closes context, gets new context, creates new socket)
     */
    void resetSocket() {
        //close

        socket.setLinger(0);
        socket.close();
        context.close();

        //reassign, reopen
        context = ZMQ.context(1);
        socket = context.socket(ZMQ.REQ);
        socket.setLinger(10);
        socket.setReqRelaxed(true);
        socket.connect(target);
        Log.e(LOGTAG, "Reconnecting to: " + target);
    }
}