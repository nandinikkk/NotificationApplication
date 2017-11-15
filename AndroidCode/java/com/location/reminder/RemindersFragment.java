package com.location.reminder;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.location.reminder.com.location.reminder.restcalls.ReminderService;
import com.location.reminder.model.ReminderAdapter;
import com.location.reminder.model.ReminderModel;
import com.location.reminder.util.Constants;

import java.util.ArrayList;

public class RemindersFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    private ListView mList;


    private ReminderAdapter mAdapter;
    ArrayList<ReminderModel> mItems = new ArrayList<ReminderModel>();

    SharedPreferences sharedpreferences;


    public RemindersFragment() {
        // Required empty public constructor
    }

    public static RemindersFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        RemindersFragment fragment = new RemindersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mAdapter != null) {
                new LoadRemindersTask(getActivity()).execute(null, null, null);
            }

        } else {

        }
    }

    TextView reminder_pending;
    TextView reminder_title;

    public void setTitleHeaders() {


        if (mItems.size() == 0) {
            reminder_pending.setText("No reminders Created");
            reminder_title.setText("Create New");
        } else {
            boolean containspendingreminders = false;
            String title = "";
            for (ReminderModel reminderModel : mItems) {

                if (reminderModel.getStatus().equals("0")) {
                    containspendingreminders = true;
                    title = reminderModel.getTitle();
                    break;
                }
            }

            if (containspendingreminders) {
                reminder_pending.setText("you have pending reminder");
                reminder_title.setText(title);
            } else {
                reminder_pending.setText("All reminders Created");
                reminder_title.setText("Create New");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPageNo);

        reminder_pending = (TextView) view.findViewById(R.id.reminder_pending);
        reminder_title = (TextView) view.findViewById(R.id.reminder_title);

        mList = (ListView) view.findViewById(R.id.reminder_list);

        sharedpreferences = getActivity().getSharedPreferences(Constants.sharedPreferences,
                Context.MODE_PRIVATE);
        mAdapter = new ReminderAdapter(getActivity(), 0, mItems, sharedpreferences.getString("userid", ""));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadRemindersTask(getActivity()).execute(null, null, null);

            }
        });

        new LoadRemindersTask(getActivity()).execute(null, null, null);


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

    private class LoadRemindersTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        ProgressDialog progressdialog;

        public LoadRemindersTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            progressdialog = ProgressDialog.show(activity, "",
                    "Please wait...", true,true);
            mItems.clear();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ReminderService reminderService = new ReminderService();

            ArrayList<ReminderModel> newitems = reminderService.getReminders(sharedpreferences.getString("userid", ""));

            System.out.println("Number of reminders" + newitems.size());
            if (newitems != null) {

                mItems.addAll(newitems);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressdialog.dismiss();
            mAdapter.notifyDataSetChanged();
            mList.setAdapter(mAdapter);
            setTitleHeaders();
        }

    }

}