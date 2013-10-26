package com.therandomist.nap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.therandomist.nap.adapter.LocationAdapter;
import com.therandomist.nap.model.Destination;
import com.therandomist.nap.model.LocationInfo;
import com.therandomist.nap.service.DestinationService;
import com.therandomist.nap.service.LocationChangeListener;
import com.therandomist.nap.service.DistanceCalculationService;
import com.therandomist.nap.service.WakeUpService;
import com.therandomist.nap.util.FontSetter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class NapActivity extends ListActivity implements Observer {

    private static NapActivity instance;
    private WakeUpService wakeUpService;
    private DistanceCalculationService distanceCalculationService;
    LocationManager locationManager;
    LocationListener gpsProviderListener;
    LocationListener networkProviderListener;

    private List<LocationInfo> locations;
    private LocationAdapter adapter;

    public static NapActivity instance(){
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

        Log.i("NAP_UNTIL_NEAR", "onCreate started for NapActivity");

        setContentView(R.layout.nap);
        setupFonts();
        setupDestinationServices();

        final Button cancelButton = (Button) findViewById(R.id.cancel_button);
		final Button awakeButton = (Button) findViewById(R.id.awake_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cancelling...", Toast.LENGTH_SHORT).show();
                goToHomeScreen();
            }
        });

        awakeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wakeUpService.shush();
                Toast.makeText(getApplicationContext(), "Good you're awake!", Toast.LENGTH_SHORT).show();
                goToHomeScreen();
            }
        });

        Log.i("NAP_UNTIL_NEAR", "onCreate finished");
    }

    private void setupFonts(){
        FontSetter.setFont((TextView) findViewById(R.id.header), getAssets());
        FontSetter.setFont((TextView) findViewById(R.id.location), getAssets());
        FontSetter.setFont((TextView) findViewById(R.id.distance), getAssets());

        FontSetter.setFont((Button) findViewById(R.id.awake_button), getAssets());
        FontSetter.setFont((Button) findViewById(R.id.cancel_button), getAssets());
    }

    public void update(Observable observable, Object o) {
        Log.i("RACHEL", "Update method called - observer was observed.");
        LocationChangeListener listener = (LocationChangeListener) observable;
        locationChanged(listener.getLocationProvider(), listener.getCurrentLocation());
    }

    public void locationChanged(String provider, Location location){

        locations.add(new LocationInfo(location, provider));
        adapter.notifyDataSetChanged();

        double distance = distanceCalculationService.getDistanceToDestination(location);
        DecimalFormat df = new DecimalFormat("0.00 km");

        Log.i("RACHEL", "Location changed: "+df.format(distance));

        TextView distanceView = (TextView) findViewById(R.id.distance);
        distanceView.setText(df.format(distance));

        if(distanceCalculationService.isCloseToDestination(location)){
            wakeUpService.wakeMeUp();
        }
    }

    public void goToHomeScreen(){
        locationManager.removeUpdates(gpsProviderListener);
        locationManager.removeUpdates(networkProviderListener);
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
    }

    public void setupDestinationServices(){
        Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		Long destinationId = (Long)bundle.get("destination_id");
        Destination currentDestination = new DestinationService(getApplicationContext()).getDestination(destinationId);

        Log.i("RACHEL", "Napping until: "+currentDestination.getName());
        Log.i("RACHEL", "Napping until: "+currentDestination.getLatitude());
        Log.i("RACHEL", "Napping until: "+currentDestination.getLongitude());

        distanceCalculationService = new DistanceCalculationService(currentDestination);

        wakeUpService = new WakeUpService(getApplicationContext(), (Vibrator)getSystemService(Context.VIBRATOR_SERVICE));

        TextView location = (TextView) findViewById(R.id.location);
        location.setText(currentDestination.getName());

        locationManager  = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        gpsProviderListener = new LocationChangeListener(LocationManager.GPS_PROVIDER, this);
        networkProviderListener = new LocationChangeListener(LocationManager.NETWORK_PROVIDER, this);

        locations = new ArrayList<LocationInfo>();
        adapter = new LocationAdapter(this, R.layout.location_row, locations, distanceCalculationService);
        setListAdapter(adapter);

        locations.clear();
        adapter.notifyDataSetChanged();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, gpsProviderListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 5, networkProviderListener);

        Log.i("RACHEL", "Location services are set up.");
    }
}


