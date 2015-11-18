package saschpe.dawandachallenge.activity.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import saschpe.dawandachallenge.R;

/**
 * Activity with navigation view functionality.
 *
 * Takes care of initialization and generic callbacks. Needs proper activity
 * layout XML files to work. Properly handles portrait and landscape mode change.
 */
public class NavigationViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final long DRAWER_CLOSE_DELAY_MS = 250; // Fancier drawer close...
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "state_selected_navigation_item";
    protected int selectedNavigationItem;
    private final Handler drawerActionHandler = new Handler();
    protected DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedNavigationItem = savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM, R.id.navigation_item_categories);
        }
    }

    /**
     * Sub-classes should override this callback and set their navigation view item id appropriately!
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Select the correct nav menu item
        navigationView.getMenu().findItem(selectedNavigationItem).setChecked(true);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initNavigationView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initNavigationView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initNavigationView();
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) drawerLayout.findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Performs the actual navigation logic, updating the main content fragment etc.
     *
     * @param itemId Menu item ID
     */
    protected void navigate(final int itemId) {
        throw new UnsupportedOperationException("Sub-classes must implement navigate()!");
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        // Update highlighted item in the navigation menu
        menuItem.setChecked(true);
        selectedNavigationItem = menuItem.getItemId();

        // Allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening
        drawerLayout.closeDrawer(GravityCompat.START);
        drawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            /*case android.support.v7.appcompat.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;*/
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, selectedNavigationItem);
    }
}
