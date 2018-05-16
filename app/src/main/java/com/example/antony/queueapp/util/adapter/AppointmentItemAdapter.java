package com.example.antony.queueapp.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.antony.queueapp.R;
import com.example.antony.queueapp.http.data.Appointment;

import java.util.ArrayList;

public class AppointmentItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<Appointment> data;
    private static LayoutInflater inflater = null;

    public AppointmentItemAdapter(Context context, ArrayList<Appointment> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.appointment_item_display, null);
        TextView text = (TextView) vi.findViewById(R.id.text);
        text.setText(data.get(position).end.toString());
        return vi;
    }
}
