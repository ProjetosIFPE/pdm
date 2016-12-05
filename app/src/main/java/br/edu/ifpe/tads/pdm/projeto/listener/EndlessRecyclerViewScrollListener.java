package br.edu.ifpe.tads.pdm.projeto.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Edmilson on 04/12/2016.
 */

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    /**
     * O número mínimo de itens a ter abaixo da posição de rolagem atual antes de carregar mais.
     */
    private int visibleThreshold = 5;
    /**
     * O index da página atual
     */
    private int currentPage = 1;
    /**
     * Total de itens após carregar mais dados
     */
    private int previousTotalItemCount = 0;
    /**
     * É true se está esperando pelos dados a serem carregados
     **/
    private boolean loading = true;
    /**
     * Index da página inicial
     */
    private int startingPageIndex = 1;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int lastVisibleItemPosition = 0;

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();

        if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        checkTotalItems(totalItemCount);

        checkLoadingState(totalItemCount);

        checkVisibleThreshold(lastVisibleItemPosition, totalItemCount, recyclerView);

    }

    /**
     * Se a contagem de itens total for zero e a anterior não for,
     * suponha que a lista está invalidada e deve ser redefinida
     * novamente para o estado inicial
     *
     * @param totalItemCount
     */
    private void checkTotalItems(int totalItemCount) {
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = Boolean.TRUE;
            }
        }
    }

    /**
     * Se ainda estiver carregando, verificamos se a contagem
     * do conjunto de dados foi alterada, caso esteja alterada,
     * concluímos que terminou de carregar  e atualizamos
     * a contagem total de itens.
     *
     * @param totalItemCount
     */
    private void checkLoadingState(int totalItemCount) {
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = Boolean.FALSE;
            previousTotalItemCount = totalItemCount;
        }
    }

    /**
     * Se ele não estiver carregando no momento, verificamos se temos violado o
     * {@code visibleThreshold} e precisamos recarregar mais dados. Se precisarmos
     * recarregar mais alguns dados, executamos onLoadMore para buscar os dados.
     *
     * @param lastVisibleItemPosition
     * @param totalItemCount
     * @param recyclerView
     */
    private void checkVisibleThreshold(int lastVisibleItemPosition, int totalItemCount, RecyclerView recyclerView) {
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, recyclerView);
            loading = Boolean.TRUE;
        }
    }

    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = Boolean.TRUE;
    }


    public abstract boolean onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView);
}
