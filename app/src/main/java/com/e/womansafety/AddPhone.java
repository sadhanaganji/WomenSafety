package com.e.womansafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddPhone extends AppCompatActivity {
    EditText Addname,Addphone;
    Button Add;
    ListView listViewPersons;
    List<Person> persons;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public String userID;

    DatabaseReference databasePersons;
    // String id = databasePersons.push().getKey();
    //FirebaseAuth mFirebaseAuth;
   // private DatabaseReference databasePersons;
    private FirebaseDatabase mFirebaseDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);
        String mUserId;

        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
         userID = FirebaseAuth.getInstance().getCurrentUser().getUid();//user.getUid();
        databasePersons = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Persons");


        Addname = findViewById(R.id.Name);
        Addphone = findViewById(R.id.phone);
        Add = findViewById(R.id.addbtn);
        listViewPersons = findViewById(R.id.lv);
        mFirebaseAuth = FirebaseAuth.getInstance();

        persons = new ArrayList<>();

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addPerson();
            }
        });

        listViewPersons.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person person = persons.get(i);
                //One one =one.get(i);
                showUpdateDeleteDialog(person. getPersonId(), person. getPersonName());
                return true;
            }
        });
    }
    // String id = databasePersons.push().getKey();
    void addPerson() {
        //getting the values to save
        String Namee = Addname.getText().toString().trim();
        String genre = Addphone.getText().toString();



        //checking if the value is provided
        if (!TextUtils.isEmpty(Namee)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id =databasePersons.push().getKey();//FirebaseAuth.getInstance().getCurrentUser().getUid();
            //databasePersons.push().getKey();
//String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
            //creating an Artist

            Person person = new Person( id,  Namee, genre,FirebaseAuth.getInstance().getCurrentUser().getUid());
            //Saving the Artist
            databasePersons.child(id).setValue(person);

            //setting edittext to blank again
            Addname.setText("");
            Addphone.setText("");

            //displaying a success toast
            Toast.makeText(this, "Person added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }




    protected void onStart() {
        super.onStart();


        databasePersons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                persons.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Person hel = postSnapshot.getValue(Person.class);

                    //adding artist to the list
                    persons.add(hel);

                }
//creating adapter

                    PersonList personAdapter = new PersonList(AddPhone.this, persons);

                    //attaching adapter to the listview
                    listViewPersons.setAdapter(personAdapter);
               // }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private boolean updateArtist(String id, String name, String genre,String uid) {
        //getting the specified artist reference

        // String id = databasePersons.push().getKey();
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Persons").child(id);

        //updating artist
        Person person = new Person(id, name, genre,uid);
        dR.setValue(person);
        Toast.makeText(getApplicationContext(), "Person Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteArtist(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Persons").child(id);

        //removing artist
        dR.removeValue();

        //getting the tracks reference for the specified artist
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("Persons").child(id);

        //removing all tracks
        drTracks.removeValue();
        Toast.makeText(getApplicationContext(), "Person Deleted", Toast.LENGTH_LONG).show();

        return true;
    }
    private void showUpdateDeleteDialog(final String artistId, String artistName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName =  dialogView.findViewById(R.id.editTextName);
        final EditText editTextPhone =  dialogView.findViewById(R.id.editTextPhone);
        final Button buttonUpdate =  dialogView.findViewById(R.id.Update);
        final Button buttonDelete =  dialogView.findViewById(R.id.Delete);

        dialogBuilder.setTitle(artistName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String genre = editTextPhone.getText().toString();
                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                //String id = databasePersons.push().getKey();
                if (!TextUtils.isEmpty(name)) {
                    updateArtist(artistId, name, genre,uid);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteArtist(artistId);
                b.dismiss();
            }
        });
    }
}

