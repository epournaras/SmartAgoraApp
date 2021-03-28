package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import smartagora.ethz.ch.roomDatabase.entityModels.SensorReadingEntity;

/**
 * @Dao Creates a Data Access Object in the database using an SensorReadingDao Interface Class
 */
@Dao
public interface SensorReadingDao {

    /**
     * @Insert parameter insert the SensorReading entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SensorReadingEntity sensorReading);


}
