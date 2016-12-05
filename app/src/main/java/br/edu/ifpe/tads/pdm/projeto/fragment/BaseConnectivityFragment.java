package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.edu.ifpe.tads.pdm.projeto.util.NetworkUtil;
import br.edu.ifpe.tads.pdm.projeto.util.Task;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;

/**
 * Created by EdmilsonS on 30/09/2016.
 */

public abstract class BaseConnectivityFragment extends BaseFragment implements AlertConnectivityFragment.AlertConnectivityListener {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createFragmentView(inflater, container);
        setFragmentView(view);

        if (NetworkUtil.isConnected(getContext())) {
            startFragmentTask();
        } else {
            adicionarAlertaConexaoIndisponivel();
        }
        return fragmentView;
    }

    /**
     * Adiciona o fragmento com um alerta de conexão indisponível
     */
    private void adicionarAlertaConexaoIndisponivel() {
        AlertConnectivityFragment alertConnectivityFragment = AlertConnectivityFragment.newInstance(this);
        getChildFragmentManager().beginTransaction()
                .add(getFragmentViewId(), alertConnectivityFragment,
                        AlertConnectivityFragment.ALERT_CONNECTIVITY_FRAGMENT).commit();
    }


    /**
     * Remover fragmento de alerta de conexão indisponível
     */
    private void removerAlertaConexaoIndisponivel() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(
                AlertConnectivityFragment.ALERT_CONNECTIVITY_FRAGMENT);
        if (fragment != null) {
            getChildFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .remove(fragment).commit();
        }
    }

    @Override
    public void onConnectivityChange() {
        if (NetworkUtil.isConnected(getContext())) {
            removerAlertaConexaoIndisponivel();
            startFragmentTask();
        }
    }

    protected View getFragmentView() {
        return fragmentView;
    }

    protected void setFragmentView(View view) {
        this.fragmentView = view;
    }

    protected int getFragmentViewId() {
        int id = 0;
        if (getFragmentView() != null) {
            id = getFragmentView().getId();
        }
        return id;
    }

    abstract View createFragmentView(LayoutInflater inflater, ViewGroup container);

    abstract void startFragmentTask();
}
