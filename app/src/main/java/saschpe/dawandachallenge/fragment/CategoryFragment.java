package saschpe.dawandachallenge.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import saschpe.dawandachallenge.R;
import saschpe.dawandachallenge.adapter.CategoryAdapter;
import saschpe.dawandachallenge.model.Category;
import saschpe.dawandachallenge.service.task.CategoryLoader;

public class CategoryFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Category>>,
        View.OnClickListener {

    // Container Activity must implement this interface
    public interface OnClickedListener {
        void onClick(int resId);
    }

    private static OnClickedListener dummy = new OnClickedListener() {
        @Override
        public void onClick(int buttonId) {
        }
    };

    private OnClickedListener onClickListener = dummy;
    private CategoryAdapter adapter;
    private Loader loader;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CategoryAdapter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_recycler, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented the callback interface. If not, it throws an exception
        try {
            onClickListener = (OnClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onClickListener = dummy;
    }

    @Override
    public void onResume() {
        super.onResume();
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.categories);
        }
    }

    @Override
    public void onClick(View v) {
        // Since 'this' is a click listener, it can be already used in the adapter for clicking
        // stuff in view holders. As soon as the fragment is attached to an activity, click events
        // are propagated.
        onClickListener.onClick(v.getId());
    }

    @Override
    public Loader<List<Category>> onCreateLoader(int id, Bundle args) {
        return new CategoryLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Category>> loader, List<Category> data) {
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Category>> loader) {
        //NOTE(saschpe): Since we only store a reference to the data in the adapter,
        // we don't need to clear the adapter's data. It's all done by the loader in onReset()
        adapter.clear();
    }

    public void reload() {
        loader.forceLoad();
    }
}
