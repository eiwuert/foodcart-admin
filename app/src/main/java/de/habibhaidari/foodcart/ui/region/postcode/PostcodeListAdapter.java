package de.habibhaidari.foodcart.ui.region.postcode;

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
import de.habibhaidari.foodcart.model.Postcode;
import de.habibhaidari.foodcart.service.PostcodeService;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PostcodeListAdapter extends ArrayAdapter<Postcode> {


    final PostcodeService postcodeService;
    final List<Postcode> postcodes;

    public PostcodeListAdapter(Context context, List<Postcode> postcodes, PostcodeService postcodeService) {
        super(context, 0, postcodes);
        this.postcodes = postcodes;
        this.postcodeService = postcodeService;
    }


    @NonNull
    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        Postcode postcode = getItem(pos);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_model_single, parent, false);
        }

        TextView title = view.findViewById(R.id.time);
        ImageButton removeIcon = view.findViewById(R.id.remove_icon);


        DestroyCallback destroyCallback = new DestroyCallback() {
            @Override
            public void onDestroyed(Boolean destroyed, String identifier) {
                removeIcon.setVisibility(VISIBLE);
                postcodeService.removeDestroyCallbackListener(this);
            }
        };

        removeIcon.setOnClickListener(l -> {
            removeIcon.setVisibility(INVISIBLE);
            postcodeService.addDestroyCallbackListener(destroyCallback);
            postcodeService.destroy(postcode.getPostcode());
        });

        title.setText(postcode.getPostcode());
        return view;
    }

}
