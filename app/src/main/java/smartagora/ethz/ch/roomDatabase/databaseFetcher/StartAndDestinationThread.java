package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import smartagora.ethz.ch.roomDatabase.entityModels.StartAndDestinationEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * StartAndDestinationThread class is used to fetch StartAndDestination table data in a separate thread by using the AsyncTask Class.
 */
public class StartAndDestinationThread {

    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Start and destination thread.
     */
    public StartAndDestinationThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * Execute the AsyncTask class by passing the assignmentName and return
     * the StartAndDestination object.
     *
     * @param assignmentName the name of assignment
     * @return the startAndDestination object
     */
    public StartAndDestinationEntity getStartAndDestination(String assignmentName){
        return databaseHandler.getStartAndDestinationDao().getStartAndDestination(assignmentName);
    }
}
