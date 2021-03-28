package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements;

import android.util.Log;

import java.util.Date;
import java.util.LinkedList;

public class Logger {
    private LinkedList<String> logFile = new LinkedList<>();
    private LinkedList<String> timeStamp = new LinkedList<>();
    public synchronized void log(String message, String sensorID){
        logFile.addLast(message + "\ndataSourceID: " + sensorID);
        timeStamp.addLast(new Date().toString());
        Log.e("LOGGER::", "Log set: " + message);
    }

    public class LogTuple{
        public LinkedList<String> logFile;
        public LinkedList<String> timestamp;
    }

    public synchronized LogTuple getLogTuple(){
        LogTuple tuple = new LogTuple();
        tuple.logFile = logFile;
        tuple.timestamp = timeStamp;
        logFile = new LinkedList<>();
        timeStamp = new LinkedList<>();

        return tuple;
    }



}

