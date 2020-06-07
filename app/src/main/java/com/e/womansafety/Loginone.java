package com.e.womansafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Loginone extends AppCompatActivity {
public int fine;
    EditText emailId, password;
    Button btnlogin;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginone);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailtwo);
        password = findViewById(R.id.passwordtwo);
        btnlogin= findViewById(R.id.logintwo);


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

                if( mFirebaseUser != null ){
                    Toast.makeText(Loginone.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Loginone.this, Homeone.class);
                    startActivity(i);


                }
                else{
                    Toast.makeText(Loginone.this,"Please Login",Toast.LENGTH_SHORT).show();
                }

            }
        };


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Loginone.this,"Please enter email id and password!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(Loginone.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){

                                Toast.makeText(Loginone.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                            }

                            else{
                              // int fine= ContextCompat.checkSelfPermission(Loginone.this,Manifest.permission.ACCESS_FINE_LOCATION);

                                Intent intToHome = new Intent(Loginone.this,Homeone.class);
                                startActivity(intToHome);
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(Loginone.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }


            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

}

