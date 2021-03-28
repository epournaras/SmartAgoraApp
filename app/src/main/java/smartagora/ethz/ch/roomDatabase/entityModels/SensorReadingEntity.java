package smartagora.ethz.ch.roomDatabase.entityModels;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * @Entity Declaring the sensorReading Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 * Specify the foreign keys in Table by using the foreignKeys attribute
 * Specify the indices in Table by using the indices attribute
 */
@Entity(tableName = "SensorReading",
        foreignKeys = {

                /*
                 *  @ForeignKey creates a foreign key in Table
                 *  entity specify the Entity class of parent Table
                 *  parentColumns specify the id from the parent Table
                 *  childColumns specify Foreign Key in the Table
                 */
                @ForeignKey(entity = SensorEntity.class,
                        parentColumns = "id",
                        childColumns = "sensorId",
                        onDelete = ForeignKey.CASCADE)
        },

        /*
         * @Index index a column for faster searching of it.
         * sensorId is a foreign key in Table
        */
        indices = {
                @Index(value = "sensorId")
        }


)



public class SensorReadingEntity {
    /**
     * SensorReading Entity class ia a modal that represents the SensorReading Table
     * It is used to create a table in Room Persistent Database.
     * @ColumnInfo Specify the custom name of column in Table
     */

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "sensorId")
    private int sensorId;
    @ColumnInfo(name = "readingValue")
    private String readingValue;
    @ColumnInfo(name = "readingTime")
    private String readingTime;

    /**
     * Instantiates a new Sensor reading entity.
     */
    public SensorReadingEntity() {
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
    @SuppressWarnings("unused")
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets sensor id.
     *
     * @return the sensor id
     */
    public int getSensorId() {
        return sensorId;
    }

    /**
     * Sets sensor id.
     *
     * @param sensorId the sensor id
     */
    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * Gets reading value.
     *
     * @return the reading value
     */
    public String getReadingValue() {
        return readingValue;
    }

    /**
     * Sets reading value.
     *
     * @param readingValue the reading value
     */
    public void setReadingValue(String readingValue) {
        this.readingValue = readingValue;
    }

    /**
     * Gets reading time.
     *
     * @return the reading time
     */
    public String getReadingTime() {
        return readingTime;
    }

    /**
     * Sets reading time.
     *
     * @param readingTime the reading time
     */
    public void setReadingTime(String readingTime) {
        this.readingTime = readingTime;
    }
}
