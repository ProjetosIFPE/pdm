package br.edu.ifpe.tads.pdm.projeto.activity;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.util.Task;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */
public class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    protected DrawerLayout drawerLayout;

    /**
     * Aplica a Toolbar como Action Bar
     * *
     * */
    protected void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null ) {
            setSupportActionBar(toolbar);
        }
    }

    /**
     *  Configura o menu lateral
     */
    protected void setUpNavDrawer() {
        final ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(Boolean.TRUE);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null && drawerLayout != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            item.setChecked(Boolean.TRUE);
                            closeDrawner();
                            onNavDrawerItemSelected(item);
                            return false;
                        }
                    });
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch( item.getItemId() ) {
            case android.R.id.home:
                if (drawerLayout != null) {
                    openDrawner();
                    return Boolean.TRUE;
                }
        }
        return  super.onOptionsItemSelected(item);
    }



    /**
     * Eventos de click no menu de navegação lateral
     * @param menuItem
     */
    public static void onNavDrawerItemSelected(MenuItem menuItem) {

    }

    /**
     * Abrir menu de navegação lateral
     */
    protected void openDrawner() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * Fechar menu de navegação lateral
     */
    protected void closeDrawner() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    protected void toast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }



    /**
     *  Inicia uma tarefa assíncrona
     *  @param listener
     *
     */
    protected <T> void startTask( TaskListener<T> listener) {
        Task<T> task = new Task<>(listener);
        task.execute();
    }

    /**
     *  Retorna o contexto da Activity
     *
     * **/
    protected Context getContext() {
        return this;
    }



}
