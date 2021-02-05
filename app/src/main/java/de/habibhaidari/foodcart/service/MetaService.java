package de.habibhaidari.foodcart.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import de.habibhaidari.foodcart.callback.model.IndexCallback;
import de.habibhaidari.foodcart.constant.APIConstants;
import de.habibhaidari.foodcart.model.Meta;

import static android.content.Context.MODE_PRIVATE;
import static de.habibhaidari.foodcart.model.Meta.GLOBAL_PREFERENCES;

public class MetaService extends ModelService<Meta> {

    private final SharedPreferences sharedPreferences;

    public MetaService(Context context) {
        super(context);
        sharedPreferences = context.getSharedPreferences(GLOBAL_PREFERENCES, MODE_PRIVATE);
    }

    @Override
    String getRoute() {
        return APIConstants.METAS_ROUTE;
    }

    @Override
    Type getListType() {
        return new TypeToken<List<Meta>>() {
        }.getType();
    }

    @Override
    Type getType() {
        return null;
    }

    @Override
    public void store(Meta object) {
        object.formatValue();
        super.store(object);
    }

    @Override
    public void update(String identifier, Meta object) {
        object.formatValue();
        super.update(identifier, object);
    }

    @Override
    public void handleIndex(String s) {
        backupQueue.pop();
        List<Meta> list = new Gson().fromJson(s, getListType());
        list.forEach(Meta::deformatValue);
        for (IndexCallback<Meta> callback : indexCallbackListener) {
            callback.onIndexed(list);
        }
    }

    public Map<String, ?> readFromPreferences() {
        return sharedPreferences.getAll();
    }

    public void saveToPreferences(Meta meta) {
        if (meta.getValue() instanceof String) {
            sharedPreferences.edit().putString(meta.getName(), (String) meta.getValue()).apply();
            return;
        }
        if (meta.getValue() instanceof Float) {
            sharedPreferences.edit().putFloat(meta.getName(), (Float) meta.getValue()).apply();
            return;
        }
        if (meta.getValue() instanceof Boolean) {
            sharedPreferences.edit().putBoolean(meta.getName(), (Boolean) meta.getValue()).apply();
        }

    }

}
