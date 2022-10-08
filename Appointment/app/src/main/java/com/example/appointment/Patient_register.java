package com.example.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Patient_register extends AppCompatActivity {
    EditText e1,e2,e3;
    Button b1,b2;
    String s1,s2,s3;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);
        db=FirebaseFirestore.getInstance();
        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);
        e3=findViewById(R.id.e3);
        b1=findViewById(R.id.b1);
        b2=findViewById(R.id.b2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1=e1.getText().toString();
                s2=e2.getText().toString();
                s3=e3.getText().toString();

                if(s1.equals("") || s2.equals("") || s3.equals("")){
                    Toast.makeText(getApplicationContext(),"Please fill all the information",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(s2.length() != 10) {
                        Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                    else{
                            registerPatient(s1,s2,s3);
                    }
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Patient_register.this, com.example.appointment.Login_patient.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void registerPatient(String s1,String s2,String s3){
        Map<String, Object> user = new HashMap<>();

        user.put("Name",s1);
        user.put("Mobile",s2);
        user.put("Address",s3);

        db.collection("Patients").document()
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                        e1.setText("");e2.setText("");e3.setText("");
                    }
                });
            }
}