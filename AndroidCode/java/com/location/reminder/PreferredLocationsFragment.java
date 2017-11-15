package com.location.reminder;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.location.reminder.com.location.reminder.restcalls.java.NearbyLocationsService;
import com.location.reminder.model.Locationhistory;
import com.location.reminder.util.Constants;

import java.util.ArrayList;

public class PreferredLocationsFragment
        extends BaseFragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    ArrayList<Locationhistory> preferenceReminders = new ArrayList<Locationhistory>();
    private NearbyPlacesAdapter adapter;
    ListView listView1;


    public PreferredLocationsFragment() {
        // Required empty public constructor
    }

    public static PreferredLocationsFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        PreferredLocationsFragment fragment = new PreferredLocationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);


    }

    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;

    public void loadnearbyplaces(final String uid, final String lat, final String lng) {

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {


                preferenceReminders.clear();
                NearbyLocationsService nearbyLocationsService = new NearbyLocationsService();
                preferenceReminders.addAll(nearbyLocationsService.getNearbyLocations(uid, lat, lng,"",sp.getInt("maxdistance", Constants.DEFAULT_PREFLOC_DISTANCE)));

                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(getActivity(), "",
                        " Please wait...", true,true);

            }

            @Override
            protected void onPostExecute(Void result) {
                dialog.dismiss();

                adapter.notifyDataSetChanged();
            }
        };
        task.execute(null, null, null);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (adapter != null) {
                GPSLocator locator = new GPSLocator(getActivity());
                locator.getMyLocation();
                double latitude = locator.getLatitude();
                double longitude = locator.getLongitude();
                loadnearbyplaces(sp.getString("userid", ""), "" + latitude, "" + longitude);
            }

        } else {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearbylocations, container, false);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPageNo);
        GPSLocator locator = new GPSLocator(getActivity());
        locator.getMyLocation();
        double latitude = locator.getLatitude();
        double longitude = locator.getLongitude();
        LatLng currentLocation = new LatLng(latitude, longitude);
        listView1 = (ListView) view.findViewById(R.id.listView);

        adapter =

                new NearbyPlacesAdapter(getActivity(), 0, preferenceReminders, currentLocation, getFontAwesomeTf());
        listView1.setAdapter(adapter);

        // loadnearbyplaces(sp.getString("userid", ""), "" + latitude, "" + longitude);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
