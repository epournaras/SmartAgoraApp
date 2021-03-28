package smartagora.ethz.ch.networkRequests.hiveServerModels;

import com.google.gson.annotations.SerializedName;

/**
 * Project Class is a JSON representation of Project which is created in Hive Server.
 * Project Class includes different attributes to define the each Project.
 */
@SuppressWarnings("ALL")
public class Project {

    @SerializedName("Id")
    private String id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Description")
    private String description;
    @SerializedName("AssetCount")
    private String assetCount;
    @SerializedName("TaskCount")
    private String taskCount;
    @SerializedName("UserCount")
    private String userCount;
    @SerializedName("AssignmentCount")
    private AssignmentCount assignmentCount;

    //All setter and getters of above variables


    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
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
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets asset count.
     *
     * @return the asset count
     */
    public String getAssetCount() {
        return assetCount;
    }

    /**
     * Sets asset count.
     *
     * @param assetCount the asset count
     */
    public void setAssetCount(String assetCount) {
        this.assetCount = assetCount;
    }

    /**
     * Gets task count.
     *
     * @return the task count
     */
    public String getTaskCount() {
        return taskCount;
    }

    /**
     * Sets task count.
     *
     * @param taskCount the task count
     */
    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    /**
     * Gets user count.
     *
     * @return the user count
     */
    public String getUserCount() {
        return userCount;
    }

    /**
     * Sets user count.
     *
     * @param userCount the user count
     */
    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    /**
     * Gets assignment count.
     *
     * @return the assignment count
     */
    public AssignmentCount getAssignmentCount() {
        return assignmentCount;
    }

    /**
     * Sets assignment count.
     *
     * @param assignmentCount the assignment count
     */
    public void setAssignmentCount(AssignmentCount assignmentCount) {
        this.assignmentCount = assignmentCount;
    }

    /**
     * Assignment count represents the Project attribute as a Json which specifies the Project's total , finished
     * and unfinished Assignments
     */
    public class AssignmentCount {

        @SerializedName("Total")
        private String total;
        @SerializedName("Finished")
        private String finished;
        @SerializedName("Unfinished")
        private String unfinished;

        //All setter and getters of above variables

        /**
         * Gets total.
         *
         * @return the total
         */
        public String getTotal() {
            return total;
        }

        /**
         * Sets total.
         *
         * @param total the total
         */
        public void setTotal(String total) {
            this.total = total;
        }

        /**
         * Gets finished.
         *
         * @return the finished
         */
        public String getFinished() {
            return finished;
        }

        /**
         * Sets finished.
         *
         * @param finished the finished
         */
        public void setFinished(String finished) {
            this.finished = finished;
        }

        /**
         * Gets unfinished.
         *
         * @return the unfinished
         */
        public String getUnfinished() {
            return unfinished;
        }

        /**
         * Sets unfinished.
         *
         * @param unfinished the unfinished
         */
        public void setUnfinished(String unfinished) {
            this.unfinished = unfinished;
        }
    }

}
