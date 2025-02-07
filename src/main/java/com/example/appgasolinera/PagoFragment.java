package com.example.appgasolinera;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner comboPago;
    private Button botonPago, botonValorar;
    private TextView textValoracion;
    private RatingBar ratingBar;
    private ProgressBar progressBar;


    public PagoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PagoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagoFragment newInstance(String param1, String param2) {
        PagoFragment fragment = new PagoFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_pago, container, false);

        // Inicializar las vistas
        comboPago = rootView.findViewById(R.id.comboPago);
        botonPago = rootView.findViewById(R.id.botonPago);
        botonValorar = rootView.findViewById(R.id.botonValorar);
        textValoracion = rootView.findViewById(R.id.textValoracion);
        ratingBar = rootView.findViewById(R.id.ratingBar);
        progressBar = rootView.findViewById(R.id.progressBar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.Pago, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboPago.setAdapter(adapter);

        // Establecer el evento de clic en el botón de pago
        botonPago.setOnClickListener(v -> {
            String metodoPago = comboPago.getSelectedItem().toString();
            Toast.makeText(getActivity(), "Método de pago seleccionado: " + metodoPago, Toast.LENGTH_SHORT).show();

            // Simulación de procesamiento de pago
            botonPago.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                botonPago.setEnabled(true);

                textValoracion.setVisibility(View.VISIBLE);
                textValoracion.setText(R.string.textValoracion);
                ratingBar.setVisibility(View.VISIBLE);
                botonValorar.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(), "Pago realizado con éxito.", Toast.LENGTH_SHORT).show();
            }, 3000); // Simulación de 3 segundos de procesamiento
        });

        // Establecer el evento de clic en el botón de valorar
        botonValorar.setOnClickListener(v -> {
            float rating = ratingBar.getRating();

            if (rating > 0) {
                Toast.makeText(getActivity(), "¡Gracias por valorar la aplicación!", Toast.LENGTH_SHORT).show();

                ratingBar.setVisibility(View.INVISIBLE);
                botonValorar.setVisibility(View.INVISIBLE);

                mostrarDialogoCerrarApp();

                textValoracion.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(getActivity(), "Por favor, valore la aplicación antes de continuar.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void mostrarDialogoCerrarApp() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Salir de la aplicación")
                .setMessage("¿Desea cerrar la aplicación y borrar todos los datos?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    if (getActivity() != null) {
                        borrarDatosYSalir();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    Toast.makeText(getActivity(), "Puede seguir usando la aplicación.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
    private void borrarDatosYSalir() {
        Context context = requireActivity().getApplicationContext();

        // Borra todos los datos de usuario
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            activityManager.clearApplicationUserData(); // Método para borrar datos
        }

        // Cierra la aplicación
        if (getActivity() != null) {
            getActivity().finishAffinity();
        }
    }
}