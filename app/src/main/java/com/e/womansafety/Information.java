package com.e.womansafety;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Information extends AppCompatActivity {
private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        txt=findViewById(R.id.infotv);
        txt.setText("About this Application\n\n"+
                "Women Safety app helps to protect women from unsafe or emergency situations.\n"+
                "In case of any unsafe situations just TAP the PANIC button to raise a emergency notification to the near by registered women safety application users"+
                "The main feature of this application is that it sends a notification of your location to the registered users within the distance of 5 km so that they can reach the victims location faster and help them\n "
                +"The other feature is that it sends SMS to the saved contacts informing that you are unsafe and need help \n "+
                "Both the SMS and Notification includes accurate currrent GPS location as a Google maps link,\n"+
                "So the SMS and notification receivers can use this Google maps link to get directions and Navigate to the exact location."
        );

    }
}
