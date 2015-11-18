package saschpe.dawandachallenge.service.task;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import saschpe.dawandachallenge.model.Product;

/**
 * Shameless copy of CategoryLoader.
 */
public class ProductLoader extends AsyncTaskLoader<List<Product>> {
    private static final String TAG = ProductLoader.class.getName();
    private static final String PRODUCT_URL = "http://public.dawanda.in/products.json";

    private ArrayList<Product> products;

    public ProductLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    public List<Product> loadInBackground() {
        Log.d(TAG, "loadInBackground(): Let's do it");
        products = new ArrayList<>();

        // I can't stand stock Android's HttpClient, sorry...
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(PRODUCT_URL)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final String responseString = response.body().string();
            Log.d(TAG, "loadInBackground(): Got response:\n" + responseString);

            try {
                // Let's not get to fancy here and hard-code JSON node hierarchy...
                final JSONObject json = new JSONObject(responseString);
                final JSONObject jsonData = json.getJSONObject("data").getJSONObject("data");
                final JSONArray jsonProducts = jsonData.getJSONArray("products");


                for (int i = 0; i < jsonProducts.length(); i++) {
                    final JSONObject jsonProduct = jsonProducts.getJSONObject(i);

                    final JSONObject jsonPrice = jsonProduct.getJSONObject("price");

                    // TODO/FIXME: Currency conversion / locale, you name it
                    final String formattedPrice = jsonPrice.getString("cents"); // symbol..

                    products.add(new Product(
                            jsonProduct.getString("title"),
                            formattedPrice,
                            // Uri.normalizeScheme() is available starting from API level 16, so we just prefix http...
                            Uri.parse("http:" + jsonProduct.getJSONObject("default_image").getString("listview"))));
                }
            } catch (JSONException e) {
                e.printStackTrace(); // Mhm, got invalid JSON?
            }
        } catch (IOException e) {
            e.printStackTrace(); // Too bad, network down?
        }

        return products;
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (products != null) {
            // If we currently have a result available, deliver it immediately
            deliverResult(products);
        }
        if (takeContentChanged() || products == null) {
            // If the data has changed since last time it was loaded or is not currently
            // available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        Log.d(TAG, "onStopLoading()");
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        Log.d(TAG, "onReset()");
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (products != null) {
            products = null;
        }
    }
}
