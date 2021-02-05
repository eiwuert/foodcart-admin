package de.habibhaidari.foodcart.ui.order.position;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.model.Position;

/**
 * A placeholder fragment containing a simple view.
 */
public class PositionFragment extends Fragment {

    public static final String POSITION_KEY = "positions";
    public static final String EMPTY_JS_ARRAY = "[]";

    List<Position> positions;


    public PositionFragment() {

    }

    public static PositionFragment newInstance(List<Position> order) {
        Bundle args = new Bundle();
        args.putString(POSITION_KEY, new Gson().toJson(order));
        PositionFragment orderFragment = new PositionFragment();
        orderFragment.setArguments(args);
        return orderFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        assert args != null;
        positions = new Gson().fromJson(args.getString(POSITION_KEY, EMPTY_JS_ARRAY), new TypeToken<List<Position>>() {
        }.getType());
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_position_list, container, false);
        ListView orderListView = root.findViewById(R.id.list_view_order);

        orderListView.setAdapter(new PositionAdapter(getContext(), positions));
        return root;
    }


}