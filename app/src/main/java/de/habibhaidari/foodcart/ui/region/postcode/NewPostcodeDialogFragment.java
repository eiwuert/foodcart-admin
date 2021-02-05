package de.habibhaidari.foodcart.ui.region.postcode;

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
import de.habibhaidari.foodcart.model.Postcode;
import de.habibhaidari.foodcart.service.PostcodeService;

public class NewPostcodeDialogFragment extends DialogFragment {

    private PostcodeService postcodeService;
    private int regionId;

    public static NewPostcodeDialogFragment newInstance(PostcodeService postcodeService, int regionId) {
        NewPostcodeDialogFragment f = new NewPostcodeDialogFragment();
        f.setPostcodeService(postcodeService);
        f.setRegionId(regionId);
        return f;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public void setPostcodeService(PostcodeService postcodeService) {
        this.postcodeService = postcodeService;
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
        View li = inflater.inflate(R.layout.dialog_new_postcode, null);

        EditText textPostcode = li.findViewById(R.id.postcode);

        builder.setView(li)
                .setPositiveButton(R.string.add, (dialog, id) -> {
                    Postcode postcode = new Postcode();
                    postcode.setPostcode(textPostcode.getText().toString());
                    postcode.setRegionId(regionId);
                    postcodeService.store(postcode);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> NewPostcodeDialogFragment.this.getDialog().cancel());


        return builder.create();
    }

}
