package br.edu.ifpe.tads.pdm.projeto.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.edu.ifpe.tads.pdm.projeto.R;

public class FilmeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);
        super.setUpToolbar();
    }
}
