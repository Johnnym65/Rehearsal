package com.example.johnny.reh;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.media.Image;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.os.Environment;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.json.JSONArray;
import org.json.JSONException;


public class Home extends AppCompatActivity implements View.OnClickListener {

    private static String mFileName = "record1";
    private MediaRecorder mRecorder = null;
    private MediaPlayer   mPlayer = null;
    Button recordButton;
    Button playButton;
    ImageButton currentPlayingBubble;
    ScrollView scroll;
    RelativeLayout ll;
    LinearLayout linLay;
    RelativeLayout topLayer;
    String currentRecordingFileName;

    String SceneName = "TestScene";

    private List<ImageButton> buttons = new ArrayList<ImageButton>();
    private List<ImageButton> betweenButtons = new ArrayList<ImageButton>();

    boolean recording = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPlayer = new MediaPlayer();
        Intent intent = getIntent();
        Log.d("myTag", "HELLO");
        Log.d("NewIntentN", intent.getStringExtra("SceneName"));
        SceneName = intent.getStringExtra("SceneName");

        this.listRecs();
    }

    public Home() {
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Recs");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Recs/";
        //mFileName += "/audiorecordtest.3gp";

        Log.d("myTag", "This is my message");
        Log.d("myTag", mFileName);

        //listRecs();


    }

    public void onClickRecordBtn(View v)
    {
        Log.d("myTag", "Record");
        Toast.makeText(this, "Recording", Toast.LENGTH_LONG).show();
        //this.startRecording();

        recordButton = (Button) findViewById(R.id.recButton);

        if(!recording) {
            startRecording(false);
            recording = true;
        }
        else {
            stopRecording(-1);
            recording = false;
        }

    }

    public void onClickPlaySceneBtn(View v)
    {
        Intent intent = new Intent(this, PlayScene.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
       // String message = editText.getText().toString();
        intent.putExtra("SceneName", "TestScene-Recordings");
        startActivity(intent);

    }

    public void onClickPlayBtn(View v)
    {
        Toast.makeText(this, "Playing", Toast.LENGTH_LONG).show();
        //this.startRecording();

        playButton = (Button) findViewById(R.id.playButton);

        startPlaying("audiorecordtest.3gp", null);

    }


    private void startRecording(boolean newline) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        boolean exists = true;
        int index = 0;

        while(exists) {

            File extStore = Environment.getExternalStorageDirectory();
            File myFile = new File(extStore.getAbsolutePath() + "/Recs/" + SceneName+"-"+(buttons.size()+index));

            if (myFile.exists()) {
                exists = true;
                index++;
            }
            else {
                exists = false;
            }
        }
        currentRecordingFileName = Integer.toString(buttons.size() + index);
        mRecorder.setOutputFile(mFileName + SceneName+"-"+(buttons.size() + index));
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
        }


        mRecorder.start();

        if(!newline)
            recordButton.setText("Stop");

    }

    private void stopRecording(int newLineID) {

        final int _newLineID = newLineID;
        //ArrayList<String> places = new ArrayList<String>(Arrays.asList("Buenos Aires", "Córdoba", "La Plata"));
        //setStringArrayPref(this, "places", places);
        Log.d("LINE ID!", Integer.toString(newLineID));
        Log.d("CurrentRecFileName", SceneName+"-"+currentRecordingFileName);
        ArrayList<String> RecordingsList = getStringArrayPref(getApplicationContext(), "TestScene-Recordings");

        if(newLineID >=0) {
            RecordingsList.add(newLineID, SceneName+"-"+currentRecordingFileName);
        }
        else{
            RecordingsList.add(SceneName+"-"+currentRecordingFileName);
            recordButton.setText("RECORD");
        }

        setStringArrayPref(getApplicationContext(), "TestScene-Recordings", RecordingsList);


        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        if(newLineID >-1) {
            /*for(int i = 0; i < buttons.size(); i++){
                if(i >= newLineID) {
                    ImageButton btn = (ImageButton)buttons.get(i);
                    btn.setY(btn.getY()+450);
                }

            }*/

            final ImageButton myButton = new ImageButton(this);
            myButton.setImageResource(R.drawable.textbubble);
            myButton.setBackgroundColor(Color.TRANSPARENT);
            //myButton.setScaleX(0);
            //myButton.setScaleY(0);
            linLay.addView(myButton, (newLineID*2));

            ImageButton betweenButton = new ImageButton(this);
            betweenButton.setBackgroundColor(Color.TRANSPARENT);
            betweenButton.setImageResource(R.drawable.recordline);
            betweenButton.setX(-300.0f);
            linLay.addView(betweenButton, newLineID*2);


            //topLayer.addView(myButton);

            final float growTo = 1.2f;
            final long duration = 1200;

            ScaleAnimation grow = new ScaleAnimation(0, 1.2f, 0, 1.2f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            grow.setDuration(duration / 2);
            AnimationSet growAndShrink = new AnimationSet(true);
            growAndShrink.addAnimation(grow);
            myButton.startAnimation(growAndShrink);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    buttons.clear();
                    betweenButtons.clear();

                    //ll.removeAllViews();
                    //bewtweenLayour.removeAllViews();
                    //topLayer.removeView(myButton);
                    linLay.removeAllViews();
                    listRecs();

                    if (_newLineID == -1) {
                        scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }


                }
            }, 2000);


        }
        else {
            buttons.clear();
            betweenButtons.clear();

            //ll.removeAllViews();
            //bewtweenLayour.removeAllViews();
            linLay.removeAllViews();
            listRecs();

            if (_newLineID == -1) {
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(View.FOCUS_DOWN);

                        ScaleAnimation grow = new ScaleAnimation(0, 1.2f, 0, 1.2f,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                        grow.setDuration(1200 / 2);
                        AnimationSet growAndShrink = new AnimationSet(true);
                        growAndShrink.addAnimation(grow);
                        buttons.get(buttons.size() - 1).startAnimation(growAndShrink);
                    }
                });
            }
        }





    }

    private void startPlaying(String recordingName, View v) {
        if(currentPlayingBubble != null)
            currentPlayingBubble.setImageResource(R.drawable.textbubble);
        currentPlayingBubble = (ImageButton) v;
        currentPlayingBubble.setImageResource(R.drawable.textbubbleplay);

        Log.d("RecPlaying", recordingName);
        try {

            //if(!mPlayer.isPlaying()){
                mPlayer.reset();
                mPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Recs/" + SceneName+"-"+recordingName);
                mPlayer.prepare();
                mPlayer.start();
                mPlayer.setOnCompletionListener(completionListener);
            Log.d("SOMETHING PLAYING", "SOMETHING PLAYING");
            //}


        } catch (IOException e) {
        }

        //playButton.setText("Stop");
        //this.listRecs();
    }

    MediaPlayer.OnCompletionListener completionListener
            = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer arg0) {
            // TODO Auto-generated method stub
            // set TextView text here
            // show Toast here
            currentPlayingBubble.setImageResource(R.drawable.textbubble);
            Log.d("FINISHED", "FINISHED");
        }
    };

    public void listRecs(){

        ArrayList<String> RecordingsList = getStringArrayPref(getApplicationContext(), "TestScene-Recordings");

        String[] recordings = null;
        if(RecordingsList != null) {
            recordings = new String[RecordingsList.size()];
            recordings = RecordingsList.toArray(recordings);

            for(String s : recordings) {
                Log.d("Recordings111", s);
            }
        }



        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Recs";
        Log.d("PATHER", path);
        File f = new File(path);
        File file[] = f.listFiles();
        Log.d("check", "check1");
        ll = (RelativeLayout)findViewById(R.id.main_layout);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Log.d("check", "check2");
        linLay = (LinearLayout)findViewById(R.id.LinLay);
        topLayer = (RelativeLayout)findViewById(R.id.topLayout);
        //LinearLayout.LayoutParams params = layout.getLayoutParams();
        scroll = (ScrollView)findViewById(R.id.recordingsScroll);
        Log.d("check", "check3");
        /*for (int i=0; i < file.length; i++) {

            Button myButton = new Button(this);
            myButton.setText(file[i].getName());
            myButton.setId(i);
            myButton.setOnClickListener(this);
            myButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Button btn = (Button) v;
                    deleteRecording(btn.getText().toString());

                    ll.removeView(btn);

                    for (int i = 0; i < buttons.size(); i++) {
                        if (i > btn.getId()) {
                            buttons.get(i).animate().translationY(buttons.get(i).getY() - 250).setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator());
                        }
                    }
                    buttons.remove(btn);

                    for (int i = 0; i < buttons.size(); i++) {
                        buttons.get(i).setId(i);
                    }

                    return true;
                }
            });
            myButton.setY(i * 250);
            myButton.setWidth(200);
            buttons.add(myButton);

            //ll = (RelativeLayout)findViewById(R.id.main_layout);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton, lp);

        }*/

        for (int i=0; i < recordings.length; i++) {
            ImageButton myButton = new ImageButton(this);
            //myButton.setText(recordings[i]);
            Log.d("check", "check4");
            myButton.setId(Integer.parseInt(recordings[i].substring(recordings[i].lastIndexOf("-") + 1)));
            Log.d("check", "check4.1");
           // myButton.setImageResource(R.drawable.textbubble);
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[] {android.R.attr.state_pressed},
                    getResources().getDrawable(R.drawable.textbubbledown));
            Log.d("check", "check4.2");
            states.addState(new int[]{},
                    getResources().getDrawable(R.drawable.textbubble));
            Log.d("check", "check4.3");
            myButton.setImageDrawable(states);
            myButton.setBackgroundColor(Color.TRANSPARENT);
            //myButton.setScaleX(0.5f);
            Log.d("check", "check5");
            //myButton.setScaleY(0.5f);
            myButton.setY(0);
            myButton.setOnClickListener(this);
            myButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ImageButton btn = (ImageButton) v;
                    deleteRecording(Integer.toString(btn.getId()));
                    //ll.removeView(btn);
                    buttons.clear();
                    betweenButtons.clear();
//                    ll.removeAllViews();
//                    bewtweenLayour.removeAllViews();
                    linLay.removeAllViews();
                    listRecs();
                    return true;
                }
            });
            Log.d("check", "check6");

           // myButton.setY((125 + (i*250)));
            buttons.add(myButton);






            ImageButton betweenButton = new ImageButton(this);
            //betweenButton.setText("add new line");
            //betweenButton.setImageResource(R.drawable.recordline);
            betweenButton.setBackgroundColor(Color.TRANSPARENT);
            StateListDrawable recordStates = new StateListDrawable();
            recordStates.addState(new int[]{android.R.attr.state_pressed},
                    getResources().getDrawable(R.drawable.recordlinedown));
            recordStates.addState(new int[]{},
                    getResources().getDrawable(R.drawable.recordline));
            betweenButton.setImageDrawable(recordStates);
            betweenButton.setX(-300.0f);
            betweenButton.setId(Integer.parseInt(recordings[i].substring(recordings[i].lastIndexOf("-") + 1)));
            //betweenButton.setY(i * 250);
            betweenButtons.add(betweenButton);
            betweenButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ImageButton btn = (ImageButton) v;
                    int id = betweenButtons.indexOf(btn);
                    Log.d("NNewLineID", Integer.toString(id));

                    if (!recording) {
                        startRecording(true);
                        recording = true;
                        btn.setImageResource(R.drawable.recordlinestop);
                        //btn.setText("Recording");
                    } else {
                        stopRecording(id);
                        recording = false;
                        btn.setImageResource(R.drawable.recordline);
                        //btn.setText("New Line");
                    }

                }
            });
            Log.d("check", "check7");
            //bewtweenLayour = (RelativeLayout)findViewById(R.id.betweenButtonLayout);
            //RelativeLayout.LayoutParams betweenlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            linLay.addView(betweenButton);
            linLay.addView(myButton);
            //linLay.setScaleY(0.5f);
        }

        //((ViewGroup)ll.getParent()).removeView(ll);
        //scroll.addView(ll);


    }

    public String getNumberOfRecordings(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Recs";

        File f = new File(path);
        File file[] = f.listFiles();

        return Integer.toString(file.length);
    }

    public String deleteRecording(String recordingName){

        ArrayList<String> RecordingsList = getStringArrayPref(getApplicationContext(), "TestScene-Recordings");
        RecordingsList.remove(SceneName+"-"+recordingName);
        setStringArrayPref(getApplicationContext(), "TestScene-Recordings", RecordingsList);

        String realName = SceneName+"-"+recordingName;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Recs";
        File f = new File(path);
        File file[] = f.listFiles();
        //RelativeLayout ll = (RelativeLayout)findViewById(R.id.main_layout);
        for (int i=0; i < file.length; i++) {
            //Log.d("HOWIYA", recordingName);
            //Log.d("HOWIYA", file[i].getName());
            if(realName.equals(file[i].getName())) {
                //Log.d("HOWIYA", "Deleted");
                file[i].delete();

            }
        }

        return "";
    }

    @Override
    public void onClick(View v) {
        ImageButton btn = (ImageButton) v;

        startPlaying(Integer.toString(btn.getId()), v);
        /*switch (v.getId()) {
            case 111:
                this.startPlaying();
                break;
            case 222:
                //do some thing here
                break;
        }*/
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
}
