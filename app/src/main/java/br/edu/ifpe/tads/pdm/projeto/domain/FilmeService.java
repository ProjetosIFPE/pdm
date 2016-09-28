package br.edu.ifpe.tads.pdm.projeto.domain;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import br.edu.ifpe.tads.pdm.projeto.R;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */
public class FilmeService extends BaseService {


    private final String SEARCH_URL_TMDB = "https://api.themoviedb.org/3/search/movie?api_key={key}&language=pt-BR&query={titulo}";

    /**
     * Realiza a consulta de filmes no TMDB, por título
     * @param context
     * @param titulo
     * @return
     */
    public List<Filme> getFilmes(Context context, String titulo){
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = SEARCH_URL_TMDB.replace("{key}", API_KEY).replace("{titulo}", titulo);
        return this.parseJSON(super.get(url));
    }

    private List<Filme> parseJSON(String json) {
        List<Filme> filmes = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        try {
            JSONObject root = new JSONObject(json);
            JSONArray resultados = root.getJSONArray("results");
            for (int i = 0; i < resultados.length(); i++) {
                JSONObject jsonObject = resultados.getJSONObject(i);
                Filme filme = new Filme();
                filme.setTitulo(jsonObject.getString("title"));
                Date dataLancamento = null;
                try {
                    dataLancamento = dateFormat.parse(jsonObject.getString("release_date"));
                    filme.setDataLancamento(dataLancamento);
                } catch (ParseException e) {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
                filme.setSinopse(jsonObject.getString("overview"));
                filme.setUrlPoster("https://image.tmdb.org/t/p/w500/" + jsonObject.getString("poster_path"));
                filmes.add(filme);
            }
        } catch (JSONException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return filmes;
    }

}
