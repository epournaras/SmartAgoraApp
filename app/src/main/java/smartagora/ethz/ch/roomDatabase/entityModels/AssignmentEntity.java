package smartagora.ethz.ch.roomDatabase.entityModels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @Entity Declaring the Assignment Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 */
@Entity(tableName = "Assignment")


public class AssignmentEntity {
    /**
     * Assignment Entity class ia a modal that represents the Assignment Table
     * It is used to create a table in Room Persistent Database.
     * @ColumnInfo Specify the custom name of column in Table
     */

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id = "";
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "mode")
    private String mode;
    @ColumnInfo(name = "state")
    private String state;
    @ColumnInfo(name = "defaultCredit")
    private String defaultCredit;

    /**
     * Instantiates a new Assignment entity.
     */
    public AssignmentEntity() {
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
     * Gets id.
     *
     * @return the id
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(@NonNull String id) {
        this.id = id;
    }

    /**
     * Gets mode.
     *
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets mode.
     *
     * @param mode the mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets default credit.
     *
     * @return the default credit
     */
    public String getDefaultCredit() {
        return defaultCredit;
    }

    /**
     * Sets default credit.
     *
     * @param defaultCredit the default credit
     */
    public void setDefaultCredit(String defaultCredit) {
        this.defaultCredit = defaultCredit;
    }
}
