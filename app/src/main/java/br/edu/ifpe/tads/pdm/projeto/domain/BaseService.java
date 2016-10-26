package br.edu.ifpe.tads.pdm.projeto.domain;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.parceler.apache.commons.lang.time.DateUtils;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.edu.ifpe.tads.pdm.projeto.util.DateUtil;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */
public class BaseService {

    protected final  OkHttpClient client = new OkHttpClient();

    protected  Logger LOG = Logger.getLogger(BaseService.class.getSimpleName());

    protected final GsonBuilder builder = new GsonBuilder();

    private final String AUTHENTICATION_HEADER = "Authorization";
    /**
     * Get a Url
     * @return String
     * @param url
     * */
    public  String get(String url) {
        Request request = new Request.Builder()
                .url(url).build();
        String body = "";
        try {
            Response response = client.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return body;
    }
    /**
     * Get a Url, com autenticação de usuário e senha
     * @return String
     * @param url
     * */
    public  String get(String url, String username, String password) {
        Request request = new Request.Builder()
                .url(url).build();
        String body = "";
        try {
            OkHttpClient newClient = new OkHttpClient.Builder()
                    .authenticator(getAuthenticationMethod(username, password)).build();
            Response response = newClient.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return body;
    }

    /**
     * Configura o método de autenticação utilizando username e password
     * **/
    public Authenticator getAuthenticationMethod(final String username, final String password) {
        return new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder()
                        .header(AUTHENTICATION_HEADER, credential).build();
            }
        };
    }


    /**
     *  Faz o parse de uma string json, removendo o objeto root, retornando um
     *  array da classe especificada no parametro
     * **/
    public <T> List<T> parseJson(Class<T[]> clazz, String json, String rootElement) {
        final Gson gson = createGson();
        Map<String, Object> map = gson.fromJson(json, Map.class);
        String jsonArray = gson.toJson(map.get(rootElement));
        return new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray, clazz)));
    }

    /**
     * Builder do objeto gson, formatando campo de datas
     * @return
     */
    public Gson createGson() {
        return builder.registerTypeAdapter(Date.class, parseDateType()).create();
    }

    /**
     * Cria um deserializer para o tipo Date
     * @return
     */
    public JsonDeserializer<Date> parseDateType() {
        return new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return DateUtil.stringToDate(json.getAsString());
            }
        };
    }


}
