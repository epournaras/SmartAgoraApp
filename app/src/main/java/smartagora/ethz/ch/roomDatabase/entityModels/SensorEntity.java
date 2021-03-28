package smartagora.ethz.ch.roomDatabase.entityModels;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * @Entity Declaring the Sensor Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 */
@Entity(tableName = "Sensor")



public class SensorEntity {
/**
 * SensorEntity class ia a modal that represents the Sensor Table
 * It is used to create a table in Room Persistent Database.
 * @ColumnInfo Specify the custom name of column in Table
 */


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;

    /**
     * Instantiates a new Sensor entity.
     */
    public SensorEntity() {
    }

    /**
     * Instantiates a new Sensor entity.
     *
     * @param name the name
     */
    @Ignore
    public SensorEntity(String name) {
        this.name = name;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the 6 sensors by calling the SensorEntity Parameterized Constructor in the SensorEntity Array
     *
     * @return these sensors in SensorEntity Array
     */
    public static SensorEntity[] insertAllSensors() {
        return new SensorEntity[]{

                new SensorEntity("Light"),
                new SensorEntity("Gyroscope"),
                new SensorEntity("Proximity"),
                new SensorEntity("Accelerometer"),
                new SensorEntity("Location"),
                new SensorEntity("Noise")

        };
    }

}
