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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import saschpe.dawandachallenge.model.Category;

public class CategoryLoader extends AsyncTaskLoader<List<Category>> {
    private static final String TAG = CategoryLoader.class.getName();
    private static final String CATEGORY_URL = "http://public.dawanda.in/category.json";
    private static final String BACKING_STORE_FILENAME = "category.json";
    private static final int MAX_TRIES = 3;  // How often do we try network requests..

    private ArrayList<Category> categories;

    public CategoryLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    public List<Category> loadInBackground() {
        Log.d(TAG, "loadInBackground(): Let's do it");
        categories = new ArrayList<>();
        final HashSet<String> categoryNamesAlreadyParsed = new HashSet<>();

        // Caution, messy code ahead!
        String responseString = null;

        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(getContext().openFileInput(BACKING_STORE_FILENAME));
            // Use magic scanner trick to fetch us a string from that pesky stream.
            java.util.Scanner s = new java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A");
            responseString = s.hasNext() ? s.next() : "";
            Log.d(TAG, "loadInBackground(): Successfully loaded from file " + BACKING_STORE_FILENAME);
        } catch (IOException e) {
            // Sorry, but Java's HttpURLConnection is a mess...
            final OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().url(CATEGORY_URL).build();

            // Fetch stuff and threat network errors, too...
            boolean success = false;
            int tries = 0;
            do {
                try {
                    Response response = client.newCall(request).execute();
                    if (tries == 0) {
                        throw new IOException("Childishly simulate a network error!");
                    }
                    responseString = new String(response.body().bytes(), "ISO-8859-1");
                    Log.d(TAG, "loadInBackground(): Got response of size " + responseString.length());
                    success = true;
                } catch (IOException ey) {
                    ey.printStackTrace(); // Too bad, network down or invalid encoding or garbage?
                    Log.d(TAG, "loadInBackground(): Whoopsie, a network error. Let's try again...");
                    tries++; // Let's try again. We could add some exponential backoff though...
                }
            } while (!success && tries < MAX_TRIES);

            // Persist data. Nobody said "use a content provider" :-)
            try {
                OutputStream outputStream = new BufferedOutputStream(getContext().openFileOutput(BACKING_STORE_FILENAME, Context.MODE_PRIVATE));
                outputStream.write(responseString.getBytes());
                outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace(); // Jeez, I just love Java ;-)
                }
            }
        }

        if (responseString != null) {
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
