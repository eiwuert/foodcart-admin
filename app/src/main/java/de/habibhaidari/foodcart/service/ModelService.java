package de.habibhaidari.foodcart.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.habibhaidari.foodcart.BuildConfig;
import de.habibhaidari.foodcart.callback.model.DestroyCallback;
import de.habibhaidari.foodcart.callback.model.IndexCallback;
import de.habibhaidari.foodcart.callback.model.ShowCallback;
import de.habibhaidari.foodcart.callback.model.StoreCallback;
import de.habibhaidari.foodcart.callback.model.UpdateCallback;
import de.habibhaidari.foodcart.volley.AuthRequest;
import de.habibhaidari.foodcart.volley.FoodCartRetryPolicy;
import de.habibhaidari.foodcart.volley.JsonBooleanRequest;

import static de.habibhaidari.foodcart.constant.FormatConstants.DATA_RETRIVAL_FAILED;
import static de.habibhaidari.foodcart.constant.FormatConstants.SERVER_CONNECTION_FAILED;
import static de.habibhaidari.foodcart.util.UrlUtils.formatParameters;

public abstract class ModelService<T> {

    private final Context context;
    private final RequestQueue requestQueue;
    public final LinkedList<Request> backupQueue = new LinkedList<>();

    final List<IndexCallback<T>> indexCallbackListener = new ArrayList<>();
    final List<DestroyCallback> destroyCallbackListener = new ArrayList<>();
    final List<StoreCallback<T>> storeCallbackListener = new ArrayList<>();
    final List<UpdateCallback<T>> updateCallbackListener = new ArrayList<>();
    final List<ShowCallback<T>> showCallbackListener = new ArrayList<>();


    public ModelService(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }


    public void addShowCallbackListener(ShowCallback<T> showCallback) {
        showCallbackListener.add(showCallback);
    }

    public void removeShowCallbackListener(ShowCallback<T> showCallback) {
        showCallbackListener.remove(showCallback);
    }

    public void addIndexCallbackListener(IndexCallback<T> indexCallback) {
        indexCallbackListener.add(indexCallback);
    }

    public void removeIndexCallbackListener(IndexCallback<T> indexCallback) {
        indexCallbackListener.remove(indexCallback);
    }

    public void addDestroyCallbackListener(DestroyCallback destroyCallback) {
        destroyCallbackListener.add(destroyCallback);
    }

    public void removeDestroyCallbackListener(DestroyCallback destroyCallback) {
        destroyCallbackListener.remove(destroyCallback);
    }


    public void addStoreCallbackListener(StoreCallback<T> storeCallback) {
        storeCallbackListener.add(storeCallback);
    }

    public void removeStoreCallbackListener(StoreCallback<T> storeCallback) {
        storeCallbackListener.remove(storeCallback);
    }


    public void addUpdateCallbackListener(UpdateCallback<T> updateCallback) {
        updateCallbackListener.add(updateCallback);
    }

    public void removeUpdateCallbackListener(UpdateCallback<T> updateCallback) {
        updateCallbackListener.remove(updateCallback);
    }

    /**
     * returns Index of Object
     */
    public void index(Map<String, String> parameters) {
        String url = BuildConfig.APP_HOST + getRoute();
        if (parameters != null) {
            url = url.concat(formatParameters(parameters));
        }
        Log.d(ModelService.class.getName(), "Sending new Index Request");
        StringRequest req = new StringRequest(
                Request.Method.GET,
                url,
                this::handleIndex,
                this::handleError
        );
        req.setRetryPolicy(new FoodCartRetryPolicy());
        backupQueue.add(req);
        requestQueue.add(req);
    }

    public void handleIndex(String s) {
        backupQueue.pop();
        List<T> list = new Gson().fromJson(s, getListType());
        for (IndexCallback<T> callback : indexCallbackListener) {
            callback.onIndexed(list);
        }
    }


    /**
     * Destroys objects by identifier
     */
    public void destroy(String identifier) {
        Log.d(ModelService.class.getName(), "Sending new Destroy Request");
        StringRequest req = new StringRequest(
                Request.Method.DELETE,
                BuildConfig.APP_HOST + getRoute() + "/" + identifier,
                e -> handleDestroy(e, identifier),
                this::handleError
        );
        req.setRetryPolicy(new FoodCartRetryPolicy());
        backupQueue.add(req);
        requestQueue.add(req);
    }

    private void handleDestroy(String s, String identifier) {
        backupQueue.pop();
        Boolean response = Boolean.parseBoolean(s);
        for (DestroyCallback callback : destroyCallbackListener) {
            callback.onDestroyed(response, identifier);
        }
    }

    /**
     * Stores new Objects
     */
    public void store(T object) {
        try {
            Log.d(ModelService.class.getName(), "Sending new Store Request");
            JSONObject request = new JSONObject();
            request.put("data", new JSONObject(new Gson().toJson(object)));

            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.POST,
                    BuildConfig.APP_HOST + getRoute(),
                    request,
                    this::handleStore,
                    this::handleError
            );
            req.setRetryPolicy(new FoodCartRetryPolicy());
            backupQueue.add(req);
            requestQueue.add(req);

        } catch (JSONException e) {
            Log.d(ModelService.class.toString(), e.toString());
        }
    }

    private void handleStore(JSONObject e) {
        backupQueue.pop();
        T object = new Gson().fromJson(e.toString(), getType());
        for (StoreCallback<T> callbacks : storeCallbackListener) {
            callbacks.onStored(object);
        }
    }

    /**
     * updating existing objects
     */
    public void update(String identifier, T object) {
        Log.d(ModelService.class.getName(), "Sending new Update Request");
        try {
            JSONObject request = new JSONObject();
            request.put("data", new JSONObject(new Gson().toJson(object)));


            JsonBooleanRequest req = new JsonBooleanRequest(
                    Request.Method.PATCH,
                    BuildConfig.APP_HOST + getRoute() + "/" + identifier,
                    request,
                    e -> handleUpdate(e, object),
                    this::handleError
            );
            req.setRetryPolicy(new FoodCartRetryPolicy());
            backupQueue.add(req);
            requestQueue.add(req);

        } catch (JSONException e) {
            Log.d(ModelService.class.toString(), e.toString());
        }
    }

    private void handleUpdate(Boolean e, T object) {
        backupQueue.pop();
        for (UpdateCallback<T> callbacks : updateCallbackListener) {
            callbacks.onUpdated(e, object);
        }
    }

    public void show(String identifier) {
        Log.d(ModelService.class.getName(), "Sending new Show Request");
        StringRequest req = new StringRequest(
                Request.Method.GET,
                BuildConfig.APP_HOST + getRoute() + "/" + identifier,
                this::handleShow,
                this::handleError
        );
        req.setRetryPolicy(new FoodCartRetryPolicy());
        backupQueue.add(req);
        requestQueue.add(req);
    }

    public void handleShow(String s) {
        backupQueue.pop();
        T object = new Gson().fromJson(s, getType());
        for (ShowCallback<T> callback : showCallbackListener) {
            callback.onShowed(object);
        }
    }


    private void handleError(VolleyError volleyError) {
        if (volleyError.networkResponse == null) {
            Toast.makeText(context, SERVER_CONNECTION_FAILED, Toast.LENGTH_LONG).show();
            backupQueue.pop();
        } else if (volleyError.networkResponse.statusCode == 403) {
            requestQueue.add(AuthRequest.newInstance(context, e -> requestQueue.add(backupQueue.getFirst())));
        } else {
            Toast.makeText(context, String.format(DATA_RETRIVAL_FAILED, volleyError.networkResponse.statusCode), Toast.LENGTH_LONG).show();
            backupQueue.pop();
        }
    }


    abstract String getRoute();

    abstract Type getListType();

    abstract Type getType();

}
