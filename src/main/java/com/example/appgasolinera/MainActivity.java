package com.example.appgasolinera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private LinearLayout tab1, tab2, tab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincular las vistas de los tabs
        tab1 = findViewById(R.id.Tab1);
        tab2 = findViewById(R.id.Tab2);
        tab3 = findViewById(R.id.Tab3);

        // Configurar los listeners para los tabs
        tab1.setOnClickListener(this::clic);
        tab2.setOnClickListener(this::clic);
        tab3.setOnClickListener(this::clic);

        // Establecer la pestaña predeterminada
        updateActiveTab(tab1);

        // Animaciones de bienvenida
        TextView textBienvenida = findViewById(R.id.textBienvenida);
        Button btnEntrar = findViewById(R.id.btnEntrar);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        textBienvenida.setVisibility(View.VISIBLE);
        textBienvenida.startAnimation(fadeIn);

        btnEntrar.setVisibility(View.VISIBLE);
        btnEntrar.startAnimation(fadeIn);
    }

    public void clic(View v) {
        Fragment selectedFragment = null;

        if (!UsuarioRegistrado() && v.getId() != R.id.Tab1) {
            Toast.makeText(this, "Debe registrarse antes de continuar.", Toast.LENGTH_SHORT).show();
            loadFragment(new MatriculaFragment());
            updateActiveTab(tab1); // Volver al Tab1 si no está registrado
            return;
        }

        if (v.getId() == R.id.Tab1) {
            selectedFragment = new MatriculaFragment();
            Log.i("Filtro clic", "Se hace el clic del Tab1");
            updateActiveTab(tab1);
        } else if (v.getId() == R.id.Tab2) {
            selectedFragment = new RepostajeFragment();
            Log.i("Filtro clic", "Se hace el clic del Tab2");
            updateActiveTab(tab2);
        } else if (v.getId() == R.id.Tab3) {
            if (!CitaCreada()) {
                Toast.makeText(this, "Debe crear una cita antes de proceder al pago.", Toast.LENGTH_SHORT).show();
                loadFragment(new RepostajeFragment());
                updateActiveTab(tab1); // Volver al Tab2 si no hay cita creada
                return;
            }
            selectedFragment = new PagoFragment();
            Log.i("Filtro clic", "Se hace el clic del Tab3");
            updateActiveTab(tab3);
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }
    }

    // Método para manejar el clic en el botón "Entrar"
    public void onClickEntrar(View v) {
        TextView textBienvenida = findViewById(R.id.textBienvenida);
        Button btnEntrar = findViewById(R.id.btnEntrar);
        LinearLayout contenidoPrincipal = findViewById(R.id.contenidoPrincipal);

        // Ocultar la pantalla de bienvenida
        textBienvenida.setVisibility(View.GONE);
        btnEntrar.setVisibility(View.GONE);

        // Mostrar el contenido principal
        contenidoPrincipal.setVisibility(View.VISIBLE);

        // Cargar el fragmento inicial y destacar el Tab1
        loadFragment(new MatriculaFragment());
        updateActiveTab(tab1);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContenidoTab, fragment)
                .commit();
    }

    private boolean UsuarioRegistrado() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppGasolineraPrefs", MODE_PRIVATE);
        return sharedPreferences.contains("MATRICULA") && sharedPreferences.contains("USUARIO");
    }

    private boolean CitaCreada() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppGasolineraPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("CITA_CREADA", false);
    }

    private void updateActiveTab(LinearLayout activeTab) {
        // Restablecer estilos para todas las pestañas
        resetTabStyle(tab1);
        resetTabStyle(tab2);
        resetTabStyle(tab3);

        // Aplicar fondo degradado a la pestaña seleccionada
        activeTab.setBackground(getDrawable(R.drawable.tab_selected_background));
        for (int i = 0; i < activeTab.getChildCount(); i++) {
            View child = activeTab.getChildAt(i);
            if (child instanceof android.widget.ImageView) {
                ((android.widget.ImageView) child).setColorFilter(ContextCompat.getColor(this, R.color.active_highlight)); // Icono activo
            } else if (child instanceof TextView) {
                ((TextView) child).setTextColor(getResources().getColor(R.color.active_icon_text)); // Texto activo
            }
        }
    }

    private void resetTabStyle(LinearLayout tab) {
        tab.setBackground(getDrawable(R.drawable.tab_item_selector)); // Fondo inactivo
        for (int i = 0; i < tab.getChildCount(); i++) {
            View child = tab.getChildAt(i);
            if (child instanceof android.widget.ImageView) {
                ((android.widget.ImageView) child).setColorFilter(ContextCompat.getColor(this, R.color.white)); // Icono inactivo
            } else if (child instanceof TextView) {
                ((TextView) child).setTextColor(getResources().getColor(R.color.white)); // Texto inactivo
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w("Reinicio", "MainActivity ON RESTART");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Resume", "MainActivity ON RESUME");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Comienzo", "MainActivity ON START");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Parada", "MainActivity ON STOP");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("Destruir", "MainActivity ON DESTRUIDA");
    }

    public void abrirAjustesSonido(View view) {
        Intent intent = new Intent(this, AjustesSonidoActivity.class);
        startActivity(intent);
    }

    public void abrirReproductor(View view){
        Intent intent = new Intent(this, AndroidMediaPlayerApp.class);
        startActivity(intent);
    }
}