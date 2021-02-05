package de.habibhaidari.foodcart.ui.openinghour;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.model.OpeningHour;
import de.habibhaidari.foodcart.service.OpeningHourService;

import static de.habibhaidari.foodcart.constant.FormatConstants.WEEKDAYS_NAMES;
import static de.habibhaidari.foodcart.util.TimeUtils.parseTime;

public class NewOpeningHourDialogFragment extends DialogFragment {

    private OpeningHourService openingHourService;

    public static NewOpeningHourDialogFragment newInstance(OpeningHourService openingHourService) {
        NewOpeningHourDialogFragment f = new NewOpeningHourDialogFragment();
        f.setOpeningHourService(openingHourService);
        return f;
    }

    public void setOpeningHourService(OpeningHourService openingHourService) {
        this.openingHourService = openingHourService;
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
        View li = inflater.inflate(R.layout.dialog_new_opening_hour, null);


        Spinner spinner = li.findViewById(R.id.weekdaySpinner);
        EditText textFrom = li.findViewById(R.id.timeFrom);
        EditText textTo = li.findViewById(R.id.timeTo);

        spinner.setAdapter(new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_spinner_dropdown_item, WEEKDAYS_NAMES));
        builder.setView(li)
                .setPositiveButton(R.string.add, (dialog, id) -> {
                    try {
                        OpeningHour oh = new OpeningHour();
                        oh.setN(spinner.getSelectedItemPosition());
                        oh.setFrom(parseTime(textFrom.getText().toString()));
                        oh.setTo(parseTime(textTo.getText().toString()));
                        openingHourService.store(oh);
                    } catch (NumberFormatException e) {
                        Log.d(NewOpeningHourDialogFragment.class.getName(), e.toString());
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> NewOpeningHourDialogFragment.this.getDialog().cancel());


        return builder.create();
    }


}
