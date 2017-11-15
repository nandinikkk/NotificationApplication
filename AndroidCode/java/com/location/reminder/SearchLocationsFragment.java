package com.location.reminder;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.location.reminder.com.location.reminder.restcalls.java.NearbyLocationsService;
import com.location.reminder.model.Locationhistory;
import com.location.reminder.util.Constants;
import com.location.reminder.util.Placetypesfonticons;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchLocationsFragment
        extends BaseFragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    ArrayList<Locationhistory> preferenceReminders = new ArrayList<Locationhistory>();
    private NearbyPlacesAdapter adapter;
    ListView listView1;


    public SearchLocationsFragment() {
        // Required empty public constructor
    }


    public static SearchLocationsFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        SearchLocationsFragment fragment = new SearchLocationsFragment();
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

    public void loadnearbyplaces(final String uid, final String lat, final String lng, final String querystring) {

        final String querytype = "searchplaces";


        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                Placetypesfonticons placetypesfonticons = new Placetypesfonticons();
                Map<String, String> placetypes = placetypesfonticons.getPlacetypesmap();
                List<String> keys = new ArrayList<>(placetypes.keySet());
                String[] queries = querystring.split("\\s+");
                ArrayList<String> placetypeslist = new ArrayList<>();


                for (String query : queries) {

                    for (String key : keys) {
                        if (key.contains(query.trim().toLowerCase())) {
                            if (!placetypeslist.contains(key))
                                placetypeslist.add(key);
                            break;
                        }
                    }

                }


                preferenceReminders.clear();

                NearbyLocationsService nearbyLocationsService = new NearbyLocationsService();
                nearbyLocationsService.querystring = android.text.TextUtils.join(",", placetypeslist);
                preferenceReminders.addAll(nearbyLocationsService.getNearbyLocations(uid, lat, lng, querytype, sp.getInt("maxdistance", Constants.DEFAULT_PREFLOC_DISTANCE)));

                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(getActivity(), "",
                        " Please wait...", true, true);

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

            }

        } else {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_searchlocations, container, false);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPageNo);
        GPSLocator locator = new GPSLocator(getActivity());
        locator.getMyLocation();
        double latitude = locator.getLatitude();
        double longitude = locator.getLongitude();
        LatLng currentLocation = new LatLng(latitude, longitude);
        listView1 = (ListView) view.findViewById(R.id.listView);

        MaterialSearchView searchView = (MaterialSearchView) view.findViewById(R.id.search_view);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search_menu);

        toolbar.setTitle("Search food,cafe etc..");
        searchView.setVoiceSearch(true);
        MenuItem item = toolbar.getMenu().findItem(R.id.action_search);
        searchView.setMenuItem(item);


        Placetypesfonticons placetypesfonticons = new Placetypesfonticons();
        Map<String, String> placetypes = placetypesfonticons.getPlacetypesmap();
        Set<String> keys = placetypes.keySet();

        String[] placetypeslist = keys.toArray(new String[keys.size()]);


        System.out.println("place types list" + placetypeslist.length);
        searchView.setSuggestions(placetypeslist);


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic

                System.out.println("query text" + query);
                GPSLocator locator = new GPSLocator(getActivity());
                locator.getMyLocation();
                double latitude = locator.getLatitude();
                double longitude = locator.getLongitude();

                loadnearbyplaces(sp.getString("userid", ""), "" + latitude, "" + longitude, query);
                return false;


            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        adapter =

                new NearbyPlacesAdapter(getActivity(), 0, preferenceReminders, currentLocation, getFontAwesomeTf());
        listView1.setAdapter(adapter);

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

