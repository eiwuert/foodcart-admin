package de.habibhaidari.foodcart.ui.region.rate;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.callback.model.DestroyCallback;
import de.habibhaidari.foodcart.callback.model.ShowCallback;
import de.habibhaidari.foodcart.callback.model.StoreCallback;
import de.habibhaidari.foodcart.model.Rate;
import de.habibhaidari.foodcart.model.Region;
import de.habibhaidari.foodcart.service.RateService;
import de.habibhaidari.foodcart.service.RegionService;

import static android.view.View.GONE;


/**
 * A placeholder fragment containing a simple view.
 */
public class RateListFragment extends Fragment implements StoreCallback<Rate>, ShowCallback<Region>, DestroyCallback {

    public static final String ARGUMENT_RATES = "rates";
    public static final String ARGUMENT_IDENTIFIER = "id";

    Integer id;

    ListView listView;
    ProgressBar progressBar;
    FloatingActionButton fab;

    RateService rateService;
    RegionService regionService;

    RateListAdapter rateListAdapter;
    List<Rate> rateList;


    public static RateListFragment newInstance(Integer id, List<Rate> rates) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_RATES, new Gson().toJson(rates));
        args.putInt(ARGUMENT_IDENTIFIER, id);
        RateListFragment fragment = new RateListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rateList = new Gson().fromJson(getArguments().getString(ARGUMENT_RATES), new TypeToken<List<Rate>>() {
        }.getType());
        if (rateList == null) {
            rateList = new ArrayList<>();
        }

        id = getArguments().getInt(ARGUMENT_IDENTIFIER);
        rateService = new RateService(getContext());
        rateService.addStoreCallbackListener(this);
        rateService.addDestroyCallbackListener(this);

        // Daten holen
        regionService = new RegionService(getContext());
        regionService.addShowCallbackListener(this);
        regionService.show(String.valueOf(id));
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_model_list, container, false);

        // progress
        progressBar = root.findViewById(R.id.progress_circular);

        //fab
        fab = root.findViewById(R.id.add_button);
        fab.setOnClickListener(l -> {
            NewRateDialogFragment dialog = NewRateDialogFragment.newInstance(rateService, id);
            dialog.show(getParentFragmentManager(), "NewRateDialogFragment");
        });

        //adapter
        rateListAdapter = new RateListAdapter(getContext(), rateList, rateService);

        //listview
        listView = root.findViewById(R.id.list_view);
        listView.setAdapter(rateListAdapter);

        return root;
    }

    @Override
    public void onShowed(Region object) {
        rateList.addAll(object.getRates());
        progressBar.setVisibility(GONE);
        rateListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStored(Rate r) {
        rateList.add(r);
        rateListAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Lieferkosten wurden hinzugefügt", Toast.LENGTH_LONG).show();
        rateListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyed(Boolean destroyed, String identifier) {
        if (destroyed) {
            rateList.removeIf(e -> e.getId() == Integer.parseInt(identifier));
            Toast.makeText(getContext(), "Lieferkosten wurden entfernt", Toast.LENGTH_LONG).show();
            rateListAdapter.notifyDataSetChanged();
        }
    }
}