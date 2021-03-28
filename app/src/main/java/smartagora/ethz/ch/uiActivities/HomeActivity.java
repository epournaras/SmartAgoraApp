package smartagora.ethz.ch.uiActivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


import diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.apiinterface.MappingWrapper;
import io.fabric.sdk.android.Fabric;
import smartagora.ethz.ch.modes.DecisionMode;
import smartagora.ethz.ch.modes.SensorsInfo;
import smartagora.ethz.ch.modes.SequenceMode;
import smartagora.ethz.ch.modes.SimpleDIASMode;
import smartagora.ethz.ch.modes.SimpleMode;
import smartagora.ethz.ch.networkRequests.NetworkCallbacks;
import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.AnswerThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.QuestionThread;
import smartagora.ethz.ch.roomDatabase.entityModels.AnswerEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.SensorEntity;
import smartagora.ethz.ch.models.ProjectModel;
import smartagora.ethz.ch.models.SliderMenuModel;
import smartagora.ethz.ch.models.StaticModel;
import smartagora.ethz.ch.uiActivities.adapter.ExpandableListAdapter;
import smartagora.ethz.ch.databases.DatabaseHandler;
import smartagora.ethz.ch.models.QuestionDataModel;
import smartagora.ethz.ch.prefrences.AppPreferences;
import smartagora.ethz.ch.prefrences.PreferencesKeys;

import smartagora.ethz.ch.utils.ActivityStackManager;
import smartagora.ethz.ch.utils.BottomNavigationViewHelper;
import smartagora.ethz.ch.utils.DeviceUuidFactory;
import smartagora.ethz.ch.utils.ExceptionalHandling;
import smartagora.ethz.ch.utils.JSONParser;
import smartagora.ethz.ch.utils.Permissions;
import smartagora.ethz.ch.utils.QuestionHandling;
import smartagora.ethz.ch.utils.utils;


/**
 * HomeActivity is the main screen shown to user after splash.
 * This activity has map view at start and when user load
 * file and click on the survey start button map
 * shows with start of user survey.
 * This activity has a slide
 * menu in which sensors
 * data and information
 * about survey is
 * shown.
 */

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {
    private ImageView car, train, cycle, walking;
    public static final double REFERENCE = 0.00002;
    public int currentSeq = 0;
    private final double tolerance = 25;
    public double accuracyTolerance;
    private static GoogleMap mMap;
    private HomeActivity mCurrentActivity;
    private final EventBus mEventBus = EventBus.getDefault();

    public static LatLng myLocation;
    public static final ArrayList<Double> localAnswers = new ArrayList<>();

    private DatabaseHandler databaseHandler;
    private QuestionHandling questionHandling;
    public int VisitedCheckPoint = 0;

    private SensorManager mSensorManager;

    public boolean handlerCheck = false;
    public Handler handlerForQuestions = null;
    private String travelMode = "walking";
    private int poolTime = 9000; // Time interval for questions to be delayed
    private Polyline line;

    private final Handler m_handler = new Handler();
    private Runnable m_statusChecker;
    private final int m_interval = 300; // interval, after which sensor value will be updated
    private ActivityStackManager mStackManager;
    private DrawerLayout drawer;
    private List<SliderMenuModel> listDataHeader; // side menu header items
    private HashMap<SliderMenuModel, List<String>> listDataChild; // side menu child items
    // Child Items of slide menu
    private final List<String> transportMeanHeading = new ArrayList<>();
    private final List<String> surveyQuestionHeading = new ArrayList<>();
    private final List<String> checkPointsHeading = new ArrayList<>();
    public final List<String> visitedPointsHeading = new ArrayList<>();
    private final List<String> locationsHeading = new ArrayList<>();
    private long lastPress;
    private Toast backPressToast;

    public ArrayList<Integer> sequenceList;
    public ArrayList<LatLng> missedPath;

    public boolean isMissed = false;
    private final int answeredQuestions = 0;

    private boolean backPressed = false;
    private static Polygon mPolygon;

    private LatLng fromLatLng;
    private double mAngle = 0.0;

    private ImageView transportToggle;
    private LinearLayout transportLayout;
    private LinearLayout tempLayout;
    private List<String> sensorsNamesList;
    private HashMap<String, String> mSensorReading;
    private Context mContext;
    private DatabaseHandler mDBHandler;
    public int mCredits;
    private StateProgressBar stateProgressBar;
    private DeviceUuidFactory mDeviceId;
    public static ProgressDialog loadingDialog;
    public static int skippedNextQuestion = 0;
    private MappingWrapper mappingWrapper;
    public boolean isDiasQuestionShowing = false;
    public static boolean isLeaveDiasCheckPoint = false;
    private HashMap<String, Double> diasSimpleLikertScaleAnswers;
    public static List<Integer> diasCheckpointQuestions;
    public static ArrayList<ProjectModel> projects;
    private static List<JSONObject> tasks;
    private static List<JSONObject> tasks1;
    private static ArrayList<Double> progressBarValues;
    //Dias progress bar
    private ImageView diasShow;

    private TextView diasPersonTv;

    private ImageView diasShowGroup;

    private TextView diasGroupTv;

    //TAG
    private static final String TAG = "HomeActivity";

    public List<AnswerEntity> answersList;
    private List<SensorEntity> sensors;

    private BottomNavigationView bottomNavigationView;

    /**
     * Called when activity is created.
     *
     * @param savedInstanceState not used, standard
     */
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Fabric.with(this, Crashlytics.getInstance());
        Crashlytics.setUserIdentifier(Build.MANUFACTURER + ": " + Build.MODEL);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate() called");

        initVariables();

        stateProgressBar = findViewById(R.id.your_state_progress_bar_id);

        diasShow = findViewById(R.id.dias_show);
        diasShowGroup = findViewById(R.id.dias_show1);
        diasGroupTv = findViewById(R.id.dias_aggregated_tv1);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
//
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        // side menu view
        ExpandableListView expandableList = findViewById(R.id.navigation_menu);
        expandableList.setDivider(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        try {
            //map
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            if (QuestionsActivity.assignmentQuestions != null) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MapStartPosition();
                        walking.setBackgroundResource(R.drawable.selectbg);
                        transportToggle.setImageDrawable(walking.getDrawable());
                    }
                }, 30);
            } else if (!Objects.equals(QuestionsActivity.FileFromList, "")) {
                getDataFromDB();
            }

            sequenceList = new ArrayList<>();
            StaticModel.skippedQuestions = new ArrayList<>();
            StaticModel.questionsQueue = new ArrayList<>();
            StaticModel.questionsSensorsQueue = new ArrayList<>();
            StaticModel.shownQuestionsQueue = new ArrayList<>();

            // setting data to slide menu items
            slideMenuData();

            ExpandableListAdapter mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

            // setting list adapter
            expandableList.setAdapter(mMenuAdapter);

            // showing message to user if GPS service is not ON
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager == null || !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }

            tempLayout = findViewById(R.id.tempLayout);

            transportToggle = findViewById(R.id.transportToggleView);
            transportLayout = findViewById(R.id.transportToggleLayout);

            transportToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transportLayout.setVisibility(View.VISIBLE);
                    transportToggle.setVisibility(View.GONE);
                    tempLayout.setVisibility(View.VISIBLE);
                }
            });

            // Used for hiding collapsible buttons when user click anywhere on the screen
            tempLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (transportLayout.getVisibility() == View.VISIBLE) {
                        transportLayout.setVisibility(View.GONE);
                        transportToggle.setVisibility(View.VISIBLE);
                    }
                    tempLayout.setVisibility(View.GONE);
                }
            });

            car = findViewById(R.id.car_mode);
            train = findViewById(R.id.train_mode);
            cycle = findViewById(R.id.cycle_mode);
            walking = findViewById(R.id.walking_mode);

            car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.clear();
                    car.setBackgroundResource(R.drawable.selectbg);
                    train.setBackgroundResource(R.color.transparent);
                    walking.setBackgroundResource(R.color.transparent);
                    cycle.setBackgroundResource(R.color.transparent);

                    transportToggle.setImageDrawable(car.getDrawable());
                    transportToggle.setVisibility(View.VISIBLE);
                    transportLayout.setVisibility(View.GONE);
                    tempLayout.setVisibility(View.GONE);
                    poolTime = 1000;
                    travelMode = "driving";
                    MapStartPosition();

                }
            });
            train.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.clear();
                    train.setBackgroundResource(R.drawable.selectbg);
                    car.setBackgroundResource(R.color.transparent);
                    walking.setBackgroundResource(R.color.transparent);
                    cycle.setBackgroundResource(R.color.transparent);

                    transportToggle.setImageDrawable(train.getDrawable());
                    transportToggle.setVisibility(View.VISIBLE);
                    transportLayout.setVisibility(View.GONE);
                    tempLayout.setVisibility(View.GONE);
                    poolTime = 500;
                    travelMode = "transit";
                    MapStartPosition();


                }
            });
            cycle.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    mMap.clear();
                    cycle.setBackgroundResource(R.drawable.selectbg);
                    train.setBackgroundResource(R.color.transparent);
                    car.setBackgroundResource(R.color.transparent);
                    walking.setBackgroundResource(R.color.transparent);

                    transportToggle.setImageDrawable(cycle.getDrawable());
                    transportToggle.setVisibility(View.VISIBLE);
                    transportLayout.setVisibility(View.GONE);
                    tempLayout.setVisibility(View.GONE);
                    poolTime = 7000;
                    travelMode = "bicycling";
                    MapStartPosition();

                }
            });
            walking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.clear();
                    walking.setBackgroundResource(R.drawable.selectbg);
                    train.setBackgroundResource(R.color.transparent);
                    car.setBackgroundResource(R.color.transparent);
                    cycle.setBackgroundResource(R.color.transparent);

                    transportToggle.setImageDrawable(walking.getDrawable());
                    transportToggle.setVisibility(View.VISIBLE);
                    transportLayout.setVisibility(View.GONE);
                    tempLayout.setVisibility(View.GONE);
                    poolTime = 9000;
                    travelMode = "walking";
                    MapStartPosition();

                }
            });

            initViews();
            setBottomNavigationView();

            if (mSensorManager == null)
                mSensorManager = utils.registerSensorListener(this);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }


        askForIPIfNecessary();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.changeIP){
            askForIp(getSharedPreferences("ServerIP", MODE_PRIVATE));
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void askForIPIfNecessary(){
        final SharedPreferences preferences = getSharedPreferences("ServerIP", MODE_PRIVATE);

        String ip = preferences.getString("IP", null);

        if (ip == null){
            askForIp(preferences);
        }
    }

    private void askForIp(final SharedPreferences preferences){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);
        et.setHint("127.0.0.1");
        builder.setView(et);
        builder.setTitle("Set IP");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String ip = et.getText().toString();


                String[] splitted = ip.split("\\.");

                boolean formatCorrect = false;

                if (splitted.length == 4){
                    formatCorrect = true;

                    for(int j = 0; j<4; j++){
                        try{
                            int g = Integer.parseInt(splitted[j]);

                            if(g < 0 || g > 255){
                                formatCorrect = false;
                            }
                        }catch (NumberFormatException e){
                            formatCorrect = false;
                        }
                    }

                }

                if(formatCorrect)
                    preferences.edit().putString("IP", ip).apply();
                else {
                    askForIp(preferences);
                    Toast.makeText(HomeActivity.this, "Please enter a valid IP", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.create().show();
    }


    private void initVariables() {
        try {
            if (mContext == null)
                mContext = this.getApplicationContext();

            if (mDBHandler == null)
                mDBHandler = new DatabaseHandler(mContext);

            if (sensorsNamesList == null)
                sensorsNamesList = new ArrayList<>();

            if (mSensorReading == null)
                mSensorReading = new HashMap<>();


            if (diasSimpleLikertScaleAnswers == null)
                diasSimpleLikertScaleAnswers = new HashMap<>();

            mCurrentActivity = this;
            mEventBus.register(mCurrentActivity);

            mStackManager = ActivityStackManager.getInstance();
            mStackManager.startLocationService(this);

            if (databaseHandler == null)
                databaseHandler = new DatabaseHandler(this);

            mCredits = 0;

            if (mDeviceId == null)
                mDeviceId = new DeviceUuidFactory(HomeActivity.this);


            if (questionHandling == null)
                questionHandling = new QuestionHandling(mContext);

            if (diasCheckpointQuestions == null)
                diasCheckpointQuestions = new ArrayList<>();

            //arrayList of dias aggregated progress bar values
            if (progressBarValues == null)
                progressBarValues = new ArrayList<>();

            if (sensors == null)
                sensors = new ArrayList<>();

            //dias person text view
            diasPersonTv = findViewById(R.id.dias_aggregated_tv);
            answersList = new ArrayList<>();
            if (loadingDialog == null)
                loadingDialog = utils.initializeProgressDialog(this);
            if (projects == null)
                projects = new ArrayList<>();
            if (tasks == null && tasks1 == null) {
                tasks1 = new ArrayList<>();
                tasks = new ArrayList<>();
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    private void setBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.dataScientistId:
                                manageDataScientistId();
                                break;
                            case R.id.selectProject:
                                navigationSetProject();
                                break;
                            case R.id.getAllAssignments:
                                navigationGetAllAssignments();
                                break;
                            case R.id.completeAssignment:
                                navigationCompleteAssignment();
                                break;
                            case R.id.help:
                                navigationHelp();
                                break;
                        }
                        return true;
                    }
                });
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    }

    /**
     * Gets all projects from the server
     */
    private void navigationSetProject() {
        if (AppPreferences.getDataScientistId(HomeActivity.this).isEmpty())
            Toast.makeText(mContext, "Please add Data Scientist Code first!", Toast.LENGTH_LONG).show();
        else
            NetworkCallbacks.getAllProjectsFromServer(HomeActivity.this);
    }

    /**
     * Gets all assignments from the server
     */
    private void navigationGetAllAssignments() {
        if (AppPreferences.getProjectId(HomeActivity.this).isEmpty()) {
            Toast.makeText(mContext, "Please select Project!", Toast.LENGTH_LONG).show();
            return;
        }

        loadingDialog = utils.initializeProgressDialog(HomeActivity.this);
        if (loadingDialog != null && (!loadingDialog.isShowing()))
            loadingDialog.show();
        final String projectId = AppPreferences.getProjectId(HomeActivity.this);
        NetworkCallbacks.getAllTasksFromServer(HomeActivity.this, projectId);
        NetworkCallbacks.getAllAssetsFromServer(HomeActivity.this, projectId);
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                if (AppPreferences.getAuto(HomeActivity.this).equals("true")) {
                    if (NetworkCallbacks.serverTasks.size() == 0) {
                        loadingDialog.dismiss();
                        Toast.makeText(HomeActivity.this, "Task is not Created on server", Toast.LENGTH_LONG).show();
                    } else
                        NetworkCallbacks.assignAssignmentsToUsers(mContext, HomeActivity.this, projectId + "", mDeviceId.getDeviceUuid().toString() + "");
                } else
                    NetworkCallbacks.getAllAssignmentsFromServer(HomeActivity.this, projectId + "", mDeviceId.getDeviceUuid().toString() + "");
            }
        }, 3000);
    }

    /**
     * Triggers the action for committing an assignment
     */
    private void navigationCompleteAssignment() {
        if (utils.assignmentSubmissionStatus) {
            Toast.makeText(mContext, "Assignment is already completed!", Toast.LENGTH_LONG).show();
            return;
        }

        final String projectId = AppPreferences.getProjectId(HomeActivity.this);
        if (projectId == null || projectId.equals("") | QuestionsActivity.selectedAssignment == null) {
            Toast.makeText(mContext, "Assignment is not loaded!", Toast.LENGTH_LONG).show();
            return;
        }

        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Submit Assignment")
                .setMessage("Are you sure you want to submit this Assignment?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        String assignmentId = QuestionsActivity.selectedAssignment.getId();
                        NetworkCallbacks.AssignmentSubmission(HomeActivity.this, projectId, mDeviceId.getDeviceUuid().toString(), assignmentId, answersList);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    /**
     * Opens the help window for the assignment
     */
    private void navigationHelp() {
        Toast.makeText(getApplicationContext(), "help", Toast.LENGTH_LONG).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    ExceptionalHandling.logException(e);
                }
            }
        }).start();
    }

    /**
     * Starting sensor service when screen is opened.
     */
    @Override
    public void onStart() {
        Log.d(TAG, "onStart() called");
        try {
            super.onStart();
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Called when activity is again called.
     */
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() called");
        try {
            super.onResume();
            utils.updateRegisterSensorListener(mSensorManager, this);
            if (!mStackManager.returnLocationFlag())
                mStackManager.startLocationService(this);

        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Called when another activity is started.
     */
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() called");
        try {
            super.onPause();
            utils.unregisterSensorListener(mSensorManager, this);
            overridePendingTransition(R.anim.rightout, R.anim.leftout);
            mStackManager.stopLocationService(this);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop() called");
        try {
            super.onStop();
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Called when activity is fully closed by user.
     */
    @Override
    protected void onDestroy() {

        Log.d(TAG, "onDestroy() called");
        try {
            if (utils.fileName.startsWith("DIAS") && mappingWrapper != null) {
                mappingWrapper.cleanclose();
                mappingWrapper = null;
            }
            handlerCheck = false;
            handlerForQuestions = null;
            fromLatLng = null;

            if (backPressed) {
                backPressed = false;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                System.exit(0);
            }
            super.onDestroy();
            finish();
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Show Alert for GPS
     */
    private void buildAlertMessageNoGps() {
        try {
            utils.buildAlertMessageNoGps(this);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * setting up side menu drawer contents.
     *
     * @param navigationView the view to add the content to
     */
    private void setupDrawerContent(NavigationView navigationView) {
        try {
            //revision: this doesn't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                            menuItem.setChecked(true);
                            drawer.closeDrawers();
                            return true;
                        }
                    });
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    /**
     * Setting slide menu Header and children data.
     */
    private void slideMenuData() {
        try {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            // Adding side menu header data
            listDataHeader.add(new SliderMenuModel("Transport Mean"));

            listDataHeader.add(new SliderMenuModel("Survey Questions"));

            listDataHeader.add(new SliderMenuModel("Check Points"));

            listDataHeader.add(new SliderMenuModel("Visited Points"));

            listDataHeader.add(new SliderMenuModel("Route Location"));

            listDataChild.put(listDataHeader.get(0), transportMeanHeading);
            listDataChild.put(listDataHeader.get(1), surveyQuestionHeading);
            listDataChild.put(listDataHeader.get(2), checkPointsHeading);
            listDataChild.put(listDataHeader.get(3), visitedPointsHeading);
            listDataChild.put(listDataHeader.get(4), locationsHeading);

        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * initializing the UI views and database handler.
     */
    private void initViews() {
        try {
            if (!Permissions.hasLocationPermissions(mCurrentActivity)) {
                Permissions.getLocationPermissions(mCurrentActivity);
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Return Questions data from database.
     */
    private void getDataFromDB() {
        try {
            if (mMap != null)
                mMap.clear();
            questionHandling.getDataFromRoomDB();

            placeMarkers(0);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Get Questions Data based on selecting from Load Trip list.
     * <p>
     * /**
     * draw route of start and destination locations on Map.
     */
    private void DrawCompleteRoute() {
        try {
            if (QuestionsActivity.startAndDestinationEntity != null && QuestionsActivity.startAndDestinationEntity.getStartLatitude() != null) {
                final String url = makeURL(Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getStartLatitude()), Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getStartLongitude()), Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getDestinationLatitude()), Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getDestinationLongitude()));
                new HomeActivity.connectAsyncTask(url, this).execute();
            } else
                handlerCheck = true;
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }

    }

    private static class connectAsyncTask extends AsyncTask<Void, Void, String> {
        final String url1;
        final WeakReference<HomeActivity> reference;

        connectAsyncTask(String url, HomeActivity context) {
            url1 = url;
            reference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            return new JSONParser().getJSONFromUrl(url1);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                HomeActivity activity = reference.get();

                if (activity == null)
                    return;

                if (result != null && !result.equals(""))
                    activity.drawPath(result);

                activity.handlerCheck = true;
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getDestinationLatitude()), Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getDestinationLongitude()))).icon(BitmapDescriptorFactory.fromResource((R.mipmap.flag_b))));
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getStartLatitude()), Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getStartLongitude()))).title("Start").icon(BitmapDescriptorFactory.fromResource((R.mipmap.flag_a))));
                activity.animateCameraTo(Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getStartLatitude()), Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getStartLongitude()));

            } catch (Exception ex) {
                ExceptionalHandling.logException(ex);
            }
        }
    }


    private String makeURL(double sourceLat, double sourceLong, double destLat, double destLong) {
        try {
            return utils.makeURL(sourceLat, sourceLong, destLat, destLong, travelMode);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
            return null;
        }
    }

    /**
     * draw path in line on Map
     *
     * @param result the result of the html query
     */
    private void drawPath(String result) {
        try {
            //Transform the string into a json object
            final JSONArray routeArray = new JSONObject(result).getJSONArray("routes");
            for (int i = 0; i < routeArray.length(); i++) {
                final JSONObject routes = routeArray.getJSONObject(i);
                JSONObject overviewPolyLines = routes.getJSONObject("overview_polyline");
                String encodedString = overviewPolyLines.getString("points");
                List<LatLng> routePoints = decodePoly(encodedString);
                if (i == 0 && routePoints != null) {
                    line = mMap.addPolyline(new PolylineOptions()
                            .addAll(routePoints)
                            .width(8)
                            .color(Color.BLUE)      //Google maps blue color
                            .geodesic(true)
                    );
                    line.setClickable(true);
                }
            }
            mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                @Override
                //changing color of polyline on click
                public void onPolylineClick(Polyline polyline) {
                    //do something with polyline
                    if (polyline.getPoints().equals(line.getPoints()) && line.getColor() == Color.GRAY) {
                        line.setColor(Color.BLUE);
                        line.setWidth(12);
                    }
                }

            });
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * decoding polyline
     *
     * @param encoded the encoded string to decode
     * @return decoded list of ployLines
     */
    private List<LatLng> decodePoly(String encoded) {
        try {
            List<LatLng> poly = new ArrayList<>();
            int index = 0;
            int len = encoded.length();
            int lat = 0;
            int lng = 0;

            while (index < len) {
                int b;
                int shift = 0;
                int result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dLat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dLat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dLng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dLng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
            return null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        try {
            Sensor sensor = sensorEvent.sensor;

            if (mSensorReading == null)
                mSensorReading = new HashMap<>();

            switch (sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    mSensorReading.put(StaticModel.ACCEL_SENSOR, sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2]);
                    break;
                case Sensor.TYPE_LIGHT:
                    mSensorReading.put(StaticModel.LIGHT_SENSOR, String.valueOf(sensorEvent.values[0]));
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    mSensorReading.put(StaticModel.GYRO_SENSOR, sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2]);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    mSensorReading.put(StaticModel.PROXIMITY_SENSOR, String.valueOf(sensorEvent.values[0]));
                    break;
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    /**
     * Placing markers on questions positions on Map.
     */
    public void placeMarkers(int j) {
        try {
            MarkerOptions unVisitedMarker = new MarkerOptions();
            unVisitedMarker.icon(BitmapDescriptorFactory.fromResource((R.mipmap.qmarker)));

            AnswerThread answerThread = new AnswerThread(mContext);
            List<AnswerEntity> visitedQuestions = answerThread.getAnsweredQuestions(utils.fileName);

            List<String> visitedQuestionIds = new ArrayList<>();

            for (int i = 0; i < visitedQuestions.size(); i++)
                visitedQuestionIds.add(String.valueOf(visitedQuestions.get(i).getQuestionId()));

            if (QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("decision")) {
                MarkerOptions decisionVisitedMarker = new MarkerOptions();
                double lat = Double.parseDouble(QuestionsActivity.assignmentQuestions.get(j).getLatitude());
                double lng = Double.parseDouble(QuestionsActivity.assignmentQuestions.get(j).getLongitude());
                if ((visitedQuestionIds.contains(Integer.toString(j + 1))) || (answersList.size() > 0 && j < answersList.size() && answersList.get(j) != null)) {
                    decisionVisitedMarker.icon((BitmapDescriptorFactory.fromResource((R.mipmap.visited_marker))));
                    decisionVisitedMarker.position(new LatLng(lat, lng));
                    mMap.addMarker(decisionVisitedMarker);
                } else {
                    unVisitedMarker.icon(BitmapDescriptorFactory.fromResource((R.mipmap.qmarker)));
                    unVisitedMarker.position(new LatLng(lat, lng));
                    mMap.addMarker(unVisitedMarker);
                }
            } else {
                for (int i = 0; i < QuestionsActivity.assignmentQuestions.size(); i++) {
                    if (QuestionsActivity.assignmentQuestions.get(i).getVisibility().equalsIgnoreCase("true")) {
                        double lat = Double.parseDouble(QuestionsActivity.assignmentQuestions.get(i).getLatitude());
                        double lng = Double.parseDouble(QuestionsActivity.assignmentQuestions.get(i).getLongitude());
                        MarkerOptions visitedMarker = new MarkerOptions();
                        if (QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("DIAS_Simple")) {

                            String qLatitude = QuestionsActivity.assignmentQuestions.get(i).getLatitude();
                            String qLongitude = QuestionsActivity.assignmentQuestions.get(i).getLongitude();
                            QuestionThread questionThread = new QuestionThread(mContext);

                            List<QuestionEntity> checkPointQuestions = questionThread.getCheckPointQuestions(utils.fileName, qLatitude, qLongitude);
                            List<QuestionEntity> checkPointAnsweredQuestions = answerThread.getCheckPointAnsweredQuestions(utils.fileName, qLatitude, qLongitude);

                            if (checkPointQuestions.size() == checkPointAnsweredQuestions.size()) {
                                unVisitedMarker = null;
                                visitedMarker.icon((BitmapDescriptorFactory.fromResource((R.mipmap.visited_marker))));
                                visitedMarker.position(new LatLng(lat, lng));
                                mMap.addMarker(visitedMarker);
                            } else {
                                unVisitedMarker = unVisitedMarker == null ? new MarkerOptions() : unVisitedMarker; //initialise when null
                                unVisitedMarker.icon(BitmapDescriptorFactory.fromResource((R.mipmap.qmarker)));
                                unVisitedMarker.position(new LatLng(lat, lng));
                                mMap.addMarker(unVisitedMarker);
                            }
                        } else if ((QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("Sequence") || QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("Simple")) &&
                                (visitedQuestionIds.contains(Integer.toString(i + 1)) || (answersList.size() > 0 && i == j && j < answersList.size() && answersList.get(j) != null))) {
                            unVisitedMarker = null;
                            visitedMarker.icon((BitmapDescriptorFactory.fromResource((R.mipmap.visited_marker))));
                            visitedMarker.position(new LatLng(lat, lng));
                            mMap.addMarker(visitedMarker);
                        } else {
                            unVisitedMarker = unVisitedMarker == null ? new MarkerOptions() : unVisitedMarker; //initialise when null
                            unVisitedMarker.icon(BitmapDescriptorFactory.fromResource((R.mipmap.qmarker)));
                            unVisitedMarker.position(new LatLng(lat, lng));
                            mMap.addMarker(unVisitedMarker);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void onMapReady(GoogleMap googleMap) {
        try {
            while (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "PERMISSION REQUIRED");
            }

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                mMap = googleMap;
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setRotateGesturesEnabled(true);

                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                int TAG_CODE_PERMISSION_LOCATION = 999;
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        TAG_CODE_PERMISSION_LOCATION);

            }

            animateCameraTo(AppPreferences.getLatitude(mCurrentActivity), AppPreferences.getLongitude(mCurrentActivity));

        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * animate the camera to required position.
     *
     * @param lat the desired latitude
     * @param lng the desired longitude
     */
    private void animateCameraTo(final double lat, final double lng) {
        try {
            if (mMap != null) {
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.
                        fromLatLngZoom(new LatLng(lat, lng), 16)), 1000, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                    }

                    @Override
                    public void onCancel() {
                        mMap.getUiSettings().setAllGesturesEnabled(true);
                    }
                });

            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }

    }

    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(String action) {
        try {
            if (action.equalsIgnoreCase(PreferencesKeys.LOCATION_UPDATED)) {
                myLocation = new LatLng(AppPreferences.getLatitude(mCurrentActivity), AppPreferences.getLongitude(mCurrentActivity));

                if (fromLatLng != null)
                    mAngle = SphericalUtil.computeHeading(fromLatLng, myLocation);
                fromLatLng = myLocation;
                //Polygon
                if (mPolygon != null)
                    mPolygon.remove();
                int vicinity = 25; // default vicinity in meter

                // get next question's vicinity
                if (QuestionsActivity.assignmentQuestions != null && QuestionsActivity.assignmentQuestions.size() > 0) {
                    String v;
                    if (skippedNextQuestion != 0)
                        v = QuestionsActivity.assignmentQuestions.get(skippedNextQuestion).getVicinity();
                    else
                        v = QuestionsActivity.assignmentQuestions.get(answeredQuestions).getVicinity();

                    vicinity = Integer.parseInt(v);
                }
                Log.i("onEvent_HomeActivity", myLocation + "");
                GoogleMap map = mMap;
                mPolygon = questionHandling.getmEllipse().drawMarkerWithEllipse(myLocation, mAngle, map, vicinity);

                if (handlerCheck && handlerForQuestions == null) {
                    handlerForQuestions = new Handler();
                    StartCheckingForQuestionInRadius();
                }
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    private void manageDataScientistId() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (layoutInflater == null) {
            Log.e("HomeActivity", "manageDataScientistId: layoutInflater was Null, not showing DataScientist dialog");
            return;
        }

        @SuppressLint("InflateParams") final View getDataScientistView = layoutInflater.inflate(R.layout.data_scientist_id, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(getDataScientistView);
        final EditText userInput = getDataScientistView.findViewById(R.id.dataScientistIdText);
        Button saveBtn;
        Button cancelBtn;
        saveBtn = getDataScientistView.findViewById(R.id.save);
        cancelBtn = getDataScientistView.findViewById(R.id.cancel);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(AppPreferences.getDataScientistId(HomeActivity.this).equals(userInput.getText().toString()))) {
                    AppPreferences.saveProjectId("", HomeActivity.this);
                    AppPreferences.saveDataScientistId(userInput.getText().toString(), HomeActivity.this);
                }
                alertDialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();

            }
        });

        alertDialog.show();
    }


    /**
     * check questions in user radius.
     */
    private void StartCheckingForQuestionInRadius() {


        if (missedPath == null) {
            missedPath = new ArrayList<>();
        }

        try {
            final Runnable r = new Runnable() {
                public void run() {
                    if (handlerCheck) {
                        accuracyTolerance = tolerance + AppPreferences.getAccuracy(mCurrentActivity);

                        SensorsInfo sensorsInfo = new SensorsInfo(HomeActivity.this, sensors, sensorsNamesList, mSensorReading, null);

                        if (questionHandling.getmEllipse().getAngle() != 0.0) {
                            if (QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("Simple"))
                                new SimpleMode(HomeActivity.this, answeredQuestions, stateProgressBar, questionHandling, sensorsInfo).call();
                            else if (QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("Sequence"))
                                new SequenceMode(HomeActivity.this, line, answeredQuestions, stateProgressBar, questionHandling, sensorsInfo).call();
                            else if (QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("Decision"))
                                new DecisionMode(HomeActivity.this, line, answeredQuestions, stateProgressBar, questionHandling, answersList, sensorsInfo).call();
                            else if (QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("DIAS_Simple"))
                                new SimpleDIASMode(HomeActivity.this, stateProgressBar, questionHandling, answeredQuestions, diasCheckpointQuestions, diasSimpleLikertScaleAnswers, answersList, sensorsInfo, diasGroupTv, diasPersonTv, diasShowGroup, diasShow).call();
                        }

                        if (answersList.size() == QuestionsActivity.assignmentQuestions.size() && QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("Simple"))
                            new SimpleMode(HomeActivity.this, answeredQuestions, stateProgressBar, questionHandling, sensorsInfo).call();
                        else if (answersList.size() == QuestionsActivity.assignmentQuestions.size() && QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("DIAS_Simple"))
                            new SimpleDIASMode(HomeActivity.this, stateProgressBar, questionHandling, answeredQuestions, diasCheckpointQuestions, diasSimpleLikertScaleAnswers, answersList, sensorsInfo, diasGroupTv, diasPersonTv, diasShowGroup, diasShow).call();
                    }
                    if (handlerForQuestions != null)
                        handlerForQuestions.postDelayed(this, poolTime);
                }
            };
            handlerForQuestions.postDelayed(r, poolTime);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * draw the map from user start and destination position
     */
    private void MapStartPosition() {
        try {
            List<QuestionDataModel> routeQuestions = questionHandling.getRouteQuestions();
            if (!StaticModel.XMLLoaded)
                questionHandling.getDataFromSavedFile();
            else
                StaticModel.XMLLoaded = false;

            sequenceList.clear();
            StaticModel.skippedQuestions.clear();
            StaticModel.questionsQueue.clear();

            StaticModel.skippedQuestions = new ArrayList<>();
            StaticModel.questionsQueue = new ArrayList<>();

            car.setEnabled(true);
            train.setEnabled(true);
            walking.setEnabled(true);
            cycle.setEnabled(true);
            //setting data to drawer

            transportMeanHeading.add(travelMode);
            for (int i = 0; i < transportMeanHeading.size(); i++)
                if (transportMeanHeading.size() > 1)
                    transportMeanHeading.remove(0);


            if (QuestionsActivity.startAndDestinationEntity != null) {
                getDataFromDB();

                locationsHeading.add("Start(Lat) = " + QuestionsActivity.startAndDestinationEntity.getStartLatitude()
                        + "\nStart(Longitude) = " + QuestionsActivity.startAndDestinationEntity.getStartLongitude()
                        + "\nDest(Latitude) = " + QuestionsActivity.startAndDestinationEntity.getDestinationLatitude()
                        + "\nDest(Longitude) = " + QuestionsActivity.startAndDestinationEntity.getDestinationLongitude());

                for (int i = 0; i < locationsHeading.size(); i++)
                    if (locationsHeading.size() > 1)
                        locationsHeading.remove(0);


                if (QuestionsActivity.assignmentQuestions != null && QuestionsActivity.assignmentQuestions.size() > 0)
                    DrawCompleteRoute();

            } else if (QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("simple") || QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("DIAS_Simple")) {
                handlerCheck = true;
                getDataFromDB();
            }

            surveyQuestionHeading.add(String.valueOf(routeQuestions.size()));
            for (int i = 0; i < surveyQuestionHeading.size(); i++)
                if (surveyQuestionHeading.size() > 1)
                    surveyQuestionHeading.remove(0);


            checkPointsHeading.add(String.valueOf(routeQuestions.size()));
            for (int i = 0; i < checkPointsHeading.size(); i++)
                if (checkPointsHeading.size() > 1)
                    checkPointsHeading.remove(0);


        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Called when Main Screen back button is pressed by user.
     */
    @Override
    public void onBackPressed() {
        try {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (transportLayout.getVisibility() == View.VISIBLE) {
                transportLayout.setVisibility(View.GONE);
                transportToggle.setVisibility(View.VISIBLE);
            }
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);

            } else {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastPress > 5000) {
                    backPressToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_LONG);
                    backPressToast.show();
                    lastPress = currentTime;
                } else {
                    if (backPressToast != null) backPressToast.cancel();
                    backPressed = true;
                    super.onBackPressed();
                    finish();
                }
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


}