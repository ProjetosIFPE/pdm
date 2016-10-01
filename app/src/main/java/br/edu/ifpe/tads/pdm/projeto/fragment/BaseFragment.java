package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.support.v4.app.Fragment;

import br.edu.ifpe.tads.pdm.projeto.util.Task;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;

/**
 * Created by EdmilsonS on 30/09/2016.
 */

public class BaseFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();

    /**
     *  Inicia uma tarefa assíncrona
     *  @param listener
     *
     */
    protected static <T> void startTask( TaskListener<T> listener) {
        Task<T> task = new Task<>(listener);
        task.execute();
    }
}
