package smartagora.ethz.ch.networkRequests;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitClient ia singleton class which is used to create a Retrofit Instance .
 */
class RetrofitClient {

    /**
     * The constant BASE_URL/ API.
     */
    //private static final String BASE_URL = "http://195.201.58.108:8080/";

    private static Retrofit retrofit = null;


    /**
     * Get Retrofit Instance.
     *
     * @return the client
     */
    static Retrofit getClient(final Context context) {

        final SharedPreferences preferences = context.getSharedPreferences("ServerIP", Context.MODE_PRIVATE);

        String ip = preferences.getString("IP", null);
        if(ip == null){
            throw new RuntimeException("IP was still null");
        }


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + ip + ":8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private void doRest(){

    }


}
