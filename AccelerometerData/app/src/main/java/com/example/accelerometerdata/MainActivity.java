package com.example.accelerometerdata;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accelerometerdata.Data_DB;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;



public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private SensorManager SensorManager;
    private Sensor senseAccelerometer;
    private  Sensor sensors;

    private LineChart gxChart,gyChart,gzChart;
    private Thread thread;
    private boolean plotData_chart = true;

    FirebaseDatabase fireDb;
    DatabaseReference myRef;
    Integer n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SensorManager  = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        senseAccelerometer =  SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        List<Sensor> sensors = SensorManager.getSensorList(Sensor.TYPE_ALL);

        for(int i=0; i<sensors.size(); i++){
            Log.d(TAG, "onCreate: Sensor "+ i + ": " + sensors.get(i).toString());
        }
        if (senseAccelerometer != null) {
            SensorManager.registerListener(this,senseAccelerometer,1000000);
        }

        fireDb = FirebaseDatabase.getInstance();
        Log.d("MainActivity","fire database _data working !");
        myRef = fireDb.getReference("SensorAccelerometer");


        gxChart = (LineChart) findViewById(R.id.xvalue);
        gyChart = (LineChart) findViewById(R.id.yvalue);
        gzChart = (LineChart) findViewById(R.id.zvalue);

        // enable description text
        gxChart.getDescription().setEnabled(true);
        gyChart.getDescription().setEnabled(true);
        gzChart.getDescription().setEnabled(true);

        // enable touch gestures
        gxChart.setTouchEnabled(true);
        gyChart.setTouchEnabled(true);
        gzChart.setTouchEnabled(true);

        // enable scaling and dragging
        gxChart.setDragEnabled(true);
        gxChart.setScaleEnabled(true);
        gxChart.setDrawGridBackground(false);

        gyChart.setDragEnabled(true);
        gyChart.setScaleEnabled(true);
        gyChart.setDrawGridBackground(false);

        gzChart.setDragEnabled(true);
        gzChart.setScaleEnabled(true);
        gzChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        gxChart.setPinchZoom(true);
        gyChart.setPinchZoom(true);
        gzChart.setPinchZoom(true);

        // set an alternative background color
//        mChart.setBackgroundColor(Color.WHITE);
        int mybackColor = getResources().getColor(R.color.graphBackColour);
        gxChart.setBackgroundColor(mybackColor);
        gyChart.setBackgroundColor(mybackColor);
        gzChart.setBackgroundColor(mybackColor);

        LineData x_data = new LineData();
        LineData y_data = new LineData();
        LineData z_data = new LineData();

        x_data.setValueTextColor(mybackColor);
        y_data.setValueTextColor(mybackColor);
        z_data.setValueTextColor(mybackColor);

        // add empty data
        gxChart.setData(x_data);
        gyChart.setData(y_data);
        gzChart.setData(z_data);

        // get the legend (only possible after setting data)
        Legend lx = gxChart.getLegend();
        Legend ly = gyChart.getLegend();
        Legend lz = gyChart.getLegend();

        // modify the legend ...
        lx.setForm(Legend.LegendForm.LINE);
        lx.setTextColor(mybackColor);

        ly.setForm(Legend.LegendForm.LINE);
        ly.setTextColor(mybackColor);

        lz.setForm(Legend.LegendForm.LINE);
        lz.setTextColor(mybackColor);

        //------------------------------------

        XAxis xl = gxChart.getXAxis();
        xl.setTextColor(mybackColor);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        XAxis x2 = gyChart.getXAxis();
        x2.setTextColor(mybackColor);
        x2.setDrawGridLines(true);
        x2.setAvoidFirstLastClipping(true);
        x2.setEnabled(true);

        XAxis x3 = gzChart.getXAxis();
        x3.setTextColor(mybackColor);
        x3.setDrawGridLines(true);
        x3.setAvoidFirstLastClipping(true);
        x3.setEnabled(true);

        //-----------------------------------

        YAxis leftAxis = gxChart.getAxisLeft();
        leftAxis.setTextColor(mybackColor);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis leftAxis_1 = gyChart.getAxisLeft();
        leftAxis_1.setTextColor(mybackColor);
        leftAxis_1.setDrawGridLines(false);
        leftAxis_1.setAxisMaximum(10f);
        leftAxis_1.setAxisMinimum(0f);
        leftAxis_1.setDrawGridLines(true);

        YAxis leftAxis_2 = gzChart.getAxisLeft();
        leftAxis_2.setTextColor(mybackColor);
        leftAxis_2.setDrawGridLines(false);
        leftAxis_2.setAxisMaximum(10f);
        leftAxis_2.setAxisMinimum(0f);
        leftAxis_2.setDrawGridLines(true);

        //--------------------------------------------------

        YAxis rightAxis = gxChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis rightAxis_1 = gyChart.getAxisRight();
        rightAxis_1.setEnabled(false);

        YAxis rightAxis_2 = gzChart.getAxisRight();
        rightAxis_2.setEnabled(false);

        //---------------------------------

        gxChart.getAxisLeft().setDrawGridLines(false);
        gxChart.getXAxis().setDrawGridLines(false);
        gxChart.setDrawBorders(false);

        gyChart.getAxisLeft().setDrawGridLines(false);
        gyChart.getXAxis().setDrawGridLines(false);
        gyChart.setDrawBorders(false);

        gzChart.getAxisLeft().setDrawGridLines(false);
        gzChart.getXAxis().setDrawGridLines(false);
        gzChart.setDrawBorders(false);

        feedMultiple();

    }

    private void addEntry(SensorEvent event) {
        LineData gxdata = gxChart.getData();
        LineData gydata = gyChart.getData();
        LineData gzdata = gzChart.getData();
        n = n + 1;

        Log.d("msg","child is given value !!");
        Date d=new Date(new Date().getTime());
        String s=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(d);

        Data_DB data = new Data_DB(event.values[0],event.values[1],event.values[2],s);

        if (gxdata != null) {

            ILineDataSet set1 = gxdata.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well
            if (set1 == null) {
                set1 = createSet();
                gxdata.addDataSet(set1);
            }
            // data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
            gxdata.addEntry(new Entry(set1.getEntryCount(), event.values[0] + 5), 0);
            Log.d("Gyrometer : ", " " + event.values[0] + " ");
            gxdata.notifyDataChanged();

            // let the chart know it's data has changed
            gxChart.notifyDataSetChanged();
            // limit the number of visible entries
            gxChart.setVisibleXRangeMaximum(150);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);
            // move to the latest entry
            gxChart.moveViewToX(gxdata.getEntryCount());
        }
        if (gydata != null) {

            ILineDataSet set2 = gydata.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well
            if (set2 == null) {
                set2 = createSet();
                gydata.addDataSet(set2);
            }
            // data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
            gydata.addEntry(new Entry(set2.getEntryCount(), event.values[1] + 5), 0);
            Log.d("Gyrometer : ", " " + event.values[1] + " ");
            gydata.notifyDataChanged();

            // let the chart know it's data has changed
            gyChart.notifyDataSetChanged();
            // limit the number of visible entries
            gyChart.setVisibleXRangeMaximum(150);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);
            // move to the latest entry
            gyChart.moveViewToX(gxdata.getEntryCount());
        }
        if (gzdata != null) {

            ILineDataSet set3 = gzdata.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well
            if (set3 == null) {
                set3 = createSet();
                gzdata.addDataSet(set3);
            }
            // data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
            gzdata.addEntry(new Entry(set3.getEntryCount(), event.values[2] + 5), 0);
            Log.d("Gyrometer : ", " " + event.values[2] + " ");
            gzdata.notifyDataChanged();

            // let the chart know it's data has changed
            gzChart.notifyDataSetChanged();
            // limit the number of visible entries
            gzChart.setVisibleXRangeMaximum(150);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);
            // move to the latest entry
            gzChart.moveViewToX(gxdata.getEntryCount());
        }
        Log.d("child: ","trying to set values");
//        myRef.child(k).setValue(data);
        myRef.child("sensor_values").child(String.valueOf(n)).setValue(data);

    }

    private LineDataSet createSet () {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.WHITE);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    private void feedMultiple () {

        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    plotData_chart = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (thread != null) {
            thread.interrupt();
        }
        SensorManager.unregisterListener(this);
    }
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if(plotData_chart){
            try {
                addEntry(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
            plotData_chart = false;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SensorManager.registerListener(this,senseAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onDestroy() {
        SensorManager.unregisterListener(MainActivity.this);
        thread.interrupt();
        super.onDestroy();
    }
}

