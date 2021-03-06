package de.habibhaidari.foodcart.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

public class InputStreamRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;
    //create a static map for directly accessing headers
    public Map<String, String> responseHeaders;

    public InputStreamRequest(int post, String mUrl, Response.Listener<byte[]> listener,
                              Response.ErrorListener errorListener) {
        super(post, mUrl, errorListener);
        // this request would never use cache.
        setShouldCache(false);
        mListener = listener;
    }



    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        //Initialise local responseHeaders map with response headers received
        responseHeaders = response.headers;

        //Pass the response data here
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
}