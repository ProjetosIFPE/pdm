package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.edu.ifpe.tads.pdm.projeto.R;


public class AlertNoResultsFragment extends BaseFragment {

    public static final String ALERT_NO_RESULTS_FRAGMENT = "ALERT_NO_RESULTS_FRAGMENT";

    public static AlertNoResultsFragment newInstance(Bundle arguments) {
        AlertNoResultsFragment alertNoResultsFragment = new AlertNoResultsFragment();
        alertNoResultsFragment.setArguments(arguments);
        return alertNoResultsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert_no_results, container, false);
        return view;
    }
}
