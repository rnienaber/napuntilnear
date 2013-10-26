package com.therandomist.nap.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.therandomist.nap.model.Destination;

import java.util.ArrayList;
import java.util.List;

public class DestinationDBAdapter {

    public static final String KEY_NAME = "name";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_MOST_RECENT = "most_recent";
	public static final String KEY_ROW_ID = "_id";

    public static final String DATABASE_TABLE = "destination";

    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
												+ " name text not null, "
                                                + " most_recent integer(1) not null, "
												+ " latitude real not null, "
												+ " longitude real not null); ";

    private DBAdapter.DatabaseHelper dbHelper;
	private SQLiteDatabase db;

    private final Context context;

	public DestinationDBAdapter(Context context) {
        this.context = context;
    }

    public DestinationDBAdapter open() throws SQLException {
        dbHelper = new DBAdapter.DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public Destination getDestination(Long destinationId){
		Cursor cursor = fetchDestination(destinationId);
		Destination destination = null;

		if(cursor != null){
			destination = getDestination(cursor);
			cursor.close();
		}
		return destination;
	}

    public List<Destination> getAllDestinations(){

        Log.i("DESTINATION_DATABASE", "getting all destinations");

        List<Destination> destinations = new ArrayList<Destination>();
        Cursor cursor = fetchAllDestinations();

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                destinations.add(getDestination(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return destinations;
    }

    protected Destination getDestination(Cursor cursor){

        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        Long id = cursor.getLong(cursor.getColumnIndex(KEY_ROW_ID));
        Double latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE));
        Double longitude = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE));
        boolean mostRecent = cursor.getInt(cursor.getColumnIndex(KEY_MOST_RECENT)) == 1;

        return new Destination(id, name, latitude, longitude,  mostRecent);
    }

    protected Cursor fetchDestination(Long destinationId) throws SQLException {
        Cursor mCursor =
            db.query(true, DATABASE_TABLE, new String[] {
					KEY_ROW_ID,
                    KEY_NAME,
                    KEY_LATITUDE,
                    KEY_LONGITUDE,
                    KEY_MOST_RECENT
				}, KEY_ROW_ID + "=" + destinationId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchAllDestinations() throws SQLException {
        Cursor mCursor =
            db.query(true, DATABASE_TABLE, new String[] {
					KEY_ROW_ID,
                    KEY_NAME,
                    KEY_LATITUDE,
                    KEY_LONGITUDE,
                    KEY_MOST_RECENT
				}, null, null, null, null, KEY_NAME, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long addDestination(Destination destination) {

        Log.i("DESTINATION_DATABASE", "adding destination: " + destination.getName());

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, destination.getName());
        values.put(KEY_LATITUDE, destination.getLatitude());
        values.put(KEY_LONGITUDE, destination.getLongitude());
        values.put(KEY_MOST_RECENT, destination.isMostRecent() ? 1 : 0);

        return db.insert(DATABASE_TABLE, null, values);
    }
}
