package de.habibhaidari.foodcart.ui.region;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.Objects;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.model.Region;
import de.habibhaidari.foodcart.service.RegionService;

import static de.habibhaidari.foodcart.ui.region.RegionListAdapter.REGION_ARGUMENT;

public class RegionActivity extends AppCompatActivity {

    RegionService regionService;
    ViewPager viewPager;

    RegionPagerAdapter regionPagerAdapter;
    Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ContentView
        setContentView(R.layout.activity_region);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //GetRegion
        region = new Gson().fromJson(getIntent().getStringExtra(REGION_ARGUMENT), Region.class);
        getSupportActionBar().setTitle("Region " + region.getId());

        //ViewPager
        viewPager = findViewById(R.id.view_pager);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //show Pager
        regionPagerAdapter = new RegionPagerAdapter(this, getSupportFragmentManager(), region);
        viewPager.setAdapter(regionPagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}