package de.habibhaidari.foodcart.ui.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.ui.ScanningActivity;

import de.habibhaidari.foodcart.R;

public class ConnectPrinterFragment extends Fragment implements SlidePolicy {

    Button choosePrinterButton;

    public static ConnectPrinterFragment newInstance() {
        return new ConnectPrinterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding_connect_printer_fragment, container, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER &&
                resultCode == Activity.RESULT_OK) {
            choosePrinterButton.setText(R.string.connected);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        choosePrinterButton = view.findViewById(R.id.choose_printer_button);
        choosePrinterButton.setOnClickListener(e -> startActivityForResult(new Intent(view.getContext(), ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER));
        if (Printooth.INSTANCE.hasPairedPrinter()) {
            choosePrinterButton.setText(R.string.connected);
        }
    }

    @Override
    public boolean isPolicyRespected() {
        return Printooth.INSTANCE.hasPairedPrinter();
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(getContext(), getContext().getString(R.string.connect_a_printer), Toast.LENGTH_SHORT).show();
    }
}
