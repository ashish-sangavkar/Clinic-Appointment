package com.example.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Patient_home extends AppCompatActivity {
    Intent i;
    String name,mb;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        bundle=new Bundle();
        i=getIntent();
        name=i.getStringExtra("name");
        mb=i.getStringExtra("mobile");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.patient_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        switch (item.getItemId()){
            case R.id.i1:
                P_show_doctor p_show_doctor=new P_show_doctor();
                bundle.putString("name",name);
                bundle.putString("mb",mb);
                p_show_doctor.setArguments(bundle);
                fragmentTransaction.replace(R.id.framelayout, p_show_doctor);
                fragmentTransaction.commit();
                break;
            case R.id.i2:
                P_cancel_appointment p_cancel_appointment=new P_cancel_appointment();
                bundle.putString("mb",mb);
                p_cancel_appointment.setArguments(bundle);
                fragmentTransaction.replace(R.id.framelayout, p_cancel_appointment);
                fragmentTransaction.commit();
                break;
            case R.id.i3:
                Toast.makeText(getApplicationContext(),"Logout Successfully",Toast.LENGTH_SHORT).show();
                this.finish();
                i=new Intent(Patient_home.this,MainActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}