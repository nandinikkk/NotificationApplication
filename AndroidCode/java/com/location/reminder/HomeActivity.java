package com.location.reminder;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.location.reminder.com.location.reminder.restcalls.ReminderService;
import com.location.reminder.model.ReminderAdapter;
import com.location.reminder.model.ReminderModel;
import com.location.reminder.util.Constants;

import org.json.JSONArray;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private MultiSelector mMultiSelector = new MultiSelector();
    SharedPreferences sharedpreferences;

    private TabLayout mTabLayout;

    private int[] mTabsIcons = {
            R.drawable.home,
            R.drawable.profile,
            R.drawable.bell,
            R.drawable.adjust,
            R.drawable.ic_action_search
    };

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        needhomebutton = false;
        setTitle("Location based reminders");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        if (viewPager != null)
            viewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout =
                navigationView.getHeaderView(0);

        sharedpreferences = getSharedPreferences(Constants.sharedPreferences,
                Context.MODE_PRIVATE);

        TextView user_fullname = (TextView) headerLayout.findViewById(R.id.username);
        user_fullname.setText(sharedpreferences.getString("name", ""));
        TextView email = (TextView) headerLayout.findViewById(R.id.email);
        email.setText(sharedpreferences.getString("email", ""));

        if (!isMyServiceRunning(AppLocationService.class)) {


            startService(new Intent(this, AppLocationService.class));
        }

        if (getIntent().hasExtra("type") && getIntent().getStringExtra("type").equals("suggestions")) {
            viewPager.postDelayed(new Runnable() {

                @Override
                public void run() {
                    viewPager.setCurrentItem(1);
                }
            }, 100);
        }
    }

    public void create_new() {

        Intent intent = new Intent(this, CreateNewReminder.class);
        startActivity(intent);

    }

    DrawerLayout drawer;

    public void openDrawer(View view) {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_logout) {

            logout_user();
            return true;

        } else if (id == R.id.nav_reminder) {
            create_new();

        } else if (id == R.id.nav_generatecode) {

            Intent intent = new Intent(this, UniqueTokenActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_addcode) {

            Intent intent = new Intent(this, AddTokenActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_Groupmembers) {

            Intent intent = new Intent(this, ShowGroupsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_Groupmembers) {

            Intent intent = new Intent(this, ShowGroupsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_preferences) {

            Intent intent = new Intent(this, EnterPreferencesActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_showpreferences) {

            Intent intent = new Intent(this, NearbyPlacesActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_yourpreferences) {

            Intent intent = new Intent(this, YourPreferencesActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_suggestions) {

            Intent intent = new Intent(this, SuggestionsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 5;

        private final String[] mTabsTitle = {"Home", "Suggestions", "Notification", "More", "Search"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.custom_tab, null);

            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            return view;
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return RemindersFragment.newInstance(1);

                case 1:
                    return NearbyLocationsFragment.newInstance(2);
                case 2:
                    return NearbyLocationsFragment.newInstance(3);
                case 3:
                    return YourPreferencesFragment.newInstance(4);
                case 4:
                    return SearchLocationsFragment.newInstance(5);
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }
    }
}
