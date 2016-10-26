package br.edu.ifpe.tads.pdm.projeto.util;

/**
 * Created by EdmilsonS on 29/09/2016.
 */

public interface TaskListener<T> {

    T execute() throws Exception;

    void updateView(T  response);

}
