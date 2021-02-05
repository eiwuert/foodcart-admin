package de.habibhaidari.foodcart.volley;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import de.habibhaidari.foodcart.BuildConfig;
import de.habibhaidari.foodcart.constant.APIConstants;
import de.habibhaidari.foodcart.service.FileService;

import static android.content.Context.MODE_PRIVATE;
import static de.habibhaidari.foodcart.constant.ApplicationConstants.APPLICATION_NAME;

public class AuthRequest extends JsonObjectRequest {


    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";

    private static final String EMAIL_FIELD = "email";
    private static final String PASSWORD_FIELD = "password";

    public static AuthRequest newInstance(Context context, Response.Listener<JSONObject> listener) {
        try {
            Log.d(AuthRequest.class.getName(), "Generate Authorize Request to Queue");
            SharedPreferences sharedPreferences = context.getSharedPreferences(APPLICATION_NAME, MODE_PRIVATE);
            String email = sharedPreferences.getString(EMAIL_KEY, "");
            String password = sharedPreferences.getString(PASSWORD_KEY, "");
            JSONObject js = new JSONObject();
            js.put(EMAIL_FIELD, email);
            js.put(PASSWORD_FIELD, password);
            return new AuthRequest(Request.Method.POST,
                    BuildConfig.APP_HOST + APIConstants.LOGIN_ROUTE,
                    js,
                    listener,
                    e -> {/* LOGIN ERROR */}
            );
        } catch (JSONException e) {
            Log.d(FileService.class.getName(), e.toString());
        }
        return null;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    public AuthRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }


}
