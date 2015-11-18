package saschpe.dawandachallenge.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import saschpe.dawandachallenge.R;
import saschpe.dawandachallenge.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> data;
    private Context context;
    private int lastPosition = -1;

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;
        private TextView price;
        private View container;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            container = itemView.findViewById(R.id.container);
        }

        public void bindTo(@NonNull Product product) {
            title.setText(product.title);
            price.setText(product.price);
            Picasso.with(itemView.getContext()).load(product.listViewImageUri).into(image); // Async for life!
        }
    }

    public ProductAdapter(@NonNull Context context) {
        data = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.view_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bindTo(data.get(position));

        setAnimation(holder.container, position);
    }

    private void setAnimation(View view, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
            view.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(@NonNull List<Product> data) {
        this.data.clear();
        addAll(data);
    }

    public void addAll(@NonNull List<Product> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}
