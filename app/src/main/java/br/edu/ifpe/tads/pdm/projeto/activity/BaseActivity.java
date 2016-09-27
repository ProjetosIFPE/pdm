package br.edu.ifpe.tads.pdm.projeto.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.edu.ifpe.tads.pdm.projeto.R;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */
public class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    /**
     * Aplica a Toolbar como Action Bar
     * *
     * */
    protected void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
