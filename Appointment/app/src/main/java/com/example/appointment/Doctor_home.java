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

public class Doctor_home extends AppCompatActivity {
    Intent i;
    String id="";
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        bundle=new Bundle();
        i=getIntent();
        id=i.getStringExtra("id");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.doctor_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        switch (item.getItemId()){
            case R.id.i1:
                D_Update_Profile d_update_profile=new D_Update_Profile();
                bundle.putString("id",id);
                d_update_profile.setArguments(bundle);
                fragmentTransaction.replace(R.id.framelayout, d_update_profile);
                fragmentTransaction.commit();
                break;
            case R.id.i2:
                D_Add_Schedule d_add_schedule=new D_Add_Schedule();
                bundle.putString("id",id);
                d_add_schedule.setArguments(bundle);
                fragmentTransaction.replace(R.id.framelayout,d_add_schedule);
                fragmentTransaction.commit();
                break;
            case R.id.i3:
                com.example.appointment.D_Check_Appointment d_check_appointment=new com.example.appointment.D_Check_Appointment();
                bundle.putString("id",id);
                d_check_appointment.setArguments(bundle);
                fragmentTransaction.replace(R.id.framelayout,d_check_appointment);
                fragmentTransaction.commit();
                break;
            case R.id.i4:
                Toast.makeText(getApplicationContext(),"Logout Successfully",Toast.LENGTH_SHORT).show();
                this.finish();
                i=new Intent(Doctor_home.this,MainActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}