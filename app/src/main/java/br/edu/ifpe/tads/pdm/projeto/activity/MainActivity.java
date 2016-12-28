package br.edu.ifpe.tads.pdm.projeto.activity;

import android.os.Bundle;
import android.view.Menu;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.adapter.FilmesPagerAdapter;


/**
 * Created by Edmilson Santana on 30/09/2016.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setUpMenuNavegacao(savedInstanceState);

        FilmesPagerAdapter filmesPagerAdapter = new FilmesPagerAdapter(getContext(),
                getSupportFragmentManager());
        super.setUpViewPagerTabs(filmesPagerAdapter);


        getSupportActionBar().setTitle(R.string.inicio);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.createSearchWidget(menu);
        return Boolean.TRUE;
    }


}
