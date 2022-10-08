package com.example.appointment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Admin_home extends AppCompatActivity {
    Intent i;
    String id;
    TextView t1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        t1=findViewById(R.id.t1);
        sharedPreferences=getSharedPreferences("name", MODE_PRIVATE);
        String name=sharedPreferences.getString("username", null);
        if(name!=null){
            t1.setText("Welcome "+name);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        switch (item.getItemId()){
            case R.id.i1:
                fragmentTransaction.replace(R.id.framelayout,new A_Doc_Add());
                fragmentTransaction.commit();
                break;
            case R.id.i2:
                fragmentTransaction.replace(R.id.framelayout,new com.example.appointment.A_Delete_Doc());
                fragmentTransaction.commit();
                break;
            case R.id.i3:
                fragmentTransaction.replace(R.id.framelayout,new com.example.appointment.A_Show_Doc());
                fragmentTransaction.commit();
                break;
            case R.id.i4:
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();
                this.finish();
                Toast.makeText(getApplicationContext(),"Logout Successfully",Toast.LENGTH_SHORT).show();
                i=new Intent(Admin_home.this, com.example.appointment.MainActivity.class);
                startActivity(i);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}