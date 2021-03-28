package ch.ethz.coss.diasclient.schedulerTest;

import org.junit.Assert;
import org.junit.Test;

import ch.ethz.coss.diasclient.pureJava.centralControl.Address;
import ch.ethz.coss.diasclient.pureJava.centralControl.AddressBook;
import ch.ethz.coss.diasclient.pureJava.javaobjects.CommunicationObject;
import ch.ethz.coss.diasclient.pureJava.javaobjects.Header;
import ch.ethz.coss.diasclient.pureJava.javaobjects.flags.ControlFlag;
import ch.ethz.coss.diasclient.pureJava.javaobjects.messages.ControlMessage;
import ch.ethz.coss.diasclient.pureJava.javaobjects.messages.SchedulingMessage;
import ch.ethz.coss.diasclient.pureJava.javaobjects.messages.data.ControlData;
import ch.ethz.coss.diasclient.pureJava.javaobjects.messages.data.SchedulingData;

/**
 * Created by Varin on 13.02.2018.
 */

public class SchedulingMessagetTest {
    @Test
    public void schedulingmessageTest() {
        AddressBook adrbk = AddressBook.getInstance();
        Header schedulermessagehead = new Header(adrbk.getConnectionString(Address.SCHEDULESINK), adrbk.getConnectionString(Address.SCHEDULESINK)); //target of ScheduleMessage
        Header encapsulatedhead = new Header(adrbk.getConnectionString(Address.CONTROLLERSINK), adrbk.getConnectionString(Address.SCHEDULESINK)); //Head of encapsulated Message, here a ControlMessage




        //Encapsulated message
        ControlMessage ctrlmsg = new ControlMessage(new ControlData(ControlFlag.SELECTEDSTATESENT, null),encapsulatedhead );
        //Scheduling message
        CommunicationObject cmobj = new CommunicationObject(ctrlmsg.toJson());
        SchedulingMessage schedulemsg = new SchedulingMessage(new SchedulingData((long) 1000, (long) 0, cmobj.header, cmobj.data),schedulermessagehead );


        //Comparing content without serializing
        Assert.assertEquals("Without serializing",ctrlmsg.toJson(), schedulemsg.data.toSend().toJson());

        //Serializing
        String schedulemsgsjson = schedulemsg.toJson();

        //deseralizing
        schedulemsg = new SchedulingMessage(schedulemsgsjson);

        //Comparing generated controlmessage to schedulemsg.data.toSend.toJson
        Assert.assertEquals("With serializing", ctrlmsg.toJson(), schedulemsg.data.toSend().toJson());
    }
}
