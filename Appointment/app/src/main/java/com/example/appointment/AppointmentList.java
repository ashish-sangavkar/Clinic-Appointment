package com.example.appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AppointmentList extends ArrayAdapter {
    List list1,list2;
    TextView t1,t2;
    Context context;
    public AppointmentList(@NonNull Context context, ArrayList list1, ArrayList list2) {
        super(context, R.layout.patient_appointment,list1);
        this.list1=list1;
        this.list2=list2;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.patient_appointment,parent,false);
        t1=view.findViewById(R.id.t1);
        t2=view.findViewById(R.id.t2);
        t1.setText((CharSequence) list1.get(position));
        t2.setText((CharSequence) list2.get(position));
        return view;
    }
}

