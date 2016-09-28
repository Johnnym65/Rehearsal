package com.example.johnny.reh;

import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import android.widget.ImageButton;

public class PlayScene extends AppCompatActivity {

    private MediaPlayer   mPlayer = null;
    int index = 0;
    ImageButton imageButton;


            //bewtweenLayour = (RelativeLayout)findViewById(R.id.betweenButtonLayout);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_scene);

        imageButton = (ImageButton)findViewById(R.id.playLineButton);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void onStartClick(View v) {

        imageButton.setImageResource(R.drawable.nextline);
        ArrayList<String> RecordingsList = getStringArrayPref(getApplicationContext(), "TestScene-Recordings");

        String[] recordings = null;
        if(RecordingsList != null) {
            recordings = new String[RecordingsList.size()];
            recordings = RecordingsList.toArray(recordings);

            for(String s : recordings) {
                Log.d("Recordings111", s);
            }
        }
        Log.d("Recordings", "recorsings211");


        if(index<recordings.length){
            startPlaying(recordings[index]);
            index++;
        }

    }

    public static void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public static ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    private void startPlaying(String recordingName) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Recs/"+recordingName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
        }

        //playButton.setText("Stop");
        //this.listRecs();
    }


}
