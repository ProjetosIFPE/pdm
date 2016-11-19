package br.edu.ifpe.tads.pdm.projeto.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.adapter.FavoritoAdapter;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeDB;

import static br.edu.ifpe.tads.pdm.projeto.R.id.favoritos_recycler_view;

public class FavoritosActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Filme> filmes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        mRecyclerView = (RecyclerView) findViewById(favoritos_recycler_view);


        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(new FavoritoAdapter(this, filmes));
    }

    @Override
    protected void onResume() {
        FilmeDB db = new FilmeDB(this);
        filmes = db.listarTodos();
        super.onResume();
    }
}
