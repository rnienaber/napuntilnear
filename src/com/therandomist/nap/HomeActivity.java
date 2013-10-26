package com.therandomist.nap;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.therandomist.nap.adapter.LocationAdapter;
import com.therandomist.nap.model.LocationInfo;
import com.therandomist.nap.service.LocationChangeListener;
import com.therandomist.nap.service.DistanceCalculationService;
import com.therandomist.nap.service.WakeUpService;
import com.therandomist.nap.util.FontSetter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class HomeActivity extends Activity implements Observer{

    private static HomeActivity instance;
    private WakeUpService wakeUpService;

    List<LocationInfo> locations;
    LocationAdapter adapter;


    public static HomeActivity instance(){
        return instance;
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupFonts();

        wakeUpService = new WakeUpService(getApplicationContext(), (Vibrator)getSystemService(Context.VIBRATOR_SERVICE));

        locations = new ArrayList<LocationInfo>();
//        adapter = new LocationAdapter(this, R.layout.location_row, locations);

        ListView locationList = (ListView) findViewById(R.id.location_list);
        locationList.setAdapter(adapter);

        TextView location = (TextView) findViewById(R.id.location);
        location.setText("Wimbledon");

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final Button startButton = (Button) findViewById(R.id.start_button);
		final Button stopButton = (Button) findViewById(R.id.stop_button);
        final Button testButton = (Button) findViewById(R.id.test_button);

        final LocationListener gpsProviderListener = new LocationChangeListener(LocationManager.GPS_PROVIDER, this);
        final LocationListener networkProviderListener = new LocationChangeListener(LocationManager.NETWORK_PROVIDER, this);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Starting", Toast.LENGTH_SHORT).show();
                Log.i("WOKING", "start button clicked");
                locations.clear();
                adapter.notifyDataSetChanged();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, gpsProviderListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 5, networkProviderListener);
                startButton.setClickable(false);
                stopButton.setClickable(true);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
                locationManager.removeUpdates(gpsProviderListener);
                locationManager.removeUpdates(networkProviderListener);
                startButton.setClickable(true);
                wakeUpService.shush();
                testButton.setClickable(true);
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wakeUpService.wakeMeUp();
                stopButton.setClickable(true);
                testButton.setClickable(false);
            }
        });

        Log.i("WOKING", "onCreate finished");
    }



    private void setupFonts(){
        FontSetter.setFont((TextView) findViewById(R.id.header), getAssets());
        FontSetter.setFont((TextView) findViewById(R.id.location), getAssets());
        FontSetter.setFont((TextView) findViewById(R.id.distance), getAssets());

        FontSetter.setFont((Button) findViewById(R.id.start_button), getAssets());
        FontSetter.setFont((Button) findViewById(R.id.stop_button), getAssets());
    }

    public void update(Observable observable, Object o) {
        LocationChangeListener listener = (LocationChangeListener) observable;
        locationChanged(listener.getLocationProvider(), listener.getCurrentLocation());
    }

    public void locationChanged(String provider, Location location){
//        locations.add(new LocationInfo(location, provider));
//        adapter.notifyDataSetChanged();
//
//        double distance = DistanceCalculationService.getDistanceToDestination(location);
//        DecimalFormat df = new DecimalFormat("0.00 km");
//
//        TextView distanceView = (TextView) findViewById(R.id.distance);
//        distanceView.setText(df.format(distance));
//
//        if(DistanceCalculationService.isCloseToDestination(location)){
//            wakeUpService.wakeMeUp();
//        }
    }
}
