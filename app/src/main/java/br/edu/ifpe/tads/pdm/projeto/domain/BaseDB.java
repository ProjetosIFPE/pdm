package br.edu.ifpe.tads.pdm.projeto.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Edmilson Santana on 15/11/2016.
 */

public class BaseDB extends SQLiteOpenHelper {
    public static final String TAG = SQLiteOpenHelper.class.getSimpleName();

    public BaseDB(Context context) {
        super(context, ApplicationDBContract.DATABASE_NAME, null,
                ApplicationDBContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ApplicationDBContract.SQL_CREATE_FILME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(ApplicationDBContract.SQL_DELETE_FILME_TABLE);
        this.onCreate(db);
    }


    public static final class ApplicationDBContract {
        public static final String DATABASE_NAME = "Application.db";
        public static final int DATABASE_VERSION = 1;

        public static final String SQL_CREATE_FILME_TABLE =
                " CREATE TABLE IF NOT EXISTS " + Filme.TABLE_NAME + " ( " +
                        Filme._ID + " INTEGER PRIMARY KEY, " +
                        Filme.COLUMN_NAME_FILME_TITULO + " TEXT, " +
                        Filme.COLUMN_NAME_FILME_TITULO_ORIGINAL + " TEXT " + ")";

        public static final String SQL_DELETE_FILME_TABLE = "DROP TABLE IF EXISTS " + Filme.TABLE_NAME;

        public static abstract class Filme implements BaseColumns {
            public static final String TABLE_NAME = "Filme";
            public static final String COLUMN_NAME_FILME_TITULO = "titulo";
            public static final String COLUMN_NAME_FILME_TITULO_ORIGINAL = "titulo_original";
        }
    }

}
