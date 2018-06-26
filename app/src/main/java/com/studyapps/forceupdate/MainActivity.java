package com.studyapps.forceupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.samagyani.forceupdate.ForceUpdate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ForceUpdate forceUpdate = new ForceUpdate(MainActivity.this);
        forceUpdate.setCanceledOnTouchOutside(false);
        forceUpdate.setCancelable(false);
        forceUpdate.setTitle("New Update Available");
        forceUpdate.setMessage("Download this Update for New Features");
        forceUpdate.build();
    }
}
