package de.habibhaidari.foodcart.ui.order.position;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Stream;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.model.Position;
import de.habibhaidari.foodcart.model.Variation;
import de.habibhaidari.foodcart.util.CurrencyUtils;

public class PositionAdapter extends ArrayAdapter<Position> {

    public PositionAdapter(Context context, List<Position> positions) {
        super(context, 0, positions);
    }


    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        Position position = getItem(pos);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_position, parent, false);
        }
        // Lookup view for data population
        TextView food = view.findViewById(R.id.food);
        TextView quantity = view.findViewById(R.id.quantity);
        TextView variant = view.findViewById(R.id.variant);
        TextView price = view.findViewById(R.id.price);

        // Populate the data into the template view using the data object
        quantity.setText(String.valueOf(position.getQuantity()).concat("x"));
        food.setText(position.getVariant().getFood().getNameWithNumber());


        String[] extrasVariations = Stream.concat(
                position.getVariant().getVariations().stream().map(Variation::getName),
                position.getExtras().stream().map(e -> "+" + e.getName())
        ).toArray(String[]::new);
        if (extrasVariations.length == 0) {
            variant.setVisibility(View.GONE);
        }
        variant.setText(String.join("\n", extrasVariations));


        price.setText(CurrencyUtils.getFormattedCurrency(position.getTotal()).concat(" €"));
        // Return the completed view to render on screen
        return view;
    }
}
