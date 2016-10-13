package com.example.accelerometer.accgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Sayli on 10/9/2016.
 */
public class SimulationView extends View implements SensorEventListener {

    private SensorManager sensorManager;
    //Sensor
    Display mDisplay;

    private Bitmap mfield;
    private Bitmap mBasket;
    private Bitmap mBitmap;
    private static final int BALL_SIZE = 64;
    private static final int BASKET_SIZE = 80;

    private float x;
    private float y;
    private float z;
    private long time;

    private float mXOrigin;
    private float mYOrigin;
    private float mHorizontalBound = 0;
    private float mVerticalBound = 0;

    public SimulationView(Context context) {
        super(context);
        Bitmap ball = BitmapFactory.decodeResource(getResources(),R.drawable.ball);
        mBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        Bitmap basket = BitmapFactory.decodeResource(getResources(),R.drawable.basket);
        mBasket = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        mfield = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);

        WindowManager mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        sensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Bitmap ball = BitmapFactory.decodeResource(getResources(),R.drawable.ball);
        mBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        Bitmap basket = BitmapFactory.decodeResource(getResources(),R.drawable.basket);
        mBitmap = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        mfield = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);

        //Context context = null;
        WindowManager mWindowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        sensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            time = event.timestamp;

            Rotation(event);
        }
    }

    public void Rotation(SensorEvent event){
        if (Surface.ROTATION_0 == mDisplay.getRotation()){
            x = event.values[0];
            y = event.values[1];
        }
        else if (Surface.ROTATION_90 == mDisplay.getRotation()){
            x = -event.values[1];
            y = event.values[0];
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void startSimulation(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_GAME);
    }

    public void stopSimulation(){
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mfield, 0, 0, null);
        canvas.drawBitmap(mBasket, mXOrigin - BASKET_SIZE/2, mYOrigin - BASKET_SIZE/2, null);

        Particle mBall = new Particle();
        mBall.updatePosition(x, y, z, time);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);

        canvas.drawBitmap(mBitmap, (mXOrigin - BALL_SIZE/2) + mBall.mPosX, (mYOrigin - BALL_SIZE/2) + mBall.mPosY, null );
        invalidate();
    }
}
