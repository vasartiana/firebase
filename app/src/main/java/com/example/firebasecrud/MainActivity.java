package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText mNameEditText;
    EditText mAddressEditText;

    EditText mUpdateNameEditText;
    EditText mUpdateAddressEditText;

    DatabaseReference mDatabaseReference;
    Student mStudent;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());

        mNameEditText = findViewById(R.id.name_edittext);
        mAddressEditText = findViewById(R.id.address_edittext);
        mUpdateNameEditText = findViewById(R.id.update_name_edittext);
        mUpdateAddressEditText = findViewById(R.id.update_address_edittext);

        findViewById(R.id.insert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        findViewById(R.id.read_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });

        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }

    private void insertData(){
        Student newStudent = new Student();
        String name = mNameEditText.getText().toString();
        String address = mAddressEditText.getText().toString();
        if (name != "" && address != ""){
            newStudent.setName(name);
            newStudent.setAddress(address);

            mDatabaseReference.push().setValue(newStudent);
            Toast.makeText(this, "Successfully insert data", Toast.LENGTH_SHORT).show();
        }
    }

    private void readData(){

        mStudent = new Student();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        key = currentData.getKey();
                        mStudent.setName(currentData.child("name").getValue().toString());
                        mStudent.setAddress(currentData.child("address").getValue().toString());
                    }
                }

                mUpdateNameEditText.setText(mStudent.getName());
                mUpdateAddressEditText.setText(mStudent.getAddress());
                Toast.makeText(MainActivity.this, "Data has been shown!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData(){
        Student updatedData = new Student();
        updatedData.setName(mUpdateNameEditText.getText().toString());
        updatedData.setAddress(mUpdateAddressEditText.getText().toString());
        mDatabaseReference.child(key).setValue(updatedData);
    }

    private void deleteData(){
        mDatabaseReference.child(key).removeValue();
    }
}