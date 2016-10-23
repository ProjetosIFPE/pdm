package br.edu.ifpe.tads.pdm.projeto.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.service.PlayerService;

public class MediaPlayerActivity extends BaseActivity {

    private Intent serviceIntent;

    private PlayerService playerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        ImageButton buttonRew = (ImageButton) findViewById(R.id.media_rew);
        buttonRew.setOnClickListener(onPlayerRewind());
        ImageButton buttonPause = (ImageButton) findViewById(R.id.media_pause);
        buttonPause.setOnClickListener(onPlayerStop());
        ImageButton buttonPlay = (ImageButton) findViewById(R.id.media_play);
        buttonPlay.setOnClickListener(onPlayerStart());
        ImageButton buttonForward = (ImageButton) findViewById(R.id.media_ff);
        buttonForward.setOnClickListener(onPlayerForward());

        serviceIntent = new Intent(getContext(), PlayerService.class);


    }

    public View.OnClickListener onPlayerStart() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, serviceIntent.toString());
                startService(serviceIntent);
                bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        };
    }

    public View.OnClickListener onPlayerStop() {
       return new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (playerService.isMediaPlayerPlaying()) {
                   unbindService(serviceConnection);
                   stopService(serviceIntent);
               }
           }
       };
    }

    public View.OnClickListener onPlayerRewind() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (playerService.isMediaPlayerPlaying()) {
                    playerService.mediaPlayerBackward();
                }
             }
        };
    }

    public View.OnClickListener onPlayerForward() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerService.isMediaPlayerPlaying()) {
                    playerService.mediaPlayerForward();
                }
            }
        };
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playerService = ((PlayerService.PlayerServiceBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playerService = null;
        }
    };
}
