package smartagora.ethz.ch.networkRequests;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import smartagora.ethz.ch.networkRequests.hiveServerModels.Asset;
import smartagora.ethz.ch.networkRequests.hiveServerModels.Assignment;
import smartagora.ethz.ch.networkRequests.hiveServerModels.Project;
import smartagora.ethz.ch.networkRequests.hiveServerModels.Task;
import smartagora.ethz.ch.networkRequests.hiveServerModels.User;

/**
 * NetworkModels class contains all models which is used in Retrofit Interface for get and post requests in Hive Server
 */
@SuppressWarnings("ALL")
public class NetworkModels {

    /**
     * UserModel class has responsibility to get and post requests in Users which is created in Hive Server.
     */
    @SuppressWarnings("unused")
    public class UserModel {
        /**
         * The Users.
         */
        @SerializedName("Users")
        List<User> users;

        /**
         * Gets users.
         *
         * @return the users
         */
        public List<User> getUsers() {
            return users;
        }

        /**
         * Sets users.
         *
         * @param users the users
         */
        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    /**
     * ProjectModel class has responsibility to get and post requests in Projects which is created in Hive Server.
     */
    @SuppressWarnings("unused")
    public class ProjectModel {
        @SerializedName("Projects")
        private List<Project> projects;

        /**
         * Gets projects.
         *
         * @return the projects
         */
        public List<Project> getProjects() {
            return projects;
        }

        /**
         * Sets projects.
         *
         * @param projects the projects
         */
        public void setProjects(List<Project> projects) {
            this.projects = projects;
        }

    }

    /**
     * TaskModel class has responsibility to get and post requests in Tasks which is created in Hive Server.
     */
    @SuppressWarnings("unused")
    public class TaskModel {

        @SerializedName("Tasks")
        private List<Task> tasks;

        /**
         * Gets tasks.
         *
         * @return the tasks
         */
        public List<Task> getTasks() {
            return tasks;
        }

        /**
         * Sets tasks.
         *
         * @param tasks the tasks
         */
        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
        }
    }

    /**
     * SingleTaskModel class has responsibility to get and post requests in specific Task which is created in Hive Server.
     */
    @SuppressWarnings("unused")
    public static class SingleTaskModel {

        @SerializedName("Task")
        private Task task;

        /**
         * Gets task.
         *
         * @return the task
         */
        public Task getTask() {
            return task;
        }

        /**
         * Sets task.
         *
         * @param task the task
         */
        public void setTask(Task task) {
            this.task = task;
        }
    }


    @SuppressWarnings("unused")
    public static class SpecificAssignmentModel {

        @SerializedName("Assignment")
        private Assignment assignment;

        public Assignment getAssignment() {
            return assignment;
        }

        public void setAssignment(Assignment assignment) {
            this.assignment = assignment;
        }
    }


    /**
     * AssignmentModel class has responsibility to get and post requests in Assignments which is created in Hive Server.
     */
    @SuppressWarnings("unused")
    public class AssignmentModel {

        @SerializedName("Assignments")
        private List<Assignment> assignments;

        /**
         * Gets assignments.
         *
         * @return the assignments
         */
        public List<Assignment> getAssignments() {
            return assignments;
        }

        /**
         * Sets assignments.
         *
         * @param assignments the assignments
         */
        public void setAssignments(List<Assignment> assignments) {
            this.assignments = assignments;
        }
    }

    /**
     * AssetModel class has responsibility to get and post requests in Assets which is created in Hive Server.
     */
    @SuppressWarnings("unused")
    public class AssetModel {

        @SerializedName("Assets")
        private List<Asset> assets;
        /**
         * Gets assets.
         *
         * @return the assets
         */
        public List<Asset> getAssets() {
            return assets;
        }

        /**
         * Sets assets.
         *
         * @param assets the assets
         */
        public void setAssets(List<Asset> assets) {
            this.assets = assets;
        }
    }


}
