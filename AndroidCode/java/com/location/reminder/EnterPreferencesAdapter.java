package com.location.reminder;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.location.reminder.model.Preference;

import java.util.ArrayList;

public class EnterPreferencesAdapter extends ArrayAdapter<Preference> {

    public ArrayList<Preference> PreferenceList;
    Context context;


    public EnterPreferencesAdapter(Context context, int textViewResourceId,
                                   ArrayList<Preference> PreferenceList) {

        super(context, textViewResourceId, PreferenceList);
        this.context = context;
        this.PreferenceList = new ArrayList<Preference>();
        this.PreferenceList.addAll(PreferenceList);
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.preference_info, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);

            holder.name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Preference Preference = (Preference) cb.getTag();

                    Preference.setSelected(cb.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Preference Preference = PreferenceList.get(position);
        //  holder.code.setText(" (" + Preference.getCode() + ")");
        holder.name.setText(Preference.getName());
        holder.name.setChecked(Preference.isSelected());
        holder.name.setTag(Preference);

        return convertView;

    }

}




