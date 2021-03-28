package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import diasclient.coss.ethz.ch.diasclientlibrarylight.R;

public class LoggerVisualiser extends AppCompatActivity {

    private ListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logger_visualiser);
        view = findViewById(R.id.loggerList);

        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.config_log, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.MenuDelete:
//                resetDB();
//                return true;
//
//            case R.id.MenuRelaod:
//                update();
//                return true;

            default:
                Toast.makeText(this, "Menu option not configured", Toast.LENGTH_SHORT).show();
                return false;
        }
    }

    private void print(LinkedList<HashMap<String, String>> list){
        StringBuilder s = new StringBuilder("*************************\n\n");
        for(HashMap<String, String> hm : list){
            s.append(hm.get("LOG") + "\n");
            s.append(hm.get("DATE") + "\n*************************\n");
        }
        String veryLongString = s.toString();

        int maxLogSize = 1000;
        //print log
        for(int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.i("LOG:         ", veryLongString.substring(start, end));
        }

    }

    private void update(){
        Gson gson = new Gson();

        String[] from = {"LOG", "DATE"};
        int[] to = {R.id.TimeoutSensor, R.id.timeoutDate};

        SharedPreferences sp = this.getSharedPreferences("DIASInterface", Context.MODE_PRIVATE);
        int indexBound = sp.getInt("INDEX", 0);
        LinkedList<HashMap<String, String>> listElems = new LinkedList<>();



        for(int i = 0; i<indexBound; i++){
            LinkedList<String> l = gson.fromJson(sp.getString("LOGGING" + i, null), new TypeToken<LinkedList<String>>(){}.getType());
            LinkedList<String> d = gson.fromJson(sp.getString("TIMESTAMPS" + i, null), new TypeToken<LinkedList<String>>(){}.getType());
            if(l == null || d == null){
                Toast.makeText(this, "No database found", Toast.LENGTH_SHORT).show();
                return;
            }
            Iterator<String> lI = l.iterator();
            Iterator<String> dI = d.iterator();
            while(lI.hasNext()){
                String log = lI.next();
                String date = dI.next();
                HashMap<String, String> map = new HashMap<>();
                map.put("LOG", log);
                map.put("DATE", date);
                listElems.add(map);
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listElems, R.layout.listviewtimeout, from, to);

        view.setAdapter(adapter);

        print(listElems);
    }

    private void resetDB(){
        getSharedPreferences("DIASInterface", Context.MODE_PRIVATE).edit().remove("INDEX").apply();
        Toast.makeText(this, "Database reset", Toast.LENGTH_SHORT).show();
        update();
    }



}
