package com.therandomist.nap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.therandomist.nap.model.Destination;
import com.therandomist.nap.overlay.CrossHairOverlay;
import com.therandomist.nap.service.DestinationService;

import java.util.List;


public class DestinationMapActivity extends MapActivity {

    DestinationService destinationService ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destinationService = new DestinationService(getApplicationContext());
        setContentView(R.layout.destination_map);

        final MapView mapView = (MapView) findViewById(R.id.mapview);
        List<Overlay> overlays = mapView.getOverlays();
        overlays.add(new CrossHairOverlay());

        final EditText nameField = (EditText) findViewById(R.id.name_textfield);

        final Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GeoPoint mapCenter = mapView.getMapCenter();

                float centerLatitude = (float) (mapCenter.getLatitudeE6()) / 1000000;
                float centerLongitude = (float) (mapCenter.getLongitudeE6()) / 1000000;

                saveDestination(nameField.getText().toString(),  centerLatitude,  centerLongitude);

                Toast.makeText(getApplicationContext(),
                        "Saving: "+ nameField.getText().toString(),
                        Toast.LENGTH_SHORT).show();

                Intent i = new Intent(mapView.getContext(), StartActivity.class);
				startActivity(i);
            }

        });

    }

    public void saveDestination(String name, float latitude, float longitude){
        Destination destination = new Destination(null, name, (double)latitude, (double)longitude, false);
        destinationService.addDestination(destination);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }


}