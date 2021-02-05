package de.habibhaidari.foodcart.ui.order;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.callback.model.UpdateCallback;
import de.habibhaidari.foodcart.model.Order;
import de.habibhaidari.foodcart.service.OrderBackgroundService;
import de.habibhaidari.foodcart.service.OrderService;
import de.habibhaidari.foodcart.util.CurrencyUtils;
import de.habibhaidari.foodcart.util.PhoneUtils;

import static de.habibhaidari.foodcart.constant.FormatConstants.ORDER_NUMBER_FORMAT;
import static de.habibhaidari.foodcart.constant.FormatConstants.RESOURCE_FORMAT_CONTACT_MAIL;
import static de.habibhaidari.foodcart.constant.FormatConstants.RESOURCE_FORMAT_CONTACT_TEL;
import static de.habibhaidari.foodcart.constant.FormatConstants.RESOURCE_FORMAT_CONTACT_WA;
import static de.habibhaidari.foodcart.model.Order.State.CLOSED;
import static de.habibhaidari.foodcart.model.Order.State.NEW;
import static de.habibhaidari.foodcart.model.Order.State.SEEN;


/**
 * A placeholder fragment containing a simple view.
 */
public class OrderFragment extends Fragment implements UpdateCallback<Order> {


    public static final String ORDER_KEY = "order";
    public static final String EMPTY_JS_OBJECT = "{}";
    static final int ID = 0;

    private Context context;

    Order order;

    TextView username;
    TextView street;
    TextView city;
    TextView floor;

    TextView state;
    TextView time;
    TextView number;

    TextView phone;
    TextView email;

    TextView notes;
    TextView method;

    TextView total;
    TextView subtotal;
    TextView delivery;
    TextView refundDelivery;
    TextView deliveryTime;
    ImageButton updateIcon;

    OrderBackgroundService orderBackgroundService;
    OrderService orderService;


    public OrderFragment() {

    }

    public static OrderFragment newInstance(Order order) {
        Bundle args = new Bundle();
        args.putString(ORDER_KEY, new Gson().toJson(order));
        OrderFragment orderFragment = new OrderFragment();
        orderFragment.setArguments(args);
        return orderFragment;
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        assert args != null;
        order = new Gson().fromJson(args.getString(ORDER_KEY, EMPTY_JS_OBJECT), Order.class);
        // Mit service verbinden
        Intent serviceIntent = new Intent(context, OrderBackgroundService.class);
        requireContext().bindService(serviceIntent, serviceConnection, Context.MODE_PRIVATE);
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order, container, false);


        RelativeLayout floorLayout = root.findViewById(R.id.floor_layout);
        RelativeLayout notesLayout = root.findViewById(R.id.notes_layout);
        RelativeLayout deliveryLayout = root.findViewById(R.id.delivery_layout);
        RelativeLayout refundDeliveryLayout = root.findViewById(R.id.refund_delivery_layout);


        username = root.findViewById(R.id.username);
        street = root.findViewById(R.id.streetcity);
        floor = root.findViewById(R.id.floor);

        state = root.findViewById(R.id.state);
        time = root.findViewById(R.id.time);
        number = root.findViewById(R.id.number);

        deliveryTime = root.findViewById(R.id.deliveryTime);


        phone = root.findViewById(R.id.phone);
        email = root.findViewById(R.id.email);


        notes = root.findViewById(R.id.notes);
        method = root.findViewById(R.id.method);


        delivery = root.findViewById(R.id.delivery);
        refundDelivery = root.findViewById(R.id.refund_delivery);


        subtotal = root.findViewById(R.id.subtotal);
        total = root.findViewById(R.id.total);

        updateIcon = root.findViewById(R.id.update_icon);
        if (order.getState().equals(NEW) || order.getState().equals(SEEN)) {
            updateIcon.setVisibility(View.VISIBLE);
        }
        updateIcon.setOnClickListener(e -> {
            Order update = new Order();
            update.setState(CLOSED);
            updateIcon.setVisibility(View.INVISIBLE);
            orderService.update(String.valueOf(order.getId()), update);
        });


        ImageButton navigationIcon = root.findViewById(R.id.navigation_icon);
        navigationIcon.setOnClickListener(e -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + order.getStreet() + ", " + String.valueOf(order.getPostcode().getPostcode()).concat(" ").concat(order.getPostcode().getCities().get(0).getName()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        ImageButton mailIcon = root.findViewById(R.id.mail_icon);
        mailIcon.setOnClickListener(e -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(String.format(RESOURCE_FORMAT_CONTACT_MAIL, order.getUser().getEmail())));
            startActivity(intent);
        });


        ImageButton phoneIcon = root.findViewById(R.id.phone_icon);
        phoneIcon.setOnClickListener(e -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(String.format(RESOURCE_FORMAT_CONTACT_TEL, order.getPhone())));
            startActivity(intent);
        });


        ImageButton whatsappIcon = root.findViewById(R.id.whatsapp_icon);
        whatsappIcon.setOnClickListener(e -> {
            String url = String.format(RESOURCE_FORMAT_CONTACT_WA, PhoneUtils.formatPhone(order.getPhone()));
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });


        username.setText(order.getName());
        street.setText(order.getStreet() + ", " + String.valueOf(order.getPostcode().getPostcode()).concat(" ").concat(order.getPostcode().getCities().get(0).getName()));
        if (order.getFloor() != null) {
            floorLayout.setVisibility(View.VISIBLE);
            floor.setText(order.getFloor());
        }

        phone.setText(order.getPhone());
        email.setText(order.getUser().getEmail());

        state.setText(order.getState().getName());
        time.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(order.getCreatedAt().getTime()));
        number.setText(String.format(ORDER_NUMBER_FORMAT, order.getId()));

        if (order.getNotes() != null) {
            notesLayout.setVisibility(View.VISIBLE);
            notes.setText(order.getNotes());
        }

        if (order.getDelivery() == -1) {
            deliveryTime.setText(R.string.as_soon_as_possible);
        } else {
            deliveryTime.setText(order.getDeliveryFormatted());
        }

        method.setText(order.getMethod().getName());


        total.setText(CurrencyUtils.getFormattedCurrency(order.getTotal()).concat(" €"));

        subtotal.setText(CurrencyUtils.getFormattedCurrency(order.getSubtotal()).concat(" €"));

        if (order.getRate() != null) {
            deliveryLayout.setVisibility(View.VISIBLE);
            delivery.setText(CurrencyUtils.getFormattedCurrency(order.getRate().getCosts()).concat(" €"));
        }
        if (order.getRefundRate() != null) {
            refundDeliveryLayout.setVisibility(View.VISIBLE);
            refundDelivery.setText((CurrencyUtils.getFormattedCurrency(order.getRefundRate().getCosts() * (-1))).concat(" €"));
        }


        return root;
    }

    /**
     * SERVICE CONNECTION
     */

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            OrderBackgroundService.LocalBinder binder = (OrderBackgroundService.LocalBinder) service;
            orderBackgroundService = binder.getServiceInstance();
            orderService = orderBackgroundService.getOrderService();
            orderService.addUpdateCallbackListener(OrderFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        orderService.removeUpdateCallbackListener(OrderFragment.this);
        requireContext().unbindService(serviceConnection);
    }


    @Override
    public void onUpdated(Boolean updated, Order object) {
        if (updated) {
            orderBackgroundService.getOrders().get(orderBackgroundService.getOrders().indexOf(order)).setState(Order.State.CLOSED);
            order.setState(object.getState());
            state.setText(order.getState().getName());
            Toast.makeText(context, "Bestellung wurde erfolgreich abgeschlossen", Toast.LENGTH_LONG).show();
        }
    }
}