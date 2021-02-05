package de.habibhaidari.foodcart.ui.region.rate;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.model.Rate;
import de.habibhaidari.foodcart.service.RateService;
import de.habibhaidari.foodcart.util.CurrencyUtils;

public class NewRateDialogFragment extends DialogFragment {

    private RateService rateService;
    private int regionId;

    public static NewRateDialogFragment newInstance(RateService rateService, int regionId) {
        NewRateDialogFragment f = new NewRateDialogFragment();
        f.setRateService(rateService);
        f.setRegionId(regionId);
        return f;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public void setRateService(RateService rateService) {
        this.rateService = rateService;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View li = inflater.inflate(R.layout.dialog_new_rate, null);

        EditText textMinimum = li.findViewById(R.id.minimum);
        EditText textCosts = li.findViewById(R.id.costs);

        builder.setView(li)
                .setPositiveButton(R.string.add, (dialog, id) -> {
                    Rate rate = new Rate();
                    rate.setMinimum(CurrencyUtils.parseFormattedCurrency(textMinimum.getText().toString()));
                    rate.setCosts(CurrencyUtils.parseFormattedCurrency(textCosts.getText().toString()));
                    rate.setRegionId(regionId);
                    rateService.store(rate);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> NewRateDialogFragment.this.getDialog().cancel());


        return builder.create();
    }

}
