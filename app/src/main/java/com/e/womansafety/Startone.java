package com.e.womansafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Startone extends AppCompatActivity {

    private Button login, register;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startone);



        login = (Button) findViewById(R.id.loginone);
        register = (Button) findViewById(R.id.registerone);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

                if( mFirebaseUser != null ){
                    //Toast.makeText(Startone.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Startone.this, Homeone.class);
                    startActivity(i);
                }
                else{
                    //Toast.makeText(Startone.this,"Please Login",Toast.LENGTH_SHORT).show();
                }

            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a2= new Intent(Startone.this, Loginone.class);
                startActivity(a2);

                return;
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a3 = new Intent(Startone.this, Registerone.class);
                startActivity(a3);

                return;
            }
        });
       /* SharedPrefs.saveSharedSetting(Startone.this, "CaptainCode", "false");
        Intent ImLoggedIn = new Intent(getApplicationContext(), Loginone.class);
        startActivity(ImLoggedIn);
        finish();*/
    }
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }


}
