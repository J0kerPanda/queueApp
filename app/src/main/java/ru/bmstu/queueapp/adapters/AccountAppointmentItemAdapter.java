package ru.bmstu.queueapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ru.bmstu.queueapp.R;
import ru.bmstu.queueapp.http.data.Appointment;
import ru.bmstu.queueapp.http.data.UserData;

public class AccountAppointmentItemAdapter extends BaseAdapter {

    private ArrayList<Appointment> data;
    private LayoutInflater inflater;

    public AccountAppointmentItemAdapter(Context context, ArrayList<Appointment> data, HashMap<Integer, UserData> hosts) {
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
            vi = inflater.inflate(R.layout.appointment_item_display, parent, false);
        }
        ((TextView) vi.findViewById(R.id.appointmentItemInterval)).setText(data.get(position).timeInterval());
        ((TextView) vi.findViewById(R.id.appointmentItemUser)).setText(data.get(position).visitorFullName());
        return vi;
    }
}
