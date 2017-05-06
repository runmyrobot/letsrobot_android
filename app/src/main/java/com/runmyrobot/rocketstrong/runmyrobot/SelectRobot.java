/**package com.runmyrobot.rocketstrong.runmyrobot;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SelectRobot extends MainActivity
{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static String getIdWithName(String name, JSONArray array) throws IOException, JSONException {
        for(int i = 0; i < array.length(); ++i) {
            JSONObject arrayitem = array.getJSONObject(i);
            String got_id = arrayitem.getString("id");
            String se_name = arrayitem.getString("name");
            if (se_name.equals(name)) {
                return got_id;
            }

        }
        return "error";
    }
    public void robot_menu() throws IOException, JSONException {
        setContentView(R.layout.select_robot);
        JSONObject json = readJsonFromUrl("https://runmyrobot.com/internal");
        final JSONArray robots = json.getJSONArray("robots");
        final Spinner dropdown = (Spinner)findViewById(R.id.robot_spinner);
        List<String> items = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);

        for(int i = 0; i < robots.length(); ++i) {
            JSONObject robot = robots.getJSONObject(i);
            String id = robot.getString("id");
            String name = robot.getString("name");
            String status = robot.getString("status");
            //boolean charging  = robot.getBoolean("charging");
            if (status.equals("online")) {
                items.add(name);
                dropdown.setAdapter(adapter);
            }
            //LoadImageFromWebOperations("http://runmyrobot.com/images/thumbnails/" + id + ".jpg");

        }

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String spinnerValue = dropdown.getSelectedItem().toString();
                try {
                    String selectedID = getIdWithName(spinnerValue, robots);
                    MainActivity.robotid = selectedID;
                    new setImage().execute("http://runmyrobot.com/images/thumbnails/" + selectedID + ".jpg");
                    WebView Videoview = (WebView) findViewById(R.id.Video);
                    Videoview.loadUrl("http://runmyrobot.com/fullview/" + MainActivity.robotid);
                } catch (Exception e) {
                    return;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        }); // (optional)
    }


public class setImage extends AsyncTask<String, Void, Drawable> {

    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Drawable doInBackground(String... Strings) {

        try {
            InputStream is = (InputStream) new URL(Strings[0]).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable result) {
        super.onPostExecute(result);
        ImageView robotImage = (ImageView) findViewById(R.id.robotImage);
        robotImage.setImageDrawable(result);
    }
}
}**/