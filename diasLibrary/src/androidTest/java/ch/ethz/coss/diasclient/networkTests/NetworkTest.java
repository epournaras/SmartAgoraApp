package ch.ethz.coss.dias_app.NetworkTest;

/**
 * Created by Varin on 31.10.2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeoutException;


import ch.ethz.coss.diasclient.pureJava.network.Network;


import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)


public class NetworkTest {

    public final String NETWORKSINK = "ipc://localhost:30000";
    public final String DATABASESINK = "ipc://localhost:40000"; //TODO get right string to emulate database
    public final String BGTASKSINK = "ipc://localhost:30001"; //TODO get right string to emulate background task to DIASBROADCAST


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ch.ethz.coss.dias_app", appContext.getPackageName());
    }

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Test
    public void testBoundService() throws TimeoutException, InterruptedException {
        // Create the service Intent.
        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(),
                        Network.class);

        // Bind the service and grab a reference to the binder.
        IBinder binder = mServiceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call
        // public methods on the binder directly.
        //TODO test service start up
        //Network service = ((Network.NetworkBinder) binder).getService();
        //assertNotNull(service);
    }

    @Test
    public void sendStatetest() throws Exception {
        /**
        //compact service start
        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), Network.class);
        IBinder binder = mServiceRule.bindService(serviceIntent);
        Network service = ((Network.NetworkBinder) binder).getService();
        //compact pusher start, receiver is running on the network thread;
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket pusher = context.socket(ZMQ.PUSH);
        pusher.setLinger(-1);
        pusher.setSendTimeOut(CONNECTIONTIMEOUT);
        pusher.setReceiveTimeOut(2 * CONNECTIONTIMEOUT);
        pusher.connect(NETWORKSINK);

        ZMQ.Socket sink = context.socket(ZMQ.PULL);
        sink.setLinger(-1);
        sink.setSendTimeOut(CONNECTIONTIMEOUT);
        sink.setReceiveTimeOut(2 * CONNECTIONTIMEOUT);
        sink.bind(DATAENGINESINK);
        Thread.sleep(100);

        //actual test
        final String sentmsg = "Testmessage"; //TODO get right string to emulate sendState message

        pusher.send(sentmsg.getBytes());

        String recvmsg = sink.recvStr();

        assertEquals(sentmsg, recvmsg); //TODO get right string to emulate getdata message

        //After test cleanup
        pusher.close();
        sink.close();
        context.term();
         **/
    }

    @Test
    public void sendToBGtasktest() throws Exception {
        /**
        //compact service start
        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), Network.class);
        IBinder binder = mServiceRule.bindService(serviceIntent);
        Network service = ((Network.NetworkBinder) binder).getService();
        //compact pusher start, receiver is running on the network thread;
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket pusher = context.socket(ZMQ.PUSH);
        pusher.setLinger(-1);
        pusher.setSendTimeOut(CONNECTIONTIMEOUT);
        pusher.setReceiveTimeOut(2 * CONNECTIONTIMEOUT);
        pusher.connect(DATAENGINESINK);

        ZMQ.Socket sink = context.socket(ZMQ.PULL);
        sink.setLinger(-1);
        sink.setSendTimeOut(CONNECTIONTIMEOUT);
        sink.setReceiveTimeOut(2 * CONNECTIONTIMEOUT);
        sink.bind(BGTASKSINK); //TODO get right string to emulate background task
        Thread.sleep(100);

        //actual test
        final String sentmsg = "Testmessage"; //TODO get right string to emulate Msg from Database

        pusher.send(sentmsg.getBytes());

        String recvmsg = sink.recvStr();

        assertEquals(sentmsg, recvmsg); //TODO get right string to emulate sendMSG

        //After test cleanup
        pusher.close();
        sink.close();
        context.term();
         **/
    }

    //TODO write tests for sendState() submethods

    @Test
    public void setflagtest() throws TimeoutException, InterruptedException {
        /**
        //compact service start
        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), Network.class);
        IBinder binder = mServiceRule.bindService(serviceIntent);
        Network service = ((Network.NetworkBinder) binder).getService();
        //compact pusher start, receiver is running on the network thread;
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket pusher = context.socket(ZMQ.PUSH);
        pusher.setLinger(-1);
        pusher.setSendTimeOut(CONNECTIONTIMEOUT);
        pusher.setReceiveTimeOut(2 * CONNECTIONTIMEOUT);
        pusher.connect(DATAENGINESINK);

        ZMQ.Socket sink = context.socket(ZMQ.PULL);
        sink.setLinger(-1);
        sink.setSendTimeOut(CONNECTIONTIMEOUT);
        sink.setReceiveTimeOut(2 * CONNECTIONTIMEOUT);
        sink.bind(DATAENGINESINK);
        Thread.sleep(100);

        //actual test
        final String sentmsg = "Testmessage"; //TODO get right string to emulate Msg from background task

        pusher.send(sentmsg.getBytes());

        String recvmsg = sink.recvStr();

        assertEquals(sentmsg, recvmsg); //TODO get right string to emulate setFlag msg

        //After test cleanup
        pusher.close();
        sink.close();
        context.term();
         **/
    }

    @Test //not relevant atm
    public void subscribetest(){
        //TODO subscribe and constantly receive feedback on setup socket
        String recvmsg;
        //sndsocket.send("SUBSCRIBE");

        for(int i=1; i<11; i++) {//test for 10 msgs
            recvmsg = null;
            //recvmsg = recvsocket.recv()
            assertNotNull(recvmsg);
        }

    }

    @Test //not relevant atm
    public void unsubscribetest(){
        //TODO subscribe, receive message, unsubscribe, do not receive messages(run into timeout)
        String recvmsg = "initialtest"; //assure that if nothing works test fails anyway


        //recvmsg = recvsocket.recv();
        assertNotNull(recvmsg);

        //sndsocket.send("UNSUBSCRIBE");

        //recvmsg = recvsocket.recv();

        assertNull(recvmsg);
    }

    @Test
    public void connectSingletest() throws TimeoutException, InterruptedException {
        /**
        //compact service start
        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), Network.class);
        IBinder binder = mServiceRule.bindService(serviceIntent);
        Network service = ((Network.NetworkBinder) binder).getService();
        //compact pusher start, receiver is running on the network thread;
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket pusher = context.socket(ZMQ.PUSH);
        pusher.setLinger(-1);
        pusher.setSendTimeOut(CONNECTIONTIMEOUT);
        pusher.setReceiveTimeOut(2 * CONNECTIONTIMEOUT);
        pusher.connect(NETWORKSINK);

        ZMQ.Socket sink = context.socket(ZMQ.PULL);
        sink.setLinger(-1);
        sink.setSendTimeOut(CONNECTIONTIMEOUT);
        sink.setReceiveTimeOut(2 * CONNECTIONTIMEOUT);
        sink.bind(BGTASKSINK); //TODO get right string to emulate background task to DIASBROADCAST
        Thread.sleep(100);

        //actual test
        final String sentmsg = "Testmessage"; //TODO get right string to emulate Connect msg

        pusher.send(sentmsg.getBytes());
        pusher.send(sentmsg.getBytes());// send msg twice to check if connection requested only once

        String recvmsg = sink.recvStr();

        assertEquals(sentmsg, recvmsg); //TODO get right string to emulate Connect msg, if key is not contained in database

        String checknull = sink.recvStr(); // run into timeout to make sure no other message is sent
        assertNull(checknull);

        //pusher.send("CONNECTIONINFOMSG"); //TODO emulate connection info msg from bg task HOWTOCHEK THIS

        //After test cleanup
        pusher.close();
        sink.close();
        context.term();
         **/

    }


    @Test //not relevant atm
    public void disconnecttest(){
        //TODO get list of connections, connect new nodes, check list, disconnect node, check list again
        //getlistold
        //sndsocket.send("DISCONNECT");
        //sleep
        //getlistnew
        assertNull(1);
    }

}