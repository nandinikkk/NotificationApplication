package com.location.reminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.location.reminder.com.location.reminder.restcalls.GroupsService;
import com.location.reminder.model.Groups;
import com.location.reminder.model.Preference;
import com.location.reminder.util.ColorCodes;
import com.location.reminder.util.GenUtil;
import com.location.reminder.util.Placetypesfonticons;

import java.util.ArrayList;
import java.util.Map;

public class YourPreferenceAdapter extends ArrayAdapter<Preference> {

    private Activity activity;
    int layoutResourceId;
    private LayoutInflater inflater;
    Preference b;
    ArrayList<Preference> data;
    String uid;
    Typeface tf;

    ArrayList<String> colorslist;

    Map<String, String> fonticonsplacetypes;

    public YourPreferenceAdapter(Activity activity, int layoutResourceId, ArrayList<Preference> data, String uid, Typeface tf) {
        super(activity, layoutResourceId, data);

        this.activity = activity;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
        this.uid = uid;
        this.tf = tf;

        Placetypesfonticons placetypesfonticons = new Placetypesfonticons();
        fonticonsplacetypes = placetypesfonticons.getPlacetypesmap();


        ColorCodes colorCodes = new ColorCodes();
        colorslist = colorCodes.getcolorCodes();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = ((Activity) activity).getLayoutInflater();

        }
        convertView = inflater.inflate(R.layout.preference_row, parent, false);
        b = data.get(position);
        TextView preference = (TextView) convertView.findViewById(R.id.preference);
        preference.setText(GenUtil.capitalize(b.getName()));
        CircularTextView faicon = (CircularTextView) convertView.findViewById(R.id.faicon);
        faicon.setStrokeWidth(1);
        faicon.setStrokeColor("#ffffff");
        int colorindex = position;
        if (colorindex > colorslist.size() - 1) {
            colorindex = position % colorslist.size()-1;
            if(colorindex<0){
                colorindex=0;
            }
        }
        faicon.setSolidColor(colorslist.get(colorindex));
        faicon.setTypeface(tf);
        faicon.setText(Html.fromHtml(fonticonsplacetypes.get(b.getName())));

        return convertView;

    }


}
