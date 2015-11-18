package saschpe.dawandachallenge.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import saschpe.dawandachallenge.R;
import saschpe.dawandachallenge.adapter.ProductAdapter;
import saschpe.dawandachallenge.model.Product;
import saschpe.dawandachallenge.service.task.ProductLoader;

public class ProductFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Product>> {

    private static final String TAG = ProductFragment.class.getName();

    private static final int PAGE_COUNT = 5;
    private int pageSize = -1;
    private int pageCounter = 0;
    private boolean isLoading;

    private ProductAdapter adapter;
    private Loader loader;

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ProductAdapter(getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_recycler, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && pageCounter < PAGE_COUNT) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= pageSize) {
                        // Maybe load a little earlier, not quite at the last item. But this way
                        // it is extra obvious :-)

                        // Emulate loading more data according to stage 2. Not quite paging but...
                        isLoading = true;
                        loader.forceLoad();
                        Log.d(TAG, "onScrolled() Page counter: " + pageCounter + " visible: " + firstVisibleItemPosition + " total: " + totalItemCount);
                        pageCounter++;
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Prepare the loader. Either re-connect with an existing one, or start a new one.
        loader = getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.products);
        }
    }

    @Override
    public Loader<List<Product>> onCreateLoader(int id, Bundle args) {
        isLoading = true;
        return new ProductLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> loader, List<Product> data) {
        // Normally, we would have a fixed page size (say 20 items) but stage 2 demands
        // to reload the entire data set five times. So our page size is the size of the first
        // (and basically all other data sets). We can use it to trigger a reload when the user
        // scrolled close to the bottom.
        if (pageSize < 0) {
            pageSize = data.size();
            Log.d(TAG, "onLoadFinished() And our page size is " + pageSize);
        }
        isLoading = false;
        adapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Product>> loader) {
        //NOTE(saschpe): Since we only store a reference to the data in the adapter,
        // we don't need to clear the adapter's data. It's all done by the loader in onReset()
        adapter.clear();
    }

    public void reload() {
        loader.forceLoad();
    }
}
