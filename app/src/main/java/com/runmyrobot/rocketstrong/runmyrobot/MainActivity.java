package com.runmyrobot.rocketstrong.runmyrobot;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;


public class MainActivity extends AppCompatActivity {
    public static String robotid = "48853711";
    public static String sesionId = "";
    public static String currentrobotName = "";
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://runmyrobot.com:8000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        WebView Videoview = (WebView) findViewById(R.id.Video);
        WebSettings webSettings = Videoview.getSettings();
        Videoview.setWebChromeClient(new WebChromeClient());
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        //webSettings.setMixedContentMode(webSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        try {
            JSONObject FirstParams = readJsonFromUrl("https://runmyrobot.com/internal/");
            JSONObject firstrobot = FirstParams.getJSONObject("robot");
            sesionId = firstrobot.getString("_id");
            robotid = firstrobot.getString("robot_id");
            currentrobotName = firstrobot.getString("robot_name");
        } catch (Exception e) {
            return;
        }

        Videoview.loadUrl("http://runmyrobot.com/fullview/" + robotid);

        mSocket.connect();
        Button changeRobot = (Button) findViewById(R.id.changeRobot);
        changeRobot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    robot_menu();
                } catch (Exception e) {
                    TextView errorview = (TextView) findViewById(R.id.robotName);
                    errorview.setText(e.toString());
                    return;
                }
            }
        });
    controlRobot(robotid,currentrobotName,sesionId);
    }

    public void controlRobot(final String contolrobotid, final String Name, final String sesid) {
        TextView TVName = (TextView) findViewById(R.id.robotName);
        WebView Videoview = (WebView) findViewById(R.id.Video);
        robotid = contolrobotid;
        TVName.setText(Name);
        Videoview.loadUrl("http://runmyrobot.com/fullview/" +  contolrobotid);
        //move forward
        Button forwardbtn = (Button) findViewById(R.id.btnForward);
        forwardbtn.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 200);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject forward = new JSONObject();
                        forward.put("_id", sesid);
                        forward.put("robot_id", contolrobotid);
                        forward.put("robot_name", Name);
                        forward.put("command", "F");
                        forward.put("user", "wipApp");
                        mSocket.emit("command_to_robot", forward);
                    } catch (JSONException e) {
                        return;
                    }
                    mHandler.postDelayed(this, 200);
                }

            };
        });

        //move backward
        Button backbtn = (Button) findViewById(R.id.btnback);
        backbtn.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 200);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject back = new JSONObject();
                        back.put("_id", sesid);
                        back.put("robot_id", contolrobotid);
                        back.put("robot_name", Name);
                        back.put("command", "B");
                        back.put("user", "wipApp");
                        mSocket.emit("command_to_robot", back);
                    } catch (JSONException e) {
                        return;
                    }
                    mHandler.postDelayed(this, 200);
                }

            };
        });

        //move left
        Button leftbtn = (Button) findViewById(R.id.btnLeft);
        leftbtn.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 200);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject left = new JSONObject();
                        left.put("_id", sesid);
                        left.put("robot_id", contolrobotid);
                        left.put("robot_name", Name);
                        left.put("command", "L");
                        left.put("user", "wipApp");
                        mSocket.emit("command_to_robot", left);
                    } catch (JSONException e) {
                        return;
                    }
                    mHandler.postDelayed(this, 200);
                }

            };
        });

        //move right
        Button rightbtn = (Button) findViewById(R.id.btnRight);
        rightbtn.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 200);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject right = new JSONObject();
                        right.put("_id", sesid);
                        right.put("robot_id", contolrobotid);
                        right.put("robot_name", Name);
                        right.put("command", "R");
                        right.put("user", "wipApp");
                        mSocket.emit("command_to_robot", right);
                    } catch (JSONException e) {
                        return;
                    }
                    mHandler.postDelayed(this, 200);
                }

            };
        });

    }

    @Override
    public void onResume() {
        WebView Videoview = (WebView) findViewById(R.id.Video);
        Videoview.loadUrl("http://runmyrobot.com/fullview/" + robotid);
        super.onResume();

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
        for (int i = 0; i < array.length(); ++i) {
            JSONObject arrayitem = array.getJSONObject(i);
            String got_id = arrayitem.getString("id");
            String se_name = arrayitem.getString("name");
            if (se_name.equals(name)) {
                return got_id;
            }

        }
        return "error";
    }
    public void  Backtonormal() {
        WebView Videoview = (WebView) findViewById(R.id.Video);
        Button rightbtn = (Button) findViewById(R.id.btnRight);
        Button leftbtn = (Button) findViewById(R.id.btnLeft);
        Button backbtn = (Button) findViewById(R.id.btnback);
        Button forwardbtn = (Button) findViewById(R.id.btnForward);
        Button changeRobot = (Button) findViewById(R.id.changeRobot);
        TextView Name = (TextView) findViewById(R.id.robotName);

        Videoview.setVisibility(View.VISIBLE);
        rightbtn.setVisibility(View.VISIBLE);
        leftbtn.setVisibility(View.VISIBLE);
        backbtn.setVisibility(View.VISIBLE);
        forwardbtn.setVisibility(View.VISIBLE);
        changeRobot.setVisibility(View.VISIBLE);
        Name.setVisibility(View.VISIBLE);

        Spinner dropdown = (Spinner) findViewById(R.id.robot_spinner);
        ImageView robotImage = (ImageView) findViewById(R.id.robotImage);
        TextView description = (TextView) findViewById(R.id.description);
        Button setupRobot = (Button) findViewById(R.id.setupRobot);

        dropdown.setVisibility(View.INVISIBLE);
        robotImage.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        setupRobot.setVisibility(View.INVISIBLE);
    }

    public void robot_menu() throws IOException, JSONException {

        final WebView Videoview = (WebView) findViewById(R.id.Video);
        Button rightbtn = (Button) findViewById(R.id.btnRight);
        Button leftbtn = (Button) findViewById(R.id.btnLeft);
        Button backbtn = (Button) findViewById(R.id.btnback);
        Button forwardbtn = (Button) findViewById(R.id.btnForward);
        Button changeRobot = (Button) findViewById(R.id.changeRobot);
        TextView Name = (TextView) findViewById(R.id.robotName);

        Videoview.setVisibility(View.INVISIBLE);
        rightbtn.setVisibility(View.INVISIBLE);
        leftbtn.setVisibility(View.INVISIBLE);
        backbtn.setVisibility(View.INVISIBLE);
        forwardbtn.setVisibility(View.INVISIBLE);
        changeRobot.setVisibility(View.INVISIBLE);
        Name.setVisibility(View.INVISIBLE);

        final Spinner dropdown = (Spinner) findViewById(R.id.robot_spinner);
        ImageView robotImage = (ImageView) findViewById(R.id.robotImage);
        TextView description = (TextView) findViewById(R.id.description);
        Button setupRobot = (Button) findViewById(R.id.setupRobot);

        dropdown.setVisibility(View.VISIBLE);
        robotImage.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        setupRobot.setVisibility(View.VISIBLE);

        //setContentView(R.layout.select_robot);
        JSONObject json = readJsonFromUrl("https://runmyrobot.com/internal/robot/" + robotid);
        JSONObject selectedrobot = json.getJSONObject("robot");
        sesionId = selectedrobot.getString("_id");
        final JSONArray robots = json.getJSONArray("robots");
        List<String> items = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);

        for (int i = 0; i < robots.length(); ++i) {
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
                    currentrobotName = spinnerValue;
                    controlRobot(selectedID, spinnerValue, sesionId);
                    new setImage().execute("http://runmyrobot.com/images/thumbnails/" + selectedID + ".jpg");
                } catch (Exception e) {
                    return;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        }); // (optional)
        setupRobot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    //setContentView(R.layout.activity_main);
                    //WebView Videoview = (WebView) findViewById(R.id.Video);
                    //Videoview.loadUrl("http://runmyrobot.com/fullview/" + robotid);
                    Backtonormal();
                } catch (Exception e) {
                    return;
                }
            }
        });
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
}