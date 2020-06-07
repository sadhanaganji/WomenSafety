package com.e.womansafety;


import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PersonList extends ArrayAdapter<Person> {
    Activity context;
     List<Person> persons;
    //public  String personId;

    public PersonList(Activity context, List<Person> persons) {
        super(context, R.layout.layout_person_list, persons);
        this.context = context;
        this.persons =persons ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_person_list, null, true);

        TextView textViewName =  listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre =  listViewItem.findViewById(R.id.textViewGenre);
        //new Person(context, "");


        Person person = new Person();
        Person hello = persons.get(position);
if(hello.getuid() == FirebaseAuth.getInstance().getCurrentUser().getUid()) {

    textViewName.setText(hello.getPersonName());
    textViewGenre.setText(hello.getPersonNumber());

}
else{
    textViewName.setText("");
    textViewGenre.setText("");
}
            return listViewItem;

    }


}
