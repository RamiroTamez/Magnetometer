package com.heartrate.radika.magnetometer;


import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mMagn, mAcce;
    private TextView text;

    @Override
    public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {

    }

    float imag;
    float azimut;
    float [] mGravity;
    float [] mGeomagnetic;


    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.texto);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagn = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAcce = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mGravity = null;
        mGeomagnetic = null;

    }

    @Override
    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAcce, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagn, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
        public void onSensorChanged(SensorEvent evento){

        switch (evento.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                mGravity = evento.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGeomagnetic = evento.values;
                break;
        }

        if ((mGravity !=null) && (mGeomagnetic !=null)){
            float   RotationMatrix[] = new float[16];
            boolean succes = SensorManager.getRotationMatrix(RotationMatrix, null, mGravity, mGeomagnetic);
                    if (succes) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(RotationMatrix, orientation);
                azimut = orientation[0] * (180 / (float) Math.PI);
            }
        }
        imag = azimut;
        text.setText("Angulo: " + Float.toString(imag) + "grados");
        float currentDegree = Float.parseFloat(null);
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                imag,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

                }

    @Override
    public void onAccuracyChanged(Sensor sensor, int exactitud) {


    }
}