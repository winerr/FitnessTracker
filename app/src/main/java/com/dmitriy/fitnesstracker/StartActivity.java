package com.dmitriy.fitnesstracker;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dmitry on 09.10.2015.
 */


class data{

    private int count;
    private double prevA;



    private final float g = 9.9F;

    data (){
        count = 0;
        prevA = 0;
    }

    void pris(double a){

        if (a>4){
            if(prevA>a){
                count++;
                prevA=0;
            }else{
                prevA = a;
            }
        }
    }


    public void setCount(int i){
        count = i;
    }
    public void setPrevA(int i) { prevA = i; }

    public int getCount() {
        return count;
    }

    public double getTime(int index) {
        return (index*0.1);
    }

    public double getEnergi(){
        double mass = (67/3)*2;
        double s = 0.5;
        return mass * g * s * count;
    }
}

public class StartActivity extends AppCompatActivity implements SensorEventListener {
    private TextView header;
    private Button start;
    private Button stop;
    private data myWork;
    private Timer t;
    private Toolbar toolbar;

    private TextView pris;
    private TextView time;
    private TextView energi;
    private TextView lb1;
    private TextView lb2;
    private TextView lb3;

    private boolean j, lb;

    private SensorManager mSensorManager;

    private float x,y,z;
    private SensorEvent s;
    private double acceleration;
    private int index, priss;
    private final float g = 9.9F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_1);
        header = (TextView) findViewById(R.id.textView);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);

        time = (TextView) findViewById(R.id.lb2);
        pris = (TextView) findViewById(R.id.lb1);
        energi = (TextView) findViewById(R.id.lb3);
        lb1 = (TextView) findViewById(R.id.time);
        lb2 = (TextView) findViewById(R.id.pris);
        lb3 = (TextView) findViewById(R.id.energi);

        j = true;

        time.setVisibility(View.INVISIBLE);
        pris.setVisibility(View.INVISIBLE);
        energi.setVisibility(View.INVISIBLE);
        lb1.setVisibility(View.INVISIBLE);
        lb2.setVisibility(View.INVISIBLE);
        lb3.setVisibility(View.INVISIBLE);

        myWork = new data();
        setToolbar();
    }

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSettings();
                return true;
            }

        });

        toolbar.inflateMenu(R.menu.menu);
    }

    public void fabClick(View v){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    void showSettings(){
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
    }

    public void start(View v){
        header.setText("Для завершення врави натисніть на кнопку");
        stop.setVisibility(View.VISIBLE);
        start.setVisibility(View.INVISIBLE);

        lb = false;
        index = 0;

        priss = 0;
        myWork.setCount(0);
        myWork.setPrevA(0);

        time.setVisibility(View.INVISIBLE);
        pris.setVisibility(View.INVISIBLE);
        energi.setVisibility(View.INVISIBLE);
        lb1.setVisibility(View.INVISIBLE);
        lb2.setVisibility(View.INVISIBLE);
        lb3.setVisibility(View.INVISIBLE);


        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if( myWork.getCount() > priss){
                    priss = myWork.getCount();
                    lb = true;
                }else{
                    if(lb){
                        lb = false;
                    }else {
                        SensorData();
                    }
                }
                index++;
            }
        }, 0, 100);
    }

    public void stop(View v){
        t.cancel();
        header.setText("Для початку виконання вправи натисніть кнопку");
        start.setVisibility(View.VISIBLE);
        stop.setVisibility(View.INVISIBLE);

        time.setVisibility(View.VISIBLE);
        pris.setVisibility(View.VISIBLE);
        energi.setVisibility(View.VISIBLE);
        lb1.setVisibility(View.VISIBLE);
        lb2.setVisibility(View.VISIBLE);
        lb3.setVisibility(View.VISIBLE);


        time.setText(Double.toString(myWork.getTime(index)));
        pris.setText(Integer.toString(myWork.getCount()));
        energi.setText(Double.toString(myWork.getEnergi()));


    }

    public void SensorData(){
        x = s.values[SensorManager.DATA_X];
        y = s.values[SensorManager.DATA_Y];
        z = s.values[SensorManager.DATA_Z];

        acceleration = Math.sqrt((x * x) + (y * y)  + (z * z)) - g;

        myWork.pris(acceleration);

    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        if (j){
            s = se;
            j = false;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
    }

}
