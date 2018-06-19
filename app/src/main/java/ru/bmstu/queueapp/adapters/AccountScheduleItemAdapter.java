package ru.bmstu.queueapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import ru.bmstu.queueapp.R;
import ru.bmstu.queueapp.http.data.Schedule;

public class AccountScheduleItemAdapter extends BaseAdapter {

    private ArrayList<Map.Entry<LocalDate, Schedule>> data;
    private LayoutInflater inflater;

    public AccountScheduleItemAdapter(Context context, HashMap<LocalDate, Schedule> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = new ArrayList<>(data.entrySet());
        Collections.sort(this.data, new Comparator<Map.Entry<LocalDate, Schedule>>() {
            @Override
            public int compare(Map.Entry<LocalDate, Schedule> o1, Map.Entry<LocalDate, Schedule> o2) {
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
            vi = inflater.inflate(R.layout.account_schedule_item_display, parent, false);
        }
        Map.Entry<LocalDate, Schedule> el = data.get(position);
        ((TextView) vi.findViewById(R.id.scheduleItemDate)).setText(el.getKey().toString());
        ((TextView) vi.findViewById(R.id.scheduleItemIntervals)).setText(el.getValue().timeIntervals());
        ((TextView) vi.findViewById(R.id.scheduleItemPlace)).setText(el.getValue().place);
        return vi;
    }
}