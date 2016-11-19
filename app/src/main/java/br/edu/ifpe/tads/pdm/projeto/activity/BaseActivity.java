package br.edu.ifpe.tads.pdm.projeto.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
public class BaseActivity extends AppCompatActivity  {

    protected final String TAG = getClass().getSimpleName();

    private final int MENU_GROUP_ID = 1;

    protected DrawerLayout drawerLayout;

    protected NavigationView navigationView;

    protected static int navIndexItem = 0;

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
        navigationView = (NavigationView) findViewById(R.id.nav_view);

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


    /**
     * Eventos de click no menu de navegação lateral
     * @param
     */
    public void onNavDrawerItemSelected(MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_item_inicio:
                intent = new Intent(getContext(), MainActivity.class);
                break;
            default:
                intent = new Intent(getContext(), CategoriaActivity.class);
                intent.putExtra(CategoriaActivity.CATEGORIA_FILME, menuItem.getTitle());
        }

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.createSearchWidget(menu);
        return Boolean.TRUE;
    }

    /**
     * Configura o SearchView do menu da Toolbar com as configurações de pesquisa
     * @param menu
     * **/
    public void createSearchWidget(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch( item.getItemId() ) {
            case android.R.id.home:
                if (drawerLayout != null) {
                    openDrawner();
                    return Boolean.TRUE;
                }
                break;
            default:
                Intent intent = new Intent(getContext(), SpotifyPlayerActivity.class);
                startActivity(intent);
        }
        return  super.onOptionsItemSelected(item);
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
