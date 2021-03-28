package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import smartagora.ethz.ch.roomDatabase.entityModels.StartAndDestinationEntity;

/**
 * @Dao Creates a Data Access Object in the database using an StartAndDestinationDao Interface Class
 */
@Dao
public interface StartAndDestinationDao {

    /**
     * @Insert parameter insert the StartAndDestination entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StartAndDestinationEntity startAndDestination);

    /**
     * Gets start and destination from Assignment.
     *
     * @return the startAndDestination entity from StartAndDestination Table through AssignmentName
     */
    @Query("SELECT StartAndDestination.id,StartAndDestination.assignmentId,StartAndDestination.destinationLatitude,StartAndDestination.destinationLongitude,StartAndDestination.startLatitude,StartAndDestination.startLongitude FROM StartAndDestination join Assignment on StartAndDestination.assignmentId = Assignment.id WHERE Assignment.name=:assignmentName")
    StartAndDestinationEntity getStartAndDestination(final String assignmentName);
}
