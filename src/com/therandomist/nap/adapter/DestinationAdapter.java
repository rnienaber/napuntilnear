package com.therandomist.nap.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.therandomist.nap.R;
import com.therandomist.nap.model.Destination;
import com.therandomist.nap.util.FontSetter;

import java.util.List;

public class DestinationAdapter extends ArrayAdapter<Destination> {

    List<Destination> destinationList;
    AssetManager assetManager;
    boolean showNameInBubble;

    public DestinationAdapter(Context context, int textViewResourceId, List<Destination> destinations, AssetManager assetManager){
		super(context, textViewResourceId, destinations);
        this.destinationList = destinations;
        this.assetManager = assetManager;
	}

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.destination_list_row, null);
		}
		Destination destination = destinationList.get(position);
		if(destination != null){
			layoutDestinationRow(view, destination);
		}
		return view;
	}



    private void layoutDestinationRow(View view, Destination destination){
		TextView name = (TextView) view.findViewById(R.id.destination_name);
        FontSetter.setFont(name, assetManager);
		if(name != null){
			name.setText(destination.getName());
		}

        if(destination.isSelected()){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.nearly_black));
        }else{
            view.setBackgroundColor(getContext().getResources().getColor(R.color.black));
        }
	}

    public Destination getSelectedDestination(){
        Destination result = null;

        for(Destination destination : destinationList){
            if(destination.isSelected()) result = destination;
        }

        return result;
    }

    public List<Destination> getDestinationList() {
        return destinationList;
    }
}