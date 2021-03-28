package smartagora.ethz.ch.networkRequests.hiveServerModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Task class is a JSON representation of Task which is created in Hive Server.
 * Task is used to represent the folder which contains all submitted Assignments.
 * It contains attributes which defines the each Task
 */
public class Task {

    @SerializedName("Id")
    private String id;
    @SerializedName("Project")
    private String project;
    @SerializedName("Name")
    private String name;
    @SerializedName("Description")
    private String description;
    @SerializedName("CurrentState")
    private String currentState;
    @SerializedName("AssignmentCriteria")
    private AssignmentCriteria assignmentCriteria;
    @SerializedName("CompletionCriteria")
    private CompletionCriteria completionCriteria;


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
     * Gets project.
     *
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets project.
     *
     * @param project the project
     */
    public void setProject(String project) {
        this.project = project;
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
     * Gets current state.
     *
     * @return the current state
     */
    public String getCurrentState() {
        return currentState;
    }

    /**
     * Sets current state.
     *
     * @param currentState the current state
     */
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    /**
     * Gets assignment criteria.
     *
     * @return the assignment criteria
     */
    public AssignmentCriteria getAssignmentCriteria() {
        return assignmentCriteria;
    }

    /**
     * Sets assignment criteria.
     *
     * @param assignmentCriteria the assignment criteria
     */
    public void setAssignmentCriteria(AssignmentCriteria assignmentCriteria) {
        this.assignmentCriteria = assignmentCriteria;
    }

    /**
     * Gets completion criteria.
     *
     * @return the completion criteria
     */
    public CompletionCriteria getCompletionCriteria() {
        return completionCriteria;
    }

    /**
     * Sets completion criteria.
     *
     * @param completionCriteria the completion criteria
     */
    public void setCompletionCriteria(CompletionCriteria completionCriteria) {
        this.completionCriteria = completionCriteria;
    }


    /**
     * AssignmentCriteria class ia a JSON model which is used in Task.
     * Assignment class contains all submitted Assignments Data in Submitted Model.
     */

    public static class AssignmentCriteria {

        @SerializedName("SubmittedData")
        private SubmittedData submittedData;


        // All setters and getters of above variables

        /**
         * Gets submitted data.
         *
         * @return the submitted data
         */
        public SubmittedData getSubmittedData() {
            return submittedData;
        }

        /**
         * Sets submitted data.
         *
         * @param submittedData the submitted data
         */
        public void setSubmittedData(SubmittedData submittedData) {
            this.submittedData = submittedData;
        }

    }

    /**
     * SubmittedData class is a JSON model which is used in Assignment Criteria
     * SubmittedData class includes all Submitted Assignments in Task.
     */
    public static class SubmittedData {

        /**
         * The Assignments.
         */
        @SerializedName("Assignments")
        @Expose
        Map<String, Assignment> assignments;

        /**
         * Gets assignments.
         *
         * @return the assignments
         */
        public Map<String, Assignment> getAssignments() {
            return assignments;
        }

        /**
         * Sets assignments.
         *
         * @param assignments the assignments
         */
        public void setAssignments(Map<String, Assignment> assignments) {
            this.assignments = assignments;
        }
    }


    /**
     * CompletionCriteria class is a JSON Model which is used in Task.
     * CompletionCriteria class is used to represent the total and matching criteria of each Task
     */
    @SuppressWarnings("unused")
    public static class CompletionCriteria {

        @SerializedName("Total")
        private int total;
        @SerializedName("Matching")
        private int matching;

        // All setters and getters of above variables


        /**
         * Gets total.
         *
         * @return the total
         */
        public int getTotal() {
            return total;
        }

        /**
         * Sets total.
         *
         * @param total the total
         */
        public void setTotal(int total) {
            this.total = total;
        }

        /**
         * Gets matching.
         *
         * @return the matching
         */
        public int getMatching() {
            return matching;
        }

        /**
         * Sets matching.
         *
         * @param matching the matching
         */
        public void setMatching(int matching) {
            this.matching = matching;
        }
    }

}
