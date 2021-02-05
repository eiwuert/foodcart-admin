package de.habibhaidari.foodcart;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import de.habibhaidari.foodcart.callback.model.UpdateCallback;
import de.habibhaidari.foodcart.model.ReportRequest;
import de.habibhaidari.foodcart.service.ReportService;

public class ReportActivity extends AppCompatActivity implements UpdateCallback<ReportRequest> {

    public static final String DATE_FROM_PARAMETER = "dateFrom";
    public static final String DATE_TO_PARAMETER = "dateTo";
    public static final String EMAIL_PARAMETER = "email";


    public static final String DEFAULT_REPORT_IDENTIFIER = "1";

    Button sendButton;
    DatePicker dateFrom;
    DatePicker dateTo;
    EditText emailTo;

    ReportService reportService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.reports);


        reportService = new ReportService(this);
        sendButton = findViewById(R.id.send_button);
        dateFrom = findViewById(R.id.date_from);
        dateTo = findViewById(R.id.date_to);
        emailTo = findViewById(R.id.email);

        reportService.addUpdateCallbackListener(this);

        sendButton.setOnClickListener(l -> {
            ReportRequest request = new ReportRequest();
            request.setDateFrom(getDate(dateFrom).getTime());
            request.setDateTo(getDate(dateTo).getTime());
            request.setEmail(emailTo.getText().toString());
            reportService.update(DEFAULT_REPORT_IDENTIFIER, request);
            Toast.makeText(this, "Bericht wird per Mail gesendet...", Toast.LENGTH_LONG).show();
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public Date getDate(DatePicker picker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
        return calendar.getTime();
    }

    @Override
    public void onUpdated(Boolean updated, ReportRequest object) {
        if (updated) {
            Toast.makeText(this, "Bericht wurde erfolgreich gesendet", Toast.LENGTH_LONG).show();
        }
    }
}