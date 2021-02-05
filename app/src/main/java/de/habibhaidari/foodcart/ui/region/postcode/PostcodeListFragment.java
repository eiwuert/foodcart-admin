package de.habibhaidari.foodcart.ui.region.postcode;

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
import de.habibhaidari.foodcart.callback.model.StoreCallback;
import de.habibhaidari.foodcart.model.Postcode;
import de.habibhaidari.foodcart.service.PostcodeService;


/**
 * A placeholder fragment containing a simple view.
 */
public class PostcodeListFragment extends Fragment implements StoreCallback<Postcode>, DestroyCallback {

    public static final String ARGUMENT_POSTCODES = "postcodes";
    public static final String ARGUMENT_IDENTIFIER = "id";

    Integer id;

    List<Postcode> postcodeList;
    PostcodeListAdapter postcodeListAdapter;

    ListView listView;
    ProgressBar progressBar;
    FloatingActionButton fab;

    PostcodeService postcodeService;


    public static PostcodeListFragment newInstance(Integer id, List<Postcode> postcodes) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_POSTCODES, new Gson().toJson(postcodes));
        args.putInt(ARGUMENT_IDENTIFIER, id);
        PostcodeListFragment fragment = new PostcodeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postcodeList = new Gson().fromJson(getArguments().getString(ARGUMENT_POSTCODES), new TypeToken<List<Postcode>>() {
        }.getType());
        id = getArguments().getInt(ARGUMENT_IDENTIFIER);
        postcodeService = new PostcodeService(getContext());
        postcodeService.addStoreCallbackListener(this);
        postcodeService.addDestroyCallbackListener(this);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_model_list, container, false);
        if (postcodeList == null) {
            postcodeList = new ArrayList<>();
        }

        // progress
        progressBar = root.findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.GONE);

        //fab
        fab = root.findViewById(R.id.add_button);
        fab.setOnClickListener(l -> {
            NewPostcodeDialogFragment dialog = NewPostcodeDialogFragment.newInstance(postcodeService, id);
            dialog.show(getParentFragmentManager(), "NewPostcodeDialogFragment");
        });

        // adapter
        postcodeListAdapter = new PostcodeListAdapter(getContext(), postcodeList, postcodeService);

        //listview
        listView = root.findViewById(R.id.list_view);
        listView.setAdapter(postcodeListAdapter);

        return root;
    }

    @Override
    public void onStored(Postcode object) {
        postcodeList.add(object);
        Toast.makeText(getContext(), String.format("Postleitzahl %s wurde hinzugefügt", object.getPostcode()), Toast.LENGTH_LONG).show();
        postcodeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyed(Boolean destroyed, String identifier) {
        if (destroyed) {
            Toast.makeText(getContext(), String.format("Postleitzahl %s wurde entfernt", identifier), Toast.LENGTH_LONG).show();
            postcodeList.removeIf(e -> identifier.equals(e.getPostcode()));
            postcodeListAdapter.notifyDataSetChanged();
        }
    }
}