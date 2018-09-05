package com.rovin.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultTextView;

    public void findWeather(View view) {

        Log.i("cityName", cityName.getText().toString());

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

        try {
            String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");

            DownloadTask task = new DownloadTask();
            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName +"&units=metric&appid=fc719653e63e170f59cfb56fb61ce35a");


        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();

        }

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                /*
                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }
                */
                BufferedReader r = new BufferedReader(reader);
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line).append('\n');
                }
                result = total.toString();

                return result;

            } catch (Exception e) {

                e.printStackTrace();

                Looper.prepare();

                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();

                Looper.loop();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                String message = "";

                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather content", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = "";
                    String description = "";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if (main != "" && description != "") {

                        message += main + ": " + description + "\n";

                    }

                }

                String main = jsonObject.getString("main");
                Log.i("main", main);
                JSONObject mainObj = new JSONObject(main);
                String currTemp = mainObj.getString("temp");
                if (currTemp != null) {
                    message += "Current Temp: " + currTemp + "\n";
                }
                String minTemp = mainObj.getString("temp_min");
                if (minTemp != null) {
                    message += "Min Temp: " + minTemp + "\n";
                }
                String maxTemp = mainObj.getString("temp_max");
                if (maxTemp != null) {
                    message += "Max Temp: " + maxTemp + "\n";
                }

                String sys = jsonObject.getString("sys");
                Log.i("sys", sys);
                JSONObject sysObj = new JSONObject(sys);
                String sunriseTime = sysObj.getString("sunrise");
                if (sunriseTime != null) {
                    Date currentDate = new Date (Long.parseLong(sunriseTime) * 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                    String date = dateFormat.format(currentDate);
                    message += "Sunrise time: " + date + "\n";
                }

                String sunsetTime = sysObj.getString("sunset");
                if (sunsetTime != null) {
                    Date currentDate = new Date (Long.parseLong(sunsetTime) * 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                    String date = dateFormat.format(currentDate);
                    message += "Sunset time: " + date + "\n";
                }

                if (message != "") {

                    resultTextView.setText(message);

                } else {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }


            } catch (JSONException e) {

                e.printStackTrace();

                Looper.prepare();
                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText) findViewById(R.id.cityName);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
    }
}
