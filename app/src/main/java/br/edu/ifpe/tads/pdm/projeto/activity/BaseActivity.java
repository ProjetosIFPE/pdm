package br.edu.ifpe.tads.pdm.projeto.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.fragment.NavigationViewFragment;
import br.edu.ifpe.tads.pdm.projeto.fragment.dialog.AboutDialog;
import br.edu.ifpe.tads.pdm.projeto.util.Task;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */
public class BaseActivity extends AppCompatActivity implements NavigationViewFragment.MenuNavegacaoListener {

    protected final String TAG = getClass().getSimpleName();
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected final String ID_MENU_SELECIONADO = "ID_MENU_SELECIONADO";

    /**
     * Aplica a Toolbar como Action Bar
     * *
     */
    protected Toolbar setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
        }
        return toolbar;
    }


    /**
     * Configura o menu lateral
     */
    protected void setUpMenuNavegacao(Bundle savedInstanceState) {
        final ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(Boolean.TRUE);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if  (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            NavigationViewFragment navigationViewFragment = NavigationViewFragment.newInstance(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.drawer_layout, navigationViewFragment).commit();
        }

    }


    @Override
    public void inicializarMenuNavegacao(NavigationView navigationView) {
        this.navigationView = navigationView;

        selecionarItemMenuNavegacao();

        if (navigationView != null && drawerLayout != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    item.setChecked(Boolean.TRUE);
                    closeDrawner();
                    onNavDrawerItemSelected(item);
                    return false;
                }
            });
        }
    }

    /**
     * Marcar item selecionado no menu de navegação
     */
    public void selecionarItemMenuNavegacao() {
        if (navigationView != null) {
            Intent intent = getIntent();
            if (intent != null) {
                int idMenuSelecionado = intent.getIntExtra(ID_MENU_SELECIONADO, 0);
                MenuItem item = navigationView.getMenu().findItem(idMenuSelecionado);
                item.setChecked(Boolean.TRUE);
            }
        }
    }

    /**
     * Configura as abas da tela
     */
    protected void setUpViewPagerTabs(FragmentPagerAdapter fragmentPagerAdapter) {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setOffscreenPageLimit(fragmentPagerAdapter.getCount());
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Eventos de click no menu de navegação lateral
     *
     * @param
     */
    public void onNavDrawerItemSelected(MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_item_inicio:
                intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra(ID_MENU_SELECIONADO, menuItem.getItemId());
                startActivity(intent);
                break;
            case R.id.nav_about_us:
                AboutDialog.showAbout(getSupportFragmentManager());
                break;
            default:
                intent = new Intent(getContext(), CategoriaActivity.class);
                intent.putExtra(CategoriaActivity.CATEGORIA_FILME, menuItem.getTitle());
                intent.putExtra(ID_MENU_SELECIONADO, menuItem.getItemId());
                startActivity(intent);
        }
    }

    /**
     * Configura o SearchView do menu da Toolbar com as configurações de pesquisa
     *
     * @param menu
     **/
    public void createSearchWidget(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout != null) {
                    openDrawner();
                    return Boolean.TRUE;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
     * Inicia uma tarefa assíncrona
     *
     * @param listener
     */
    protected <T> void startTask(TaskListener<T> listener) {
        Task<T> task = new Task<>(listener);
        task.execute();
    }

    /**
     * Retorna o contexto da Activity
     **/
    protected Context getContext() {
        return this;
    }


}
