package br.edu.ifpe.tads.pdm.projeto.application;

import android.app.Application;

import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeService;

/**
 * Created by Edmilson on 23/10/2016.
 */

public class ApplicationService extends Application {

    private  final String TAG = getClass().getSimpleName();

    private static ApplicationService instance = null;

    private FilmeService filmeService;

    public static ApplicationService getInstance() {
        return instance;
    }

    public FilmeService getFilmeService() {
        return filmeService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        filmeService = new FilmeService();
    }
}
