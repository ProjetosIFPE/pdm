package br.edu.ifpe.tads.pdm.pdm;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by Edmilson on 24/08/2016.
 */
public class BaseActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;

    public void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if ( toolbar != null ) {
            setSupportActionBar(toolbar);
        }

    }

    public void setUpNavDrawer() {

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(Boolean.TRUE);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if ( navigationView != null && drawerLayout != null ) {
            navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener());
        }
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(Boolean.TRUE);
                closeDrawer();
                return false;
            }
        };
    }

    private void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case android.R.id.home:
                if ( drawerLayout != null ) {
                    openDrawer();
                    return Boolean.TRUE;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}
