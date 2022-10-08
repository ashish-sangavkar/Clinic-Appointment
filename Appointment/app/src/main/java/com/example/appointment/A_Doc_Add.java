package com.example.appointment;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class A_Doc_Add extends Fragment {
    EditText e1,e2,e3,e5,e6;
    Button b1;
    TextView t1;
    String s1,s2,s3,s4,s5,s6;
    FirebaseFirestore db;
    int fees=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_a__doc__add,container,false);
        e1=view.findViewById(R.id.e1);
        e2=view.findViewById(R.id.e2);
        e3=view.findViewById(R.id.e3);
        e5=view.findViewById(R.id.e5);
        e6=view.findViewById(R.id.e6);

        b1=view.findViewById(R.id.b1);
        db = FirebaseFirestore.getInstance();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1=e1.getText().toString();
                s2=e2.getText().toString();
                s3=e3.getText().toString();
                s5=e5.getText().toString();
                s6=e6.getText().toString();
                if(s1.equals("") || s2.equals("") || s3.equals("") || s5.equals("") || s6.equals("")){
                    Toast.makeText(getContext(), "Please fill all the information", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(s3.length()!=10){
                        Toast.makeText(getContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        addDoctor(s1,s2,s3,s5,s6);
                    }
                }
            }
        });
        return view;
    }

    public void addDoctor(String s1,String s2,String s3,String s5,String s6) {

        Map<String, Object> user = new HashMap<>();

        user.put("name",s1);
        user.put("department",s2);
        user.put("mobile",s3);
        user.put("username",s5);
        user.put("password",s6);
        user.put("fees",fees);
        db.collection("Doctors").document(s1)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"Doctor Added",Toast.LENGTH_SHORT).show();
                        e1.setText("");e2.setText("");e3.setText("");e5.setText("");e6.setText("");
                    }
                });
        msgSend(s1,s3,s5,s6);
    }
    private void msgSend(String nm,String mobile,String user,String pass) {
        try{
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(mobile,null,"Hello, "+nm+"\nYou have been successfully added to clinic application your username and password are follows\nUsername : "+s5+"\nand\nPassword : "+s6,null,null);
        }catch (Exception e){
            Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
        }
    }

}
