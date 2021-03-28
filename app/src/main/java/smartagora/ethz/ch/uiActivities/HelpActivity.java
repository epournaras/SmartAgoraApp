package smartagora.ethz.ch.uiActivities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import smartagora.ethz.ch.R;
import smartagora.ethz.ch.prefrences.AppPreferences;
import smartagora.ethz.ch.utils.DeviceUuidFactory;


public class HelpActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView tv = findViewById(R.id.help_text);
        Context mContext = this.getApplicationContext();
        tv.setText(R.string.basic_help_section);
        tv.setMovementMethod(new ScrollingMovementMethod());
        new DeviceUuidFactory(HelpActivity.this);


        String help = AppPreferences.getHelp(mContext);
        if (help == null)
            tv.setText(R.string.basic_help_section);
        else
            tv.setText(AppPreferences.getHelp(mContext));

    }
}