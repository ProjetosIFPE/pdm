package br.edu.ifpe.tads.pdm.projeto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Categoria;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;

/**
 * Created by Douglas Albuquerque on 29/11/2016.
 */

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private List<Categoria> categorias;

    private Context context;

    private CategoriaOnClickListener categoriaOnClickListener;

    public CategoriaAdapter(Context context, List<Categoria> categorias, CategoriaOnClickListener onClickListener) {
        this.context = context;
        this.categorias = categorias;
        this.categoriaOnClickListener = onClickListener;
    }

    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.categorias_listitem, parent, false);
        CategoriaAdapter.CategoriaViewHolder holder = new CategoriaAdapter.CategoriaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        if (categoria != null) {
            holder.descricaoCategoria.setText(categoria.getDescricao());
        }
    }


    @Override
    public int getItemCount() {
        return categorias != null ? categorias.size() : 0;
    }

    class CategoriaViewHolder extends RecyclerView.ViewHolder {
        public TextView descricaoCategoria;

        public CategoriaViewHolder(View itemView) {
            super(itemView);
            descricaoCategoria = (TextView) itemView.findViewById(R.id.categoria_descricao);

        }
    }

    /**
     *  Aplica a ação de click através da interface CategoriaOnClickListener
     *  @param filmeViewHolder
     *  @param position
     * **/
    public View.OnClickListener getOnClickListener(final FilmeAdapter.FilmeViewHolder filmeViewHolder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( categoriaOnClickListener != null ) {
                    categoriaOnClickListener.onClickCategoria(filmeViewHolder.itemView, position);
                }
            }
        };
    }

    public interface CategoriaOnClickListener {
        public void onClickCategoria(View view, int idx);
    }

}
