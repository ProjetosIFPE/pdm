package br.edu.ifpe.tads.pdm.projeto;

import android.test.AndroidTestCase;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.domain.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.FilmeService;

/**
 * Created by EdmilsonS on 26/09/2016.
 */
public class FilmeServiceTest extends AndroidTestCase {

    public void testGetFilmes() {
        FilmeService filmeService = new FilmeService();
        String querySearch = "Matrix";
        List<Filme> filmes = filmeService.getFilmes(getContext(), querySearch);
        assertFalse(filmes.isEmpty());
    }
}
