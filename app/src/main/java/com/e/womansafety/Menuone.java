package com.e.womansafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Menuone extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    private Button logout;
    private Button addphone;
private Button about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuone);

        logout =  findViewById(R.id.logoutbtn);
        addphone =  findViewById(R.id.Addphone);
        about = findViewById(R.id.info);


        mFirebaseAuth = FirebaseAuth.getInstance();
        logout.setBackgroundColor(Color.parseColor("#73B8D1"));
        addphone.setBackgroundColor(Color.parseColor("#73B8D1"));
        about.setBackgroundColor(Color.parseColor("#73B8D1"));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent a1=new Intent(Menuone.this,Startone.class);
                startActivity(a1);
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        addphone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Intent a3=new Intent(Menuone.this,AddPhone.class);
                startActivity(a3);
            }

        });
    }

    public void in(View view) {
        Intent a1=new Intent(Menuone.this,Information.class);
        startActivity(a1);
    }
}
