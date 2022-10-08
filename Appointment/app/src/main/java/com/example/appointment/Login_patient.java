package com.example.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login_patient extends AppCompatActivity {
    Button b1,b2,b3;
    EditText e1,e2;
    final int[] j = {0};
    String name,mb;
    int b=0,k=0;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_patient);

        e1=findViewById(R.id.mobile);
        e2=findViewById(R.id.otp);
        b3=findViewById(R.id.register);
        b1=findViewById(R.id.send_otp);
        b2=findViewById(R.id.verify);
        db= FirebaseFirestore.getInstance();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s=e1.getText().toString();
                if(s.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter Mobile Number",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(s.length()!=10){
                        Toast.makeText(getApplicationContext(),"Please Enter Valid Mobile Number",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        db.collection("Patients")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if(s.equals(document.get("Mobile"))){
                                                    k+=1;
                                                    name=document.getString("Name").toString();
                                                    mb=document.getString("Mobile").toString();
                                                }

                                            }
                                            if(k==0){
                                                Toast.makeText(getApplicationContext(),"Plzz Register First",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                int min = 1000;
                                                int max = 9999;
                                                b = (int)(Math.random()*(max-min+1)+min);
                                                sendOtp(e1.getText().toString(),b);
                                                k=0;
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp(""+b);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login_patient.this,Patient_register.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void sendOtp(String mb,int otp){
        try{
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(mb,null,"Your One Time Password is : "+otp,null,null);
            e2.setEnabled(true);
            b2.setEnabled(true);
            Toast.makeText(getApplicationContext(),"OTP Sent",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
        }
    }
    public void verifyOtp(String otp){
        String check=e2.getText().toString();
        if(check.matches(otp)){
            Intent i=new Intent(Login_patient.this, com.example.appointment.Patient_home.class);
            i.putExtra("name",name);
            i.putExtra("mobile",mb);
            startActivity(i);
            finish();
        }
        else{
          Toast.makeText(getApplicationContext(),"Enter valid OTP",Toast.LENGTH_SHORT).show();
        }
    }
}