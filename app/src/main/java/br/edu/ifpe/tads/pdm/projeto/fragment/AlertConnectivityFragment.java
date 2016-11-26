package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.edu.ifpe.tads.pdm.projeto.R;


public class AlertConnectivityFragment extends BaseFragment {

    private AlertConnectivityListener alertConnectivityListener;
    public static final String ALERT_CONNECTIVITY_FRAGMENT = "ALERT_CONNECTIVITY_FRAGMENT";

    public static AlertConnectivityFragment newInstance(AlertConnectivityListener alertConnectivityListener) {
        AlertConnectivityFragment alertConnectivityFragment = new AlertConnectivityFragment();
        alertConnectivityFragment.setAlertConnectivityListener(alertConnectivityListener);
        return alertConnectivityFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_alert_connectivity, container, Boolean.FALSE);
        Button btTentarNovamente = (Button) view.findViewById(R.id.bt_tentar_novamente);
        btTentarNovamente.setOnClickListener(onClickBtTentarNovamente());
        return view;
    }

    public View.OnClickListener onClickBtTentarNovamente() {
       return new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (alertConnectivityListener != null) {
                   alertConnectivityListener.onConnectivityChange();
               }
           }
       };
    }

    public void setAlertConnectivityListener(AlertConnectivityListener alertConnectivityListener) {
        this.alertConnectivityListener = alertConnectivityListener;
    }
    public interface AlertConnectivityListener {
        void onConnectivityChange();
    }
}
