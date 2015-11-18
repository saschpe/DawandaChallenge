package saschpe.dawandachallenge.model;

import android.net.Uri;
import android.support.annotation.NonNull;

public class Category {
    public String name;
    public Uri imageUri;
    public boolean sticky;

    public Category(@NonNull String name, @NonNull Uri imageUri, @NonNull boolean sticky) {
        this.name = name;
        this.imageUri = imageUri;
        this.sticky = sticky;
    }
}
