package de.habibhaidari.foodcart.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.habibhaidari.foodcart.BuildConfig;
import de.habibhaidari.foodcart.callback.model.ShowCallback;
import de.habibhaidari.foodcart.volley.AuthRequest;
import de.habibhaidari.foodcart.volley.InputStreamRequest;

import static de.habibhaidari.foodcart.constant.FormatConstants.APPLICATION_NAME;
import static de.habibhaidari.foodcart.constant.FormatConstants.FILE_RETRIVAL_FAILED;
import static de.habibhaidari.foodcart.constant.FormatConstants.SERVER_CONNECTION_FAILED;

public abstract class FileService {


    private static final String DEFAULT_FILENAME = "file";
    private final Context context;
    private final RequestQueue requestQueue;
    public final LinkedList<Request> backupQueue = new LinkedList<>();

    public static final int FILENAME_SPLIT_LIMIT = 3;
    public static final String HEADER_DELIMITER = "\"";
    public static final String SLASH_CHAR = "/";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    final List<ShowCallback<File>> showCallbackListener = new ArrayList<>();


    public FileService(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);

    }


    public void addShowCallbackListener(ShowCallback<File> showCallback) {
        showCallbackListener.add(showCallback);
    }

    public void removeShowCallbackListener(ShowCallback<File> showCallback) {
        showCallbackListener.remove(showCallback);
    }

    public void show(String identifier) {
        Log.d(FileService.class.getName(), "Sending new Show Request");
        InputStreamRequest req = new InputStreamRequest(
                Request.Method.GET,
                BuildConfig.APP_HOST + getRoute() + SLASH_CHAR + identifier,
                this::handleShow,
                this::handleError
        );
        backupQueue.add(req);
        requestQueue.add(req);
    }

    private void handleShow(byte[] file) {
        InputStreamRequest r = (InputStreamRequest) backupQueue.pop();
        File localFile = saveFileLocal(file, r.responseHeaders);
        for (ShowCallback<File> callback : showCallbackListener) {
            callback.onShowed(localFile);
        }
    }

    protected File saveFileLocal(byte[] file, Map<String, String> responseHeaders) {
        File localFile = null;
        String filename = DEFAULT_FILENAME;
        try {
            //Checking the availability state of the External Storage.
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state) || file == null) {
                return null; // throw exception
            }

            //Read file name from headers
            String disposition = responseHeaders.get(HEADER_CONTENT_DISPOSITION);
            if (disposition != null) {
                filename = disposition.split(HEADER_DELIMITER, FILENAME_SPLIT_LIMIT)[1];
            }
            Log.d(FileService.class.getName(), filename);

            localFile = new File(context.getExternalFilesDir(APPLICATION_NAME + SLASH_CHAR + getLocalFoldername()), filename);
            localFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(localFile, true);
            outputStream.write(file);
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            Log.d(FileService.class.getName(), e.toString());
            e.printStackTrace();
        }
        return localFile;
    }


    private void handleError(VolleyError volleyError) {
        if (volleyError.networkResponse == null) {
            Toast.makeText(context, SERVER_CONNECTION_FAILED, Toast.LENGTH_LONG).show();
            backupQueue.pop();
        } else if (volleyError.networkResponse.statusCode == 403) {
            requestQueue.add(AuthRequest.newInstance(context, e -> requestQueue.add(backupQueue.getFirst())));
        } else {
            Toast.makeText(context, String.format(FILE_RETRIVAL_FAILED, volleyError.networkResponse.statusCode), Toast.LENGTH_LONG).show();
            backupQueue.pop();
        }
    }

    abstract String getRoute();

    abstract String getLocalFoldername();
}