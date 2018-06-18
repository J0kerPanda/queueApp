package ru.bmstu.queueapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import ru.bmstu.queueapp.R;
import ru.bmstu.queueapp.http.data.Appointment;

public class AppointmentItemAdapter extends BaseAdapter {

    private ArrayList<Map.Entry<LocalTime, Appointment>> data;
    private LayoutInflater inflater;

    public AppointmentItemAdapter(Context context, HashMap<LocalTime, Appointment> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = new ArrayList<>(data.entrySet());
        Collections.sort(this.data, new Comparator<Map.Entry<LocalTime, Appointment>>() {
            @Override
            public int compare(Map.Entry<LocalTime, Appointment> o1, Map.Entry<LocalTime, Appointment> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getValue();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.appointment_item_display, parent, false);
        }
        Appointment el = data.get(position).getValue();
        ((TextView) vi.findViewById(R.id.appointmentItemInterval)).setText(el.timeInterval());
        ((TextView) vi.findViewById(R.id.appointmentItemUser)).setText(el.visitorFullName());
        return vi;
    }
}
