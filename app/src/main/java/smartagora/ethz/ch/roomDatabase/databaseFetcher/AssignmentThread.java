package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;
import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.AssignmentEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * AssignmentThread class is used to fetch Assignment table data
 */
public class AssignmentThread {

    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Assignment thread.
     */
    public AssignmentThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * returns the list of  Assignment Entities.
     *
     * @return the all assignments
     */
    public List<AssignmentEntity> getAllAssignments(){
        return databaseHandler.getAssignmentDao().getAllAssignments();
    }


    /**
     * returns the names of Assignments.
     *
     * @return all the assignment names
     */
    public List<String> getAllAssignmentNames(){
        return databaseHandler.getAssignmentDao().getAllAssignmentNames();
    }


    /**
     * returns the Assignment Entity.
     *
     * @param assignmentName the name of assignment
     * @return the assignment object
     */
    public AssignmentEntity getAssignment(String assignmentName){
        return databaseHandler.getAssignmentDao().getAssignment(assignmentName);
    }
}