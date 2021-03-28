package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.QuestionSensorsEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.SensorEntity;

/**
 * @Dao Creates a Data Access Object in the database using an QuestionSensorsDao Interface Class
 */
@Dao
public interface QuestionSensorsDao {

    /**
     * @Insert parameter insert the QuestionSensors entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuestionSensorsEntity question);

    /**
     * Gets all sensors from specific question in assignment.
     *
     * @return the sensors of specific question from assignment as a List of SensorEntity
     */
    @Query("SELECT Sensor.id , Sensor.name FROM Sensor JOIN QuestionSensors ON Sensor.id = QuestionSensors.sensorId WHERE QuestionSensors.assignmentId=:assignmentId AND QuestionSensors.questionId=:questionId")
    List<SensorEntity> getAllSensorsFromAssignmentQuestion(final String assignmentId, final int questionId);


}
