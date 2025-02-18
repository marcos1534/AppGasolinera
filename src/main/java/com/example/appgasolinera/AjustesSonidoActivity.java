package com.example.appgasolinera;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;

public class AjustesSonidoActivity extends AppCompatActivity {
    private SeekBar volumenSeekBar;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_sonido);

        volumenSeekBar = findViewById(R.id.volumenSeekBar);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Configurar la barra de volumen
        volumenSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumenSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    public void volverReproductor(View view) {
        finish(); // Cierra la actividad y vuelve a la anterior
    }
}
