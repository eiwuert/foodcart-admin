package de.habibhaidari.foodcart.ui.main;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.callback.OrderServiceCallback;
import de.habibhaidari.foodcart.model.Order;
import de.habibhaidari.foodcart.service.OrderBackgroundService;
import de.habibhaidari.foodcart.ui.order.OrderActivity;

import static de.habibhaidari.foodcart.model.Order.State.NEW;
import static de.habibhaidari.foodcart.model.Order.State.SEEN;

/**
 * A placeholder fragment containing a simple view.
 */
public class OrderListFragment extends Fragment implements OrderListAdapter.ItemClickListener, OrderServiceCallback {

    static final String POSITION_ARGUMENT = "position";
    int position;
    ArrayList<Order> orders;
    LinearLayoutManager layoutManager;
    OrderListAdapter orderListAdapter;
    OrderBackgroundService orderBackgroundService;

    ProgressBar progress;

    public static OrderListFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION_ARGUMENT, position);
        OrderListFragment orderListFragment = new OrderListFragment();
        orderListFragment.setArguments(args);
        return orderListFragment;
    }


    public OrderListFragment() {
    }


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            OrderBackgroundService.LocalBinder binder = (OrderBackgroundService.LocalBinder) service;
            orderBackgroundService = binder.getServiceInstance();
            orderBackgroundService.addOrderServiceCallbackListener(OrderListFragment.this);
            onOrdersChanged(orderBackgroundService.getOrders());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };


    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // order instanziieren
        orders = new ArrayList<>();
        // Argumente auslesen
        Bundle args = getArguments();
        position = args.getInt("position", 0);
        // Verbinden mit Service
        Intent serviceIntent = new Intent(getContext(), OrderBackgroundService.class);
        requireContext().bindService(serviceIntent, serviceConnection, Context.MODE_PRIVATE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        orderBackgroundService.deleteOrderServiceCallbackListener(OrderListFragment.this);
        requireContext().unbindService(serviceConnection);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order_list, container, false);

        progress = root.findViewById(R.id.progress_circular);

        RecyclerView orderRecyclerView = root.findViewById(R.id.recycler_view_order);

        layoutManager = new LinearLayoutManager(getContext());
        orderRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(),
                layoutManager.getOrientation()));
        orderRecyclerView.setLayoutManager(layoutManager);

        orderListAdapter = new OrderListAdapter(getContext(), orders);
        orderListAdapter.setClickListener(this);
        orderRecyclerView.setAdapter(orderListAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (orderBackgroundService != null) {
            onOrdersChanged(orderBackgroundService.getOrders());
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Order o = orders.get(position);
        if (o.getState().equals(NEW)) {
            o.setState(SEEN);
        }
        Intent intent = new Intent(getActivity(), OrderActivity.class);
        intent.putExtra(OrderActivity.ORDER_ARGUMENT, new Gson().toJson(o));
        startActivity(intent);
    }


    @Override
    public void onOrdersChanged(ArrayList<Order> o) {
        if (o != null) {
            progress.setVisibility(View.GONE);

            switch (position) {
                case 0:
                    orders = o.stream().filter(e -> e.getState().equals(NEW) || e.getState().equals(SEEN)).collect(Collectors.toCollection(ArrayList::new));
                    break;

                case 1:
                    orders = o.stream().filter(e -> !e.getState().equals(NEW) && !e.getState().equals(SEEN)).collect(Collectors.toCollection(ArrayList::new));
                    break;

            }
            orderListAdapter.update(orders);
        }
    }
}