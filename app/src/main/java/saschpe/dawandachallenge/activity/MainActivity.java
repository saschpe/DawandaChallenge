package saschpe.dawandachallenge.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import saschpe.dawandachallenge.R;
import saschpe.dawandachallenge.activity.base.NavigationViewActivity;
import saschpe.dawandachallenge.fragment.CategoryFragment;
import saschpe.dawandachallenge.fragment.ProductFragment;
import saschpe.dawandachallenge.helper.ActionBarHelper;

public class MainActivity extends NavigationViewActivity implements
        CategoryFragment.OnClickedListener,
        FragmentManager.OnBackStackChangedListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean isTwoPane;

    private ActionBarDrawerToggle drawerToggle;
    private CategoryFragment categoryFragment;
    private ProductFragment productFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up action bar / custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        drawerLayout.setDrawerListener(drawerToggle);
        setSupportActionBar(toolbar);
        ActionBarHelper.initActionBar(this);

        // The detail container view will be present only in the large-screen layouts
        // (res/values-large and res/values-sw600dp). If this view is present, then the
        // activity should be in two-pane mode.
        isTwoPane = findViewById(R.id.detailContainer) != null;

        if (savedInstanceState == null) {
            getSupportFragmentManager().addOnBackStackChangedListener(this);
            categoryFragment = (CategoryFragment) getSupportFragmentManager().findFragmentById(R.id.categoryFragment);

            if (isTwoPane) {
                if (productFragment == null) {
                    productFragment = ProductFragment.newInstance();
                }
                replaceFragment(productFragment, false);
            }
        }
    }

    @Override
    protected void onStart() {
        // TODO: Select sth. different based on currently active fragment....
        selectedNavigationItem = R.id.navigation_item_categories;
        super.onStart();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void navigate(final int itemId) {
        switch (itemId) {
            case R.id.navigation_item_categories:
                //TODO(saschpe): Display fragment!
                break;
            case R.id.navigation_item_settings:
                //startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerToggle.onOptionsItemSelected(item);
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
                return true;
            case R.id.action_refresh:
                categoryFragment.reload();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int resId) {
        if (productFragment == null) {
            productFragment = ProductFragment.newInstance();
        }
        replaceFragment(productFragment, true);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        int id;
        if (isTwoPane) {
            id = R.id.detailContainer;
        } else {
            id = R.id.masterContainer;
        }
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(id, fragment);
        if (addToBackStack) {
            t.addToBackStack(fragment.getClass().getName());
            drawerToggle.setDrawerIndicatorEnabled(false);
        }
        t.commit();
    }

    @Override
    public void onBackStackChanged() {
        drawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
    }
}
