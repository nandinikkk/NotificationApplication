package com.location.reminder;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.location.reminder.com.location.reminder.restcalls.java.UserpreferencesService;
import com.location.reminder.model.Preference;
import com.location.reminder.util.Placetypesfonticons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class YourPreferencesFragment extends BaseFragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    ListView listView1;
    ArrayList<Preference> preferences = new ArrayList<Preference>();
    private YourPreferenceAdapter adapter;


    public YourPreferencesFragment() {
        // Required empty public constructor
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        System.out.println("user visible hint" + isVisibleToUser + " " + adapter);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (adapter != null) {

                loadPreferences(sp.getString("userid", ""));

            }

        } else {
        }
    }

    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;

    public void loadPreferences(final String uid) {

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                preferences.clear();
                UserpreferencesService preferencesService = new UserpreferencesService();
                preferences.addAll(preferencesService.getPreferences(uid));

                System.out.println("PREFERENCES SIZE" + preferences.size());
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

    public static YourPreferencesFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        YourPreferencesFragment fragment = new YourPreferencesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);

        System.out.println("On create");
    }

    GridView gridview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("On create View");

        View view = inflater.inflate(R.layout.fragment_yourpreferences, container, false);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPageNo);
        gridview = (GridView) view.findViewById(R.id.gridview);

        adapter =
                new YourPreferenceAdapter(getActivity(), 0, preferences, sp.getString("userid", ""), getFontAwesomeTf());
        gridview.setAdapter(adapter);


        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
               final Preference preference = preferences.get(position);
                new MaterialDialog.Builder(getActivity())
                        .title("Delete Preference?")
                        .positiveText("Ok").negativeText("Cancel")
                        .items(new String[]{"Delete"})
                        .itemsCallbackSingleChoice(
                                0,
                                new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                        deletePreference(preference.getName(), sp.getString("userid", ""));
                                        return false;
                                    }
                                })
                        .show();

                return true;
            }
        });

        //loadPreferences(sp.getString("userid", ""));
        ImageView openplacetypes = (ImageView) view.findViewById(R.id.openplacetypes);
        openplacetypes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openPlacetypespopup();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openPlacetypespopup();
            }
        });


        loadPreferences(sp.getString("userid", ""));

        return view;
    }


    public void openPlacetypespopup() {


        Placetypesfonticons placetypesfonticons = new Placetypesfonticons();
        Map<String, String> placetypesmap = placetypesfonticons.getPlacetypesmap();
        List<String> keys = new ArrayList<>(placetypesmap.keySet());

        for (Preference preference : preferences) {
            keys.remove(preference.getName());
        }

        new MaterialDialog.Builder(getActivity())
                .title("Select Preference")
                .positiveText("Ok").negativeText("Cancel")
                .items(keys)
                .itemsCallbackSingleChoice(
                        0,
                        new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                addPreference(text.toString(), sp.getString("userid", ""));
                                return false;
                            }
                        })
                .show();
    }


    public void deletePreference(final String placetype, final String uid) {


        System.out.println("dd preference");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                UserpreferencesService preferencesService = new UserpreferencesService();
                preferencesService.deletePreferences(uid, placetype);
                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(getActivity(), "",
                        " Please wait...", true);
            }

            @Override
            protected void onPostExecute(Void result) {
                dialog.dismiss();
                loadPreferences(sp.getString("userid", ""));

            }
        }.execute(null, null, null);

    }

    public void addPreference(final String placetype, final String uid) {


        System.out.println("sdd preference");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                UserpreferencesService preferencesService = new UserpreferencesService();
                preferencesService.addPreferences(uid, placetype);
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
                loadPreferences(sp.getString("userid", ""));

            }
        }.execute(null, null, null);

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