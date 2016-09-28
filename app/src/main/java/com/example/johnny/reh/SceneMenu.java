package com.example.johnny.reh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class SceneMenu extends AppCompatActivity implements View.OnClickListener {

    LinearLayout sceneMenuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_menu);

        createSceneList();
    }

    public void createSceneList() {
        sceneMenuLayout = (LinearLayout)findViewById(R.id.sceneLinLay);


        ArrayList<String> sceneListArrayList = getStringArrayPref(getApplicationContext(), "SceneList");

        String[] sceneList = null;
        if(sceneListArrayList != null) {
            sceneList = new String[sceneListArrayList.size()];
            sceneList = sceneListArrayList.toArray(sceneList);

            for(String s : sceneList) {
                Log.d("Recordings111", s);
            }
        }

        for (int i = 0; i < sceneList.length; i++) {
            ImageButton myButton = new ImageButton(this);
            //myButton.setText(recordings[i]);
            myButton.setId(Integer.parseInt(sceneList[i]));
            // myButton.setImageResource(R.drawable.textbubble);
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{android.R.attr.state_pressed},
                    getResources().getDrawable(R.drawable.scenebutton));
            states.addState(new int[]{},
                    getResources().getDrawable(R.drawable.scenebutton));
            myButton.setImageDrawable(states);
            myButton.setBackgroundColor(Color.TRANSPARENT);
            //myButton.setScaleX(0.5f);
            //myButton.setScaleY(0.5f);
            //myButton.setY(0);
            myButton.setOnClickListener(this);
            myButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ImageButton btn = (ImageButton) v;
                    ArrayList<String> sceneList1 = getStringArrayPref(getApplicationContext(), "SceneList");
                    Log.d("sceneToRemove", Integer.toString(btn.getId()));
                    sceneList1.remove(Integer.toString(btn.getId()));
                    //sceneList1.remove("2");
                    setStringArrayPref(getApplicationContext(), "SceneList", sceneList1);
                    sceneMenuLayout.removeAllViews();
                    createSceneList();
                    return true;
                }
            });

            sceneMenuLayout.addView(myButton);

            TextView sceneName = new TextView(this);
            sceneName.setText("Scene "+sceneList[i]);
            sceneName.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            sceneName.setY(-240.0f);
            sceneName.setTextColor(Color.parseColor("#FFFFFF"));
            sceneName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    //sceneName.setId(5);
                    //valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

                    sceneMenuLayout.addView(sceneName);
        }



    }

    public void newScene(View v) {
        ImageButton btn = (ImageButton) v;
        ArrayList<String> sceneListArrayList = getStringArrayPref(getApplicationContext(), "SceneList");

        String[] sceneList = null;
        if(sceneListArrayList != null) {
            sceneList = new String[sceneListArrayList.size()];
            sceneList = sceneListArrayList.toArray(sceneList);

            for(String s : sceneList) {
                Log.d("Recordings111", s);
            }
        }

        int highestSceneName = 0;
        if(sceneList.length >0) {
            for(int i  = 0; i < sceneList.length; i++)
            {
                if(Integer.parseInt(sceneList[i]) > highestSceneName) {
                    highestSceneName = Integer.parseInt(sceneList[i]);
                }
            }
        }
        else{
            highestSceneName = 0;
        }



        String newSceneName = Integer.toString(highestSceneName+1);

        sceneListArrayList.add(newSceneName);

        setStringArrayPref(getApplicationContext(), "SceneList", sceneListArrayList);

        sceneMenuLayout.removeAllViews();
        createSceneList();

    }

    @Override
    public void onClick(View v) {
        ImageButton btn = (ImageButton) v;
        Log.d("btnID", Integer.toString(btn.getId()));


        Intent intent = new Intent(this, Home.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        // String message = editText.getText().toString();
        intent.putExtra("SceneName", Integer.toString(btn.getId()));
        startActivity(intent);
    }
    public void openScene(View v) {
        Intent intent = new Intent(this, Home.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        // String message = editText.getText().toString();
        //intent.putExtra("SceneName", "TestScene-Recordings");
        startActivity(intent);
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
