package saschpe.dawandachallenge.helper;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.app.supportv7.app.AppCompatPreferenceActivity;

public class ActionBarHelper {

    /**
     * Initializes the activity's action bar with up navigation enabled.
     * @param activity Parent activity
     */
    public static void initActionBar(@NonNull AppCompatActivity activity) {
        final ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Initializes the activity's action bar with up navigation enabled.
     * @param activity Parent activity
     */
    public static void initActionBar(@NonNull AppCompatPreferenceActivity activity) {
        final ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    /**
     * Initializes the activity's action bar with a toolbar.
     *
     * @param activity Parent activity
     * @param toolbarId Toolbar view ID
     */
    public static void initToolbar(@NonNull AppCompatActivity activity, int toolbarId) {
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbarId);
        activity.setSupportActionBar(toolbar);
        initActionBar(activity);
    }

    /**
     * Initializes the activity's action bar with a toolbar and a custom view.
     *
     * @param activity Parent activity
     * @param toolbarId Toolbar view ID
     * @param customViewId Custom view ID
     * @return Inflated custom view
     */
    public static View initToolbarWithCustomView(@NonNull AppCompatActivity activity, int toolbarId, int customViewId) {
        initToolbar(activity, toolbarId);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true); // Custom toolbar
            actionBar.setDisplayShowTitleEnabled(false);

            View customView = activity.getLayoutInflater().inflate(customViewId, null);
            actionBar.setCustomView(customView);
            return customView;
        }
        return null;
    }
}
