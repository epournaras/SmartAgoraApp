package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import smartagora.ethz.ch.roomDatabase.entityModels.SensorEntity;

/**
 * @Dao Creates a Data Access Object in the database using an SensorDao Interface Class
 */
@Dao
public interface SensorDao {

    /**
     * @Insert parameter insert the Sensor entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SensorEntity... sensor);


}
