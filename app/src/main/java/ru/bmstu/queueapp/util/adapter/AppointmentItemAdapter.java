package ru.bmstu.queueapp.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ru.bmstu.queueapp.R;
import ru.bmstu.queueapp.http.data.Appointment;

import java.util.ArrayList;

public class AppointmentItemAdapter extends BaseAdapter {

    private ArrayList<Appointment> data;
    private LayoutInflater inflater;

    public AppointmentItemAdapter(Context context, ArrayList<Appointment> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
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
        return position;
    }

    private static String transformName(String name) {
        if (name == null) {
            return null;
        }
        String[] parts = name.split(" ");
        if (parts.length >= 3) {
            return String.format("%s %s. %s.", parts[0], parts[1].charAt(0), parts[2].charAt(0));
        } else {
            return name;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.appointment_item_display, parent, false);
        }
        ((TextView) vi.findViewById(R.id.appointmentItemInterval)).setText(data.get(position).timeInterval());
        ((TextView) vi.findViewById(R.id.appointmentItemUser)).setText(transformName(data.get(position).visitorFullName));
        return vi;
    }
}
