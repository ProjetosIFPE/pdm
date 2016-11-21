package br.edu.ifpe.tads.pdm.projeto.application;

import android.app.Application;

import com.squareup.otto.Bus;

import br.edu.ifpe.tads.pdm.projeto.domain.BaseDB;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeService;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.MusicaService;

/**
 * Created by Edmilson on 23/10/2016.
 */

public class ApplicationService extends Application {

    private  final String TAG = getClass().getSimpleName();

    private static ApplicationService instance = null;

    private Bus bus = new Bus();

    private BaseDB baseDB = null;

    private FilmeService filmeService;

    private MusicaService musicaService;

    public static ApplicationService getInstance() {
        return instance;
    }

    public FilmeService getFilmeService() {
        return filmeService;
    }

    public MusicaService getMusicaService() {
        return musicaService;
    }

    public Bus getBus() {
        return bus;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        filmeService = new FilmeService();
        musicaService = new MusicaService();
    }


}
