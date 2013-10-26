package com.therandomist.nap.service;

import android.content.Context;
import com.therandomist.nap.model.Destination;
import com.therandomist.nap.service.database.DestinationDBAdapter;

import java.util.List;

public class DestinationService {

    private DestinationDBAdapter database;

    public DestinationService(Context context) {
        database = new DestinationDBAdapter(context);
    }

    public Destination getDestination(Long destinationId){
        database.open();
        Destination destination = database.getDestination(destinationId);
        database.close();
        return destination;
    }

    public List<Destination> getAllDestinations(){
        database.open();
        List<Destination> result = database.getAllDestinations();
        database.close();
        return result;
    }

    public void addDestination(Destination destination){
        if(destination != null){
            database.open();
            long id = database.addDestination(destination);
            database.close();
            destination.setId(id);
        }
    }
}
