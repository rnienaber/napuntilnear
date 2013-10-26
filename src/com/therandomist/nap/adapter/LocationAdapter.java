package com.therandomist.nap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.therandomist.nap.R;
import com.therandomist.nap.model.LocationInfo;
import com.therandomist.nap.service.DistanceCalculationService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationAdapter extends ArrayAdapter<LocationInfo> {

    List<LocationInfo> locationList;
    DistanceCalculationService distanceCalculationService;

    public LocationAdapter(Context context, int textViewResourceId, List<LocationInfo> locations, DistanceCalculationService distanceCalculationService){
		super(context, textViewResourceId, locations);
        this.locationList = locations;
        this.distanceCalculationService = distanceCalculationService;

        if(locationList == null){
            locationList = new ArrayList<LocationInfo>();
        }
	}

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.location_row, null);
		}
		LocationInfo location = locationList.get(position);
		if(location != null){
			layoutRow(view, location);
		}
		return view;
	}



    private void layoutRow(View view, LocationInfo location){
		TextView locationInfo = (TextView) view.findViewById(R.id.location_information);

		if(locationInfo != null){

            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            DecimalFormat df = new DecimalFormat("00.00000");
            DecimalFormat df2 = new DecimalFormat("0.00");

			locationInfo.setText("Time: "+sdf.format(now)+" at "
                    + df.format(location.getLatitude()) + " | "
                    + df.format(location.getLongitude())
                    + " -- "+ df2.format(distanceCalculationService.getDistanceToDestination(location.getLocation()))
                    + "km ("+location.getProvider()+")" );
		}
	}
}
