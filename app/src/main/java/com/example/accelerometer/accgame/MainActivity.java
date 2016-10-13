package com.example.accelerometer.accgame;

import android.app.Activity;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends Activity {

    private static final String TAG = "com.example.accelerometer.accgame.MainActivity";
    private PowerManager.WakeLock mWakeLock;
    private SimulationView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager mPowerManager = (PowerManager)getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        sv=new SimulationView(this);
        setContentView(sv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        sv.startSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        sv.stopSimulation();
    }
}
