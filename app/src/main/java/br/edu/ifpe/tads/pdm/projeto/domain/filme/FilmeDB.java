package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.domain.BaseDB;

/**
 * Created by Douglas Albuquerque on 15/11/2016.
 */

public class FilmeDB extends BaseDB {
    public static final String TAG = FilmeDB.class.getSimpleName();

    public FilmeDB(Context context) {
        super(context);
    }


    public long salvar(Filme filme) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(BaseDB.ApplicationDBContract.Filme._ID, filme.getId());
            values.put(BaseDB.ApplicationDBContract.Filme.COLUMN_NAME_FILME_TITULO, filme.getTitulo());
            values.put(BaseDB.ApplicationDBContract.Filme.COLUMN_NAME_FILME_TITULO_ORIGINAL, filme.getTituloOriginal());
            values.put(ApplicationDBContract.Filme.COLUMN_NAME_FILME_SINOPSE, filme.getSinopse());
            long newId = db.insert(BaseDB.ApplicationDBContract.Filme.TABLE_NAME, null, values);
            return newId;
        } finally {
            db.close();
        }
    }

    public long remover(Filme filme) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String selectionClause = BaseDB.ApplicationDBContract
                    .Filme.COLUMN_NAME_FILME_TITULO_ORIGINAL + " LIKE ?";
            String[] selectionArgs = {filme.getTituloOriginal()};
            long count = db.delete(BaseDB.ApplicationDBContract.Filme.TABLE_NAME,
                    selectionClause, selectionArgs);
            return count;
        } finally {
            db.close();
        }

    }

    public boolean existe(Filme filme) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String selectionClause = BaseDB.ApplicationDBContract
                    .Filme.COLUMN_NAME_FILME_TITULO_ORIGINAL + " LIKE ?";
            String[] selectionArgs = {filme.getTituloOriginal()};
            Cursor cursor = db.query(BaseDB.ApplicationDBContract.Filme.TABLE_NAME, null,
                    selectionClause, selectionArgs, null, null, null, null);
            return cursor.getCount() > 0;
        } finally {
            db.close();
        }
    }

    public List<Filme> findAll() {

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String[] colunas = {BaseDB.ApplicationDBContract.Filme._ID,
                    BaseDB.ApplicationDBContract.Filme.COLUMN_NAME_FILME_TITULO,
                    BaseDB.ApplicationDBContract.Filme.COLUMN_NAME_FILME_TITULO_ORIGINAL,
                    BaseDB.ApplicationDBContract.Filme.COLUMN_NAME_FILME_SINOPSE};

            Cursor cursor = db.query(BaseDB.ApplicationDBContract.Filme.TABLE_NAME,
                    colunas, null, null, null, null, null);
            return toList(cursor);
        } finally {
            db.close();
        }
    }

    public List<Filme> toList(Cursor cursor) {
        List<Filme> filmes = new ArrayList<>();

        if (cursor.moveToFirst()) {
           do {
               Filme filme = new Filme();
               filme.setId(Long.valueOf(cursor.getInt(cursor.getColumnIndex(
                       BaseDB.ApplicationDBContract.Filme._ID))));
               filme.setTitulo(cursor.getString(cursor.getColumnIndex(
                       BaseDB.ApplicationDBContract.Filme.COLUMN_NAME_FILME_TITULO)));
               filme.setTituloOriginal(cursor.getString(cursor.getColumnIndex(
                       BaseDB.ApplicationDBContract.Filme.COLUMN_NAME_FILME_TITULO_ORIGINAL)));
               filme.setSinopse(cursor.getString(cursor.getColumnIndex(
                       ApplicationDBContract.Filme.COLUMN_NAME_FILME_SINOPSE)));
               filmes.add(filme);
           } while (cursor.moveToNext());
        }
        return filmes;
    }

    public List<Filme> getFavoritos() {
        return this.findAll();
    }


}
