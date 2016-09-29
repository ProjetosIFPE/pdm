package br.edu.ifpe.tads.pdm.projeto.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.util.Task;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;

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
