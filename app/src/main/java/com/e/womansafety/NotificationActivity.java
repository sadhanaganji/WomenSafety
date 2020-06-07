package com.e.womansafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class NotificationActivity extends AppCompatActivity {
    DatabaseReference ref,dR;
    public String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification2);
       // userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("location");


    }

    public void open(View view) {
        dR = FirebaseDatabase.getInstance().getReference("panicclickers");

        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

                // displayNotification();
                String hunt = dataSnapshot.child("one").getValue().toString();

                GeoFire geoFire = new GeoFire(ref);
                geoFire.getLocation(hunt, new LocationCallback() {

                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        if (location != null) {
                            System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",  location.latitude,location.longitude);
                            // getlocation(location.latitude,location.longitude);

                            Intent browserintent=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(browserintent);
                        } else {
                            System.out.println(String.format("There is no location for key %s in GeoFire", key));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("There was an error getting the GeoFire location: " + databaseError);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
