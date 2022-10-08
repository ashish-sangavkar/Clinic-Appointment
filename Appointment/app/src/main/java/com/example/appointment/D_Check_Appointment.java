package com.example.appointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class D_Check_Appointment extends Fragment {
    ListView lv;
    ArrayList<String> arrayList1,arrayList2;
    String nm,dept,mb,date,time;
    EditText e1,e2;
    Button b1;
    int hour,min;
    Calendar calendar=Calendar.getInstance();
    final int y=calendar.get(Calendar.YEAR);
    final int m=calendar.get(Calendar.MONTH);
    final int d=calendar.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog.OnDateSetListener setListener;
    TimePickerDialog timePickerDialog,timePickerDialog1;
    DateFormat dateFormat=DateFormat.getDateInstance();
    FirebaseFirestore db;
    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_d__check__appointment,container,false);
        lv=view.findViewById(R.id.patient_appointment);
        arrayList1=new ArrayList<>();
        arrayList2=new ArrayList<>();
        e1=view.findViewById(R.id.e1);
        e2=view.findViewById(R.id.e2);
        b1=view.findViewById(R.id.b1);
        bundle=new Bundle();

        bundle=getArguments();
        nm=bundle.getString("id");
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
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                arrayList1.clear();
                arrayList2.clear();
                AppointmentList arrayAdapter=new AppointmentList(getContext(),arrayList1,arrayList2);
                lv.setAdapter(arrayAdapter);

                date=e1.getText().toString();
                time=e2.getText().toString();

                if(date.equals("") || time.equals("")){
                    Toast.makeText(getContext(),"Plzz fill all the information",Toast.LENGTH_SHORT).show();
                }
                else{
                    loadList(date,time);
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String selectedItem = (String) arg0.getItemAtPosition(position);
            }
        });
        return view;
    }

    public void loadList(String d, String t) {
        db.collection("Appointments").document(nm).collection(d).document(t).collection("info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nm1=document.get("pname").toString();
                                String mb1=document.get("mb").toString();
                                arrayList1.add(nm1);
                                arrayList2.add(mb1);
                            }
                            if(arrayList1.isEmpty()){
                                Toast.makeText(getContext(), "Appointments Not Available", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                AppointmentList arrayAdapter=new AppointmentList(getContext(),arrayList1,arrayList2);
                                lv.setAdapter(arrayAdapter);
                            }
                        } else {
                            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void selectDate(){
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
}
