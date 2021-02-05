package de.habibhaidari.foodcart;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.os.Build;
import android.provider.Settings;

import com.mazenrashed.printooth.Printooth;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class ApplicationClass extends Application {

    public static final String CHANNEL_ID = "OrderService";
    public static final String CHANNEL_ID_NEW_ORDER = "NewOrder";

    @Override
    public void onCreate() {
        super.onCreate();
        Printooth.INSTANCE.init(this);
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        createNotificationChannels();
    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);

            // Bestellservice
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT));

            // Neue Bestellungen
            NotificationChannel newOrderChannel = new NotificationChannel(CHANNEL_ID_NEW_ORDER, getResources().getString(R.string.new_orders), NotificationManager.IMPORTANCE_HIGH);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            newOrderChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, audioAttributes);

            manager.createNotificationChannel(newOrderChannel);


        }

    }


}
// MINDESTENS ANDROID OREO