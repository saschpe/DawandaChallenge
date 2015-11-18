package saschpe.dawandachallenge.model;

import android.net.Uri;
import android.support.annotation.NonNull;

public class Product {
    public String title;
    public String price;
    public Uri listViewImageUri;

    public Product(@NonNull String title, @NonNull String price, @NonNull Uri listViewImageUri)
    {
        this.title = title;
        this.price = price;
        this.listViewImageUri = listViewImageUri;
    }
}
