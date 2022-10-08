package com.example.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class D_Update_Profile extends Fragment {
    EditText e1,e3;
    Button b1;
    String s1,s2,s3,s4,s5,s;
    FirebaseFirestore db;
    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_d__update__profile,container,false);
        bundle=getArguments();
        s=bundle.getString("id");
        e1=view.findViewById(R.id.e1);
        e3=view.findViewById(R.id.e3);

        b1=view.findViewById(R.id.b1);
        db = FirebaseFirestore.getInstance();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1=e1.getText().toString();
                s3=e3.getText().toString();
                if(s1.equals("") || s3.equals("")){
                    Toast.makeText(getContext(), "Please fill all the information", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(s1.length()!=10){
                        Toast.makeText(getContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        updateDoctor(s1,s3);
                    }
                }
            }
        });
        return view;
    }
    public void updateDoctor(String a1, String a2) {

        Map<String, Object> user = new HashMap<>();

        user.put("mobile",a1);
        user.put("address",a2);

        db.collection("Doctors").document(s).update(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"Data Updated",Toast.LENGTH_SHORT).show();
                        e1.setText("");
                        e3.setText("");
                    }
                });
    }
}