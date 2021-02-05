package de.habibhaidari.foodcart.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.habibhaidari.foodcart.ApplicationClass;
import de.habibhaidari.foodcart.BuildConfig;
import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.callback.OrderServiceCallback;
import de.habibhaidari.foodcart.callback.model.IndexCallback;
import de.habibhaidari.foodcart.model.Order;
import de.habibhaidari.foodcart.ui.main.MainActivity;

import static de.habibhaidari.foodcart.model.Order.State.NEW;

public class OrderBackgroundService extends Service implements Runnable, IndexCallback<Order> {

    //order
    public static final Integer FIRST_ORDER_INDEX = 0;
    //id
    public static final Integer ORDER_SERVICE_START_ID = 123412341;
    //actions
    public static final String ACTION_STOP_SERVICE = "STOP_SERVICE";
    // wakelock
    private static final String WAKELOCK_TAG = "FoodCart::MyWakelock";
    private static final String TIME_FROM_PARAMETER_NAME = "timeFrom";
    // binder für UI
    private final IBinder binder = new LocalBinder();
    // periodisches Ausführen der Methoden
    private final Handler handler = new Handler();
    private PowerManager.WakeLock wakeLock;
    // Liste aller aktuellen Bestellungen
    private ArrayList<Order> orders = null;


    public ArrayList<Order> getOrders() {
        return orders;
    }

    // Benachrichtigungen
    private NotificationManagerCompat notificationManager;

    // Orderservice
    private OrderService orderService;

    // OrderChanged
    private final List<OrderServiceCallback> orderServiceCallbackListener = new ArrayList<>();

    public void addOrderServiceCallbackListener(OrderServiceCallback callback) {
        orderServiceCallbackListener.add(callback);
    }

    public void deleteOrderServiceCallbackListener(OrderServiceCallback callback) {
        orderServiceCallbackListener.remove(callback);
    }

    public void stopService() {
        handler.removeCallbacks(this);
        stopForeground(true);
    }


    @SuppressLint("WakelockTimeout")
    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);
        wakeLock.acquire();

        orderService = new OrderService(this);
        orderService.addIndexCallbackListener(this);

        handler.post(this);
        notificationManager = NotificationManagerCompat.from(this);


        startForeground(ORDER_SERVICE_START_ID, new NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.waiting_for_orders))
                .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                .build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Override
    public void run() {
        if (orders == null) {
            orderService.index(null);
        } else {
            if (orders.size() > 0) {
                Map<String, String> parameters = new HashMap<>();
                parameters.put(TIME_FROM_PARAMETER_NAME, String.valueOf(orders.get(FIRST_ORDER_INDEX).getCreatedAt().getTime()));
                orderService.index(parameters);
            }
        }
        handler.postDelayed(this, BuildConfig.REFRESH_RATE);
    }

    public OrderService getOrderService() {
        return orderService;
    }

    /**
     * BACKGROUNDSERVICE EVENTS
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();

    }

    /**
     * BINDER ZEUG
     */

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public OrderBackgroundService getServiceInstance() {
            return OrderBackgroundService.this;
        }
    }

    /***
     *
     * REST
     */

    private Date getYesterday() {
        Calendar current = Calendar.getInstance();
        current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DATE) - 1, 0, 0, 0);
        return current.getTime();
    }

    @Override
    public void onIndexed(List<Order> index) {
        if (orders == null) {
            orders = new ArrayList<>();

        } else if (index.size() > 0 && index.stream().anyMatch(o -> o.getState().equals(NEW))) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
            notificationManager.notify(Objects.hash(index.toString()),
                    new NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID_NEW_ORDER)
                            .setContentTitle(getResources().getString(R.string.app_name))
                            .setContentText(getResources().getString(R.string.new_orders))
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build());
        }
        orders.addAll(index);
        orders = orders.stream()
                .filter(e -> e.getCreatedAt().after(getYesterday()))
                .sorted((e1, e2) -> Long.compare(e2.getCreatedAt().getTime(), e1.getCreatedAt().getTime()))
                .collect(Collectors.toCollection(ArrayList::new));
        for (OrderServiceCallback c : orderServiceCallbackListener) {
            c.onOrdersChanged(orders);
        }
    }


}

