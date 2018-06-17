package ru.bmstu.queueapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.bmstu.queueapp.R;
import ru.bmstu.queueapp.http.data.AppointmentInterval;

public class AppointmentIntervalItemAdapter extends BaseAdapter {

    private ArrayList<AppointmentInterval> data;
    private LayoutInflater inflater;

    public AppointmentIntervalItemAdapter(Context context, ArrayList<AppointmentInterval> data) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.appointment_interval_item_display, parent, false);
        }
        AppointmentInterval el = data.get(position);
        ((TextView) vi.findViewById(R.id.appointmentIntervalItemText)).setText(el.toString());
        return vi;
    }
}
