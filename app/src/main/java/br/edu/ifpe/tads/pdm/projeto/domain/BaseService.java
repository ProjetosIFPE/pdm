package br.edu.ifpe.tads.pdm.projeto.domain;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */
public class BaseService {

    protected OkHttpClient client = new OkHttpClient();

    protected Logger LOG = Logger.getLogger(getClass().getSimpleName());

    /**
     * Get a Url
     * @return String
     * @param url
     * */
    public String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        String body = "";
        try {
            Response response = client.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return body;
    }
}
