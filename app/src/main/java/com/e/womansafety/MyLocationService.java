package com.e.womansafety;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.LocationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyLocationService extends BroadcastReceiver {
    DatabaseReference ref;
    public String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();


    public static final String ACTION_PROCESS_UPDATE= "com.e.womansafety.UPDATE_LOCATION";
    // Double latittude = location.getLatitude();
    //Double longitude = location.getLongitude();

    @Override
    public void onReceive(Context context, Intent intent) {
        ref = FirebaseDatabase.getInstance().getReference().child("location");
        if(intent !=null)
        {
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action))
            {
                //Toast.makeText(Homeone,"noo", Toast.LENGTH_SHORT).show();
                LocationResult result = LocationResult.extractResult(intent);
                if(result != null)
                {
                    Location location = result.getLastLocation();
                    String location_string = new StringBuilder(""+location.getLatitude())
                            .append("/")
                            .append(location.getLongitude())
                            .toString();
double lat=location.getLatitude();
double lon= location.getLongitude();

                    try{

                        //Homeone.getInstance().updateTextview(location_string);
                       GeoFire geoFire = new GeoFire(ref);
                        geoFire.setLocation( userID, new GeoLocation( location.getLatitude(), location.getLongitude()));
                       // Homeone.getInstance().GetNeatByPeople(lat, lon);


                    }catch(Exception ex){
                        Toast.makeText(context,location_string, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


}
