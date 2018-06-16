package ru.bmstu.queueapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.bmstu.queueapp.R;
import ru.bmstu.queueapp.http.data.AccountAppointment;

public class AccountAppointmentItemAdapter extends BaseAdapter {

    private ArrayList<AccountAppointment> data;
    private LayoutInflater inflater;

    public AccountAppointmentItemAdapter(Context context, ArrayList<AccountAppointment> data) {
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
            vi = inflater.inflate(R.layout.account_appointment_item_display, parent, false);
        }
        ((TextView) vi.findViewById(R.id.appointmentItemDate)).setText(data.get(position).date.toString());
        ((TextView) vi.findViewById(R.id.appointmentItemInterval)).setText(data.get(position).start.toString("HH:mm"));
        ((TextView) vi.findViewById(R.id.appointmentItemUser)).setText(data.get(position).hostFullName());
        return vi;
    }
}
