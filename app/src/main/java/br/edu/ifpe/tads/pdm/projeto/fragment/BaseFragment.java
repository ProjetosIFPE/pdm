package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.util.NetworkUtil;
import br.edu.ifpe.tads.pdm.projeto.util.Task;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;

/**
 * Created by EdmilsonS on 30/09/2016.
 */

public abstract class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    private View fragmentView;

    /**
     * Inicia uma tarefa assíncrona
     *
     * @param listener
     */
    protected static <T> void startTask(TaskListener<T> listener) {
        Task<T> task = new Task<>(listener);
        task.execute();
    }

    protected void toast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Adiciona o fragmento com um alerta de nenhum resultado disponível
     */
    protected void adicionarAlertaNenhumResultadoDisponível(int fragmentId) {
        Bundle arguments = new Bundle();
        AlertNoResultsFragment alertNoResultsFragment = AlertNoResultsFragment.newInstance(arguments);
        if (getActivity() != null) {
            getChildFragmentManager().beginTransaction()
                    .add(fragmentId, alertNoResultsFragment,
                            AlertNoResultsFragment.ALERT_NO_RESULTS_FRAGMENT).commit();
        }
    }


    /**
     * Remover fragmento de alerta de nenhum resultado disponível
     */
    protected void removerAlertaNenhumResultado() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(
                AlertNoResultsFragment.ALERT_NO_RESULTS_FRAGMENT);
        if (fragment != null) {
            getChildFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .remove(fragment).commit();
        }
    }


}
