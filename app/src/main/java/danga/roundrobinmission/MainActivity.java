package danga.roundrobinmission;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private UpdateSpotAsyncTask updateRating;
    private int[] companiesId = {105,11,10,162,44};
    private final String TAG = "MainActivity";

    private final String PREFERENCE_ASSENT_INFO    =   "preference asset info";
    //first run flag
    private final String PREFERENCE_FIRST_RUN       =   "preference first run";


    //-----------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //insert the asset name and icon to shared preference data base.
        savePreference();


        ListView listView = (ListView)this.findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this,PREFERENCE_ASSENT_INFO,companiesId);
        listView.setAdapter(adapter);

        updateRating = new UpdateSpotAsyncTask(adapter);
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

        //basic info that need to get into the data base
        final int[] companiesIcon = {R.drawable.facebook_icon,R.drawable.apple_icon,R.drawable.google_icon,R.drawable.alibaba_icon,R.drawable.twitter_icon};
        final String[] companiesName = {"FACEBOOK","APPLE","GOOGLE","ALIBABA","TWITTER"};

        //get the sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_ASSENT_INFO, Context.MODE_PRIVATE);
        boolean firstRun = sharedPreferences.getBoolean(PREFERENCE_FIRST_RUN, true);

        Log.i(TAG,"first run: " + firstRun);
        //only in the first run we update the shared preference
        if(firstRun){
            //insert all the company names
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //update the first run flag
            editor.putBoolean(PREFERENCE_FIRST_RUN, false);
            //inert the data to the shared preference
            for(int i=0; i<companiesId.length; i++){
                editor.putString(String.valueOf(companiesId[i]), companiesName[i]);
                editor.putInt(companiesName[i], companiesIcon[i]);
            }
            editor.apply();
        }// close if
    }

    //-----------------------------------------------------
    @Override
    protected void onStop() {
        super.onStop();
        //stop the background thread
        updateRating.cancel(true);
    }
}
