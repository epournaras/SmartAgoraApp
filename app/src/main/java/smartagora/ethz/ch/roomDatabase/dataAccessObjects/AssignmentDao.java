package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.AssignmentEntity;

/**
 * @Dao Creates a Data Access Object in the database using an AssignmentDao Interface Class
 */
@Dao
public interface AssignmentDao {


    /**
     * @Insert parameter insert the assignment entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AssignmentEntity assignment);


    /**
     * update the Assignment state by assignment Id
     *
     * @param assignmentId the unique id of the Assignment
     * @param state        the state of the Assignment                     .
     */
    @Query("Update Assignment set state =:state where id=:assignmentId")
    void disableAssignment(String assignmentId, String state);

    /**
     * Gets all assignments from Assignment Table.
     *
     * @return the a assignments
     */
    @Query("SELECT id,name,mode,state,defaultCredit FROM Assignment")
    List<AssignmentEntity> getAllAssignments();

    /**
     * Gets all assignment names from Assignment Table.
     *
     * @return the all assignment names
     */
    @Query("SELECT name FROM Assignment")
    List<String> getAllAssignmentNames();


    /**
     * Gets assignment by Assignment Name.
     *
     * @return the assignment entity
     */
    @Query("SELECT id,name,mode,state,defaultCredit FROM Assignment WHERE name=:assignmentName")
    AssignmentEntity getAssignment(final String assignmentName);


    /**
     * Gets assignment name by Assignment Id.
     *
     * @return the assignment name
     */
    @Query("SELECT name FROM Assignment WHERE id=:assignmentId")
    String getAssignmentName(final String assignmentId);


}
