package saschpe.dawandachallenge.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * Easy storage and retrieval of preferences.
 */
public class PreferencesHelper {

    private PreferencesHelper() {
        //no instance
    }

    public static SharedPreferences.Editor getEditor(@NonNull Context context) {
        return getSharedPreferences(context).edit();
    }

    public static SharedPreferences.Editor getEditor(@NonNull Context context, String preferencesName) {
        return getSharedPreferences(context, preferencesName).edit();
    }

    public static SharedPreferences getSharedPreferences(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences getSharedPreferences(@NonNull Context context, String preferencesName) {
        return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }
}
