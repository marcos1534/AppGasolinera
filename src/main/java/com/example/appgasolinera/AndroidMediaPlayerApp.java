package com.example.appgasolinera;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AndroidMediaPlayerApp extends Activity {
    private List<Cancion> cancionList;
    private static MediaPlayer mediaPlayer;
    private TextView txtTitulo, txtTiempoRestante;
    private ImageButton btnPlay, btnStop, btnNext, btnPrev, btnDecelerar, btnAcelerar;
    private SeekBar seekbar;
    private Handler handler = new Handler();
    private static boolean isPlaying = false;
    private ImageView image;
    private static Cancion cancionActual;
    private static int cancionIndex = -1;
    private LinearLayout songContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reproductor);

        initializeViews();
        initializeSongs();
    }

    private void initializeViews() {
        txtTitulo = findViewById(R.id.songName);
        txtTiempoRestante = findViewById(R.id.songDuration);
        seekbar = findViewById(R.id.seekBar);
        image = findViewById(R.id.mp3Image);
        btnPlay = findViewById(R.id.btnPlayCancion);
        btnStop = findViewById(R.id.btnPararCancion);
        btnNext = findViewById(R.id.btnSiguienteCancion);
        btnPrev = findViewById(R.id.btnCancionAnterior);
        btnDecelerar = findViewById(R.id.btnDecelerarCancion);
        btnAcelerar = findViewById(R.id.btnAcelerarCancion);
        songContainer = findViewById(R.id.songContainer);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void playPause(View view) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPlaying = false;
                btnPlay.setImageResource(R.drawable.reproducir_musica); // Cambia al icono de reproducir
                handler.removeCallbacks(updateSeekBar); // Detiene la actualización del SeekBar
            } else {
                mediaPlayer.start();
                isPlaying = true;
                btnPlay.setImageResource(R.drawable.pausar_musica); // Cambia al icono de pausa
                handler.post(updateSeekBar); // Reanuda la actualización del SeekBar
            }
        }
    }

    public void stop(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            try {
                AssetFileDescriptor afd = getResources().openRawResourceFd(cancionActual.getAudio());
                if (afd != null) {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void nextSong(View view) {
        playSong((cancionIndex + 1) % cancionList.size());
    }

    public void backSong(View view) {
        playSong((cancionIndex - 1 + cancionList.size()) % cancionList.size());
    }

    public void rewind(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(Math.max(mediaPlayer.getCurrentPosition() - 5000, 0));
        }
    }

    public void forward(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(Math.min(mediaPlayer.getCurrentPosition() + 5000, mediaPlayer.getDuration()));
        }
    }

    private void initializeSongs() {
        cancionList = new ArrayList<>();
        cancionList.add(new Cancion("Chilling", "Pedro", 92, R.drawable.ascensor, R.raw.musica1));
        cancionList.add(new Cancion("Passing_by", "Adolfo", 47, R.drawable.musica, R.raw.musica2));
        cancionList.add(new Cancion("Patatas", "Paco", 91, R.drawable.ascensor, R.raw.musica3));
        cancionList.add(new Cancion("Patos", "Javier", 78, R.drawable.musica, R.raw.musica4));
        cancionList.add(new Cancion("Rest", "Dead", 84, R.drawable.ascensor, R.raw.musica5));

        populateSongContainer();
    }

    private void populateSongContainer() {
        songContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < cancionList.size(); i++) {
            View songView = inflater.inflate(R.layout.item_song, songContainer, false);
            ImageView songImage = songView.findViewById(R.id.songImage);
            TextView songTitle = songView.findViewById(R.id.songTitle);
            TextView songArtist = songView.findViewById(R.id.songArtist);

            Cancion cancion = cancionList.get(i);
            songImage.setImageResource(cancion.getImagen());
            songTitle.setText(cancion.getTitulo());
            songArtist.setText(cancion.getAutor());

            int finalI = i;
            songView.setOnClickListener(v -> playSong(finalI));
            songContainer.addView(songView);
        }
    }

    private void playSong(int index) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            handler.removeCallbacks(updateSeekBar);
        }

        cancionActual = cancionList.get(index);
        mediaPlayer = MediaPlayer.create(this, cancionActual.getAudio());

        if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
            cancionIndex = index;

            actualizarUIReproductor(cancionActual);

            mediaPlayer.setOnCompletionListener(mp -> playSong((cancionIndex + 1) % cancionList.size()));
            handler.post(updateSeekBar);
        }
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                seekbar.setProgress(mediaPlayer.getCurrentPosition());
                actualizarTiempoRestante();
                if (isPlaying) {
                    handler.postDelayed(this, 500);
                }
            }
        }
    };

    private void actualizarUIReproductor(Cancion cancion) {
        txtTitulo.setText(cancion.getTitulo());
        image.setImageResource(cancion.getImagen());
        seekbar.setMax(mediaPlayer.getDuration());
    }

    private void actualizarTiempoRestante() {
        if (mediaPlayer != null && isPlaying) {
            int tiempoRestante = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
            txtTiempoRestante.setText(formatoTiempo(tiempoRestante));
        }
    }

    private String formatoTiempo(int millis) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
    }
}
