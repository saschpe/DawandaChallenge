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
import saschpe.dawandachallenge.model.Category;

import static saschpe.dawandachallenge.helper.FontHelper.typefaceLiberationMonoRegular;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> data;
    private Context context;
    private View.OnClickListener onClickListener;
    private int lastPosition = -1;

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView image;
        private View container;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
            container = itemView.findViewById(R.id.container);
        }

        public void bindTo(@NonNull Category category) {
            name.setText(category.name);
            // Use a custom font here...
            name.setTypeface(typefaceLiberationMonoRegular(itemView.getContext()));
            Picasso.with(itemView.getContext()).load(category.imageUri).into(image); // Async for life!
        }
    }

    public CategoryAdapter(@NonNull Context context, View.OnClickListener onClickListener) {
        data = new ArrayList<>();
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.view_category, parent, false);
        v.setOnClickListener(onClickListener);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bindTo(data.get(position));

        setAnimation(holder.container, position);
    }

    private void setAnimation(View view, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            view.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(@NonNull List<Category> data) {
        this.data.clear();
        addAll(data);
    }

    public void addAll(@NonNull List<Category> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}
