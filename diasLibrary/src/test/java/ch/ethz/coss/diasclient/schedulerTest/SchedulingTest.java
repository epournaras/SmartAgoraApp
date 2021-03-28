package ch.ethz.coss.diasclient.schedulerTest;

import junit.framework.Assert;

import org.junit.Test;
import org.zeromq.ZMQ;

import ch.ethz.coss.diasclient.pureJava.centralControl.Address;
import ch.ethz.coss.diasclient.pureJava.centralControl.AddressBook;
import ch.ethz.coss.diasclient.pureJava.javaobjects.Header;
import ch.ethz.coss.diasclient.pureJava.javaobjects.flags.ControlFlag;
import ch.ethz.coss.diasclient.pureJava.javaobjects.messages.ControlMessage;
import ch.ethz.coss.diasclient.pureJava.javaobjects.messages.SchedulingMessage;
import ch.ethz.coss.diasclient.pureJava.javaobjects.messages.data.ControlData;
import ch.ethz.coss.diasclient.pureJava.javaobjects.messages.data.SchedulingData;
import ch.ethz.coss.diasclient.pureJava.scheduler.SchedulerRunner;

/**
 * Created by Varin on 13.02.2018.
 */

public class SchedulingTest {
    ZMQ.Context context;
    AddressBook adrbk;
    SchedulerRunner schedrun;
    ZMQ.Socket pusher;
    ZMQ.Socket receiver;
    @Test
    public void SchedulingTest(){
        adrbk = AddressBook.getInstance();
        context = ZMQ.context(1);
        schedrun = new SchedulerRunner(context);
        pusher = context.socket(ZMQ.PUSH);
        pusher.connect(adrbk.getConnectionString(Address.SCHEDULESINK));

        receiver = context.socket(ZMQ.PULL);
        receiver.bind(adrbk.getConnectionString(Address.CONTROLLERSINK));
        Thread scheduleTherad = new Thread(schedrun);
        scheduleTherad.start();

        Header head = new Header(adrbk.getConnectionString(Address.SCHEDULESINK), adrbk.getConnectionString(Address.SCHEDULESINK));
        Header ctrhead = new Header(adrbk.getConnectionString(Address.CONTROLLERSINK), adrbk.getConnectionString(Address.SCHEDULESINK));

        ControlMessage ctrlmsg = new ControlMessage(new ControlData(ControlFlag.SELECTEDSTATESENT, null),ctrhead );


        SchedulingMessage schmsg = new SchedulingMessage(new SchedulingData((long) 1000, (long) 0, ctrlmsg),head );
        pusher.send(schmsg.toJson());

        int i = 0;
        while(true){
            System.out.println(String.valueOf(System.currentTimeMillis()/1000) +"RECV"+ " receiving message" + String.valueOf(i));
            String recvmsg = receiver.recvStr();
            Assert.assertEquals(ctrlmsg.toJson(), recvmsg);
            i++;
            if(i == 3){
                break;
            }
        }

        schmsg = new SchedulingMessage(new SchedulingData((long) 5000, (long) 2000, ctrlmsg),head );
        pusher.send(schmsg.toJson());
        while(true){
            System.out.println(String.valueOf(System.currentTimeMillis()/1000)+ "RECV"+ " receiving message" + String.valueOf(i));
            String recvmsg = receiver.recvStr();
            Assert.assertEquals(ctrlmsg.toJson(), recvmsg);
            i++;
            if(i == 7){
                break;
            }
        }
        Assert.assertTrue(true);

        ControlMessage msg = new ControlMessage(new ControlData(ControlFlag.KILL, null),
                new Header(adrbk.getConnectionString(Address.SCHEDULESINK), adrbk.getConnectionString(Address.CONTROLLERSINK)));

        pusher.send(msg.toJson());
        pusher.setLinger(0);
        pusher.close();

        receiver.recvStr();
        receiver.setLinger(0);
        receiver.close();
        context.term();
    }

}
