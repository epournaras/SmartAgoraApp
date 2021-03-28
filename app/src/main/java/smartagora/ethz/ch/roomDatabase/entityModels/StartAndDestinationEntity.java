package smartagora.ethz.ch.roomDatabase.entityModels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


/**
 * @Entity Declaring the StartAndDestination Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 * Specify the foreign keys in Table by using the foreignKeys attribute
 * Specify the indices in Table by using the indices attribute
 */
@Entity(tableName = "StartAndDestination",
        foreignKeys = {

                /*
                   @ForeignKey creates a foreign key in Table
                 *  entity specify the Entity class of parent Table
                 *  parentColumns specify the id from the parent Table
                 *  childColumns specify Foreign Keys in the Table
                 */
                @ForeignKey(entity = AssignmentEntity.class,
                        parentColumns = "id",
                        childColumns = "assignmentId",
                        onDelete = ForeignKey.CASCADE)
        },
        /*
         * @Index index a column for faster searching of it.
         * assignmentId is a foreign key in Table
        */
        indices = {
                @Index(value = "assignmentId")
        }
)



public class StartAndDestinationEntity {
    /**
     * StartAndDestination Entity class ia a modal that represents the StartAndDestination Table
     * It is used to create a table in Room Persistent Database.
     * @ColumnInfo Specify the custom name of column in Table
     */


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "assignmentId")
    private String assignmentId;
    @ColumnInfo(name = "startLatitude")
    private String startLatitude;
    @ColumnInfo(name = "startLongitude")
    private String startLongitude;
    @ColumnInfo(name = "destinationLatitude")
    private String destinationLatitude;
    @ColumnInfo(name = "destinationLongitude")
    private String destinationLongitude;


    /**
     * Instantiates a new Start and destination entity.
     */
    public StartAndDestinationEntity() {
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
     * Gets assignment id.
     *
     * @return the assignment id
     */
    public String getAssignmentId() {
        return assignmentId;
    }

    /**
     * Sets assignment id.
     *
     * @param assignmentId the assignment id
     */
    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    /**
     * Gets start latitude.
     *
     * @return the start latitude
     */
    public String getStartLatitude() {
        return startLatitude;
    }

    /**
     * Sets start latitude.
     *
     * @param startLatitude the start latitude
     */
    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    /**
     * Gets start longitude.
     *
     * @return the start longitude
     */
    public String getStartLongitude() {
        return startLongitude;
    }

    /**
     * Sets start longitude.
     *
     * @param startLongitude the start longitude
     */
    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    /**
     * Gets destination latitude.
     *
     * @return the destination latitude
     */
    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    /**
     * Sets destination latitude.
     *
     * @param destinationLatitude the destination latitude
     */
    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    /**
     * Gets destination longitude.
     *
     * @return the destination longitude
     */
    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    /**
     * Sets destination longitude.
     *
     * @param destinationLongitude the destination longitude
     */
    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }


}
