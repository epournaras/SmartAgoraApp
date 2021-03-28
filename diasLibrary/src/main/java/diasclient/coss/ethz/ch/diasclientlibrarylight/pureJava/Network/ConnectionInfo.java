package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network;

/**
 * Created by Varin on 10.01.2018.
 */

public class ConnectionInfo { //describes connections
    public String connection; //connection to Node
    long timestampmillis; //when last connection try was made
    public boolean firstconnect = true; //firstconnect, to determine action, e.g. send possible and selected states even if they did not change
    public ConnectionInfo(String connection, long timestampmillis) {
        this.connection = connection;
        this.timestampmillis = timestampmillis;
    }
}
