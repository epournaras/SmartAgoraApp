package diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.Network;

import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.ConnectionInfo;

interface IPersistentStorage {

	void put(String dataSourceID, String connection, long timestampmillis); //save a value
	void remove(String key); //remove a value
	ConnectionInfo get(String key); //get a value
	String getUUID();
	void savePeerInfo(String dataSourceID, Integer peerID);
}
