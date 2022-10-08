package com.example.appointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class D_Add_Schedule extends Fragment {
    TextView e1;
    FirebaseFirestore db;
    String date,fromtime,totime,s;
    EditText e2,e3;
    Button b1;
    Bundle bundle;
    int hour,min;
    Calendar calendar=Calendar.getInstance();
    final int y=calendar.get(Calendar.YEAR);
    final int m=calendar.get(Calendar.MONTH);
    final int d=calendar.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog.OnDateSetListener setListener;
    TimePickerDialog timePickerDialog,timePickerDialog1;
    DateFormat dateFormat=DateFormat.getDateInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_d__add__schedule,container,false);
        e1=view.findViewById(R.id.e1);
        e2=view.findViewById(R.id.e2);
        e3=view.findViewById(R.id.e3);
        bundle=getArguments();
        s=bundle.getString("id");


        b1=view.findViewById(R.id.b1);
        db=FirebaseFirestore.getInstance();
        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFromTime();
            }
        });
        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectToTime();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date=e1.getText().toString();
                fromtime=e2.getText().toString();
                totime=e3.getText().toString();
                if(date.equals("") || fromtime.equals("") || totime.equals("")){
                    Toast.makeText(getContext(), "Plzz fill all the information", Toast.LENGTH_SHORT).show();
                }
                else{
                    saveSlots(date,fromtime,totime);
                }

            }
        });
        return view;
    }
    public void selectDate(){
        DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month=month+1;
                String date=day+"-"+month+"-"+year;
                e1.setText(date);
            }
        },y,m,d);
        datePickerDialog.show();
    }
    public void selectFromTime(){

        timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm;
                if(hourOfDay==0){
                    hourOfDay+=12;
                    am_pm="AM";
                    e2.setText(hourOfDay+" : "+minute+" "+am_pm);
                }
                else if (hourOfDay==12){
                    am_pm="PM";
                    e2.setText(hourOfDay+" : "+minute+" "+am_pm);
                }
                else if (hourOfDay>12){
                    hourOfDay-=12;
                    am_pm="PM";
                    e2.setText(hourOfDay+" : "+minute+" "+am_pm);
                }
                else{
                    am_pm="AM";
                    e2.setText(hourOfDay+" : "+minute+" "+am_pm);
                }
            }
        },hour,min,false);
        timePickerDialog.show();
    }
    public void selectToTime(){
        timePickerDialog1=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm;
                if(hourOfDay==0){
                    hourOfDay+=12;
                    am_pm="AM";
                    e3.setText(hourOfDay+" : "+minute+" "+am_pm);
                }
                else if (hourOfDay==12){
                    am_pm="PM";
                    e3.setText(hourOfDay+" : "+minute+" "+am_pm);
                }
                else if (hourOfDay>12){
                    hourOfDay-=12;
                    am_pm="PM";
                    e3.setText(hourOfDay+" : "+minute+" "+am_pm);
                }
                else{
                    am_pm="AM";
                    e3.setText(hourOfDay+" : "+minute+" "+am_pm);
                }
            }
        },hour,min,false);
        timePickerDialog1.show();
    }
    public void saveSlots(String date,String fromtime,String totime){

        Map<String, Object> user = new HashMap<>();

        user.put("date",date);
        user.put("from_time",fromtime);
        user.put("to_time",totime);

        db.collection("Doctors").document(s).collection("Schedule").document(date).collection("info").document(fromtime)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"Schedule Created",Toast.LENGTH_SHORT).show();
                        e1.setText("");e2.setText("");e3.setText("");
                    }
                });
    }
}
