package smartagora.ethz.ch.uiActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jaredrummler.android.device.DeviceName;

import java.util.ArrayList;
import java.util.Objects;

import smartagora.ethz.ch.networkRequests.hiveServerModels.User;
import smartagora.ethz.ch.networkRequests.NetworkCallbacks;
import smartagora.ethz.ch.R;
import smartagora.ethz.ch.models.ProjectModel;
import smartagora.ethz.ch.prefrences.AppPreferences;
import smartagora.ethz.ch.utils.DeviceUuidFactory;


public class ProjectsActivity extends Activity {

    private ProjectsAdapter dataAdapter = null;
    private Context mContext;
    private DeviceUuidFactory mDeviceId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        //Generate list View from ArrayList

        mContext = this.getApplicationContext();

        Button okBtn = findViewById(R.id.ok);
        Button cancelBtn = findViewById(R.id.cancel);

        mDeviceId = new DeviceUuidFactory(ProjectsActivity.this);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ArrayList<ProjectModel> projectList = dataAdapter.projectList;
                for (int i = 0; i < projectList.size(); i++) {
                    ProjectModel projectModel = projectList.get(i);
                    if (projectModel.isSelected()) {


                        User user = new User();
                        user.setEmail("testEmail");
                        user.setId(mDeviceId.getDeviceUuid().toString());
                        AppPreferences.saveProjectId(projectModel.getId(), ProjectsActivity.this);
                        user.setName(DeviceName.getDeviceInfo(mContext).marketName + "_" + DeviceName.getDeviceInfo(mContext).model);
                        NetworkCallbacks.createUserOnServer(ProjectsActivity.this,user,projectModel.getId());

                        if (projectModel.getAutoAssignment()) {
                            AppPreferences.saveAuto("true", mContext);
                        } else {
                            AppPreferences.saveAuto("false", mContext);
                        }
                        if (projectModel.getHelp() != null) {
                            AppPreferences.saveHelpForProject(projectModel.getHelp(), mContext);
                        } else {
                            AppPreferences.saveHelpForProject(null, mContext);
                        }
                    }

                }
                finish();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        displayListView();

    }


    private void displayListView() {

        dataAdapter = new ProjectsActivity.ProjectsAdapter(ProjectsActivity.this,
                HomeActivity.projects);
        ListView listView = findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    private class ProjectsAdapter extends ArrayAdapter<ProjectModel> {
        private final ArrayList<ProjectModel> projectList;

        ProjectsAdapter(Context context,
                        ArrayList<ProjectModel> projectList) {
            super(context, R.layout.radio_button_project_view_model, projectList);
            this.projectList = new ArrayList<>();
            this.projectList.addAll(projectList);
        }

        private class ViewHolder {
            TextView code;
            RadioButton name;
        }

        @NonNull
        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                if(vi != null)
                    convertView = vi.inflate(R.layout.project_radio_button, null);
                else{
                    Log.e("ProjectsActivity", "getView: LayoutInflater was null, created empty View!");
                    convertView = new View(null);
                }

                holder = new ViewHolder();
                holder.code = convertView.findViewById(R.id.code);
                holder.name = convertView.findViewById(R.id.radiobutton1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        RadioButton cb = (RadioButton) v;
                        ProjectModel country = (ProjectModel) cb.getTag();
                        country.setSelected(cb.isChecked());

                        // Update project status in projectList
                        // and also update the view
                        for (int i=0;i<projectList.size();i++){
                            if(Objects.equals(projectList.get(i).getId(), country.getId())){
                                projectList.get(i).setSelected(true);

                            }else{
                                projectList.get(i).setSelected(false);
                            }
                        }
                        notifyDataSetChanged();
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ProjectModel project = projectList.get(position);

            holder.code.setText("");
            holder.name.setText(project.getName());
            holder.name.setChecked(project.isSelected());
            holder.name.setTag(project);

            return convertView;

        }

    }

}