package br.edu.ifpe.tads.pdm.projeto.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Video;

/**
 * Created by Edmilson Santana on 26/12/2016.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    protected static final String TAG = VideoAdapter.class.getSimpleName();

    private final List<Video> videos;

    private final Context context;

    private VideoAdapter.VideoOnClickListener videoOnClickListener;


    public VideoAdapter(Context context, List<Video> videos, VideoAdapter.VideoOnClickListener videoOnClickListener) {
        this.context = context;
        this.videos = videos;
        this.videoOnClickListener = videoOnClickListener;
    }

    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.videos_listitem, parent, false);
        VideoAdapter.VideoViewHolder holder = new VideoAdapter.VideoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VideoAdapter.VideoViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.descricaoVideo.setText(video.getDescricao());
        holder.img.setOnClickListener(getOnClickListener(holder, position));
    }


    /**
     * Aplica a ação de click através da interface {@link VideoOnClickListener}
     *
     * @param videoViewHolder
     * @param position
     **/
    public View.OnClickListener getOnClickListener(final VideoAdapter.VideoViewHolder videoViewHolder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoOnClickListener != null) {
                    videoOnClickListener.onClickVideo(videoViewHolder.itemView, position);
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return this.videos != null ? this.videos.size() : 0;
    }


    public interface VideoOnClickListener {
        void onClickVideo(View view, int idx);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView descricaoVideo;

        public VideoViewHolder(View itemView) {
            super(itemView);
            descricaoVideo = (TextView) itemView.findViewById(R.id.videoDescricao);
            img = (ImageView) itemView.findViewById(R.id.imgPlayVideo);
        }
    }
}
