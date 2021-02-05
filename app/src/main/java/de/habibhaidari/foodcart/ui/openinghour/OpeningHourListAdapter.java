package de.habibhaidari.foodcart.ui.openinghour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.callback.model.DestroyCallback;
import de.habibhaidari.foodcart.model.OpeningHour;
import de.habibhaidari.foodcart.service.OpeningHourService;

import static android.view.View.VISIBLE;
import static de.habibhaidari.foodcart.constant.FormatConstants.FROM_TO_TIME_FORMAT;

public class OpeningHourListAdapter extends ArrayAdapter<OpeningHour> {


    final OpeningHourService openingHourService;

    public OpeningHourListAdapter(Context context, List<OpeningHour> openingHours, OpeningHourService service) {
        super(context, 0, openingHours);
        this.openingHourService = service;
    }


    @NonNull
    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        OpeningHour openingHour = getItem(pos);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_model, parent, false);
        }
        // Lookup view for data population
        TextView time = view.findViewById(R.id.time);
        TextView weekday = view.findViewById(R.id.subtitle);
        ImageButton removeIcon = view.findViewById(R.id.remove_icon);


        DestroyCallback destroyCallback = new DestroyCallback() {
            @Override
            public void onDestroyed(Boolean destroyed, String identifier) {
                removeIcon.setVisibility(VISIBLE);
                openingHourService.removeDestroyCallbackListener(this);
            }
        };

        removeIcon.setOnClickListener(l -> {
            removeIcon.setVisibility(View.INVISIBLE);
            openingHourService.addDestroyCallbackListener(destroyCallback);
            openingHourService.destroy(String.valueOf(openingHour.getId()));
        });

        // Populate the data into the template view using the data object
        time.setText(String.format(FROM_TO_TIME_FORMAT, openingHour.getFromFormatted(), openingHour.getToFormatted()));

        weekday.setText(openingHour.getWeekdayFormatted());

        return view;
    }

}
