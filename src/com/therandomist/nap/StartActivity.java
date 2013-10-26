package com.therandomist.nap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.therandomist.nap.adapter.DestinationAdapter;
import com.therandomist.nap.model.Destination;
import com.therandomist.nap.service.DestinationService;
import com.therandomist.nap.util.FontSetter;

import java.util.List;

public class StartActivity extends ListActivity {

    private static StartActivity instance;
    private DestinationService destinationService;
    private DestinationAdapter destinationAdapter;

    public static StartActivity instance(){
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
    protected void onResume() {
        super.onResume();

        List<Destination> destinations = destinationService.getAllDestinations();
        for(Destination destination : destinations){
            destination.setSelected(false);
        }

        destinationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        destinationService = new DestinationService(getApplicationContext());
        destinationAdapter = new DestinationAdapter(this, R.layout.destination_list_row, destinationService.getAllDestinations(), getAssets());
        setListAdapter(destinationAdapter);

        setContentView(R.layout.start);
        setupFonts();

        Button napButton = (Button) findViewById(R.id.nap_button);
		napButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {

                Destination selectedDestination = destinationAdapter.getSelectedDestination();
                if(selectedDestination != null){
                    Intent i = new Intent(view.getContext(), NapActivity.class);
                    i.putExtra("destination_id", selectedDestination.getId());
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Choose a destination", Toast.LENGTH_SHORT).show();
                }
			}
		});

        Button addButton = (Button) findViewById(R.id.add_destination_button);
		addButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				Intent i = new Intent(view.getContext(), DestinationMapActivity.class);
				startActivity(i);
			}
		});
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        List<Destination> destinations = destinationAdapter.getDestinationList();

        for(int i=0; i< destinations.size(); i++){
            Destination destination = destinations.get(i);
            if(i == position){
                destination.setSelected(!destination.isSelected());
                Toast.makeText(getApplicationContext(), "Selected: "+destination.getName(), Toast.LENGTH_SHORT).show();
            }else{
                destination.setSelected(false);
            }
        }

        destinationAdapter.notifyDataSetChanged();
    }

    private void setupFonts(){
        FontSetter.setFont((TextView) findViewById(R.id.header), getAssets());
        FontSetter.setFont((TextView) findViewById(R.id.instruction), getAssets());

        FontSetter.setFont((Button) findViewById(R.id.nap_button), getAssets());
        FontSetter.setFont((Button) findViewById(R.id.add_destination_button), getAssets());
    }
}
