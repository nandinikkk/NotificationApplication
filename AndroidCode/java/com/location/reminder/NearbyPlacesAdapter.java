package com.location.reminder;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.location.reminder.model.Locationhistory;
import com.location.reminder.model.PreferenceReminder;
import com.location.reminder.util.ColorCodes;
import com.location.reminder.util.GenUtil;
import com.location.reminder.util.Placetypesfonticons;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class NearbyPlacesAdapter extends ArrayAdapter<Locationhistory> {


    private Activity activity;
    int layoutResourceId;
    private LayoutInflater inflater;
    Locationhistory b;
    ArrayList<Locationhistory> data;

    LatLng currentLocation;

    Typeface tf;
    ArrayList<String> colorslist;
    Map<String, String> fonticonsplacetypes;


    public NearbyPlacesAdapter(Activity activity, int layoutResourceId, ArrayList<Locationhistory> data, LatLng currentLocation, Typeface tf) {
        super(activity, layoutResourceId, data);

        this.activity = activity;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
        this.currentLocation = currentLocation;
        this.tf = tf;
        Placetypesfonticons placetypesfonticons = new Placetypesfonticons();
        fonticonsplacetypes = placetypesfonticons.getPlacetypesmap();
        ColorCodes colorCodes = new ColorCodes();
        colorslist = colorCodes.getcolorCodes();
    }

    public void openMap(String srclatitude, String srclongitude, String destlatitude, String destlongitude) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + srclatitude + "," + srclongitude + "&daddr=" + destlatitude + "," + destlongitude + ""));
        activity.startActivity(intent);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            inflater = ((Activity) activity).getLayoutInflater();

        }
        convertView = inflater.inflate(R.layout.place_row, parent, false);
        b = data.get(position);


        TextView preference = (TextView) convertView.findViewById(R.id.preference);
        preference.setText(GenUtil.capitalize(b.getPlacetypes().get(0).getPlacetype()));
        CircularTextView faicon = (CircularTextView) convertView.findViewById(R.id.faicon);
        faicon.setStrokeWidth(1);
        faicon.setStrokeColor("#ffffff");
        int colorindex = position;
        if (colorindex > colorslist.size() - 1) {
            colorindex = position % colorslist.size() - 1;
            if (colorindex < 0) {
                colorindex = 0;
            }
        }
        faicon.setSolidColor(colorslist.get(colorindex));
        faicon.setTypeface(tf);
        faicon.setText(Html.fromHtml(fonticonsplacetypes.get(b.getPlacetypes().get(0).getPlacetype())));
        //Picasso.with(activity).load(b.getImage()).into(thumbnail_image);


        System.out.println("Location details" + b.getLocationdetails());
        TextView recycle_title = (TextView) convertView.findViewById(R.id.recycle_title);
        recycle_title.setText(b.getLocationdetails());

        TextView reminder_location = (TextView) convertView.findViewById(R.id.reminder_location);
        reminder_location.setText(b.getCity() + " " + b.getState());

        TextView reminder_info = (TextView) convertView.findViewById(R.id.reminder_info);
        if (b.getPlacetypes() != null)
            reminder_info.setText(b.getCountry());

//        TextView rating = (TextView) convertView.findViewById(R.id.rating);
//        rating.setText("Rating : " + b.getRating());
//
//        TextView price_level = (TextView) convertView.findViewById(R.id.price_level);
//        price_level.setText("Price Level : " + b.getPrice_level());

//        TextView preferencerating = (TextView) convertView.findViewById(R.id.preferencerating);
//        preferencerating.setText("Preference Level : " + new DecimalFormat("#.##").format(b.getPreferencerating()));

        Button mapview = (Button) convertView.findViewById(R.id.mapview);

        mapview.setTag(b);

        mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locationhistory locationhistory = (Locationhistory) view.getTag();


                openMap("" + currentLocation.latitude, "" + currentLocation.longitude, "" + locationhistory.getLatitude(), "" + locationhistory.getLongitude());

            }
        });

        TextView textView = (TextView) convertView.findViewById(R.id.distance);
        textView.setText("Distance : " +b.getDistance()+" Meters");
        return convertView;
    }

}
