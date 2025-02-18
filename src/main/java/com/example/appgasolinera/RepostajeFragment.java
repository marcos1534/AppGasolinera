package com.example.appgasolinera;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RepostajeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepostajeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button botonFecha, botonHora, botonFinalizar, botonUbicacion, botonRecycleView;
    private EditText editTextFecha, editTextHora, editTextUbicacion;
    private Spinner comboTipo, comboCantidad;
    private TextView textSeleccion, textConfirmacion;
    private int dia, mes, anio, hora, min;
    private int[][] gasolineraZonas = {
            {-50, 450, 650, 1250}, // Gasolinera 1: x1, x2, y1, y2
            {380, 600, 450, 1050}, // Gasolinera 2: x1, x2, y1, y2
            {920, 1500, -120, 450}, // Gasolinera 3: x1, x2, y1, y2
            {1350, 1950, 700, 1250}, // Gasolinera 4: x1, x2, y1, y2
            {1550, 2250, -200, 320} // Gasolinera 5: x1, x2, y1, y2
    };
    private Gasolinera[] listaGasolineras;

    // Nombres de las ubicaciones correspondientes
    private final String[] ubicacionesGasolineras = {
            "Gasolinera Ballenoil (Arroyomolinos)",
            "Gasolinera Repsol (A-5 km 21)",
            "Gasolinera Alcampo (Alcorcón)",
            "Gasolinera Alcampo (Fuenlabrada)",
            "Gasolinera Shell (Alcorcón)"
    };

    public RepostajeFragment() {
        // Required empty public constructor
    }

    public static RepostajeFragment newInstance(String param1, String param2) {
        RepostajeFragment fragment = new RepostajeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_repostaje, container, false);

        this.generarArticulos();

        // Inicializar vistas
        editTextFecha = rootView.findViewById(R.id.editTextFecha);
        editTextHora = rootView.findViewById(R.id.editTextHora);
        botonFecha = rootView.findViewById(R.id.botonFecha);
        botonHora = rootView.findViewById(R.id.botonHora);
        botonFinalizar = rootView.findViewById(R.id.botonFinalizarRepostaje);
        botonUbicacion = rootView.findViewById(R.id.botonUbicacion);
        editTextUbicacion = rootView.findViewById(R.id.editTextUbicacion);

        comboTipo = rootView.findViewById(R.id.comboTipo);
        comboCantidad = rootView.findViewById(R.id.comboCantidad);
        textSeleccion = rootView.findViewById(R.id.textViewDatos);

        botonRecycleView = rootView.findViewById(R.id.botonRecycleView);
        botonRecycleView.setOnClickListener(this);

        // Configurar adaptadores para los Spinners
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(getContext(),
                R.array.Combustible, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterCantidad = ArrayAdapter.createFromResource(getContext(),
                R.array.Precios, android.R.layout.simple_spinner_item);
        adapterCantidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboCantidad.setAdapter(adapterCantidad);

        // Configurar eventos de los botones
        botonFecha.setOnClickListener(this);
        botonHora.setOnClickListener(this);
        botonFinalizar.setOnClickListener(this);
        botonUbicacion.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == botonRecycleView) {
            Intent intent = new Intent(requireContext(), InfoGasolineras.class);
            intent.putExtra("gasolineras", listaGasolineras); // Pasar datos
            startActivity(intent);

        } else if (v == botonFecha) {
            final Calendar calendario = Calendar.getInstance();
            dia = calendario.get(Calendar.DAY_OF_MONTH);
            mes = calendario.get(Calendar.MONTH);
            anio = calendario.get(Calendar.YEAR);

            DatePickerDialog dpd = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                editTextFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }, anio, mes, dia);
            dpd.show();
        } else if (v == botonHora) {
            final Calendar calendarioHora = Calendar.getInstance();
            hora = calendarioHora.get(Calendar.HOUR_OF_DAY);
            min = calendarioHora.get(Calendar.MINUTE);

            TimePickerDialog tpd = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
                editTextHora.setText(hourOfDay + ":" + minute);
            }, hora, min, true);
            tpd.show();
        } else if (v == botonFinalizar) {
            // Verificación de campos vacíos (incluyendo la ubicación)
            if (editTextFecha.getText().toString().isEmpty() || editTextHora.getText().toString().isEmpty()
                    || comboTipo.getSelectedItem() == null || comboCantidad.getSelectedItem() == null
                    || editTextUbicacion.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Validar fecha y hora
                String[] fechaSeleccionada = editTextFecha.getText().toString().split("/");
                String[] horaSeleccionada = editTextHora.getText().toString().split(":");

                int diaSeleccionado = Integer.parseInt(fechaSeleccionada[0]);
                int mesSeleccionado = Integer.parseInt(fechaSeleccionada[1]) - 1; // Meses en Calendar van de 0 a 11
                int anioSeleccionado = Integer.parseInt(fechaSeleccionada[2]);

                int horaSeleccionadaInt = Integer.parseInt(horaSeleccionada[0]);
                int minutoSeleccionado = Integer.parseInt(horaSeleccionada[1]);

                // Obtener fecha y hora actuales
                Calendar ahora = Calendar.getInstance();
                Calendar fechaHoraSeleccionada = Calendar.getInstance();

                fechaHoraSeleccionada.set(anioSeleccionado, mesSeleccionado, diaSeleccionado, horaSeleccionadaInt, minutoSeleccionado);

                if (fechaHoraSeleccionada.before(ahora)) {
                    Toast.makeText(getContext(), "La fecha y hora seleccionadas no pueden ser anteriores a la actual.", Toast.LENGTH_SHORT).show();
                } else if (esMismoDia(ahora, fechaHoraSeleccionada) &&
                        diferenciaHoras(ahora, fechaHoraSeleccionada) < 1) {
                    Toast.makeText(getContext(), "La hora debe ser al menos una hora más tarde que la actual.", Toast.LENGTH_SHORT).show();
                } else {
                    String tipoCombustible = comboTipo.getSelectedItem().toString();
                    String cantidad = comboCantidad.getSelectedItem().toString();
                    String fecha = editTextFecha.getText().toString();
                    String hora = editTextHora.getText().toString();
                    String ubicacion = editTextUbicacion.getText().toString();

                    String textoFinal = "Usted ha seleccionado " +
                            "\nCombustible: " + tipoCombustible +
                            "\nUna cantidad de: " + cantidad +
                            "\nPara la fecha " + fecha + " y la hora " + hora +
                            "\nUbicación: " + ubicacion +
                            "\nSe ha creado la cita correctamente; puede proseguir al pago.";

                    textSeleccion.setText(textoFinal);
                    textSeleccion.setVisibility(View.VISIBLE);

                    // Guardar en SharedPreferences que la cita ha sido creada
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppGasolineraPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("CITA_CREADA", true);
                    editor.apply();

                    Toast.makeText(getContext(), "Cita creada correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v == botonUbicacion) {
            mostrarDialogoMapa();
        }
    }
    private boolean esMismoDia(Calendar fecha1, Calendar fecha2) {
        return fecha1.get(Calendar.YEAR) == fecha2.get(Calendar.YEAR) &&
                fecha1.get(Calendar.DAY_OF_YEAR) == fecha2.get(Calendar.DAY_OF_YEAR);
    }
    private long diferenciaHoras(Calendar fecha1, Calendar fecha2) {
        long diferenciaMilisegundos = fecha2.getTimeInMillis() - fecha1.getTimeInMillis();
        return diferenciaMilisegundos / (60 * 60 * 1000); // Convertir milisegundos a horas
    }

    private void generarArticulos() {
        this.listaGasolineras = new Gasolinera[5];
        this.listaGasolineras[0] = new Gasolinera (1, "Ballenoil Arroyomolinos",
                "Diesel\n\t\t1.209€\n" +
                        "Diesel Premium\n\t\t1.459€\n" +
                        "Sin plomo 95\n\t\t1.519€\n" +
                        "Sin plomo 98\n\t\t1.339€\n", R.drawable.ballen_oil);

        this.listaGasolineras[1] = new Gasolinera (2, "Repsol A-5(km 21)",
                "Diesel\n\t\t1.609€\n" +
                        "Diesel Premium\n\t\t1.699€\n" +
                        "Sin plomo 95\n\t\t1.689€\n" +
                        "Sin plomo 98\n\t\t1.839€\n", R.drawable.repsol);

        this.listaGasolineras[2] = new Gasolinera (3, "Alcampo Alcorcón",
                "Diesel\n\t\t1.389€\n" +
                        "Diesel Premium\n\t\t1.469€\n" +
                        "Sin plomo 95\n\t\t1.439€\n" +
                        "Sin plomo 98\n\t\t1.639€\n", R.drawable.alcampo_alcorcon);

        this.listaGasolineras[3] = new Gasolinera (4, "Alcampo Fuenlabrada",
                "Diesel\n\t\t1.389€\n" +
                        "Diesel Premium\n\t\t1.469€\n" +
                        "Sin plomo 95\n\t\t1.439€\n" +
                        "Sin plomo 98\n\t\t1.639€\n", R.drawable.alcampo_fuenlabrada);

        this.listaGasolineras[4] = new Gasolinera (5, "Shell Alcorcón",
                "Diesel\n\t\t1.489€\n" +
                        "Diesel Premium\n\t\t1.549€\n" +
                        "Sin plomo 95\n\t\t1.579€\n" +
                        "Sin plomo 98\n\t\t1.719€\n", R.drawable.shell);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void mostrarDialogoMapa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialogo_mapa, null);
        builder.setView(dialogView);

        // Referencia a la imagen del mapa
        ImageView mapa = dialogView.findViewById(R.id.imagenMapa);

        // Detectar clics en el mapa
        mapa.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                float x = motionEvent.getX(); // Coordenada original X
                float y = motionEvent.getY(); // Coordenada original Y

                // Escalar las coordenadas táctiles al tamaño original de la imagen
                float escalaX = (float) mapa.getDrawable().getIntrinsicWidth() / mapa.getWidth();
                float escalaY = (float) mapa.getDrawable().getIntrinsicHeight() / mapa.getHeight();
                x *= escalaX; // Coordenada escalada X
                y *= escalaY; // Coordenada escalada Y

                Log.d("Coordenadas", "Original: (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
                Log.d("Coordenadas", "Escaladas: (" + x + ", " + y + ")");

                // Determinar la zona más cercana usando las coordenadas escaladas
                int index = obtenerZonaMasCercana((int) x, (int) y);

                if (index != -1) {
                    String ubicacionSeleccionada = ubicacionesGasolineras[index];
                    editTextUbicacion.setText(ubicacionSeleccionada);
                    Toast.makeText(getContext(), "Ubicación seleccionada: " + ubicacionSeleccionada, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No hay una gasolinera en esta ubicación.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        builder.setNegativeButton("Cerrar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int obtenerZonaMasCercana(int x, int y) {
        for (int i = 0; i < gasolineraZonas.length; i++) {
            int[] zona = gasolineraZonas[i];
            Log.d("Zona " + i, "Rango X: " + zona[0] + "-" + zona[1] + ", Rango Y: " + zona[2] + "-" + zona[3]);
            Log.d("Zona " + i, "Punto X: " + x + ", Punto Y: " + y);

            if (x >= zona[0] && x <= zona[1] && y >= zona[2] && y <= zona[3]) {
                Log.d("Zona " + i, "¡Coincidencia encontrada!");
                return i; // Devuelve el índice de la gasolinera si el punto está en el rango.
            }
        }
        Log.d("obtenerZonaMasCercana", "No se encontró ninguna coincidencia.");
        // Si no se encuentra una coincidencia, devolver -1 o mostrar un mensaje de error.
        return -1;
    }
}