package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.application.ApplicationService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Categoria;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeManager;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeService;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;


public class NavigationViewFragment extends BaseFragment {

    private NavigationView navigationView;
    private List<Categoria> categorias;
    private MenuNavegacaoListener menuNavegacaoListener;
    private static final int ORDEM_MENU_CATEGORIAS = 2;

    public static NavigationViewFragment newInstance(Bundle arguments) {
        NavigationViewFragment navigationViewFragment = new NavigationViewFragment();
        navigationViewFragment.setArguments(arguments);
        return navigationViewFragment;
    }

    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de categoris de filmes
     **/
    public void getCategorias() {
        startTask(taskGetCategorias());
    }

    /**
     * Realiza a pesquisa de categorias de filmes
     **/
    private TaskListener<List<Categoria>> taskGetCategorias() {
        return new TaskListener<List<Categoria>>() {
            @Override
            public List<Categoria> execute() throws Exception {
                FilmeManager filmeManager = ApplicationService.getInstance().getFilmeManager();
                return categorias = filmeManager.getCategorias(getContext());
            }

            @Override
            public void updateView(List<Categoria> categorias) {
                popularCategoriasMenuNavegacao(navigationView);
                menuNavegacaoListener = (MenuNavegacaoListener) getActivity();
                menuNavegacaoListener.inicializarMenuNavegacao(navigationView);
            }

        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        getCategorias();

        return view;
    }

    private void popularCategoriasMenuNavegacao(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        if (categorias != null && !categorias.isEmpty()) {
            for (Categoria categoria : categorias) {
                menu.add(R.id.categorias, categoria.getId().intValue(),
                        ORDEM_MENU_CATEGORIAS, categoria.getDescricao());
            }
        }
    }

    public interface MenuNavegacaoListener {
        void inicializarMenuNavegacao(NavigationView navigationView);
    }

}
