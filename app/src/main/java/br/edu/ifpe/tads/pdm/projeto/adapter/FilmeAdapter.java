package br.edu.ifpe.tads.pdm.projeto.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.Filme;

/**
 * Created by Aluno on 28/09/2016.
 */
public class FilmeAdapter extends ArrayAdapter<Filme> {

    private Filme[] filmes;

    public FilmeAdapter(Context context, int resource, Filme[] filmes) {
        super(context, resource);
        Log.d("Adapter", "Filmes: " + filmes.length);
        this.filmes = filmes;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View listItem = null;
        ViewHolder holder = null;
        if ( view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            listItem = inflater.inflate(R.layout.filme_listitem, null, true);

            holder = new ViewHolder();
            holder.filmePoster = (ImageView) listItem.findViewById(R.id.filme_poster);
            holder.filmeTitulo = (TextView) listItem.findViewById(R.id.filme_titulo);

            listItem.setTag(holder);
        } else {
            listItem = view;
            holder = (ViewHolder) view.getTag();
        }

        Picasso.with(getContext()).load(filmes[position].getUrlPoster()).into(holder.filmePoster);
        holder.filmeTitulo.setText(filmes[position].getTitulo());

        return listItem;

    }

    static class ViewHolder {
        ImageView filmePoster;
        TextView filmeTitulo;
    }
}
