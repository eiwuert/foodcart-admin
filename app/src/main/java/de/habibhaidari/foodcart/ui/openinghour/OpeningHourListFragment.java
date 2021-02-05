package de.habibhaidari.foodcart.ui.openinghour;

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
import de.habibhaidari.foodcart.model.OpeningHour;
import de.habibhaidari.foodcart.service.OpeningHourService;

public class OpeningHourListFragment extends Fragment implements StoreCallback<OpeningHour>, DestroyCallback, IndexCallback<OpeningHour> {


    public static final String OPENING_HOURS_KEY = "opening_hours";
    public static final String EMPTY_JS_ARRAY = "[]";

    ListView listView;

    OpeningHourListAdapter openingHourListAdapter;
    List<OpeningHour> openingHourList;

    OpeningHourService openingHourService;
    ProgressBar progressBar;
    FloatingActionButton fab;

    public static OpeningHourListFragment newInstance() {
        Bundle args = new Bundle();
        OpeningHourListFragment fragment = new OpeningHourListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openingHourService = new OpeningHourService(getContext());
        openingHourService.addDestroyCallbackListener(this);
        openingHourService.addStoreCallbackListener(this);
        openingHourService.addIndexCallbackListener(this);
        openingHourService.index(null);
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        openingHourList = new ArrayList<>();

        View root = inflater.inflate(R.layout.fragment_model_list, container, false);
        listView = root.findViewById(R.id.list_view);

        // Views
        progressBar = root.findViewById(R.id.progress_circular);
        fab = root.findViewById(R.id.add_button);

        //Listener
        fab.setOnClickListener(l -> {
            NewOpeningHourDialogFragment dialog = NewOpeningHourDialogFragment.newInstance(openingHourService);
            dialog.show(getParentFragmentManager(), "NewOpeningHourDialogFragment");
        });

        openingHourListAdapter = new OpeningHourListAdapter(getContext(), openingHourList, openingHourService);
        listView.setAdapter(openingHourListAdapter);

        return root;
    }


    @Override
    public void onIndexed(List<OpeningHour> index) {
        progressBar.setVisibility(View.GONE);
        openingHourList.addAll(index);
        openingHourListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStored(OpeningHour object) {
        openingHourList.add(object);
        Toast.makeText(getContext(), "Öffnungszeit wurde hinzugefügt", Toast.LENGTH_LONG).show();
        openingHourListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyed(Boolean destroyed, String identifier) {
        if (destroyed) {
            openingHourList.removeIf(e -> e.getId() == Integer.parseInt(identifier));
            Toast.makeText(getContext(), "Öffnungszeit wurde entfernt", Toast.LENGTH_LONG).show();
            openingHourListAdapter.notifyDataSetChanged();
        }
    }
}
