package danga.roundrobinmission;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dan on 9/18/2015.
 *
 */
public class UpdateSpotAsyncTask extends AsyncTask<Void, ArrayList<Asset>, Void> {

    private CustomAdapter adapter;
    private final String TAG = "AsyncTask";

    public UpdateSpotAsyncTask(CustomAdapter adapter) {
        this.adapter = adapter;
    }

    //------------------------------------------------------
    @Override
    protected void onProgressUpdate(ArrayList<Asset>... updateValues) {
        boolean needToNotify = adapter.updateRating(updateValues[0]);
        Log.i(TAG,"needToNotify:" + needToNotify);
        // only if some info change we update the viewList
        if (needToNotify)
            adapter.notifyDataSetChanged();
    }

    //------------------------------------------------------
    @Override
    protected Void doInBackground(Void... params) {

        while (true) {
            //  1)  send post              //////////////
            String response = null;
            try {
                response = sendPost();
            } catch (Exception e) {
                e.printStackTrace();
                //if we dont get a response we just keep trying
                continue;
            }

            //  2)  parse response         //////////////
            ArrayList<Asset> assetsList = parseResponse(response);

            //  3)  publish assetsList     //////////////
            publishProgress(assetsList);

            //  4)  go to sleep:           //////////////
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.i(TAG, "thread interrupted");
                break;
            }

        }// while(true)

        return null;
    }

    //------------------------------------------------------
    // send HTTP POST request
    private String sendPost() throws Exception {

        //URL url = new URL("http://requestb.in/rsqe4xrs");                                //just for debug
        URL url = new URL("https://twentyfourwidgets.herokuapp.com/assets/multi");         //real url
        HttpURLConnection con = (HttpURLConnection) url.openConnection();                  //create new HttpsURLConnection
        String response = "";

        try {

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            String body = "[105, 11, 10, 162, 44]";

            // Send post request with body
            con.setDoOutput(true);      //for sending body, default=false
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(body);
            out.flush();
            out.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response += line;
                }
            }else{
                Log.w(TAG,"responseCode not equal to 200");
            }
        }
        finally {
            con.disconnect();
        }

        return response;
    }

    //------------------------------------------------------
    private ArrayList<Asset> parseResponse(String response) {

        ArrayList<Asset> assetsList = new ArrayList<Asset>();

        String logString = "";
        try {
            JSONArray jsonarray = new JSONArray(response);

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Asset asset = new Asset(jsonobject.getInt("assetId"),jsonobject.getDouble("spot"));
                assetsList.add(asset);
                logString += asset.assetId + "," + asset.spot + "; ";
            }
            Log.i(TAG,"response: " + logString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return assetsList;
    }

}
