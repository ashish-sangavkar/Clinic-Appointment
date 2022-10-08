package com.example.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    EditText e1,e2;
    Button b1,b2;
    String s="",user="",pass="";
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Intent[] i = {getIntent()};
        s= i[0].getStringExtra("control");

        e1=(EditText)findViewById(R.id.user);
        e2=(EditText)findViewById(R.id.pass);
        b1=(Button)findViewById(R.id.login_button);
        db= FirebaseFirestore.getInstance();
        sharedPreferences=getSharedPreferences("name", MODE_PRIVATE);
        String name=sharedPreferences.getString("username",null);
        if(name!=null){
            Intent i1=new Intent(Login.this, Admin_home.class);
            startActivity(i1);
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent[] i = new Intent[1];
                user=e1.getText().toString();
                pass=e2.getText().toString();
                if(!user.matches("") && !pass.matches("")){
                    final int[] j = {0};
                    db.collection(s)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(user.equalsIgnoreCase(document.getString("username")) && pass.equalsIgnoreCase(document.getString("password"))){
                                                progressDialog=new ProgressDialog(Login.this);
                                                progressDialog.show();
                                                progressDialog.setContentView(R.layout.progress_dialog);
                                                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                if(s.equals("Admins")) {
                                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                                    editor.putString("username", user);
                                                    editor.apply();
                                                    i[0] =new Intent(Login.this,Admin_home.class);
                                                } else if(s.equals("Doctors")){
                                                    i[0] =new Intent(Login.this, com.example.appointment.Doctor_home.class);
                                                }
                                                else{
                                                }
                                                String id=document.getId().toString();
                                                i[0].putExtra("id",id);
                                                startActivity(i[0]);
                                                j[0] +=1;

                                            }
                                        }
                                        if(j[0] == 0){
                                            Toast.makeText(getApplicationContext(),"Invalid Username or Password",Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Document not found",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        progressDialog.dismiss();
    }
}