package com.example.appgasolinera;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InfoGasolineras extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GasolineraAdapter adapter;
    private List<Gasolinera> gas;  // Fuente de datos para el RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_gasolineras);

        recyclerView = findViewById(R.id.gasRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Gasolinera[] gasolineras = (Gasolinera[]) getIntent().getSerializableExtra("gasolineras");

        if (gasolineras != null) {
            adapter = new GasolineraAdapter(gasolineras);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("InfoGasolineras", "No se recibieron datos de gasolineras.");
        }

        Button volverButton = findViewById(R.id.volver_button);
        volverButton.setOnClickListener(view -> finish());
    }

    private List<Gasolinera> getData() {
        List<Gasolinera> items = new ArrayList<>();
        // Agregar datos a la lista
        return items;
    }
}