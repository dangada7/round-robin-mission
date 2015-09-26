package danga.roundrobinmission;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dan on 9/18/2015.
 */
public class UpdateRatingAsyncTask extends AsyncTask<Void, int[], Void> {

    private int[] values;
    private CustomAdapter adapter;

    private final String USER_AGENT = "Mozilla/5.0";

    public UpdateRatingAsyncTask(CustomAdapter adapter) {
        this.adapter = adapter;
        values = new int[5];
    }

    //------------------------------------------------------
    @Override
    protected void onPreExecute() {

    }

    //------------------------------------------------------
    @Override
    protected void onProgressUpdate(int[]... updateValues) {
        adapter.updateRating(updateValues[0]);
        adapter.notifyDataSetChanged();
    }

    //------------------------------------------------------
    @Override
    protected void onPostExecute(Void aVoid) {

    }

    //------------------------------------------------------
    @Override
    protected Void doInBackground(Void... params) {

        //randomNumber();
        try {
            sendPost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //------------------------------------------------------
    private void randomNumber() {
        while (true) {
            for (int i = 0; i < values.length; i++)
                values[i] = getRandomNumber();
            publishProgress(values);
            Log.w("doInBackground", Arrays.toString(values));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.i("doInBackground", "thread interrupted");
                break;
            }
        }
    }

    //------------------------------------------------------
    private int getRandomNumber() {
        int min = 0;
        int max = 500;

        Random r = new Random();
        int ans = r.nextInt(max - min + 1) + min;

        return ans;
    }

    //------------------------------------------------------
    // HTTP GET request
    private void sendGet() throws Exception {

        String url = "http://www.google.com/search?q=mkyong";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }

    //------------------------------------------------------
    // HTTP POST request
    private void sendPost() throws Exception {

        //initialize url
        //String urlStr = "http://requestb.in/tgbuqutg";                            //just for debug
        String urlStr = "https://twentyfourwidgets.herokuapp.com/assets/multi";     //real url

        URL url = new URL(urlStr);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
       // con.setRequestProperty("User-Agent", USER_AGENT);
       // con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String body = "[105,11,10,162,44]";

        // Send post request
        con.setDoOutput(true);      //Set the DoOutput flag to true if you intend to use the URL connection for output
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();

        System.out.println("Sending 'POST' request to URL : " + urlStr);
        System.out.println("Post body : " + body);

        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println("response: " + response.toString());

    }


}
