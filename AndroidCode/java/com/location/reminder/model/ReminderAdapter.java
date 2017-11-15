package com.location.reminder.model;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.location.reminder.R;
import com.location.reminder.ReminderNearbyPlacesActivity;
import com.location.reminder.TextView_Lato;
import com.location.reminder.UserRegistrationPendingActivity;
import com.location.reminder.com.location.reminder.restcalls.GroupsService;
import com.location.reminder.com.location.reminder.restcalls.ReminderService;

import org.w3c.dom.Text;

public class ReminderAdapter extends ArrayAdapter<ReminderModel> {
    private ArrayList<ReminderModel> mItems;

    Activity activity;

    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    private String uid;
    SimpleDateFormat sm = new SimpleDateFormat("MM-dd-yyyy");


    public ReminderAdapter(Activity activity, int layoutResourceId, ArrayList<ReminderModel> mItems, String uid) {
        super(activity, layoutResourceId, mItems);

        this.activity = activity;
        this.mItems = mItems;
        this.uid = uid;


    }


    private LayoutInflater inflater;


    // View holder for recycler view items
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = ((Activity) activity).getLayoutInflater();

        }
        View itemView = inflater.inflate(R.layout.remind_items, parent, false);
        TextView remindertitle, dateandtimetext, reminderinformation, mactive_status, assignedto;
        TextView daterange;

        remindertitle = (TextView) itemView.findViewById(R.id.recycle_title);
        dateandtimetext = (TextView) itemView.findViewById(R.id.recycle_date_time);
        reminderinformation = (TextView) itemView.findViewById(R.id.recycle_repeat_info);
        mactive_status = (TextView) itemView.findViewById(R.id.active_status);
        assignedto = (TextView) itemView.findViewById(R.id.assignedto);
        daterange = (TextView) itemView.findViewById(R.id.daterange);


        ReminderModel item = mItems.get(position);
        remindertitle.setText(item.getTitle());

        if (item.getFromtime() != 0 && item.getTotime() != 0) {

            int hours = item.getFromtime() / 60;
            int minutes = item.getFromtime() % 60;


            String fromtime = hours + " : " + String.format("%02d", minutes);

            hours = item.getTotime() / 60;
            minutes = item.getTotime() % 60;
            String endtime = hours + " : " + String.format("%02d", minutes);

            dateandtimetext.setText("" + fromtime + " to: " + endtime);
        }

        if (item.getFromdate() != 0 && item.getTodate() != 0) {
            Date date = new Date();
            date.setTime((long) item.getFromdate() * 1000);

            String startdate = sm.format(date);

            date = new Date();
            date.setTime((long) item.getTodate() * 1000);
            String enddate = sm.format(date);
            daterange.setVisibility(View.VISIBLE);
            daterange.setText("Start date : " + startdate + " End date  :" + enddate);

        } else {
            daterange.setVisibility(View.GONE);

        }

        setReminderRepeatInfo(item.getRepeat(), item.getRepeatinterval(), reminderinformation);

        View reminderstatus_circle = itemView.findViewById(R.id.reminderstatus_circle);
        View leftbar = itemView.findViewById(R.id.leftbar);


        setStatus(item.getStatus(), mactive_status,reminderstatus_circle,leftbar);

        TextView reminder_location = (TextView) itemView.findViewById(R.id.reminder_location);

        reminder_location.setText("Reminder info" + item.getReminderinfo());


        TextView reminder_info = (TextView) itemView.findViewById(R.id.reminder_info);
        reminder_info.setText("Latitude : " + item.getLatitude() + "\n Longitude : " + item.getLongitude());

        TextView createdby = (TextView) itemView.findViewById(R.id.createdby);
        createdby.setText("Created by : " + item.getRemindercreatedby());

        final TextView_Lato sharereminder = (TextView_Lato) itemView.findViewById(R.id.sharereminder);
        sharereminder.setTag(item);
        sharereminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReminderModel tag = ((ReminderModel) view.getTag());

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Reminder : " + tag.getTitle() + "\nReminder information : " + tag.getReminderinfo() + "\n Reminder assigned to : " + tag.getAssignedto() + "\nReminder created by" + tag.getRemindercreatedby()
                        + "\n";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share token");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));


            }
        });

        final TextView_Lato delete = (TextView_Lato) itemView.findViewById(R.id.delete);
        delete.setTag(item);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(activity)
                        .setTitle("Delete Remidner")
                        .setMessage("Are you sure you want to delete this reminder?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int reminderid = ((ReminderModel) delete.getTag()).getId();
                                deleteReminder("" + reminderid, (ReminderModel) delete.getTag());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        final TextView_Lato nearbyplaces = (TextView_Lato) itemView.findViewById(R.id.nearbyplaces);
        nearbyplaces.setTag(item);
        nearbyplaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ReminderNearbyPlacesActivity.class);
                int reminderid = ((ReminderModel) view.getTag()).getId();
                intent.putExtra("reminderid", reminderid);
                activity.startActivity(intent);
            }
        });


        final TextView_Lato assignusers = (TextView_Lato) itemView.findViewById(R.id.assignusers);
        assignusers.setTag(item);

        assignusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int reminderid = ((ReminderModel) view.getTag()).getId();

                showusersdialog(reminderid + "");

            }
        });
        assignedto.setText("Assigned to : " + item.getAssignedto());
        return itemView;
    }


    public void setReminderDateTime(String datetime, TextView dateandtimetext) {
        dateandtimetext.setText(datetime);
    }

    public void setReminderRepeatInfo(int repeat, int repeatNo, TextView reminderinformation) {
        if (repeat == 1) {
            reminderinformation.setText("Repeats every " + repeatNo + " day(s)");
        } else {
            reminderinformation.setText("No Repeat");
        }
    }

    // Set active image as on or off
    public void setStatus(String status, TextView mactive_status,View reminderstatus_circle,View leftbar) {



        if (status.equals("0")) {
            mactive_status.setText("Pending".toUpperCase());
            mactive_status.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            reminderstatus_circle.setBackgroundResource(R.drawable.orange_circle );
            leftbar.setBackgroundResource(R.drawable.orange_circle );


        } else {
            mactive_status.setText("Reminded".toUpperCase());
            mactive_status.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            reminderstatus_circle.setBackgroundResource(R.drawable.gree_circle );
            leftbar.setBackgroundResource(R.drawable.gree_circle );



        }
    }

    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;
    AsyncTask<Void, Void, Void> mRegisterTask;
    final ArrayList<Integer> selList = new ArrayList();
    static String[] classes;

    static String[] users;

    public void deleteReminder(final String reminderid, final ReminderModel reminderModel) {

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {


                ReminderService reminderService = new ReminderService();

                if (reminderModel.getReminderby().equals(uid)) {
                    reminderService.deleteReminder(reminderid);
                } else {

                    reminderService.removeuserfromreminder(reminderid, uid);
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(activity, "",
                        " Please wait...", true,true);

            }

            @Override
            protected void onPostExecute(Void result) {
                dialog.dismiss();

                mItems.remove(reminderModel);

                notifyDataSetChanged();
            }
        };
        task.execute(null, null, null);

    }

    public ArrayList<Groups> groupslist = new ArrayList<Groups>();
    boolean[] checkedValues;
    AlertDialog.Builder d;


    public void showusersdialog(final String reminderid) {
        d = new AlertDialog.Builder(activity);
        d.setTitle("Choose Users");

        mRegisterTask = new AsyncTask<Void, Void, Void>() {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog =
                        ProgressDialog.show(activity, "Loading.", "Loading users", true,true);

            }

            @Override
            protected Void doInBackground(Void... params) {

                GroupsService g = new GroupsService();
                groupslist = g.getGroupMember(uid);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                mRegisterTask = null;

                List<String> temp = new ArrayList<String>();

                users = new String[groupslist.size()];

                checkedValues = new boolean[groupslist.size()];

                // ArrayList<String> presentgroups = mNavigationDrawerFragment.checkedValues;
                ArrayList<String> presentgroups = new ArrayList<>();
                int i = 0;

                for (Groups dto : groupslist) {

                    temp.add(dto.getName());
                    if (presentgroups.contains(dto.getId())) {

                        checkedValues[i] = true;
                        selList.add(groupslist.get(i).getId());

                    }

                    i++;
                }


                classes = temp.toArray(new String[temp.size()]);


                d.setMultiChoiceItems(classes, checkedValues, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // if (isChecked) {

                        if (isChecked) {

                            // If user select a item then add it in selected items
                            selList.add(groupslist.get(which).getId());
                        } else {
                            // if the item is already selected then remove it
                            if (selList.contains(groupslist.get(which).getId()))
                                selList.remove(Integer.valueOf(groupslist.get(which).getId()));
                        }
                        // }
                    }
                });


                d.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {

                                GroupsService dao =
                                        new GroupsService();
                                for (int i = 0; i < selList.size(); i++) {

                                    System.out.println(selList.get(i));
                                }
                                dao.updateGroups(reminderid, selList);
                                return null;
                            }


                            @Override
                            protected void onPostExecute(Void result) {
                                ///   dialog.dismiss();

                                Toast.makeText(activity, "Made Changes, Click refresh for update", Toast.LENGTH_SHORT).show();

                            }

                        };
                        task.execute(null, null, null);

                    }
                });

                d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                progressDialog.dismiss();

                d.show();


            }

        };
        mRegisterTask.execute(null, null, null);
    }

}
