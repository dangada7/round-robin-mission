package danga.roundrobinmission;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends ActionBarActivity {


    private UpdateRatingAsyncTask updateRating;
    private String[] companiesId = {"105","11","10","162","44"};
    private int[] companiesIcon = {R.drawable.facebook_icon,R.drawable.apple_icon,R.drawable.google_icon,R.drawable.alibaba_icon,R.drawable.twitter_icon};
    private String[] companiesName = {"FACEBOOK","APPLE","GOOGLE","ALIBABA","TWITTER"};

    private final String PREFERENCE_COMPANY_INFO="preference company info";
    private final String PREFERENCE_FIRST_RUN = "preference first run";

    //-----------------------------------------------------
    @Override
    protected void onStop() {
        super.onStop();
        updateRating.cancel(true);
    }

    //-----------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savePreference();

        ListView listView = (ListView)this.findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this,PREFERENCE_COMPANY_INFO,companiesId);
        listView.setAdapter(adapter);

        updateRating = new UpdateRatingAsyncTask(adapter);
        updateRating.execute();

    }

    //-----------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //-----------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //-----------------------------------------------------
    public void savePreference(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_COMPANY_INFO, Context.MODE_PRIVATE);

        boolean firstRun = sharedPreferences.getBoolean(PREFERENCE_FIRST_RUN,true);

        System.out.println(firstRun);

        if(firstRun){
            //insert all the company names
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean(PREFERENCE_FIRST_RUN, false);

            for(int i=0; i<companiesId.length; i++){
                editor.putString(companiesId[i], companiesName[i]);
                editor.putInt(companiesName[i], companiesIcon[i]);
            }
            editor.apply();
        }
    }

    //-----------------------------------------------------
    public void showInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_COMPANY_INFO, Context.MODE_PRIVATE);
        String name =sharedPreferences.getString(companiesId[0], "preference does not exist");
        int icon =sharedPreferences.getInt(companiesName[0], 0);

        System.out.println(name);
        System.out.println(String.valueOf(icon));

        Toast.makeText(this,name,Toast.LENGTH_LONG).show();

    }


}
