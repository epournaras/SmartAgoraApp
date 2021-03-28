package smartagora.ethz.ch.networkRequests;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import smartagora.ethz.ch.networkRequests.hiveServerModels.Asset;
import smartagora.ethz.ch.networkRequests.hiveServerModels.Assignment;
import smartagora.ethz.ch.networkRequests.hiveServerModels.Project;
import smartagora.ethz.ch.networkRequests.hiveServerModels.Task;
import smartagora.ethz.ch.networkRequests.hiveServerModels.User;
import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.AssignmentThread;
import smartagora.ethz.ch.roomDatabase.entityModels.AnswerEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.AssignmentEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.CheckBoxDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.CheckBoxEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.CombinationEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionSensorsEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.RadioButtonEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.RadioDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.SensorEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.StartAndDestinationEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.TextBoxDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.TextBoxEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;
import smartagora.ethz.ch.models.ProjectModel;
import smartagora.ethz.ch.prefrences.AppPreferences;
import smartagora.ethz.ch.uiActivities.HomeActivity;
import smartagora.ethz.ch.uiActivities.ProjectsActivity;
import smartagora.ethz.ch.uiActivities.QuestionsActivity;
import smartagora.ethz.ch.utils.utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static smartagora.ethz.ch.uiActivities.HomeActivity.loadingDialog;
import static smartagora.ethz.ch.uiActivities.HomeActivity.projects;

/**
 * NetworkCallbacks class has a responsibility to call the Retrofit Interface API methods.
 */
public class NetworkCallbacks {

    private static final List<Asset> serverAssets = new ArrayList<>();
    public static final List<Task> serverTasks = new ArrayList<>();
    private static final List<Assignment> serverAssignments = new ArrayList<>();
    private static NetworkModels.SingleTaskModel singleTaskModel;
    private static RetrofitInterface apiService;

    /**
     * Gets all projects from Hive Server.
     *
     * @param context the context
     */
    public static void getAllProjectsFromServer(final Context context) {

        loadingDialog = utils.initializeProgressDialog(context);

        projects = new ArrayList<>();
        if (loadingDialog != null && (!loadingDialog.isShowing()))
            HomeActivity.loadingDialog.show();

        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);

        Call<NetworkModels.ProjectModel> call = apiService.getAllProjects();

        call.enqueue(new Callback<NetworkModels.ProjectModel>() {


            @Override
            public void onResponse(@Nonnull Call<NetworkModels.ProjectModel> call, @Nonnull Response<NetworkModels.ProjectModel> response) {

                if (response.isSuccessful()) {
                    NetworkModels.ProjectModel projectList;
                    projectList = response.body(); // what happens if projectList is null, even if the response is successful? should be handled at all such places.


                    if (projectList == null || projectList.getProjects() == null) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                        Toast.makeText(context, context.getString(R.string.no_projects), Toast.LENGTH_SHORT).show();
                    } else {
                        int projectsSize = projectList.getProjects().size();
                        //iterate through all the projects in Hive Server
                        for (int i = 0; i < projectsSize; i++) {
                            Project currentProject = projectList.getProjects().get(i);
                            ProjectModel projectModel = new ProjectModel(currentProject.getName(), currentProject.getId(), false, currentProject.getDescription());

                            //check if projectId contains selected dataScientistId e.g 01022019194131779-new1122
                            //then add project into projects list
                            if (projectModel.getId().contains("-") && projectModel.getId().substring(projectModel.getId().lastIndexOf('-')).equals("-" + AppPreferences.getDataScientistId(context))) {
                                HomeActivity.projects.add(projectModel);
                            }
                        }

                        if (HomeActivity.projects.size() == 0) {
                            if (loadingDialog != null && loadingDialog.isShowing())
                                loadingDialog.dismiss();
                            Toast.makeText(context, context.getString(R.string.no_projects), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(context, ProjectsActivity.class);
                            context.startActivity(intent);
                            if (loadingDialog != null && loadingDialog.isShowing())
                                loadingDialog.dismiss();
                        }
                    }//else
                }
            }

            @Override
            public void onFailure(@Nonnull Call<NetworkModels.ProjectModel> call, @Nonnull Throwable t) {
                Log.i("FailureMethod", t.getMessage() + "");
                if (loadingDialog != null && loadingDialog.isShowing())
                    loadingDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.network_problem), Toast.LENGTH_LONG).show();

            }
        });

    }

    /**
     * Gets all tasks from Hive server.
     *
     * @param context   the application context
     * @param projectId the project id
     */
    public static void getAllTasksFromServer(final Context context, final String projectId) {

        serverTasks.clear();

        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);

        Call<NetworkModels.TaskModel> call = apiService.getTasks(projectId);

        call.enqueue(new Callback<NetworkModels.TaskModel>() {
            @Override
            public void onResponse(@Nonnull Call<NetworkModels.TaskModel> call, @Nonnull Response<NetworkModels.TaskModel> response) {

                if (response.isSuccessful()) {
                    NetworkModels.TaskModel tasks = response.body();

                    if (tasks == null || tasks.getTasks() == null) {
                        Log.i("TaskApn", "No");
                    } else {

                        int size = tasks.getTasks().size();
                        for (int i = 0; i < size; i++) {
                            Task task = new Task();
                            Task currentTasks = tasks.getTasks().get(i);

                            task.setId(currentTasks.getId());
                            task.setName(currentTasks.getName());
                            task.setDescription(currentTasks.getDescription());
                            task.setProject(currentTasks.getProject());
                            task.setCurrentState(currentTasks.getCurrentState());
                            task.setCompletionCriteria(currentTasks.getCompletionCriteria());
                            task.setAssignmentCriteria(currentTasks.getAssignmentCriteria());

                            NetworkCallbacks.serverTasks.add(task);
                        }
                    }
                }//responseSuccessful
            }

            @Override
            public void onFailure(@Nonnull Call<NetworkModels.TaskModel> call, @Nonnull Throwable t) {
                Log.i("TasksFailureMethod", t.getMessage() + "");

                if (loadingDialog != null && loadingDialog.isShowing())
                    loadingDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.network_problem), Toast.LENGTH_LONG).show();

            }
        });

    }

    /**
     * Gets all assets from Hive server.
     *
     * @param projectId the project id
     */
    public static void getAllAssetsFromServer(final Context context, String projectId) {

        serverAssets.clear();
        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);
        Call<NetworkModels.AssetModel> call = apiService.getAllAssetsFromSpecificProject(projectId);

        call.enqueue(new Callback<NetworkModels.AssetModel>() {
            @Override
            public void onResponse(@Nonnull Call<NetworkModels.AssetModel> call, @Nonnull Response<NetworkModels.AssetModel> response) {
                if (response.isSuccessful()) {
                    NetworkModels.AssetModel assetModel = response.body();

                    if (assetModel == null || assetModel.getAssets() == null) {
                        loadingDialog.dismiss();
                        Toast.makeText(context, context.getString(R.string.no_assignments), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (assetModel.getAssets().size() == 0) {
                        Log.i("Assignments", "No Assignments");
                        return;
                    }

                    for (int i = 0; i < assetModel.getAssets().size(); i++) {
                        Asset currentAsset = assetModel.getAssets().get(i);
                        Asset asset = new Asset();
                        asset.setId(currentAsset.getId());
                        asset.setName(currentAsset.getName());
                        asset.setProject(currentAsset.getProject());


                        NetworkCallbacks.serverAssets.add(asset);

                        Log.i("AssetName", currentAsset.getName());
                    }

                }
            }

            @Override
            public void onFailure(@Nonnull Call<NetworkModels.AssetModel> call, @Nonnull Throwable t) {
                Log.i("AssetsFailureMethod", t.getMessage() + "");
                if (loadingDialog != null && loadingDialog.isShowing())
                    loadingDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.network_problem), Toast.LENGTH_LONG).show();

            }
        });


    }

    /**
     * Create user on server.
     *
     * @param context   the context
     * @param user      the user
     * @param projectId the project id
     */
    public static void createUserOnServer(final Context context, final User user, final String projectId) {
        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);
        Call<NetworkModels.UserModel> call = apiService.createUser(user, projectId);

        call.enqueue(new Callback<NetworkModels.UserModel>() {
            @Override
            public void onResponse(@Nonnull Call<NetworkModels.UserModel> call, @Nonnull Response<NetworkModels.UserModel> response) {
                if (response.isSuccessful()) {
                    Log.i("User", "User Created on Server");
                }
            }

            @Override
            public void onFailure(@Nonnull Call<NetworkModels.UserModel> call, @Nonnull Throwable t) {
                Log.i("FailureUser", t.getMessage() + "");
                Toast.makeText(context, context.getString(R.string.network_problem), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Create task on server.
     *
     * @param task      the task
     * @param projectId the project id
     * @param taskId    the task id
     */
    private static void createTaskOnServer(Context context, Task task, String projectId, String taskId) {
        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);

        Call<NetworkModels.SingleTaskModel> call = apiService.adminCreateTasks(task, projectId, taskId);

        call.enqueue(new Callback<NetworkModels.SingleTaskModel>() {
            @Override
            public void onResponse(@Nonnull Call<NetworkModels.SingleTaskModel> call, @Nonnull Response<NetworkModels.SingleTaskModel> response) {
                if (response.isSuccessful()) {
                    Log.i("Task Creation", "Successful");
                }// successful response
            }

            @Override
            public void onFailure(@Nonnull Call<NetworkModels.SingleTaskModel> call, @Nonnull Throwable t) {
                Log.i("Task Failure", t.getMessage() + "");

            }
        });


    }

    /**
     * Create assignment on server.
     *
     * @param projectId the project id
     * @param taskId    the task id
     * @param assetId   the asset id
     * @param userId    the user id
     */
    private static void createAssignmentOnServer(Context context, final String projectId, final String taskId, String assetId, final String userId) {
        //pass cookie
        String cookieHeader = projectId + "_user_id=" + userId;

        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);

        Call<NetworkModels.AssignmentModel> call = apiService.assignAsset(projectId, taskId, assetId, cookieHeader);

        call.enqueue(new Callback<NetworkModels.AssignmentModel>() {
            @Override
            public void onResponse(@Nonnull Call<NetworkModels.AssignmentModel> call, @Nonnull Response<NetworkModels.AssignmentModel> response) {
                if (response.isSuccessful()) {
                    Log.i("Assignment Creation", "Successful");
                }// successful response
            }

            @Override
            public void onFailure(@Nonnull Call<NetworkModels.AssignmentModel> call, @Nonnull Throwable t) {
                Log.i("FailureMethod", t.getMessage() + "");
                if (loadingDialog != null && loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }
        });


    }

    /**
     * Assign assignments to users.
     *
     * @param c         the c
     * @param projectId the project id
     * @param userId    the user id
     */
    public static void assignAssignmentsToUsers(final Context context, final Context c, final String projectId, final String userId) {
        projects = new ArrayList<>();

        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);

        Call<NetworkModels.UserModel> call = apiService.getAllUsersFromSpecificProject(projectId);

        call.enqueue(new Callback<NetworkModels.UserModel>() {
            @Override
            public void onResponse(@Nonnull Call<NetworkModels.UserModel> call, @Nonnull Response<NetworkModels.UserModel> response) {
                if (response.isSuccessful()) {
                    NetworkModels.UserModel users = response.body();
                    if (users == null)
                        Log.i("User", "No User Found");

                    else {
                        int size = users.getUsers().size();

                        for (int j = 0; j < size; j++) {
                            User currentUser = users.getUsers().get(j);

                            for (int i = 0; i < serverAssets.size(); i++) {
                                String assignmentId = projectId + "HIVE" + serverTasks.get(0).getId() + "HIVE" + serverAssets.get(i).getId() + "HIVE" + currentUser.getId();
                                getAssignment(context, projectId, assignmentId, serverTasks.get(0).getId(), serverAssets.get(i).getId(), currentUser.getId());
                            }
                        }
                        getAllAssignmentsFromServer(c, projectId, userId);
                    }
                }//responseSuccessful
            }

            @Override
            public void onFailure(@Nonnull Call<NetworkModels.UserModel> call, @Nonnull Throwable t) {
                Log.i("FailureMethod", t.getMessage() + "");
                if (loadingDialog != null && loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }
        });


    }

    /**
     * Gets all assignments from server.
     *
     * @param context   the context
     * @param projectId the project id
     * @param userId    the user id
     */
    public static void getAllAssignmentsFromServer(final Context context, String projectId, final String userId) {
        serverAssignments.clear();

        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);

        Call<NetworkModels.AssignmentModel> call = apiService.getAllAssignmentsFromSpecificProject(projectId);

        call.enqueue(new Callback<NetworkModels.AssignmentModel>() {
            @Override
            public void onResponse(@Nonnull Call<NetworkModels.AssignmentModel> call, @Nonnull Response<NetworkModels.AssignmentModel> response) {

                if (response.isSuccessful()) {
                    NetworkModels.AssignmentModel assignmentModel = response.body();

                    if (assignmentModel != null) {

                        int size = assignmentModel.getAssignments().size();
                        if (size == 0) {
                            Log.i("Size", "No Assignment");
                        } else {
                            for (int i = 0; i < size; i++) {
                                Assignment currentAssignment = assignmentModel.getAssignments().get(i);
                                Log.i("User", currentAssignment.getUserName());
                                if (currentAssignment.getUserName().equals(userId) && currentAssignment.getProjectName().equals(AppPreferences.getProjectId(context))) {
                                    serverAssignments.add(currentAssignment);
                                    Log.i("AssignmentName", currentAssignment.getAsset().getName());
                                    Log.i("AssignmentUser", currentAssignment.getUserName());
                                    Log.i("AssignmentTask", currentAssignment.getTask());
                                    Log.i("AssignmentProject", currentAssignment.getProjectName());
                                    Log.i("AssignmentState", currentAssignment.getState());
                                }
                            }
                            saveDataToRoomDatabase(context);
                        }
                    } else {
                        Log.e("NetworkCallbacks", "getAllAssignmentsFromServer:onResponse: assignmentModel was null");
                    }
                }//responseSuccessful
            }

            @Override
            public void onFailure(@Nonnull Call<NetworkModels.AssignmentModel> call, @Nonnull Throwable t) {
                Log.i("FailureMethod", t.getMessage() + "");
                if (loadingDialog != null && loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }
        });


    }

    private static void getAssignment(final Context context, final String projectId, final String assignmentId, final String taskId, final String assetId, final String userId) {
        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);

        Call<NetworkModels.SpecificAssignmentModel> call = apiService.getSpecificAssignment(projectId, assignmentId);

        call.enqueue(new Callback<NetworkModels.SpecificAssignmentModel>() {
            @Override
            public void onResponse(@Nonnull Call<NetworkModels.SpecificAssignmentModel> call, @Nonnull Response<NetworkModels.SpecificAssignmentModel> response) {


                if (response.code() == 500) {
                    createAssignmentOnServer(context, projectId, taskId, assetId, userId);
                    return;
                }
                if (response.code() == 200) {
                    if (response.body() == null) {
                        Log.e("NetworkCallbacks", "getAssignment:onResponse: response.body() was null");
                        return;
                    }

                    String s = response.body().getAssignment().getState();

                    if (s.equalsIgnoreCase("finished")) {
                        Log.i("AssignmentState", "Finished");
                    } else {
                        createAssignmentOnServer(context, projectId, taskId, assetId, userId);
                    }
                }
            }


            @Override
            public void onFailure(@Nonnull Call<NetworkModels.SpecificAssignmentModel> call, @Nonnull Throwable t) {
                Log.i("ErrorMessage", t.getMessage());
            }
        });

    }

    /**
     * Update Assignment in Specific Task then create task in Hive Server and Save Assignment Data into the File.
     *
     * @param context    the context
     * @param projectId  the project id
     * @param assignment the assignment
     * @param task       the task
     * @param taskId     the task id
     * @param userId     the user id
     */
    private static void updateAssignmentInTask(final Context context, final String projectId, final Assignment assignment, final Task task, final String taskId, String userId) {
        //pass cookie
        String cookieHeader = projectId + "_user_id=" + userId;

        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);
        try {
            Call<NetworkModels.AssignmentModel> call = apiService.userCreateAssignment(projectId, assignment, taskId, cookieHeader);


            call.enqueue(new Callback<NetworkModels.AssignmentModel>() {
                @Override
                public void onResponse(@Nonnull Call<NetworkModels.AssignmentModel> call, @Nonnull Response<NetworkModels.AssignmentModel> response) {
                    if (response.isSuccessful()) {

                        if (response.code() != 200)
                            Log.i("Update Assignment", "Successful");

                        createTaskOnServer(context, task, projectId, taskId);
                        saveAssignmentInFile(context, assignment);
                        RoomDatabaseHandler.getAppDatabase(context).getAssignmentDao().disableAssignment(assignment.getAssignmentid(), "finished");
                        utils.assignmentSubmissionStatus = true;


                    } else if (response.errorBody() != null) {
                        Log.i("SuccessError", response.errorBody().toString());
                    } else {
                        Log.i("SuccessError", "response.isSuccessful() was false, but response.errBody() was null, not information over the error, http status code " + response.code());
                    }
                }

                @Override
                public void onFailure(@Nonnull Call<NetworkModels.AssignmentModel> call, @Nonnull Throwable t) {
                    Log.i("Assignment Failure", t.getMessage() + "");
                }
            });

        } catch (Exception e) {
            Log.i("Exception", e.getMessage() + "");
        }

    }

    /**
     * Get Specific Task from Server and Update Assignment method in Task.
     *
     * @param context    the context
     * @param projectId  the project id
     * @param assignment the assignment
     * @param answers    the answers
     * @param taskId     the task id
     * @param userId     the user id
     */
    private static void getSpecificTask(final Context context, final String projectId, final Assignment assignment, final List<Assignment.Answer> answers, final String taskId, final String userId) {
        singleTaskModel = new NetworkModels.SingleTaskModel();
        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);

        Call<NetworkModels.SingleTaskModel> call = apiService.getSpecificTask(projectId, taskId);

        call.enqueue(new Callback<NetworkModels.SingleTaskModel>() {
            @Override
            public void onResponse(@Nonnull Call<NetworkModels.SingleTaskModel> call, @Nonnull Response<NetworkModels.SingleTaskModel> response) {

                if (response.isSuccessful()) {

                    Log.i("Task Getting", "Success");
                    NetworkModels.SingleTaskModel t = response.body();

                    if (t == null || t.getTask() == null) {
                        Log.i("Task Getting", "Null");
                    } else {
                        singleTaskModel = t;

                        Task task1 = new Task();
                        if (singleTaskModel.getTask() != null) {
                            Log.i("TaskValue", "Yes");
                            if (singleTaskModel.getTask().getProject() != null)
                                task1.setProject(singleTaskModel.getTask().getProject());

                            if (singleTaskModel.getTask().getId() != null)
                                task1.setId(singleTaskModel.getTask().getId());

                            if (singleTaskModel.getTask().getName() != null)
                                task1.setName(singleTaskModel.getTask().getName());

                            if (singleTaskModel.getTask().getCurrentState() != null)
                                task1.setCurrentState(singleTaskModel.getTask().getCurrentState());

                            if (singleTaskModel.getTask().getCompletionCriteria() != null)
                                task1.setCompletionCriteria(singleTaskModel.getTask().getCompletionCriteria());

                            if (singleTaskModel.getTask().getAssignmentCriteria() != null)
                                task1.setAssignmentCriteria(singleTaskModel.getTask().getAssignmentCriteria());
                        }

                        Assignment.AssignmentSubmittedData assignmentSubmittedData = new Assignment.AssignmentSubmittedData();
                        assignmentSubmittedData.setAnswer(answers);


                        try {
                            utils.closeSensorInfoFile(context);
                            // get all sensor Info from a File in Internal Storage
                            String sensorData = utils.getSensorData(context);

                            Gson gson = new Gson();

                            Type type = new TypeToken<List<Map<String, List<Assignment.SensorReading>>>>() {
                            }.getType();
                            List<Map<String, List<Assignment.SensorReading>>> reading = gson.fromJson(sensorData, type);
                            assignmentSubmittedData.setSensorreadings(reading);

                        } catch (Exception ignored) {

                        }


                        assignment.setSubmittedData(assignmentSubmittedData);

                        if (singleTaskModel.getTask().getAssignmentCriteria().getSubmittedData().getAssignments() == null) {
                            Map<String, Assignment> assignmentMap = new HashMap<>();
                            assignmentMap.put(assignment.getAssignmentid(), assignment);
                            Task.SubmittedData submittedData = new Task.SubmittedData();
                            submittedData.setAssignments(assignmentMap);
                            Task.AssignmentCriteria assignmentCriteria = new Task.AssignmentCriteria();
                            assignmentCriteria.setSubmittedData(submittedData);
                            task1.setAssignmentCriteria(assignmentCriteria);
                        } else {
                            Task.SubmittedData submittedData = singleTaskModel.getTask().getAssignmentCriteria().getSubmittedData();
                            submittedData.getAssignments().put(assignment.getAssignmentid(), assignment);
                            Task.AssignmentCriteria assignmentCriteria = new Task.AssignmentCriteria();
                            assignmentCriteria.setSubmittedData(submittedData);
                            task1.setAssignmentCriteria(assignmentCriteria);
                        }


                        updateAssignmentInTask(context, projectId, assignment, task1, taskId, userId);


                    }

                }
            }

            @Override
            public void onFailure(@Nonnull Call<NetworkModels.SingleTaskModel> call, @Nonnull Throwable t) {

                Log.i("Task Failure", t.getMessage() + "");

            }
        });

    }

    /**
     * Get loaded assignment from Server and GetSpecificTask method called to get the Task and update Assignment in that Task.
     *
     * @param context      the context
     * @param projectId    the project id
     * @param userId       the user id
     * @param assignmentId the assignment
     * @param answersList  the answers
     */
    public static void AssignmentSubmission(final Context context, final String projectId, final String userId, final String assignmentId, final List<AnswerEntity> answersList) {
        apiService = RetrofitClient.getClient(context).create(RetrofitInterface.class);

        Call<NetworkModels.AssignmentModel> call = apiService.getAllAssignmentsFromSpecificProject(projectId);

        call.enqueue(new Callback<NetworkModels.AssignmentModel>() {
            @Override
            public void onResponse(@Nonnull Call<NetworkModels.AssignmentModel> call, @Nonnull Response<NetworkModels.AssignmentModel> response) {

                if (response.isSuccessful()) {
                    NetworkModels.AssignmentModel assignmentModel = response.body();
                    int size;

                    if (assignmentModel != null) {
                        size = assignmentModel.getAssignments().size();
                    } else {
                        size = 0;
                        Log.e("NetworkCallbacks", "AssignmentSubmission:onResponse: assignmentModel was null, set size to 0");
                    }

                    Assignment assignment;
                    for (int i = 0; i < size; i++) {
                        Assignment currentAssignment = assignmentModel.getAssignments().get(i);
                        if (currentAssignment.getUserName().equals(userId) && currentAssignment.getAssignmentid().equals(assignmentId)) {

                            assignment = currentAssignment;
                            assignment.setState("finished");


                            int unfinished = assignment.getAsset().getCounts().getUnfinished();
                            assignment.getAsset().getCounts().setUnfinished(unfinished - 1);
                            int finished = assignment.getAsset().getCounts().getFinished();
                            assignment.getAsset().getCounts().setFinished(finished + 1);

                            String project = assignment.getProjectName();
                            String task = assignment.getTask();


                            List<Assignment.Answer> answers = new ArrayList<>();

                            for (int j = 0; j < answersList.size(); j++) {
                                AnswerEntity currentAnswer = answersList.get(j);

                                Assignment.Answer answer = new Assignment.Answer();
                                AnswerEntity answerEntity = new AnswerEntity();

                                answer.setAnswer(currentAnswer.getAnswer());

                                String assignmentName = RoomDatabaseHandler.getAppDatabase(context).getAssignmentDao().getAssignmentName(currentAnswer.getAssignmentId());

                                answer.setFiles_name(assignmentName);

                                int qId = assignment.getAsset().getMetadata().getRecord().getSampleDataModel().get(j).getId();

                                answerEntity.setAnswer(currentAnswer.getAnswer());

                                answerEntity.setQuestionId(qId);
                                answerEntity.setAnswerTime(currentAnswer.getAnswerTime());
                                answerEntity.setAssignmentId(assignment.getAssignmentid());
                                answerEntity.setCredits(currentAnswer.getCredits());

                                RoomDatabaseHandler.getAppDatabase(context).getAnswerDao().insert(answerEntity);


                                answer.setId(currentAnswer.getId());


                                QuestionEntity question = RoomDatabaseHandler.getAppDatabase(context).getQuestionDao().getQuestion(currentAnswer.getAssignmentId(), String.valueOf(currentAnswer.getQuestionId()));

                                answer.setLatitude(question.getLatitude());
                                answer.setLongitude(question.getLongitude());
                                answer.setQuestion(question.getQuestion());
                                answer.setType(question.getType());
                                answer.setTimeAtAnswering(currentAnswer.getAnswerTime());
                                answers.add(answer);


                            }

                            getSpecificTask(context, project, assignment, answers, task, userId);


                        }
                    }//for loop


                }

            }

            @Override
            public void onFailure(@Nonnull Call<NetworkModels.AssignmentModel> call, @Nonnull Throwable t) {
                Log.i("AssignmentFound", t.getMessage() + "");
            }
        });
    }

    /**
     * Save assignment data into the File.
     *
     * @param context    the context
     * @param assignment the assignment
     */
    private static void saveAssignmentInFile(Context context, Assignment assignment) {
        try {
            String f = assignment.getAsset().getName() + ".json";

            ContextWrapper contextWrapper = new ContextWrapper(context);
            File assignmentFolder = contextWrapper.getExternalFilesDir("Assignments"); //Creating an internal dir;
            File assignmentFile = new File(assignmentFolder, f); //Getting a file within the dir.


            Writer output;
            output = new BufferedWriter(new FileWriter(assignmentFile));
            output.write(assignment.toString());
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Save Assignments Data of current Project into Database.
     */
    private static void saveDataToRoomDatabase(final Context context) {

        // iterate through all Assignments in selected Project
        for (int i = 0; i < serverAssignments.size(); i++) {

            Assignment currentAssignment = serverAssignments.get(i);
            if (currentAssignment.getAsset() != null) {

                AssignmentThread assignmentThread = new AssignmentThread(context);
                //get assignment names from Room Database
                List<String> filesList = assignmentThread.getAllAssignmentNames();

                if (currentAssignment.getAsset().getMetadata() != null) {
                    if (currentAssignment.getAsset().getMetadata().getRecord() != null) {

                        String filename = currentAssignment.getAsset().getName();

                        if (filename != null)
                            utils.fileName = filename;

                        //check if assignment data is already exist in database
                        if (!filesList.contains(filename) && filename != null) {

                            StartAndDestinationEntity startAndDestinationEntity = new StartAndDestinationEntity();
                            AssignmentEntity assignmentEntity = new AssignmentEntity();

                            if (currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getDefaultCredit() != null) {
                                assignmentEntity.setId(currentAssignment.getAssignmentid());
                                assignmentEntity.setName(currentAssignment.getAsset().getName());
                                assignmentEntity.setDefaultCredit(currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getDefaultCredit());

                            }
                            if (currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getMode() != null) {
                                assignmentEntity.setMode(currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getMode());
                                assignmentEntity.setState(currentAssignment.getState());
                            }


                            if (currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getStartLatitude() != null) {
                                startAndDestinationEntity.setStartLatitude(currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getStartLatitude());
                                startAndDestinationEntity.setAssignmentId(currentAssignment.getAssignmentid());
                            }
                            if (currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getDestinationLatitude() != null) {
                                startAndDestinationEntity.setDestinationLatitude(currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getDestinationLatitude());
                            }
                            if (currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getDestinationLongitude() != null) {
                                startAndDestinationEntity.setDestinationLongitude(currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getDestinationLongitude());
                            }
                            if (currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getStartLongitude() != null) {
                                startAndDestinationEntity.setStartLongitude(currentAssignment.getAsset().getMetadata().getRecord().getStartAndDestinationModel().getStartLongitude());
                            }


                            //save Assignment in Room Database
                            RoomDatabaseHandler.getAppDatabase(context).getAssignmentDao().insert(assignmentEntity);

                            //check if Assignment has start and ending location
                            if (startAndDestinationEntity.getDestinationLatitude() != null)
                                //save Assignment start and ending location in Room Database
                                RoomDatabaseHandler.getAppDatabase(context).getStartAndDestinationDao().insert(startAndDestinationEntity);


                            //get Questions List Size in Assignment
                            int sampleDataModelListSize = currentAssignment.getAsset().getMetadata().getRecord().getSampleDataModel().size();

                            //iterate through all the questions
                            for (int j = 0; j < sampleDataModelListSize; j++) {

                                Asset.SampleDataModel currentSampleDataModel = currentAssignment.getAsset().getMetadata().getRecord().getSampleDataModel().get(j);

                                QuestionEntity questionEntity = new QuestionEntity();

                                if (!currentSampleDataModel.getSequence().equalsIgnoreCase("disable"))
                                    questionEntity.setSequence(currentSampleDataModel.getSequence());


                                if (currentSampleDataModel.getVicinity() != null)
                                    questionEntity.setVicinity(currentSampleDataModel.getVicinity());


                                questionEntity.setId(currentSampleDataModel.getId());
                                questionEntity.setAssignmentId(currentAssignment.getAssignmentid());
                                questionEntity.setQuestion(currentSampleDataModel.getQuestion());
                                questionEntity.setType(currentSampleDataModel.getType());
                                questionEntity.setFrequency(currentSampleDataModel.getFrequency());
                                questionEntity.setTime(currentSampleDataModel.getTime());
                                questionEntity.setLatitude(currentSampleDataModel.getLatitude());
                                questionEntity.setLongitude(currentSampleDataModel.getLongitude());
                                questionEntity.setVisibility(currentSampleDataModel.getVisibility());
                                questionEntity.setMandatory(currentSampleDataModel.getMandatory());

                                //Save Question in Room Database
                                RoomDatabaseHandler.getAppDatabase(context).getQuestionDao().insert(questionEntity);

                                int questionId = currentSampleDataModel.getId();
                                String AssignmentId = currentAssignment.getAssignmentid();
                                String questionType = currentSampleDataModel.getType();

                                //get Sensors list size of Question
                                int sensorSize = currentSampleDataModel.getSensor().size();

                                //iterate through all the question sensors
                                for (int k = 0; k < sensorSize; k++) {
                                    SensorEntity sensorEntity = new SensorEntity();
                                    sensorEntity.setName(currentSampleDataModel.getSensor().get(k).getName());

                                    switch (sensorEntity.getName()) {
                                        case "Light":
                                            sensorEntity.setId(1);
                                            break;
                                        case "Gyroscope":
                                            sensorEntity.setId(2);
                                            break;
                                        case "Proximity":
                                            sensorEntity.setId(3);
                                            break;
                                        case "Accelerometer":
                                            sensorEntity.setId(4);
                                            break;
                                        case "Location":
                                            sensorEntity.setId(5);
                                            break;
                                        case "Noise":
                                            sensorEntity.setId(6);
                                            break;
                                    }

                                    int sensorId = sensorEntity.getId();

                                    // Save Question and Sensor Id in Room Database
                                    QuestionSensorsEntity questionSensorsEntity = new QuestionSensorsEntity();
                                    questionSensorsEntity.setAssignmentId(AssignmentId);
                                    questionSensorsEntity.setQuestionId(questionId);
                                    questionSensorsEntity.setSensorId(sensorId);
                                    RoomDatabaseHandler.getAppDatabase(context).getQuestionSensorsDao().insert(questionSensorsEntity);

                                }

                                // check if Assignment question type is TextBox then save question in TextBox Table
                                if (questionType.equals("textBox")) {
                                    TextBoxEntity textBoxEntity = new TextBoxEntity();
                                    textBoxEntity.setAssignmentId(AssignmentId);
                                    textBoxEntity.setQuestionId(questionId);
                                    textBoxEntity.setName(currentSampleDataModel.getOption().get(0).getName());

                                    if ((currentSampleDataModel.getOption().get(0).getCredits() != null) &&
                                            (!currentSampleDataModel.getOption().get(0).getCredits().equalsIgnoreCase("disable")))
                                        textBoxEntity.setCredits(currentSampleDataModel.getOption().get(0).getCredits());

                                    //Save TextBox Question in Room Database
                                    RoomDatabaseHandler.getAppDatabase(context).getTextBoxDao().insert(textBoxEntity);

                                    //check if TextBox question has Associate question then save associate Question in TextBoxDecisionMode Table
                                    if ((currentSampleDataModel.getOption().get(0).getNextQuestion() != null) &&
                                            (!currentSampleDataModel.getOption().get(0).getNextQuestion().equalsIgnoreCase("disable"))) {
                                        int nextQuestionId = Integer.parseInt(currentSampleDataModel.getOption().get(0).getNextQuestion());
                                        TextBoxDecisionModeEntity textBoxDecisionModeEntity = new TextBoxDecisionModeEntity();
                                        textBoxDecisionModeEntity.setNextQuestionId(nextQuestionId);
                                        textBoxDecisionModeEntity.setNextAssignmentId(currentAssignment.getAssignmentid());
                                        textBoxDecisionModeEntity.setCredits(currentSampleDataModel.getOption().get(0).getCredits());
                                        //get last inserted TextBox from Room Database
                                        TextBoxEntity textBoxEntity1 = RoomDatabaseHandler.getAppDatabase(context).getTextBoxDao().getLastInsertedRow();
                                        //set textBox id as a foreign key in TextBoxDecisionMode Table
                                        textBoxDecisionModeEntity.setTextBoxId(textBoxEntity1.getId());
                                        //Save Associate Question in TextBoxDecisionMode Table
                                        RoomDatabaseHandler.getAppDatabase(context).getTextBoxDecisionModeDao().insert(textBoxDecisionModeEntity);
                                    }// next Question not Null
                                    continue;
                                }//TextBox Insertion in Room


                                // check if Assignment question type is likertScale then save question in LikertScale Table
                                if (questionType.equals("likertScale")) {
                                    LikertScaleEntity likertScaleEntity = new LikertScaleEntity();
                                    likertScaleEntity.setAssignmentId(AssignmentId);
                                    likertScaleEntity.setQuestionId(questionId);
                                    likertScaleEntity.setName(currentSampleDataModel.getOption().get(0).getName());

                                    if ((currentSampleDataModel.getOption().get(0).getCredits() != null) &&
                                            (!currentSampleDataModel.getOption().get(0).getCredits().equalsIgnoreCase("disable")))
                                        likertScaleEntity.setCredits(currentSampleDataModel.getOption().get(0).getCredits());

                                    //Save LikertScale Question in Room Database
                                    RoomDatabaseHandler.getAppDatabase(context).getLikertScaleDao().insert(likertScaleEntity);

                                    //get All likertScale options/values list size
                                    int likertScaleSize = currentSampleDataModel.getOption().size();

                                    //iterate through the likertScale options/values size
                                    for (int likertScale = 0; likertScale < likertScaleSize; likertScale++) {
                                        //check if LikertScale question has Associate question then save associate Question in LikertScaleDecisionMode Table
                                        Asset.Option currentLikertScale = currentSampleDataModel.getOption().get(likertScale);

                                        if ((currentLikertScale.getNextQuestion() != null) &&
                                                (!currentLikertScale.getNextQuestion().equalsIgnoreCase("disable"))) {
                                            LikertScaleDecisionModeEntity likertScaleDecisionModeEntity = new LikertScaleDecisionModeEntity();
                                            int nextQuestionId = Integer.parseInt(currentLikertScale.getNextQuestion());
                                            likertScaleDecisionModeEntity.setValue(currentLikertScale.getId());
                                            likertScaleDecisionModeEntity.setNextQuestionId(nextQuestionId);
                                            likertScaleDecisionModeEntity.setNextAssignmentId(currentAssignment.getAssignmentid());
                                            likertScaleDecisionModeEntity.setCredits(currentLikertScale.getCredits());
                                            //get last inserted likertScale from Room Database
                                            LikertScaleEntity likertScaleEntity1 = RoomDatabaseHandler.getAppDatabase(context).getLikertScaleDao().getLastInsertedRow();
                                            //set likertScale id as a foreign key in LikertScaleDecisionMode Table
                                            likertScaleDecisionModeEntity.setLikertScaleId(likertScaleEntity1.getId());
                                            //Save Associate Question in LikertScaleDecisionMode Table
                                            RoomDatabaseHandler.getAppDatabase(context).getLikertScaleDecisionModeDao().insert(likertScaleDecisionModeEntity);
                                        }// next Question not Null
                                    }//For loop
                                    continue;
                                }//Likert Scale Insertion in Room


                                // check if Assignment question type is radioButton then save question in RadioButton Table
                                if (questionType.equals("radio")) {
                                    //get radiobutton options list size of Question
                                    int optionSize = currentSampleDataModel.getOption().size();
                                    for (int l = 0; l < optionSize; l++) {
                                        Asset.Option currentOption = currentSampleDataModel.getOption().get(l);

                                        RadioDecisionModeEntity radioDecisionModeEntity = new RadioDecisionModeEntity();

                                        RadioButtonEntity radioButtonEntity = new RadioButtonEntity();
                                        radioButtonEntity.setAssignmentId(AssignmentId);
                                        radioButtonEntity.setQuestionId(questionId);
                                        radioButtonEntity.setName(currentOption.getName());

                                        if ((currentOption.getCredits() != null) &&
                                                (!currentOption.getCredits().equalsIgnoreCase("disable")))
                                            radioButtonEntity.setCredits(currentOption.getCredits());

                                        //Save Radiobutton question option in Room Database
                                        RoomDatabaseHandler.getAppDatabase(context).getRadioButtonDao().insert(radioButtonEntity);

                                        //check if radioButton question option has Associate question then save associate Question in RadioDecisionMode Table
                                        if ((currentOption.getNextQuestion() != null) &&
                                                (!currentOption.getNextQuestion().equalsIgnoreCase("disable"))) {
                                            //get last inserted radiobutton question option from Room Database
                                            RadioButtonEntity radioButton = RoomDatabaseHandler.getAppDatabase(context).getRadioButtonDao().getLastInsertedRow();
                                            int nextQuestionId = Integer.parseInt(currentOption.getNextQuestion());
                                            //set radiobutton question option id as a foreign key in RadioDecisionMode Table
                                            radioDecisionModeEntity.setRadioId(radioButton.getId());
                                            radioDecisionModeEntity.setNextQuestionId(nextQuestionId);
                                            radioDecisionModeEntity.setNextAssignmentId(currentAssignment.getAssignmentid());
                                            radioDecisionModeEntity.setCredits(currentOption.getCredits());
                                            //Save Associate Question in RadioDecisionMode Table
                                            RoomDatabaseHandler.getAppDatabase(context).getRadioDecisionModeDao().insert(radioDecisionModeEntity);
                                        }// next Question not Null
                                    }// radiobutton options Loop
                                    continue;
                                }//Radio Button Question Type

                                // check if Assignment question type is checkbox then save question in Checkbox Table
                                if (questionType.equals("checkbox")) {
                                    List<Asset.Combination> ce = currentSampleDataModel.getCombination();
                                    // check checkbox combinations are not null
                                    if (ce != null) {

                                        //get checkbox combinations size
                                        int siz = ce.size();

                                        //iterate through all the combinations of checkboxes
                                        for (int k = 0; k < siz; k++) {

                                            CombinationEntity combinationEntity = new CombinationEntity();
                                            CheckBoxDecisionModeEntity checkBoxDecisionModeEntity = new CheckBoxDecisionModeEntity();
                                            String order = "";

                                            Asset.Combination currentCombination = ce.get(k);

                                            if (currentCombination.getCredits() != null) {
                                                combinationEntity.setCredits(currentCombination.getCredits());
                                                checkBoxDecisionModeEntity.setCredits(currentCombination.getCredits());

                                                //check
                                                if (currentCombination.getSelected() != null) {
                                                    int size = currentCombination.getSelected().size();

                                                    if (size == 1) {
                                                        order = currentCombination.getSelected().get(0).getOrder();
                                                    } else {
                                                        for (int ord = 0; ord < size; ord++) {
                                                            String currentOrder = currentCombination.getSelected().get(ord).getOrder();
                                                            if (currentOrder != null) {
                                                                if (Objects.equals(order, ""))
                                                                    order = currentOrder;
                                                                else
                                                                    order = order + "," + currentOrder;

                                                            }
                                                        }//orders loop
                                                    }
                                                }//selected

                                                combinationEntity.setOrder(order);
                                                combinationEntity.setQuestionId(questionId);
                                                combinationEntity.setAssignmentId(AssignmentId);
                                                RoomDatabaseHandler.getAppDatabase(context).getCombinationDao().insert(combinationEntity);
                                            }


                                            if (currentCombination.getNextQuestion() != null) {

                                                CombinationEntity combinationEntity1 = RoomDatabaseHandler.getAppDatabase(context).getCombinationDao().getLastInsertedRow();

                                                int nextQuestionId = Integer.parseInt(currentCombination.getNextQuestion());
                                                checkBoxDecisionModeEntity.setNextQuestionId(nextQuestionId);
                                                checkBoxDecisionModeEntity.setNextAssignmentId(currentAssignment.getAssignmentid());
                                                checkBoxDecisionModeEntity.setCombinationId(String.valueOf(combinationEntity1.getId()));

                                                RoomDatabaseHandler.getAppDatabase(context).getCheckBoxDecisionModeDao().insert(checkBoxDecisionModeEntity);
                                            }

                                        }//combinations loop
                                    }


                                    //get checkbox options size
                                    int optionSize = currentSampleDataModel.getOption().size();
                                    //iterate through checkbox options
                                    for (int l = 0; l < optionSize; l++) {

                                        Asset.Option currentOption = currentSampleDataModel.getOption().get(l);
                                        CheckBoxEntity checkBoxEntity = new CheckBoxEntity();
                                        checkBoxEntity.setAssignmentId(AssignmentId);
                                        checkBoxEntity.setQuestionId(questionId);
                                        checkBoxEntity.setName(currentOption.getName());

                                        if ((currentOption.getCredits() != null) &&
                                                (!currentOption.getCredits().equalsIgnoreCase("disable")))
                                            checkBoxEntity.setCredits(currentOption.getCredits());

                                        //Save checkbox option in Room Database
                                        RoomDatabaseHandler.getAppDatabase(context).getCheckBoxDao().insert(checkBoxEntity);
                                    }
                                }//CheckBox Question Type

                            }// for loop for all questions


                        }//filename

                    }//Assignments record

                }//meta data


            }//asset


        }//for loop


        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();

        if (serverAssignments.size() != 0) {
            Intent intent = new Intent(context, QuestionsActivity.class);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "No Assignments", Toast.LENGTH_SHORT).show();
        }


    }

}
