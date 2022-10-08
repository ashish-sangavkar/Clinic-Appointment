package com.example.appointment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class P_doc_schedule extends AppCompatActivity {
    ListView lv;
    Button b1;
    ArrayList<String> arrayList1, arrayList2;
    String from_time, to_time, slots, name, date, pname, pmobile, selectedTime,data;
    FirebaseFirestore db;
    EditText e1;
    Intent i;
    AlertDialog.Builder builder;
    Calendar calendar=Calendar.getInstance();
    final int y=calendar.get(Calendar.YEAR);
    final int m=calendar.get(Calendar.MONTH);
    final int d=calendar.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog.OnDateSetListener setListener;
    DateFormat dateFormat=DateFormat.getDateInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_doc_schedule);
        lv = findViewById(R.id.schedulelist);
        arrayList1 = new ArrayList<>();
        arrayList2 = new ArrayList<>();
        e1 = findViewById(R.id.e1);
        b1 = findViewById(R.id.b1);

        builder = new AlertDialog.Builder(this);

        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        i = getIntent();
        name = i.getStringExtra("name");
        pname = i.getStringExtra("pname");
        pmobile = i.getStringExtra("pmobile");
        db = FirebaseFirestore.getInstance();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList1.clear();
                arrayList2.clear();
                com.example.appointment.ScheduleList arrayAdapter = new com.example.appointment.ScheduleList(getApplicationContext(), arrayList1, arrayList2);
                lv.setAdapter(arrayAdapter);

                date = e1.getText().toString();
                if(date.equals("")) {
                    Toast.makeText(getApplicationContext(), "Plzz Fill The Date Box", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        Date d=new SimpleDateFormat("dd-mm-yyyy").parse(date);
                        loadSchedule(date);
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), "Plzz Enter Date In Format - DD-MM-YYYY", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                selectedTime = (String) arg0.getItemAtPosition(position);
                createInput();
            }
        });
    }

    private void loadSchedule(String date) {
        db.collection("Doctors").document(name).collection("Schedule").document(date).collection("info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                from_time = document.get("from_time").toString();
                                to_time = document.get("to_time").toString();
                                arrayList1.add("From Time : " + from_time);
                                arrayList2.add("To Time  : " + to_time);
                            }
                            if(arrayList1.isEmpty()){
                                Toast.makeText(getApplicationContext(),"Schedule Not Available",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                com.example.appointment.ScheduleList arrayAdapter = new com.example.appointment.ScheduleList(getApplicationContext(), arrayList1, arrayList2);
                                lv.setAdapter(arrayAdapter);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setConfirmation(String data) {
        final int[] count = {0};
        db.collection("Appointments").document(name).collection(date).document(selectedTime.substring(12)).collection("info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count[0] += 1;
                            }
                            if (count[0] == 5) {
                                Toast.makeText(getApplicationContext(), "Schedule Full", Toast.LENGTH_SHORT).show();
                            } else {

                                builder.setMessage("Are You Sure You Want To Book Appointment")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                bookAppointment(data);
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.setTitle("Appointment Confirmation");
                                alert.show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Document not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void bookAppointment(String data) {
        final int[] cnt = {0};
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> user1 = new HashMap<>();

        user.put("pname", data);
        user.put("mb", pmobile);
        user.put("from_time", selectedTime.substring(12));
        db.collection("Appointments").document(name).collection(date).document(selectedTime.substring(12)).collection("info").document(data)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Appointment Booked", Toast.LENGTH_SHORT).show();
                    }
                });

        user1.put("pname",data);
        user1.put("mb",pmobile);
        user1.put("dname",name);
        user1.put("date",date);
        user1.put("from_time",selectedTime.substring(12));
        db.collection("Records").document(pmobile).collection("info").document(data)
                .set(user1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
        private void selectDate(){
            DatePickerDialog datePickerDialog=new DatePickerDialog(P_doc_schedule.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    month=month+1;
                    String date=day+"-"+month+"-"+year;
                    e1.setText(date);
                }
            },y,m,d);
            datePickerDialog.show();
        }
        private void createInput(){
            final AlertDialog.Builder alert=new AlertDialog.Builder(P_doc_schedule.this);
            View v1=getLayoutInflater().inflate(R.layout.custom_input,null);
            final EditText e1= v1.findViewById(R.id.e1);
            Button b1=v1.findViewById(R.id.b1);
            Button b2=v1.findViewById(R.id.b2);

            alert.setView(v1);
            final AlertDialog alertDialog=alert.create();

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data=e1.getText().toString();
                    setConfirmation(data);
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
}