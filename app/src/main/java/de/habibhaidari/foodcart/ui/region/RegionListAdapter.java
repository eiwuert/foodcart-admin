package de.habibhaidari.foodcart.ui.region;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.callback.model.DestroyCallback;
import de.habibhaidari.foodcart.model.Region;
import de.habibhaidari.foodcart.service.RegionService;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class
RegionListAdapter extends ArrayAdapter<Region> {


    public static final String COMMA_SEPARATION = ", ";
    public static final String REGION_ARGUMENT = "region";

    final RegionService regionService;

    public RegionListAdapter(Context context, List<Region> regions, RegionService service) {
        super(context, 0, regions);
        this.regionService = service;
    }


    @NonNull
    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        Region region = getItem(pos);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_model, parent, false);
        }
        // Lookup view for data population
        TextView title = view.findViewById(R.id.time);
        TextView postcodes = view.findViewById(R.id.subtitle);
        ImageButton removeIcon = view.findViewById(R.id.remove_icon);

        DestroyCallback destroyCallback = new DestroyCallback() {
            @Override
            public void onDestroyed(Boolean destroyed, String identifier) {
                removeIcon.setVisibility(VISIBLE);
                regionService.removeDestroyCallbackListener(this);
            }
        };

        removeIcon.setOnClickListener(l -> {
            removeIcon.setVisibility(INVISIBLE);
            regionService.addDestroyCallbackListener(destroyCallback);
            regionService.destroy(String.valueOf(region.getId()));
        });

        view.setOnClickListener(l -> {
            Intent i = new Intent(getContext(), RegionActivity.class);
            Bundle b = new Bundle();
            i.putExtra(REGION_ARGUMENT, new Gson().toJson(region));
            getContext().startActivity(i);
        });

        // Populate the data into the template view using the data object
        title.setText(String.format("Region %d", region.getId()));
        if (region.getPostcodes() != null) {
            postcodes.setText(
                    region.getPostcodes()
                            .stream()
                            .map(e -> String.valueOf(e.getPostcode()))
                            .collect(Collectors.joining(COMMA_SEPARATION)));
        }

        return view;
    }
}