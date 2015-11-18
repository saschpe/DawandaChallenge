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
import java.util.HashSet;
import java.util.List;

import saschpe.dawandachallenge.model.Category;

public class CategoryLoader extends AsyncTaskLoader<List<Category>> {
    private static final String TAG = CategoryLoader.class.getName();
    private static final String CATEGORY_URL = "http://public.dawanda.in/category.json";

    private ArrayList<Category> categories;

    public CategoryLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    public List<Category> loadInBackground() {
        Log.d(TAG, "loadInBackground(): Let's do it");
        categories = new ArrayList<>();
        final HashSet<String> categoryNamesAlreadyParsed = new HashSet<>();

        // Sorry, but Java's HttpURLConnection is a mess...
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(CATEGORY_URL)
                //.addHeader("Content-Type", "application/json; charset=utf-8") // Not quite needed here...
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final String responseString = new String(response.body().bytes(), "ISO-8859-1");
            Log.d(TAG, "loadInBackground(): Got response of size " + responseString.length());

            try {
                // Let's not get to fancy here and hard-code JSON node hierarchy...
                final JSONObject json = new JSONObject(responseString);
                final JSONObject jsonData = json.getJSONObject("data");
                final JSONArray jsonCategories = jsonData.getJSONArray("data");

                for (int i = 0; i < jsonCategories.length(); i++) {
                    final JSONObject jsonCategory = jsonCategories.getJSONObject(i);
                    final String jsonName = jsonCategory.getString("name");
                    if (!categoryNamesAlreadyParsed.contains(jsonName)) {
                        categoryNamesAlreadyParsed.add(jsonName); // Avoid duplicates
                        categories.add(new Category(
                                jsonName,
                                // Uri.normalizeScheme() is available starting from API level 16, so we just prefix http...
                                Uri.parse("http:" + jsonCategory.getString("image_url")),
                                jsonCategory.getBoolean("sticky")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace(); // Mhm, got invalid JSON?
            }
        } catch (IOException e) {
            e.printStackTrace(); // Too bad, network down?
        }

        return categories;
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (categories != null) {
            // If we currently have a result available, deliver it immediately
            deliverResult(categories);
        }
        if (takeContentChanged() || categories == null) {
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

        if (categories != null) {
            categories = null;
        }
    }
}
