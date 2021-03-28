package smartagora.ethz.ch.networkRequests;

import smartagora.ethz.ch.networkRequests.hiveServerModels.Assignment;
import smartagora.ethz.ch.networkRequests.hiveServerModels.Task;
import smartagora.ethz.ch.networkRequests.hiveServerModels.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * RetrofitInterface is a interface which contains all APIs for GET & POST requests in Hive Server .
 */
@SuppressWarnings("WeakerAccess")
public interface RetrofitInterface {

    /**
     * Gets all projects from Hive Server.
     *
     * @return the all projects
     */
    @GET("/admin/projects?from=0&size=3000")
    Call<NetworkModels.ProjectModel> getAllProjects();

    /**
     * Gets all assets in specific project From Hive Server .
     *
     * @param projectId the project id
     * @return all assets from specific project
     */
    @GET("/admin/projects/{projectId}/assets")
    Call<NetworkModels.AssetModel> getAllAssetsFromSpecificProject(@Path(value = "projectId", encoded = true) String projectId);

    /**
     * Gets all users in specific project from Hive Server.
     *
     * @param projectId the project id
     * @return all users from specific project
     */
    @GET("/admin/projects/{projectId}/users")
    Call<NetworkModels.UserModel> getAllUsersFromSpecificProject(@Path(value = "projectId", encoded = true) String projectId);

    /**
     * Gets all assignments in specific project from Hive Server.
     *
     * @param projectId the project id
     * @return all assignments from specific project
     */
    @GET("/admin/projects/{projectId}/assignments?from=0&size=3000")
    Call<NetworkModels.AssignmentModel> getAllAssignmentsFromSpecificProject(@Path(value = "projectId", encoded = true) String projectId);

    /**
     * Gets all tasks from Hive Server.
     *
     * @param projectId the project id
     * @return all tasks
     */
    @GET("/projects/{projectId}/tasks")
    Call<NetworkModels.TaskModel> getTasks(@Path(value = "projectId", encoded = true) String projectId);

    /**
     * Gets specific task from Hive Server.
     *
     * @param projectId the project id
     * @param taskId    the task id
     * @return the specific task
     */
    @GET("/projects/{projectId}/tasks/{taskId}")
    Call<NetworkModels.SingleTaskModel> getSpecificTask(@Path(value = "projectId", encoded = true) String projectId, @Path(value = "taskId", encoded = true) String taskId);

    /**
     * Gets specific assignment from Hive Server.
     *
     * @param projectId    the project id
     * @param assignmentId the assignment id
     * @return the specific assignment
     */
    @GET(" /projects/{projectId}/assignments/{assignmentId}")
    Call<NetworkModels.SpecificAssignmentModel> getSpecificAssignment(@Path(value = "projectId", encoded = true) String projectId, @Path(value = "assignmentId", encoded = true) String assignmentId);

    /**
     * Assign assets to Assignment in Hive Server
     *
     * @param projectId the project id
     * @param taskId    the task id
     * @param assertId  the assert id
     * @param userId    the user id
     * @return assignments
     */
    @GET(" /projects/{projectId}/tasks/{taskId}/assets/{assetId}/assignments")
    Call<NetworkModels.AssignmentModel> assignAsset(@Path(value = "projectId", encoded = true) String projectId, @Path(value = "taskId", encoded = true) String taskId, @Path(value = "assetId", encoded = true) String assertId, @Header("Cookie") String userId);


    /**
     * Create assignment in Hive Server.
     *
     * @param projectId  the project id
     * @param assignment the assignment
     * @param taskId     the task id
     * @param userId     the user id
     * @return the call
     */
    @POST("/projects/{projectId}/tasks/{taskId}/assignments")
    Call<NetworkModels.AssignmentModel> userCreateAssignment(@Path(value = "projectId", encoded = true) String projectId, @Body Assignment assignment, @Path(value = "taskId", encoded = true) String taskId, @Header("Cookie") String userId);


    /**
     * Create task in Hive Server.
     *
     * @param t         the task
     * @param projectId the project id
     * @param taskId    the task id
     * @return the call
     */
    @POST("/admin/projects/{projectId}/tasks/{taskId}")
    Call<NetworkModels.SingleTaskModel> adminCreateTasks(@Body Task t, @Path(value = "projectId", encoded = true) String projectId, @Path(value = "taskId", encoded = true) String taskId);

    /**
     * Create user in Hive Server.
     *
     * @param u         the user
     * @param projectId the project id
     * @return the call
     */
    @POST("/projects/{projectId}/user")
    Call<NetworkModels.UserModel> createUser(@Body User u, @Path(value = "projectId", encoded = true) String projectId);


}
