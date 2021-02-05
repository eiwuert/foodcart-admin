package de.habibhaidari.foodcart.volley;

import com.android.volley.DefaultRetryPolicy;

public class FoodCartRetryPolicy extends DefaultRetryPolicy {

    public static final int MAX_RETRIES = -1;
    public static final int TIMEOUT_MS = 0;

    public FoodCartRetryPolicy() {
        super(TIMEOUT_MS,
                MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
