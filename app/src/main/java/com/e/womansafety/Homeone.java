package com.e.womansafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Homeone extends AppCompatActivity {
//location
    static Homeone instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView txt_location;
    public static Homeone getInstance(){
        return instance;
    }
//location;
   // Location location;
    private Button menu;
    Button panic;
    TextView tv;
    DatabaseReference databasePersons,ref,eva,dR;
    public String userID;
    List<Person> persons;
    public  String phone;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    Context context = this;
   // private LatLng pickupLocation;
   // private FusedLocationProviderClient mFusedLocationClient;
  // private GoogleApiClient googleApiClient;
   private int radius = 1;
    private Boolean driverFound = false;
    private String driverFoundID;
    GeoQuery geoQuery;
    private final String CHANNEL_ID="personal_notification";
    private final int NOTIFICATION_ID = 001;
    private RequestQueue mrequestQueue;
    private String URL="https://fcm.googleapis.com/fcm/send";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeone);
        //location


//int fine=ContextCompat.checkSelfPermission(Homeone.this,
        //Manifest.permission.ACCESS_COARSE_LOCATION;
            instance = this;
            //txt_location = findViewById(R.id.loc);
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.SEND_SMS

                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                updateLocation();
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

            //location

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databasePersons = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Persons");
        ref = FirebaseDatabase.getInstance().getReference().child("location");
        persons = new ArrayList<>();
        menu = (Button) findViewById(R.id.menubtn);
       // tv= findViewById(R.id.tvsad);
        //TV=findViewById(R.id.tvhana);
        panic = findViewById(R.id.panicbtn);
       // fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mrequestQueue = Volley.newRequestQueue(this);
       // FirebaseMessaging.getInstance().subscribeToTopic(userID);
        panic.setBackgroundColor(Color.parseColor("#DE4A48"));
        FirebaseDatabase.getInstance().getReference("panicclickers")
                .child("one")
                .setValue(userID);








        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //int coa=ContextCompat.checkSelfPermission(Homeone.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                //int fin=ContextCompat.checkSelfPermission(Homeone.this, Manifest.permission.ACCESS_FINE_LOCATION);
              //  if (coa== PackageManager.PERMISSION_GRANTED) {
                  //  fetchLocation();
                    // Permission is not granted
                    // Should we show an explanation?

               // }
                //if (fin== PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?

               // }
                Intent a4 = new Intent(Homeone.this, Menuone.class);
                startActivity(a4);
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    //  Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }
                                String token = task.getResult().getToken();

                                FirebaseDatabase.getInstance().getReference("token")
                                        .child(userID)
                                        .setValue(token);
                                System.out.println(String.format("--------------"));
                                System.out.println(String.format(token));
                                // datafunction(key,token);


                                // Get new Instance ID token



                                // Log and toast
                                // String msg = getString(R.string.msg_token_fmt, token);
                                // Log.d(TAG, msg);
                                //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });


        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panic.setBackgroundColor(Color.parseColor("#b3fff0"));
                //int permissionCheck = ContextCompat.checkSelfPermission(Homeone.this, Manifest.permission.SEND_SMS);


                //if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                  MyMessage();

                final GeoFire geoFire = new GeoFire(ref);
                geoFire.getLocation(userID, new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        double one= location.latitude;
                        double two=location.longitude;

                        if (location != null) {
                            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(one, two), 10);
                            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                @Override
                                public void onKeyEntered(final String key, final GeoLocation location) {
                                   // System.out.println(String.format("got one"));
                                    System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                                        //String one=dR.getKey();
                                    dR = FirebaseDatabase.getInstance().getReference("token");

                                    dR.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                           // if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

                                                // displayNotification();
                                            String hunt=dataSnapshot.child(key).getValue().toString();
                                            System.out.println(String.format("ssssssssssssss"));
                                            System.out.println(String.format(hunt));
                                           // sendNotification(hunt);
                                            getlocation(hunt);
                                           /*for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                //getting artist
                                               token hel = postSnapshot.getValue(token.class);

                                                System.out.println(String.format(String.valueOf(hel)));
                                                //adding artist to the list


                                            }*/
                                               //
                                                //String found=dataSnapshot.getKey();
                                                //System.out.println(String.format(dataSnapshot.getKey()));
                                               // String one=dR.push().getKey();


                                           // }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });



                                }

                                @Override
                                public void onKeyExited(String key) {
                                    System.out.println(String.format("Key %s is no longer in the search area", key));
                                }

                                @Override
                                public void onKeyMoved(String key, GeoLocation location) {
                                    System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
                                }

                                @Override
                                public void onGeoQueryReady() {
                                    System.out.println("All initial data has been loaded and events have been fired!");
                                }

                                @Override
                                public void onGeoQueryError(DatabaseError error) {
                                    System.err.println("There was an error with this query: " + error);
                                }
                            });

                            System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                        } else {
                            System.out.println(String.format("There is no location for key %s in GeoFire", key));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("There was an error getting the GeoFire location: " + databaseError);
                    }
                });
                  GetNeatByPeople();

               // } else {
              //  ActivityCompat.requestPermissions(Homeone.this, new String[]{Manifest.permission.SEND_SMS}, 0);
               //}
               // int hel=ContextCompat.checkSelfPermission(Homeone.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                //if(hel==PackageManager.PERMISSION_GRANTED)
                //{
                  //  updateLocation();
                //}
            }
        });

    }

    private void datafunction(final String key, final String token) {
        eva = FirebaseDatabase.getInstance().getReference("location").child(key);
        eva.addValueEventListener(new ValueEventListener() {
          @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

        // displayNotification();

        System.out.println(String.format("got one"));
        String found=dataSnapshot.getKey();
         System.out.println(String.format(dataSnapshot.getKey()));

        }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
        });

    }

    private void sendNotification(String one) {
        //token="topic/key";

        GeoFire geoFire = new GeoFire(ref);
        geoFire.getLocation(userID, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",  location.latitude,location.longitude);
                   // getlocation(location.latitude,location.longitude);

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

    private void getlocation( final String one) {
        GeoFire geoFire = new GeoFire(ref);
        geoFire.getLocation(userID, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    System.out.println(String.format("tttttttttttttttttt"));
                   // System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                    final String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",  location.latitude,location.longitude);
                    //getlocation(location.latitude,location.longitude);

                    JSONObject mainObj = new JSONObject();
                   createNotificationChannel();
                    try {
                        mainObj.put("to",one);
                        JSONObject notificationObj = new JSONObject();

                        notificationObj.put("title","Women Safety");
                        notificationObj.put("body","A Person near your location is in DANGER\nTAP to get location");
                        notificationObj.put("click_action","SOMEACTIVITY");
                       mainObj.put("notification",notificationObj);
                       // notificationObj.put("click_action",startActivity(intent));
                       // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        //startActivity(intent);

                        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST, URL,
                                mainObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                        ){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> header = new HashMap<>();
                                header.put("content-type","application/json");
                                header.put("authorization","key=");
                                return header;
                            }
                        };
                        mrequestQueue.add(request);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

    private boolean clicking() {
       // Intent a5 = new Intent(Homeone.this, Notification.class);
        //startActivity(a5);
        return true;
    }

    private void displayNotification() {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
       // builder.setSmallIcon(R.drawable.ic_sms_black_24dp);
        builder.setContentTitle("simple Notification");
        builder.setContentText("this is simple notification");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());



    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            CharSequence name="Personal Notification";
            String description = "Include all the personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);

            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    MyLocationService myLocationService=new MyLocationService();

    public MyLocationService getMyLocationService() {
       
        return myLocationService;
    }

    void GetNeatByPeople() {
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference("Users/location");
//String lat= String.valueOf(location.getLatitude());
  //      String lon= String.valueOf(location.getLongitude());
//Location location=new Location();
       /* GeoFire geoFire = new GeoFire(ref);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(lot), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
               // if (!driverFound ){
                    //DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(key);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() ){
                                txt_location.setText(dataSnapshot.getKey());
                               // if (driverFound){
                                  //  return;
                                //}

                                //if(driverMap.get("service").equals(requestService)){









                                //}
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                //}
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound)
                {
                    radius++;
                    //GetNeatByPeople();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });*/
    }


    //location
    private void updateLocation() {
       // Toast.makeText(this, "do it", Toast.LENGTH_SHORT).show();
        buildLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent());

    }

    private PendingIntent getPendingIntent() {
        //Toast.makeText(this, "request denied", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }
    public void updateTextview(final String value)
    {


        Toast.makeText(this, "nope", Toast.LENGTH_SHORT).show();
        Homeone.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                txt_location.setText(value);
            }
        });
    }

    //location
    private void MyMessage () {


        dR = FirebaseDatabase.getInstance().getReference("panicclickers");

        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

                // displayNotification();
                String hunt = dataSnapshot.child("one").getValue().toString();

                GeoFire geoFire = new GeoFire(ref);
                geoFire.getLocation(userID, new LocationCallback() {

                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        if (location != null) {
                            System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                            final String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",  location.latitude,location.longitude);
                            // getlocation(location.latitude,location.longitude);

                            databasePersons.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                        phone = ds.child("personNumber").getValue().toString();

                                        // tv.setText(phone);
                                        //String Phone = person.getPersonNumber();
                                        String mesg = "I am in DANGER\n"+uri ;
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(phone, null, mesg, null, null);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
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
        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case 0:
                    if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        MyMessage();
                    } else {
                        Toast.makeText(this, "request denied", Toast.LENGTH_SHORT).show();
                    }
            }

        }
    //private void fetchLocation() {
            // Permission has already been granted
       // fusedLocationProviderClient.getLastLocation()
                   // .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                     //   @Override
                       // public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                           // if (location != null) {
                                // Logic to handle location object
                               //Double latittude = location.getLatitude();
                                //Double longitude = location.getLongitude();

                               //tv.setText("Latitude = "+latittude + "\nLongitude = " + longitude);
                               //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                               // GeoFire geoFire = new GeoFire(ref);
                                //geoFire.setLocation( userID, new GeoLocation( latittude, longitude));

                            }
                       // }
                   // });

//        }

 //   }






