package br.edu.ifpe.tads.pdm.projeto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import br.edu.ifpe.tads.pdm.projeto.application.ApplicationService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.DaoMaster;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.DaoSession;

/**
 * Created by Edmilson on 23/10/2016.
 */

@RunWith(AndroidJUnit4.class)
public class BaseTestCase  {
    public Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }

    protected DaoSession getDaoSession() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), ApplicationService.DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        return new DaoMaster(db).newSession();
    }

}
