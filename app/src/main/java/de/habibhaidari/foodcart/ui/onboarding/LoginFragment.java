package de.habibhaidari.foodcart.ui.onboarding;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;

import de.habibhaidari.foodcart.R;

import static android.content.Context.MODE_PRIVATE;
import static de.habibhaidari.foodcart.constant.ApplicationConstants.APPLICATION_NAME;
import static de.habibhaidari.foodcart.volley.AuthRequest.EMAIL_KEY;
import static de.habibhaidari.foodcart.volley.AuthRequest.PASSWORD_KEY;

public class LoginFragment extends Fragment implements SlidePolicy {

    TextView editTextTextEmailAddress;
    TextView editTextTextPassword;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding_login_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextTextEmailAddress = view.findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = view.findViewById(R.id.editTextTextPassword);

    }


    @Override
    public boolean isPolicyRespected() {
        if (editTextTextEmailAddress.getText().length() != 0 && editTextTextPassword.getText().length() != 0) {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences(APPLICATION_NAME, MODE_PRIVATE);
            sharedPreferences.edit().putString(EMAIL_KEY, editTextTextEmailAddress.getText().toString()).apply();
            sharedPreferences.edit().putString(PASSWORD_KEY, editTextTextPassword.getText().toString()).apply();
            return true;
        }
        return false;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(getContext(), requireContext().getString(R.string.login_credentials), Toast.LENGTH_SHORT).show();

    }
}
