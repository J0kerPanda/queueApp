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
import ru.bmstu.queueapp.http.data.RepeatedSchedule;
import ru.bmstu.queueapp.http.data.Schedule;

public class AccountRepeatedScheduleItemAdapter extends BaseAdapter {

    private ArrayList<Map.Entry<LocalDate, RepeatedSchedule>> data;
    private LayoutInflater inflater;

    public AccountRepeatedScheduleItemAdapter(Context context, HashMap<LocalDate, RepeatedSchedule> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = new ArrayList<>(data.entrySet());
        Collections.sort(this.data, new Comparator<Map.Entry<LocalDate, RepeatedSchedule>>() {
            @Override
            public int compare(Map.Entry<LocalDate, RepeatedSchedule> o1, Map.Entry<LocalDate, RepeatedSchedule> o2) {
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
            vi = inflater.inflate(R.layout.account_repeated_schedule_item_display, parent, false);
        }
        Map.Entry<LocalDate, RepeatedSchedule> el = data.get(position);
        ((TextView) vi.findViewById(R.id.repeatedScheduleItemDate)).setText(el.getKey().toString());
        ((TextView) vi.findViewById(R.id.repeatedScheduleItemPlace)).setText(el.getValue().place);
        return vi;
    }
}
