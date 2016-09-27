package br.edu.ifpe.tads.pdm.projeto.activity;

import android.os.Bundle;

import br.edu.ifpe.tads.pdm.projeto.R;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
    }

}
