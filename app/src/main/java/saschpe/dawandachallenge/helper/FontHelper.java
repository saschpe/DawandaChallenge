package saschpe.dawandachallenge.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

/**
 * Custom font helper.
 */
public class FontHelper {

    private static Typeface liberationMonoRegular;

    private FontHelper() {

    }

    public static Typeface typefaceLiberationMonoRegular(@NonNull Context context) {
        if (liberationMonoRegular == null) {
            liberationMonoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/LiberationMono-Regular.ttf");
        }
        return liberationMonoRegular;
    }
}
