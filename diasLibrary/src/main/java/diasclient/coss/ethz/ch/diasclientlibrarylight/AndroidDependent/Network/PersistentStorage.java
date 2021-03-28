package diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.ConnectionInfo;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.NetworkConstants;


// this is supposed to be Hardware specific Code and provide persistent Storage

/**
 * This class keeps a hashmap of all connected peers (key is the dataSourceID, the value is the connection information) and also stores this hashmap with the savepersisten method over system restarts etc.
 * edited by Renato Kunz
 */

public class PersistentStorage implements IPersistentStorage {

    private String LOGTAG = "PersistentNetworkStorage";

    private static final String PERSISTENT_MAP = "PERSISTENT_HASHMAP"; //Sharedpreferences key to hashmap

    public static final String PERSISTENT_STORAGE = "PERSISTENT_NETWORK_STORAGE"; //Sharedpreferences filename

    private final Context context;
    private HashMap<String, ConnectionInfo> networkconnections = new HashMap<>(); //connection holder not persistent atm
    private Gson gson;
    private SharedPreferences prefs;
    private final String GATEWAY;// = "78.46.75.138:4987";

    public PersistentStorage(Context context, String gatewayIP) {
        this.context = context;
        //get from shared prefs ANDROID
        GATEWAY = gatewayIP;
        prefs = context.getSharedPreferences(PERSISTENT_STORAGE, Context.MODE_PRIVATE);
        String gsonHashmap = prefs.getString(PERSISTENT_MAP, null);
        gson = new Gson();
        if (gsonHashmap != null) {
            networkconnections = gson.fromJson(gsonHashmap, new TypeToken<HashMap<String, ConnectionInfo>>() {
            }.getType());
        }
        this.put(NetworkConstants.DIASGATEWAY, GATEWAY, 1); //DIAS node needs to be hardcoded in the beginning but can be changed later

    }

    /**
     * saves the connection at the given ID
     *
     * @param dataSourceID
     * @param connection      String of connection for ZMQ
     * @param timestampmillis Time when saved
     */
    @Override
    public synchronized void put(String dataSourceID, String connection, long timestampmillis) {
        networkconnections.put(dataSourceID, new ConnectionInfo(connection, timestampmillis));
        savepersistent();
        Log.d(LOGTAG, "Received Peer Address: " + get(dataSourceID).connection);
    }

    /**
     * removes requested connection at given ID
     *
     * @param key
     */
    @Override
    public synchronized void remove(String key) {
        networkconnections.remove(key);
        savepersistent();
    }

    /**
     * gets connection Info
     *
     * @param key
     * @return
     */
    @Override
    public synchronized ConnectionInfo get(String key) {
        return networkconnections.get(key);
    }

    /**
     * Saves hashmap in storage
     */
    private void savepersistent() { //hardware specific part(ANDROID atm)
        //save in shared prefs
        prefs.edit().putString(PERSISTENT_MAP, gson.toJson(networkconnections)).apply();
    }

    /**
     * TODO: Currently it is not the optimal solution, get rid of android dependencies!
     * TODO: create UUID to use, atm this is the android_id which can change over usystem updates and device changes. This ID should be persitent over device updates and reinstalls
     * Return unique ID from android context.
     */
    @Override
    public String getUUID() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * @param dataSourceID
     * @param peerID
     */
    @Override
    public void savePeerInfo(String dataSourceID, Integer peerID) {
        context.getSharedPreferences(PERSISTENT_STORAGE, Context.MODE_PRIVATE).edit().putInt("ID:" + dataSourceID, peerID).apply();
    }

    /**
     * Resets all connections other than the gateway, this method needs to be called before any peerInfo is retrieved as this class can't handle already initialized sockets
     */
    public synchronized void reset() {
        networkconnections.clear();
        this.put(NetworkConstants.DIASGATEWAY, GATEWAY, 1);
        savepersistent();
        Log.d(LOGTAG, "Reset Connections");
    }
}