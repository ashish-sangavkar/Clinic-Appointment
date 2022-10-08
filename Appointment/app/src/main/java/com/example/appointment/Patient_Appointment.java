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

public class Patient_Appointment extends ArrayAdapter {
    List list1,list2,list3,list4;
    TextView t1,t2,t3,t4;
    Context context;
    public Patient_Appointment(@NonNull Context context, ArrayList list1, ArrayList list2, ArrayList list3, ArrayList list4) {
        super(context, R.layout.p_appointments,list1);
        this.list1=list1;
        this.list2=list2;
        this.list3=list3;
        this.list4=list4;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.p_appointments,parent,false);
        t1=view.findViewById(R.id.t1);
        t2=view.findViewById(R.id.t2);
        t3=view.findViewById(R.id.t3);
        t4=view.findViewById(R.id.t4);
        t1.setText((CharSequence) list1.get(position));
        t2.setText((CharSequence) list2.get(position));
        t3.setText((CharSequence) list3.get(position));
        t4.setText((CharSequence) list4.get(position));
        return view;
    }
}


