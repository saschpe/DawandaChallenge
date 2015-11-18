package saschpe.dawandachallenge.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private ProductAdapter adapter;
    private Loader loader;

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ProductAdapter();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_recycler, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

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
        return new ProductLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> loader, List<Product> data) {
        adapter.setData(data);
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
