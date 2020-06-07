package com.e.womansafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registerone extends AppCompatActivity   {


    EditText emailId, Password, phoneno, Name;
    private ProgressBar progressBar;
    Button btnregister;
    FirebaseAuth mFirebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registerone);


        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailone);
        Password = findViewById(R.id.passwordone);
        phoneno = findViewById(R.id.phoneone);
        Name = findViewById(R.id.nametv);
        progressBar = findViewById(R.id.progressone);
        btnregister = findViewById(R.id.registertwo);

        progressBar.setVisibility(View.GONE);



        btnregister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                final String name = Name.getText().toString().trim();
                final String email = emailId.getText().toString().trim();
                String password = Password.getText().toString().trim();
                final String phone = phoneno.getText().toString().trim();

                if (name.isEmpty()) {
                    Name.setError("Please enter name");
                    Name.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    emailId.setError("Please enter Email");
                    emailId.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailId.setError("Invalid email Address");
                    emailId.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    Password.setError("Please enter Password");
                    Password.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    Password.setError("Password is less than 6 characters");
                    Password.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    phoneno.setError("Please enter phone number");
                    phoneno.requestFocus();
                    return;
                }

                if (phone.length() != 10) {
                    phoneno.setError("Invalid phone number");
                    phoneno.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    User user = new User(
                                            name,
                                            email,
                                            phone
                                    );

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Registerone.this, "registration_success", Toast.LENGTH_LONG).show();

                                            } else {

                                                Toast.makeText(Registerone.this, "registration_unsuccess", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(Registerone.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }



        });
    }
}
