package br.edu.ifpe.tads.pdm.projeto.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.otto.Bus;

import br.edu.ifpe.tads.pdm.projeto.domain.filme.CategoriaDao;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.DaoMaster;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.DaoSession;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeDao;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeManager;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.VideoDao;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.MusicaService;
import br.edu.ifpe.tads.pdm.projeto.service.MediaPlayerService;
import br.edu.ifpe.tads.pdm.projeto.util.PreferencesUtil;

/**
 * Created by Edmilson on 23/10/2016.
 */

public class ApplicationService extends Application {

    private final String TAG = getClass().getSimpleName();

    private static ApplicationService instance = null;

    public static final String DATABASE_NAME = "Application.db";

    private Bus bus = new Bus();

    private FilmeManager filmeManager;

    private DaoSession daoSession;

    private MusicaService musicaService;

    private MediaPlayerService mediaPlayerService;

    public static ApplicationService getInstance() {
        return instance;
    }

    public FilmeManager getFilmeManager() {
        return filmeManager;
    }

    public MusicaService getMusicaService() {
        return musicaService;
    }

    public Bus getBus() {
        return bus;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public FilmeDao getFilmeDao() {
        return getDaoSession().getFilmeDao();
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        daoSession = newSession();
        CategoriaDao categoriaDao = daoSession.getCategoriaDao();
        FilmeDao filmeDao = daoSession.getFilmeDao();
        VideoDao videoDao = daoSession.getVideoDao();

        filmeManager = new FilmeManager(videoDao, categoriaDao, filmeDao, new FilmeService());
        musicaService = new MusicaService();


    }



    private DaoSession newSession() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        return new DaoMaster(db).newSession();
    }


}
