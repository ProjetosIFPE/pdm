package br.edu.ifpe.tads.pdm.projeto.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.Filme;
import br.edu.ifpe.tads.pdm.projeto.fragment.FilmeFragment;
import br.edu.ifpe.tads.pdm.projeto.fragment.PlaylistFragment;

public class FilmeActivity extends BaseActivity {

    public static final String FILME = "filme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);

        Intent intent = getIntent();

        Filme filme = (Filme) intent.getSerializableExtra(FilmeActivity.FILME);
        Log.d(TAG, filme.getTitulo());
        Bundle arguments = new Bundle();
        arguments.putSerializable(FilmeActivity.FILME, filme);

        FilmeFragment filmeFragment = FilmeFragment.newInstance(arguments);

        Bundle bundle = new Bundle();
        PlaylistFragment playlistFragment = PlaylistFragment.newInstance(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, filmeFragment).add(R.id.fragment_listmusic,playlistFragment).commit();


    }
}
