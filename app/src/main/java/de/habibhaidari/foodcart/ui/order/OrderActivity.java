package de.habibhaidari.foodcart.ui.order;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.utilities.Printing;

import java.io.File;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.callback.model.DestroyCallback;
import de.habibhaidari.foodcart.callback.model.ShowCallback;
import de.habibhaidari.foodcart.model.Order;
import de.habibhaidari.foodcart.printable.OrderPrintable;
import de.habibhaidari.foodcart.service.InvoiceService;
import de.habibhaidari.foodcart.service.MetaService;
import de.habibhaidari.foodcart.service.OrderBackgroundService;
import de.habibhaidari.foodcart.service.OrderService;

import static de.habibhaidari.foodcart.constant.FormatConstants.ORDER_NUMBER_NAME_FORMAT;

public class OrderActivity extends AppCompatActivity implements ShowCallback<File>, DestroyCallback {

    public static final String ORDER_ARGUMENT = "order";
    Printing printing;
    Order order;

    InvoiceService invoiceService;
    MetaService metaService;

    OrderBackgroundService orderBackgroundService;
    OrderService orderService;

    private MenuItem cancelOrder;
    private MenuItem printOrder;


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            OrderBackgroundService.LocalBinder binder = (OrderBackgroundService.LocalBinder) service;
            orderBackgroundService = binder.getServiceInstance();
            orderService = orderBackgroundService.getOrderService();
            // DestroyListener
            orderService.addDestroyCallbackListener(OrderActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //read which item selected
        order = new Gson().fromJson(getIntent().getStringExtra(ORDER_ARGUMENT), Order.class);
        Intent serviceIntent = new Intent(this, OrderBackgroundService.class);
        bindService(serviceIntent, serviceConnection, Context.MODE_PRIVATE);

        // contentView
        setContentView(R.layout.activity_order);

        //Actionbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.format(ORDER_NUMBER_NAME_FORMAT, order.getId(), order.getName()));

        //sectionpager
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new OrderPagerAdapter(this, getSupportFragmentManager(), order));
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        initPrinter();

        //invoice download
        invoiceService = new InvoiceService(this);
        invoiceService.addShowCallbackListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu, menu);
        cancelOrder = menu.findItem(R.id.cancel_order);
        printOrder = menu.findItem(R.id.print);
        if (order.getReferenceOrder() != null) {
            printOrder.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (order.getState().equals(Order.State.CANCELED)
                || order.getReferenceOrder() != null) {
            cancelOrder.setVisible(false);
        }

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.print) {
            MetaService metaService = new MetaService(this);
            printing.print(new OrderPrintable(order, metaService.readFromPreferences()));
        } else if (id == R.id.download_invoice) {
            Toast.makeText(this, "Rechnung wird heruntergeladen...", Toast.LENGTH_LONG).show();
            invoiceService.show(String.valueOf(order.getId()));
        } else if (id == R.id.cancel_order) {
            Toast.makeText(this, "Bestellung wird storniert...", Toast.LENGTH_LONG).show();
            orderService.destroy(String.valueOf(order.getId()));
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPrinter() {
        if (Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();
    }

    @Override
    public void onShowed(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
        intent.setDataAndType(uri, invoiceService.getFiletype());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unbindService(serviceConnection);
        orderService.removeDestroyCallbackListener(OrderActivity.this);
    }

    @Override
    public void onDestroyed(Boolean destroyed, String identifier) {
        if (destroyed) {
            orderBackgroundService.getOrders().get(orderBackgroundService.getOrders().indexOf(order)).setState(Order.State.CANCELED);
            order.setState(Order.State.CANCELED);
            Toast.makeText(this, "Bestellung wurde erfolgreich storniert", Toast.LENGTH_LONG).show();
        }
    }
}