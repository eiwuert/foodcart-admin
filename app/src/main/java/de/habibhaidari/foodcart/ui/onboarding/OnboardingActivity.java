package de.habibhaidari.foodcart.ui.onboarding;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.callback.model.IndexCallback;
import de.habibhaidari.foodcart.model.Meta;
import de.habibhaidari.foodcart.service.MetaService;

import static de.habibhaidari.foodcart.constant.ApplicationConstants.APPLICATION_NAME;
import static de.habibhaidari.foodcart.ui.main.MainActivity.ONBOARDING_COMPLETE_KEY;

public class OnboardingActivity extends AppIntro implements IndexCallback<Meta> {

    MetaService metaService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load restaurant
        metaService = new MetaService(getApplicationContext());
        metaService.addIndexCallbackListener(this);
        metaService.index(null);

        // Settings AppIntro
        showStatusBar(true);
        setWizardMode(true);
        setImmersiveMode();
        setVibrate(true);
        setSystemBackButtonLocked(true);


        // Slides
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.welcome_message),
                getString(R.string.app_description),
                R.drawable.ic_baseline_fastfood_256
        ));

        askForPermissions(getPermissions(), 1, true);

        addSlide(ConnectPrinterFragment.newInstance());

        addSlide(LoginFragment.newInstance());
    }

    private String[] getPermissions() {
        List<String> permissions = Stream.of(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.INTERNET,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).collect(Collectors.toList());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.addAll(Stream.of(Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.FOREGROUND_SERVICE).collect(Collectors.toList()));

        }
        return permissions.toArray(new String[0]);
    }


    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences sharedPreferences = getSharedPreferences(APPLICATION_NAME, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(ONBOARDING_COMPLETE_KEY, true).apply();
        finish();
    }


    @Override
    public void onIndexed(List<Meta> index) {
        for (Meta e : index) {
            metaService.saveToPreferences(e);
        }
    }
}
