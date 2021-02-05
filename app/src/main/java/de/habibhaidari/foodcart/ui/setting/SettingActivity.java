package de.habibhaidari.foodcart.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.List;
import java.util.Objects;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.callback.model.DestroyCallback;
import de.habibhaidari.foodcart.callback.model.IndexCallback;
import de.habibhaidari.foodcart.callback.model.UpdateCallback;
import de.habibhaidari.foodcart.model.Meta;
import de.habibhaidari.foodcart.service.MetaService;


public class SettingActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, IndexCallback<Meta>, UpdateCallback<Meta>, DestroyCallback {

    MetaService metaService;
    ProgressBar progressBar;


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        metaService = new MetaService(getApplicationContext());
        metaService.addIndexCallbackListener(this);
        metaService.addUpdateCallbackListener(this);
        metaService.addDestroyCallbackListener(this);
        metaService.index(null);

        progressBar = findViewById(R.id.progress_circular);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Object o = sharedPreferences.getAll().get(s);
        if (o instanceof String && o.equals("")) {
            metaService.destroy(s);
        } else {
            Meta m = new Meta();
            m.setName(s);
            m.setValue(o);
            metaService.update(s, m);
        }
    }


    @Override
    public void onUpdated(Boolean updated, Meta object) {
        if (updated) {
            Toast.makeText(this, String.format("Einstellung '%s' wurde übernommen", object.getName()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onIndexed(List<Meta> index) {
        index.forEach(e -> metaService.saveToPreferences(e));
        progressBar.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onDestroyed(Boolean destroyed, String identifier) {
        if (destroyed) {
            Toast.makeText(this, String.format("Einstellung '%s' wurde entfernt", identifier), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}