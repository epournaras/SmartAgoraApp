package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network;

/**
 * Edited by Renato Kunz
 */
public final class NetworkConstants {
	//Buffer size for REQ sockets
	final static int BUFFERSIZE = 20;

	final static String SensorPrefix = "net";

	//DIAS Gateway ID "dataSourceID"
	public final static String DIASGATEWAY = "0"; //DIAS Gateway sensor ID in persistent Storage


	final static String OUTPROTOCOL = "tcp://"; //TODO change back line 77 in network and in pesistent storage

	//public final static String SENSORTOSEND = "20"; //make sure you use only this sensor ID to send to DIAS otherwise the server could crash and the data will not be meaningful

	//TODO adjust ackmessage text to contain message types

	//TODO this needs to be unique what do you guys propose, android ID is unique but system dependent (currently implemented in PersitentStorage.java
	final static int UUIDPORT = 0;


	//TIMEOUTS
	final static int CONNECTIONTIMEOUT = 2000;
	final static int ONLINETIMEOUT = 45000;
	final static int PEERTIMEOUT = 60000;
}