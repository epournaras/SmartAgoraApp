package ch.ethz.coss.dias_app.NetworkTest;

import java.util.ArrayList;

/**
 * Created by Varin on 31.10.2017.
 */

public class TestData {

    public class DummyMessages{
        public DummyMessages(String target, String msg){
            this.target = target;
            this.msg = msg;
        }
        public String target;
        public String msg;
    }

    public ArrayList<DummyMessages> getTestMsgs(){
        ArrayList<DummyMessages> ar = new ArrayList<DummyMessages>();
        ar.add(new DummyMessages("tcp://localhost:8000", "Heiri"));
        ar.add(new DummyMessages("tcp://localhost:8000", "Hans"));
        ar.add(new DummyMessages("tcp://localhost:8100", "Heiri"));
        ar.add(new DummyMessages("tcp://localhost:8100", "Heiri"));
        ar.add(new DummyMessages("10.10.10.10", "REALMESSAGE")); //real message, wrong target
        ar.add(new DummyMessages("tcp://localhost:4000", "REALMESSAGE"));
        return ar;
    }

}
