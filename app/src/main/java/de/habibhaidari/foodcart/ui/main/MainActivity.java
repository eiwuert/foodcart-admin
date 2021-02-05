package de.habibhaidari.foodcart.ui.main;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.ReportActivity;
import de.habibhaidari.foodcart.service.OrderBackgroundService;
import de.habibhaidari.foodcart.ui.onboarding.OnboardingActivity;
import de.habibhaidari.foodcart.ui.openinghour.OpeningHourListActivity;
import de.habibhaidari.foodcart.ui.region.RegionListActivity;
import de.habibhaidari.foodcart.ui.setting.SettingActivity;

import static de.habibhaidari.foodcart.constant.ApplicationConstants.APPLICATION_NAME;

public class MainActivity extends AppCompatActivity {

    public static final String ONBOARDING_COMPLETE_KEY = "onboarding_complete";

    OrderBackgroundService orderBackgroundService;
    Intent serviceIntent;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            OrderBackgroundService.LocalBinder binder = (OrderBackgroundService.LocalBinder) service;
            orderBackgroundService = binder.getServiceInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };


    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(APPLICATION_NAME, MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(ONBOARDING_COMPLETE_KEY, false)) {
            startActivity(new Intent(this, OnboardingActivity.class));
        } else {
            serviceIntent = new Intent(this, OrderBackgroundService.class);
            startService(serviceIntent);
            bindService(serviceIntent, serviceConnection, Context.MODE_PRIVATE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        OrderListPagerAdapter orderListPagerAdapter = new OrderListPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(orderListPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        if (item.getItemId() == R.id.open_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        if (item.getItemId() == R.id.opening_hours) {
            startActivity(new Intent(this, OpeningHourListActivity.class));
            return true;
        }

        if (item.getItemId() == R.id.open_report) {
            startActivity(new Intent(this, ReportActivity.class));
            return true;
        }

        if (item.getItemId() == R.id.delivery_costs) {
            startActivity(new Intent(this, RegionListActivity.class));
            return true;
        }

        if (item.getItemId() == R.id.close_application) {
            orderBackgroundService.stopService();
            stopService(serviceIntent);
            finishAndRemoveTask();
        }

        return super.onOptionsItemSelected(item);

    }


}