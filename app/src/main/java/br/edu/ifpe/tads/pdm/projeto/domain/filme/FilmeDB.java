package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Douglas Albuquerque on 15/11/2016.
 */

public class FilmeDB extends SQLiteOpenHelper{
    public static final String TAG = "sql";

    public static final String  NOME_BANCO = "tb_filmes_favoritos";
    private static final int VERSAO_BANCO = 1;

    public FilmeDB(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando a tabela favoritos...");
        String sql = "CREATE  TABLE IF NOT EXISTS filmes_favoritos(id INTEGER PRIMARY KEY autoincrement, " +
                "title TEXT NOT NULL, original_title TEXT NOT NULL);";
        db.execSQL(sql);
        Log.d(TAG, "Tabela favoritos criada com sucesso...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long save(Filme filme){
        long id = filme.getId();
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues data = new ContentValues();
            data.put("title", filme.getTitulo());
            data.put("original_title", filme.getTituloOriginal());
            if(id != 0){
                  // remove o filme da lista de favoritos
                  int count = db.delete("filmes_favoritos", "_title=?", new String[]{String.valueOf(filme.getTitulo())});
                    return count;
            }else{
                 //adiciona um registro na tabela
                id = db.insert("filmes_favoritos",null, data);
                return id;
            }
        }finally {
            db.close();
        }
}

    public List<Filme> listarTodos(){
        List<Filme> listFilmesFavoritos = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT * FROM filmes_favoritos;",null);
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String originalTitle = cursor.getString(cursor.getColumnIndex("original_title"));
                Filme filme = new Filme(title,originalTitle);
                listFilmesFavoritos.add(filme);

            }
            return listFilmesFavoritos;
        }finally {
            db.close();
        }

    }
}
