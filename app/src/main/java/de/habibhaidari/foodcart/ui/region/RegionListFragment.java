package de.habibhaidari.foodcart.ui.region;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.callback.model.DestroyCallback;
import de.habibhaidari.foodcart.callback.model.IndexCallback;
import de.habibhaidari.foodcart.callback.model.StoreCallback;
import de.habibhaidari.foodcart.model.Region;
import de.habibhaidari.foodcart.service.RegionService;

public class RegionListFragment extends Fragment implements StoreCallback<Region>, DestroyCallback, IndexCallback<Region> {

    public static final String REGIONS_KEY = "regions";
    public static final String EMPTY_JS_ARRAY = "[]";

    final List<Region> regionList = new ArrayList<>();

    RegionService regionService;

    ProgressBar progressBar;
    FloatingActionButton fab;
    ListView regionsListView;
    RegionListAdapter regionListAdapter;


    public RegionListFragment() {

    }

    public static RegionListFragment newInstance() {
        Bundle args = new Bundle();
        RegionListFragment fragment = new RegionListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regionService = new RegionService(getContext());
        regionService.addDestroyCallbackListener(this);
        regionService.addStoreCallbackListener(this);
        regionService.addIndexCallbackListener(this);
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_model_list, container, false);

        regionsListView = root.findViewById(R.id.list_view);
        progressBar = root.findViewById(R.id.progress_circular);

        fab = root.findViewById(R.id.add_button);
        fab.setOnClickListener(l -> regionService.store(new Region()));

        regionListAdapter = new RegionListAdapter(getContext(), regionList, regionService);
        regionsListView.setAdapter(regionListAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        regionList.clear();
        regionListAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        regionService.index(null);
    }

    @Override
    public void onStored(Region region) {
        regionList.add(region);
        Toast.makeText(getContext(), "Region wurde hinzugefügt", Toast.LENGTH_LONG).show();
        regionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyed(Boolean destroyed, String identifier) {
        if (destroyed) {
            Toast.makeText(getContext(), "Region wurde entfernt", Toast.LENGTH_LONG).show();
            regionList.removeIf(e -> Integer.parseInt(identifier) == e.getId());
            regionListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onIndexed(List<Region> index) {
        progressBar.setVisibility(View.GONE);
        regionList.addAll(index);
        regionListAdapter.notifyDataSetChanged();
    }


}
