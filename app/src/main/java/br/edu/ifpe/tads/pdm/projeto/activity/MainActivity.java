package br.edu.ifpe.tads.pdm.projeto.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.adapter.TabsAdapter;
import br.edu.ifpe.tads.pdm.projeto.fragment.FilmesFragment;


/**
 * Created by Edmilson Santana on 30/09/2016.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setUpNavDrawer();
        setUpViewPagerTabs();
    }



    /**
     * Configura as abas da tela
     */
    private void setUpViewPagerTabs() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new TabsAdapter(getContext(), getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        int cor = ContextCompat.getColor(getContext(), R.color.white);

        tabLayout.setTabTextColors(cor, cor);
    }





}
