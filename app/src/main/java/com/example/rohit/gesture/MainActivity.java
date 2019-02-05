package com.example.rohit.gesture;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private Sensor sensor;
    private Sensor gyrosensor;
    private SensorManager sensorManager;
    private TextView x,y,z,acc;
    private Switch acc2gy;
    private long currtime,acctotal,pretime=0;
    private ArrayList<Integer> colors;
    private int i=0,j=2;
    private double chopprevtime=0, zprevtime =0;
    private Boolean chopgesturetrigerd=false, zgesturetrigered =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        gyrosensor =sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,gyrosensor,SensorManager.SENSOR_DELAY_NORMAL);

        colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.WHITE);
        colors.add(Color.BLACK);



        x = findViewById(R.id.text1);
        y = findViewById(R.id.text2);
        z = findViewById(R.id.text3);
        acc=findViewById(R.id.acctot);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            double currrnttime = SystemClock.currentThreadTimeMillis();


                float ax = sensorEvent.values[0];
                float ay = sensorEvent.values[1];
                float az = sensorEvent.values[2];


                x.setText("x" + String.valueOf(ax));
                y.setText("y" + String.valueOf(ay));
                z.setText("z" + String.valueOf(az));


                double temp = ax + ay;
                acc.setText(String.valueOf(temp));
                Log.d("shakeEvent", "onSensorChanged: "+az);

            if ((chopgesturetrigerd | zgesturetrigered) == false) {


                if (temp < -25) {

                    Log.d("shakeEvent", "onSensorChanged: " + acctotal);
                    this.getWindow().getDecorView().setBackgroundColor(colors.get(i));

                    i++;
                    if (i > 1)
                        i = 0;

                    chopgesturetrigerd=true;
                    chopprevtime=currrnttime;
                }

                if((Math.sqrt(az*az))>20){

                    Log.d("z", "onSensorChanged: " + (Math.sqrt(az*az)));
                    getWindow().getDecorView().setBackgroundColor(colors.get(j));

                    j++;
                    if (j > 3)
                        j = 2;

                    zgesturetrigered =true;
                    zprevtime =currrnttime;
                }

            }

            if(chopgesturetrigerd==true){
                double choptimeint = currrnttime-chopprevtime;
                if(choptimeint>100)
                    chopgesturetrigerd=false;
            }

            if(zgesturetrigered ==true){
                double rolltimeint = currrnttime- zprevtime;
                if(rolltimeint>100)
                    zgesturetrigered =false;
            }


        }//end of liner acc


        if(sensorEvent.sensor.getType()==Sensor.TYPE_GYROSCOPE){


            double currrnttime = SystemClock.currentThreadTimeMillis();


                float ax = sensorEvent.values[0];
                float ay = sensorEvent.values[1];
                float az = sensorEvent.values[2];

                double temp = Math.sqrt(ay*ay);

            Log.d("gy", "onSensorChanged: "+ax+"    "+ay+"    "+az);
                    if (temp > 15) {


                        Log.d("gyroscope", "onSensorChanged: " + acctotal);
                        getWindow().getDecorView().setBackgroundColor(colors.get(j));

                        j++;
                        if (j > 3)
                            j = 2;

                    }

        }//end gyro


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
