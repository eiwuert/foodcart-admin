package de.habibhaidari.foodcart.ui.region.rate;

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
import de.habibhaidari.foodcart.model.Rate;
import de.habibhaidari.foodcart.service.RateService;
import de.habibhaidari.foodcart.util.CurrencyUtils;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static de.habibhaidari.foodcart.constant.FormatConstants.MINIMUM_SUBTOTAL_FORMAT;

public class RateListAdapter extends ArrayAdapter<Rate> {


    final RateService rateService;

    public RateListAdapter(Context context, List<Rate> rates, RateService rateService) {
        super(context, 0, rates);
        this.rateService = rateService;
    }


    @NonNull
    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        Rate rate = getItem(pos);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_model, parent, false);
        }

        TextView title = view.findViewById(R.id.time);
        TextView subtitle = view.findViewById(R.id.subtitle);
        ImageButton removeIcon = view.findViewById(R.id.remove_icon);

        DestroyCallback destroyCallback = new DestroyCallback() {
            @Override
            public void onDestroyed(Boolean destroyed, String identifier) {
                removeIcon.setVisibility(VISIBLE);
                rateService.removeDestroyCallbackListener(this);
            }
        };

        removeIcon.setOnClickListener(l -> {
            removeIcon.setVisibility(INVISIBLE);
            rateService.addDestroyCallbackListener(destroyCallback);
            rateService.destroy(String.valueOf(rate.getId()));
        });

        title.setText(String.format("%s € Lieferkosten", CurrencyUtils.getFormattedCurrency(rate.getCosts())));
        subtitle.setText(String.format(MINIMUM_SUBTOTAL_FORMAT, CurrencyUtils.getFormattedCurrency(rate.getMinimum())));
        return view;
    }

}
