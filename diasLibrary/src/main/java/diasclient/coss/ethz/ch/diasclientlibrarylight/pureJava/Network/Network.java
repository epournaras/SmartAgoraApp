package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.Network.MessageBackupSystem;
import diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.Network.PersistentStorage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.Network.SelectedState;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements.Logger;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements.NOKError;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements.TimeoutReport;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.AggregationType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.MessageType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.AckMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.AggregateRequestMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.AggregateValueMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.GUIMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.HeartbeatMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.LeaveMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.PeerAddressMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.PeerAddressRequestMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.PossibleStatesMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.SelectedStateMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.SensorSetControlMessage;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg.StartAggregationMessage;

import static diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.NetworkConstants.OUTPROTOCOL;

/**
 * This class handles the communication to the DIAS server
 */
public class Network extends Thread {
    private String LOGTAG = "NETWORK:";

    //Thread own values

    private volatile boolean running;
    private boolean somethingHappened = false;

    //Messaging object
    private LinkedBlockingQueue<GUIMessage> messageList;

    //Network communication
    private Gson json;
    private boolean isOnline;
    private MessageBackupSystem mbs;
    private PersistentStorage persistentStorage;
    private MessageCreator messageCreator;
    public HashMap<String, Socketholder> socketMap;
    private ArrayList<String> livingIDs;
    private HashMap<String, HashMap<AggregationType, LinkedList<AggregateResult>>> aggregateResultBuffer;
    private int aggregateRequestCounter = 0;
    private int lastAggID = 0;

    //Arrays save messages of not yet connected dataSourceID's
    private HashMap<String, ArrayList<String>> connectionSocketBuffer;

    //Debugging classes
    private final int REPORTLISTSIZES = 100;
    private ArrayList<NOKError> receivedNOKs;
    private ArrayList<TimeoutReport> savedTimeoutReports;

    //timeout Variables
    private final long HEARTBEATINTERVAL = NetworkConstants.PEERTIMEOUT / 2;
    private long lastLogged;
    private long lastOnline;
    private long lastHeartBeat;

    private Logger logger;
    Context context;

    public Network(Context appContext, String gatewayIP, Logger logger) {
        //Thread own values
        this.running = true;

        context = appContext;

        //Messaging object
        messageList = new LinkedBlockingQueue<>();

        //Network communication
        json = new Gson();
        isOnline = false; //first network is offline
        mbs = new MessageBackupSystem(appContext);
        persistentStorage = new PersistentStorage(appContext, gatewayIP);
        messageCreator = new MessageCreator(persistentStorage);
        socketMap = new HashMap<>();
        livingIDs = new ArrayList<>();
        aggregateResultBuffer = new HashMap<>();

        //Arrays save messages of not yet connected dataSourceID's
        connectionSocketBuffer = new HashMap<>();

        //timeout variables
        lastLogged = 0;
        lastOnline = 0;
        lastHeartBeat = 0;

        //Debugging classes
        receivedNOKs = new ArrayList<>();
        savedTimeoutReports = new ArrayList<>();

        this.logger = logger;
    }

    /**
     * Returns an AggregateResult, which will return the from the server received value, once available
     *
     * @param dataSourceID
     * @param type
     * @return
     */
    public synchronized AggregateResult getAggregate(String dataSourceID, AggregationType type) {
        HashMap<AggregationType, LinkedList<AggregateResult>> hashMap = aggregateResultBuffer.get(dataSourceID);

        if (hashMap == null) {
            hashMap = new HashMap<>();
            for (AggregationType typeForInit : AggregationType.values()) {
                hashMap.put(typeForInit, new LinkedList<AggregateResult>());
            }
            aggregateResultBuffer.put(dataSourceID, hashMap);
        }

        if(!hashMap.containsKey(type)){
            hashMap.put(type, new LinkedList<AggregateResult>());
        }

        LinkedList<AggregateResult> buffer = hashMap.get(type);
        if (buffer.size() > 20) {
            Log.e(LOGTAG, "AggregatesBuffer overflow");
            buffer.removeFirst().cancel();
        }
        if (!appendAggregateRequestMessage(dataSourceID, type))
            return null;

        int UUID = getAggregateRequestCounterAndIncrement();
        lastAggID = UUID;
        mbs.appendAggregateRequestMessageID(dataSourceID, UUID);
        mbs.addNewArType(dataSourceID, type);

        AggregateResult ar = new AggregateResult(UUID, logger, dataSourceID);
        buffer.addLast(ar);
        Log.d(LOGTAG, "added AR no. " + UUID);
        return ar;
    }

    /**
     * Handles a received aggregateValueMessage
     *
     * @param Stringmessage
     */
    private synchronized void handleAggregateMessage1D(String Stringmessage) {
        AggregateValueMessage message = json.fromJson(Stringmessage, AggregateValueMessage.class);

        String dataSourceID = retreiveDataSourceID(message);
        AggregationType type = mbs.removeLastArType(dataSourceID);

        Integer UUID = mbs.retrieveRequestMessageId(dataSourceID);
        if (UUID == null) {
            Log.e(LOGTAG, "MessageBackupSystem didn't have any request-UUID for the requested peer");
            return;
        }
        HashMap<AggregationType, LinkedList<AggregateResult>> hashMap = aggregateResultBuffer.get(dataSourceID);

        if(!hashMap.containsKey(type)){
            hashMap.put(type, new LinkedList<AggregateResult>());
        }

        LinkedList<AggregateResult> buffer = hashMap.get(type);
        AggregateResult ar = null;
        for (int i = 0; i<buffer.size(); i++) {
            AggregateResult r = buffer.get(i);
            if (r.matchesID(UUID)) {
                buffer.remove(i);
                ar = r;
                break;
            }
        }
        if (ar == null) {
            Log.e(LOGTAG, "No element in buffer found UUID: " + UUID);
            return;
        }

        double value = (double) message.data.get(0);

        Log.d(LOGTAG, "Received Aggregate for dataSourceID: " + dataSourceID + ", type:" + type + ", value: " + value);

        ar.set(value);
    }

    /**
     * closes everything, needs to be called before nulling the object!
     */
    public void closeAll() {
        running = false;
        try {
            this.join();
        } catch (InterruptedException e) {
            Log.e(LOGTAG, "Error whilst waiting for thread to close: " + e.getMessage());
        }
        for (Socketholder socket : socketMap.values()) {
            socket.socket.setLinger(0);
            socket.socket.close();
        }
        mbs.close();
        Log.d(LOGTAG, "Closed Network");
    }

    /**
     * Sends next message of socket to server
     * (last message was not answered, so online value gets set to false)
     *
     * @param dataSourceID
     * @param socket
     */
    private void sendNextMessage(String dataSourceID, Socketholder socket) {
        removeIfAggregateRequest(dataSourceID, socket.getLastSentMessage());
        appendTimeoutReport(dataSourceID);
        socket.sent = false;
        socket.sendmsg();
        setOnline(false);
    }

    /**
     * Saves the peerAddress
     *
     * @param MessageAsString
     */
    private void savePeerAddress(String MessageAsString) {
        PeerAddressMessage message = json.fromJson(MessageAsString, PeerAddressMessage.class);

        String dataSourceID = retreiveDataSourceID(message);

        persistentStorage.put(dataSourceID, message.peerFinger, System.currentTimeMillis());
        persistentStorage.savePeerInfo(dataSourceID, message.peerId);

        Socketholder socket = getSocket(dataSourceID);
        sendTimeOutMessage(dataSourceID, socket);
        if (connectionSocketBuffer.containsKey(dataSourceID)) {
            for (String msg : connectionSocketBuffer.get(dataSourceID))
                socket.putmsg(msg);
            connectionSocketBuffer.remove(dataSourceID);
        }

    }

    void removeIfAggregateRequest(String dataSourceID, String lstMsg){
        GUIMessage msg = json.fromJson(lstMsg, GUIMessage.class);
        if (msg.MessageType.equals("AggregateRequestMessage"))
            removeAggregateRequest(dataSourceID);
        else Log.e(LOGTAG, "Message was not a request");
    }

    /**
     * Removes AggregateResult of cancelled request
     *
     * @param dataSourceID
     */
    private synchronized void removeAggregateRequest(String dataSourceID) {
        Integer UUID = mbs.retrieveRequestMessageId(dataSourceID);
        if (UUID == null) {
            Log.e(LOGTAG, "MessageBackupSystem didn't have any request-UUID for the requested peer");
            return;
        }
        HashMap<AggregationType, LinkedList<AggregateResult>> hashMap = aggregateResultBuffer.get(dataSourceID);
        AggregationType type = mbs.removeLastArType(dataSourceID);

        if(!hashMap.containsKey(type)){
            hashMap.put(type, new LinkedList<AggregateResult>());
        }

        LinkedList<AggregateResult> buffer = hashMap.get(type);
        AggregateResult ar = null;
        for (int i = 0; i<buffer.size(); i++) {
            AggregateResult r = buffer.get(i);
            if (r.matchesID(UUID)) {
                buffer.remove(i);
                ar = r;
                break;
            }
        }
        if (ar == null) {
            Log.e(LOGTAG, "No element in buffer found UUID: " + UUID + ", type: " + type);
            return;
        }
        ar.cancel();
        Log.i(LOGTAG, "Cancelled no " + UUID);
    }

    /**
     * Sends custom Timeout Message to peer, based on programmed timeout in NetworkConstants
     *
     * @param dataSourceID
     */
    private void sendTimeOutMessage(String dataSourceID, Socketholder socketholder) {
        long timeOut = NetworkConstants.PEERTIMEOUT;
        socketholder.putmsg(toJson(messageCreator.createTimeOutMessage(dataSourceID, timeOut)));

    }

    private void handleAckMessage(String stringMessage) {
        AckMessage message = json.fromJson(stringMessage, AckMessage.class);
        boolean handled = false;
        String dataSourceID = null;
        try{
            dataSourceID = retreiveDataSourceID(message);
        }catch (NullPointerException e){

        }
        int errCode = determineErrorCode(message);

        if (message.ackText.equals("OK")) { //ErrorCode 0 means no Error
            return;
        }
        if (getLatsMessageTypeSent(dataSourceID) == MessageType.AggregateRequest)
            removeAggregateRequest(dataSourceID);

        switch (errCode) {
            case 310: { //no possible states received

                String lastSent = recreateLastSentMessage(dataSourceID);
                Socketholder socket = getSocket(dataSourceID);
                socket.putOnFront(lastSent);
                resendPSPrioritized(dataSourceID);
                handled = true;
                break;
            }

            case 400: { //invalid stateID -> last sent message must be SS-message -> resend PS and SS
                //Append SS first, as PossibleStates need to be processed first from the peer (LIFO, if added at the head of Queue, like with a stack)
                resendSSPrioritized(dataSourceID);
                resendPSPrioritized(dataSourceID);
                handled = true;
                break;
            }
            case 401: {//SelectedState value mismatch for stateID, last message must be SS-Message -> resend PS and SS
                //Append SS first, as PossibleStates need to be processed first from the peer (LIFO, if added at the head of Queue, like with a stack)
                resendSSPrioritized(dataSourceID);
                resendPSPrioritized(dataSourceID);
                handled = true;
                break;
            }
            case 410: { //no slected state received
                String lastSent = recreateLastSentMessage(dataSourceID);
                Socketholder socket = getSocket(dataSourceID);
                socket.putOnFront(lastSent);
                resendSSPrioritized(dataSourceID);

                handled = true;
                break;
            }
            case 500: { //neither SS nor PS were received
                String lastSent = recreateLastSentMessage(dataSourceID);
                //Append SS first, as PossibleStates need to be processed first from the peer (LIFO, if added at the head of Queue, like with a stack)
                Socketholder socket = getSocket(dataSourceID);
                socket.putOnFront(lastSent);
                resendSSPrioritized(dataSourceID);
                resendPSPrioritized(dataSourceID);
            }
            default: {
                Log.e(LOGTAG, "Error case not handled, errno: " + errCode);
                if(message.errorText.equals("No peers available")){
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "No peers Available", Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.e(LOGTAG, "*************************** NO PEERS AVAILABLE **********************************");
                }
            }
        }
        appendNOKError(dataSourceID, errCode, message.errorText, handled);
    }

    /**
     * Only for error correcting purposes, adds message to front of queue of Socket
     *
     * @param dataSourceID
     */
    private void resendPSPrioritized(String dataSourceID) {
        double[] PS = mbs.getPossibleStates(dataSourceID);
        PossibleStatesMessage possibleStatesMessage = messageCreator.createPSMessage1D(dataSourceID, PS);
        appendPriorizedMessageDirectlyToSocket(possibleStatesMessage);
    }

    /**
     * Only for error correcting purposes, adds message to front of queue of Socket
     *
     * @param dataSourceID
     */
    private void resendSSPrioritized(String dataSourceID) {
        SelectedState selectedState = mbs.getSelectedState(dataSourceID);
        SelectedStateMessage selectedStateMessage =
                messageCreator.cresteSSMessage1D(dataSourceID, selectedState.value, selectedState.index);
        appendPriorizedMessageDirectlyToSocket(selectedStateMessage);
    }

    private MessageType getLatsMessageTypeSent(String dataSourceID){
        String messageAsString = getLastMessageString(dataSourceID);
        GUIMessage message = json.fromJson(messageAsString, GUIMessage.class);
        if(message == null)
            return null;

        String type = message.MessageType;

        switch (type) {
            case "Heartbeat":
                return MessageType.HeartBeat;

            case "StartAggregation":
                return MessageType.StartAggregation;

            case "Command":
                return MessageType.SensorSetControl;

            case "PeerAddressRequest":
                return null;

            case "AggregateRequestMessage":
                return MessageType.AggregateRequest;

            case "Leave":
                return MessageType.LeaveMessage;

            case "SelectedState":
                return MessageType.SelectedStates;

            case "PossibleStates":
                return MessageType.PossibleStates;

            default:
                return null;
        }
    }

    /**
     * Recreates a new version of the last sent message, already as a json
     *
     * @param dataSourceID
     * @return
     */
    private String recreateLastSentMessage(String dataSourceID) {
        if(dataSourceID == null)
            return null;
        String messageAsString = getLastMessageString(dataSourceID);
        if(messageAsString == null)
            return null;
        GUIMessage message = json.fromJson(messageAsString, GUIMessage.class);
        String type = message.MessageType;

        switch (type) {
            case "Heartbeat":
                HeartbeatMessage heartBeatMessage = messageCreator.createHeartBeatMessage(dataSourceID);
                return toJson(heartBeatMessage);

            case "StartAggregation":
                StartAggregationMessage startAggregationMessage = messageCreator.createStartAggregationMessage(dataSourceID);
                return toJson(startAggregationMessage);

            case "Command":
                SensorSetControlMessage sensorSetControlMessage = messageCreator.createTimeOutMessage(dataSourceID, NetworkConstants.PEERTIMEOUT);
                return toJson(sensorSetControlMessage);

            case "PeerAddressRequest":
                PeerAddressRequestMessage peerAddressRequestMessage = messageCreator.createPeerAddressRequestMessage(dataSourceID);
                return toJson(peerAddressRequestMessage);

            case "AggregateRequestMessage":
                AggregateRequestMessage oldMessage = json.fromJson(messageAsString, AggregateRequestMessage.class);
                AggregateRequestMessage aggregateRequestMessage = messageCreator.createAggregateRequestMessage(dataSourceID, AggregationType.valueOf(oldMessage.aggregateType));
                return toJson(aggregateRequestMessage);

            case "Leave":
                LeaveMessage leaveMessage = messageCreator.createLeaveMessage(dataSourceID);
                return toJson(leaveMessage);

            case "SelectedState":
                SelectedState ss = mbs.getSelectedState(dataSourceID);
                SelectedStateMessage selectedStateMessage = messageCreator.cresteSSMessage1D(dataSourceID, ss.value, ss.index);
                return toJson(selectedStateMessage);

            case "PossibleStates":
                double[] ps = mbs.getPossibleStates(dataSourceID);
                PossibleStatesMessage possibleStatesMessage = messageCreator.createPSMessage1D(dataSourceID, ps);
                return toJson(possibleStatesMessage);

            default:
                return null;
        }
    }

    /**
     * retrieves the last sent message from the socket
     *
     * @param dataSourceID
     * @return
     */
    private String getLastMessageString(String dataSourceID) {
        Socketholder socketholder = getSocket(dataSourceID);
        if(socketholder == null)
            return null;
        return socketholder.getLastSentMessage();
    }

    /**
     * returns the errorCode of the given AckMessage
     *
     * @param message
     * @return
     */
    private int determineErrorCode(AckMessage message) {
        return message.errorCode;
    }

    /**
     * Handles the received messages form the different sockets, which are open
     */
    private void sendReceiveHandle() {
        final long now = System.currentTimeMillis();
        for (Map.Entry<String, Socketholder> entry : socketMap.entrySet()) {
            Socketholder socket = entry.getValue();
            String dataSourceID = entry.getKey();
            if (socket.sent) {
                if (socket.lastsent <= now - 2 * NetworkConstants.CONNECTIONTIMEOUT)
                    sendNextMessage(dataSourceID, socket);

                String receivedMessage = null;

                try {
                    receivedMessage = socket.socket.recvStr(ZMQ.NOBLOCK);
                } catch (Exception e) {
                    Log.d(LOGTAG, "resetting socket " + dataSourceID);
                    socket.resetSocket();
                }

                if (receivedMessage != null) {
                    somethingHappened = true;
                    setOnline(true);
                    socket.sent = false;
                    Log.d(LOGTAG, "message received from: " + dataSourceID + ": " + receivedMessage);

                    GUIMessage message = json.fromJson(receivedMessage, GUIMessage.class);

                    switch (message.MessageType) {
                        case "PeerAddress":
                            savePeerAddress(receivedMessage);
                            break;

                        case "AggregateValueMessage":
                            handleAggregateMessage1D(receivedMessage);
                            break;

                        case "Ack":
                            handleAckMessage(receivedMessage);
                            break;

                        default:
                            Log.e(LOGTAG, "Message of type " + message.MessageType + " could not be resolved");
                    }
                }
            } else {
                socket.sendmsg();
            }
        }
        if (System.currentTimeMillis() - lastOnline > NetworkConstants.ONLINETIMEOUT) {
            setOnline(false);
        }
    }

    /**
     * appends timeout report to proper list (solely for debugging reasons)
     *
     * @param dataSourceID
     */
    private void appendTimeoutReport(String dataSourceID) {
        TimeoutReport report = new TimeoutReport(dataSourceID);

        if (savedTimeoutReports.size() >= REPORTLISTSIZES)
            savedTimeoutReports.remove(savedTimeoutReports.size() - 1);

        savedTimeoutReports.add(0, report); //newest reports are always more interesting -> in front of list
    }

    /**
     * appends NOK error to proper list (solely for debugging reasons)
     *
     * @param dataSourceID
     */
    private void appendNOKError(String dataSourceID, int errorID, String errormsg, boolean handled) {
        NOKError error = new NOKError(dataSourceID, errormsg, errorID, handled);

        if (receivedNOKs.size() >= REPORTLISTSIZES)
            receivedNOKs.remove(receivedNOKs.size() - 1);

        receivedNOKs.add(0, error); //newest errors are always more interesting -> in front of list
    }

    /**
     * returns all saved timeout reports (up to 50 max)
     *
     * @return
     */
    public ArrayList<TimeoutReport> getSavedTimeoutReports() {
        return new ArrayList<>(savedTimeoutReports);
    }

    /**
     * returns all received NOK messages (up to 50 max)
     *
     * @return
     */
    public ArrayList<NOKError> getReceivedNOKs() {
        return new ArrayList<>(receivedNOKs);
    }

    /**
     * Puts message in the socketbuffer, if there is no connection established yet for the corresponding ID,
     * asks the sever for a new connection. In this case the message is stored in a global buffer and added to
     * the socket buffer, as soon as the server sends the connection data for the Sensor.
     *
     * @param dataSourceID
     * @param message
     */
    private void prepareSend(String dataSourceID, GUIMessage message) {
        Socketholder sender = getSocket(dataSourceID);
        String serializedMessage = toJson(message);
        if (sender != null) {
            sender.putmsg(serializedMessage);
            Log.d(LOGTAG, "Sensor " + dataSourceID + ": Message thrown in queue: " + serializedMessage);
            return;
        }
        //else

        appendInGeneralBuffer(dataSourceID, serializedMessage);
    }

    private void appendInGeneralBuffer(String dataSourceID, String serializedMessage) {
        ArrayList<String> buffer = connectionSocketBuffer.get(dataSourceID);
        if (buffer == null) { //initialize buffer
            buffer = new ArrayList<String>();
            connectionSocketBuffer.put(dataSourceID, buffer);
        }

        if (buffer.size() >= NetworkConstants.BUFFERSIZE) {
            buffer.remove(0);
            Log.e(LOGTAG, "Bufferoverflow in buffer with dataSourceID; " + dataSourceID);
        }


        Log.d(LOGTAG, "Sensor " + dataSourceID + ": Message thrown in buffer");
        buffer.add(serializedMessage);
    }

    /**
     * handles incoming messages and appends them to the corresponding queue
     */
    private void handleMessage() {
        long now = System.currentTimeMillis();
        if (now - lastLogged > 30000) { //some logging to indicate that the thread is still running
            lastLogged = now;
            Log.i(LOGTAG, "still alive");
            for(String key : aggregateResultBuffer.keySet()){
                HashMap<AggregationType, LinkedList<AggregateResult>> list = aggregateResultBuffer.get(key);
                Log.i(LOGTAG, key + ": ");
                for(AggregationType type: list.keySet()){
                    LinkedList<AggregateResult> results = list.get(type);
                    Log.i(LOGTAG, type + ": " + results.toString());
                }
            }
        }

        GUIMessage message = retreiveMessage();

        if (message != null) {
            somethingHappened = true;
            String dataSourceID = retreiveDataSourceID(message);
            Log.d(LOGTAG, "handling message: " + message.MessageType + " of dataSourceID: " + dataSourceID);

            prepareSend(dataSourceID, message);
        }
        sendReceiveHandle();
    }

    /**
     * sends heartbeats if necessary
     */
    private void handleHeartBeats() {
        long currTimeMillis = System.currentTimeMillis();
        if (currTimeMillis - lastHeartBeat <= HEARTBEATINTERVAL)
            return;
        lastHeartBeat = currTimeMillis;

        for (String dataSourceID : livingIDs)
            appendHeartBeatMessage(dataSourceID);
        somethingHappened = true;
    }

    /**
     * Either gets Socket, if connection-information exists, or asks server for new socket.
     * returns null if no connection established yet;
     *
     * @param dataSourceID
     * @return
     */
    private Socketholder getSocket(String dataSourceID) {
        Socketholder socket = socketMap.get(dataSourceID);
        if (socket != null)
            return socket;

        ConnectionInfo connectionInfo = persistentStorage.get(dataSourceID);

        if (connectionInfo == null || connectionInfo.connection == null) {
            requestSocket(dataSourceID, connectionInfo);
            return null;
        }

        String connection = OUTPROTOCOL + connectionInfo.connection;
        Log.d(LOGTAG, "Connecting " + dataSourceID + " to " + connection);
        socket = new Socketholder(connection, this, dataSourceID);
        socketMap.put(dataSourceID, socket);

        return socket;
    }

    /**
     * Asks server for socket if timeout reached
     *
     * @param dataSourceID
     * @param info
     */
    private void requestSocket(String dataSourceID, ConnectionInfo info) {
        //if timeout not reached, do not retry yet
        if (info != null && info.timestampmillis > System.currentTimeMillis() - 4 * NetworkConstants.CONNECTIONTIMEOUT)
            return;


        Socketholder DIASGateway = getSocket(NetworkConstants.DIASGATEWAY);
        String messageString = toJson(messageCreator.createPeerAddressRequestMessage(dataSourceID));
        DIASGateway.putmsg(messageString);

        //save timestamp for timeout
        persistentStorage.put(dataSourceID, null, System.currentTimeMillis());

    }

    /**
     * Returns if Network thread has a working connection to the server
     *
     * @return
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * If onlineStatus is changed, send status to InterfaceListener,
     * save lastOnline - time in Millis
     *
     * @param isOnline whether online or not
     */
    private void setOnline(boolean isOnline) {
        if (isOnline)
            lastOnline = System.currentTimeMillis();

        if (this.isOnline == isOnline)
            return;

        this.isOnline = isOnline;
    }

    /**
     * Thread-safe method for adding methods to the tail of the pending queue of the Network-thread
     * If network was stopped, no new messages can be added
     *
     * @param message message to add
     * @return true if added successfully
     */
    private boolean appendMessage(GUIMessage message, boolean forced) {
        if (!(running || forced))
            return false;
        try {
            messageList.put(message);
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * Should only be used for error correcting messages, like resend of possible states, if the server did not receive them
     * Adds message directly to the head of the corresponding socket
     *
     * @param message
     */
    void appendPriorizedMessageDirectlyToSocket(GUIMessage message) {
        String dataSourceID = retreiveDataSourceID(message);

        Socketholder sender = getSocket(dataSourceID);
        String serializedMessage = toJson(message);
        if (sender != null) {
            sender.putOnFront(serializedMessage);
            Log.d(LOGTAG, "Sensor: " + dataSourceID + ": Message thrown at HEAD of queue: " + serializedMessage);
            return;
        }
        //else, should not be called (read log.e() below)

        Log.e(LOGTAG, "Ceck code, a priorized message was added to general buffer\n" +
                "this means there is no socket, but somehow a NOK message was received,\n" +
                "or at least processed concerning this dataSourceID, there is possibly a bug in the code!");

        appendInGeneralBuffer(dataSourceID, serializedMessage);
    }

    /**
     * Retreives message, returns null if queue is empty
     *
     * @return
     */
    private GUIMessage retreiveMessage() {
        return messageList.poll();
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                somethingHappened = false;
                handleMessage();
                handleHeartBeats();

                if (!somethingHappened) //if nothing happened, thread sleeps for a little while (not too long tho)
                    Thread.sleep(10);
            } catch (Exception e) {
                Log.i("NETOWRK", "CAUGHT EXCEPTION!!!!!: " + e.getMessage());
            }
        }

        //send leave for still connected peers
        String leaveMessage = "Appended finishing leave message without waiting to following peers: ";
        for (String dataSourceID : livingIDs)
            if (appendForcedLeaveMessage(dataSourceID))
                leaveMessage += dataSourceID + ", ";
        Log.d(LOGTAG, leaveMessage.substring(0, leaveMessage.length() - 2));

        sendRestOfMessages();
    }

    private void sendRestOfMessages() {
        String goodByeMessage = "Sent rest of messages, " + messageList.size() + " in total: " + messageList.toString();
        while (!messageList.isEmpty()) {
            handleMessage();
            handleHeartBeats();
        }
        Log.d(LOGTAG, goodByeMessage);
    }

    /**
     * Creates and appends a leave Message, even if the thread is already is closed and no more messages are accepted
     *
     * @param dataSourceID
     * @return
     */
    private boolean appendForcedLeaveMessage(String dataSourceID) {
        LeaveMessage leaveMessage = messageCreator.createLeaveMessage(dataSourceID);
        if (appendMessage(leaveMessage, true)) {
            if (livingIDs.contains(dataSourceID)) {
                Log.i("Network", "Sensor with ID " + dataSourceID + "removed");
                livingIDs.remove(dataSourceID);
            } else
                Log.i("Network", "Couldn't remove sensor " + dataSourceID + ", Hashmap: " + livingIDs.toString());
            return true;
        }
        return false;
    }

    /**
     * little wrapper method for json, is not that useful tho...
     *
     * @param message
     * @return
     */
    private String toJson(GUIMessage message) {
        return json.toJson(message);
    }

    /**
     * Retreives dataSourceID from creates Message
     *
     * @param message
     * @return
     */
    private String retreiveDataSourceID(GUIMessage message) {
        return message.srcSensor.SensorName.substring(NetworkConstants.SensorPrefix.length());
    }

    /**
     * gives a unique id for the AggregateRequest to match up with
     *
     * @return
     */
    private synchronized int getAggregateRequestCounterAndIncrement() {
        int returnValue = aggregateRequestCounter++;
        aggregateRequestCounter = aggregateRequestCounter % 5000;
        return returnValue;
    }

    //************** message sending part **************//

    /**
     * Creates and appends a heartbeat message
     *
     * @param dataSourceID
     * @return
     */
    private boolean appendHeartBeatMessage(String dataSourceID) {
        HeartbeatMessage heartbeatMessage = messageCreator.createHeartBeatMessage(dataSourceID);
        return appendMessage(heartbeatMessage, false);
    }

    /**
     * Creates and appends a PossibleStates Message
     *
     * @param dataSourceID
     * @param possibleStates
     * @return
     */
    public boolean appendPSMessage1D(String dataSourceID, double[] possibleStates) {
        mbs.savePossibleStates(dataSourceID, possibleStates);

        PossibleStatesMessage psMessage = messageCreator.createPSMessage1D(dataSourceID, possibleStates);
        if (appendMessage(psMessage, false)) {
            if (!livingIDs.contains(dataSourceID))
                livingIDs.add(dataSourceID);
            return true;
        }
        return false;
    }

    /**
     * Creates and appends a SelectedStates Message for SelectedStates in a single dimension!
     *
     * @param dataSourceID
     * @param selectedState
     * @return
     */
    public boolean appendSSMessage1D(String dataSourceID, double selectedState) {
        double[] PS = mbs.getPossibleStates(dataSourceID);

        int stateID = matchUpSelectedStateToPossibleStates(selectedState, PS);

        double matchedSelectedState = PS[stateID];

        mbs.saveSelectedState(dataSourceID, matchedSelectedState, stateID);

        SelectedStateMessage ssMessage = messageCreator.cresteSSMessage1D(dataSourceID, matchedSelectedState, stateID);
        if (appendMessage(ssMessage, false)) {
            if (!livingIDs.contains(dataSourceID))
                livingIDs.add(dataSourceID);
            return true;
        }
        return false;
    }

    /**
     * Creates and appends a leave Message
     *
     * @param dataSourceID
     * @return
     */
    public boolean appendLeaveMessage(String dataSourceID) {
        LeaveMessage leaveMessage = messageCreator.createLeaveMessage(dataSourceID);
        if (appendMessage(leaveMessage, false)) {
            if (livingIDs.contains(dataSourceID)) {
                Log.i("Network", "Sensor with ID " + dataSourceID + "removed");
                livingIDs.remove(dataSourceID);
            } else
                Log.e("Network", "Coldn't remove sensor " + dataSourceID + ", Hashmap: " + livingIDs.toString());
            return true;
        }
        return false;
    }

    /**
     * Creates and appends a start aggregation Message
     *
     * @param dataSourceID
     * @return
     */
    public boolean appendStartAggregationMessage(String dataSourceID) {
        StartAggregationMessage startAggregationMessage = messageCreator.createStartAggregationMessage(dataSourceID);
        if (appendMessage(startAggregationMessage, false)) {
            if (!livingIDs.contains(dataSourceID))
                livingIDs.add(dataSourceID);
            return true;
        }
        return false;
    }

    /**
     * Creates and appends an AggregateRequest Message, use getAggregate for using this outside of the Network class
     *
     * @param dataSourceID
     * @param type
     * @return
     */
    private boolean appendAggregateRequestMessage(String dataSourceID, AggregationType type) {
        AggregateRequestMessage aggReqMessage = messageCreator.createAggregateRequestMessage(dataSourceID, type);
        if (appendMessage(aggReqMessage, false)) {
            if (!livingIDs.contains(dataSourceID))
                livingIDs.add(dataSourceID);
            return true;
        }
        return false;
    }

    /**
     * matches up the index of the selected state in the possibleStates
     *
     * @param selectedState
     * @param PS
     * @return
     */
    private int matchUpSelectedStateToPossibleStates(double selectedState, double[] PS) {
        if (PS == null)
            throw new IllegalStateException("PossibleStates not yet set");

        int index = -1;
        double min = Double.MAX_VALUE;

        for (int i = 0; i < PS.length; i++) {
            double state = PS[i];
            double diff = Math.abs(selectedState - state);
            if (diff < min) {
                index = i;
                min = diff;
            }
        }
        return index;
    }

    /**
     * Removes all IP adresses other than gateway. It is important that this method is called BEFORE contacting the server.
     */
    public void reset() {
        persistentStorage.reset();
    }
}
