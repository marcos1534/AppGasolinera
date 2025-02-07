package com.example.appgasolinera;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MatriculaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatriculaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextMatricula;
    private EditText editTextUsuario;
    private TextView textContinuar;
    private SharedPreferences sharedPreferences;

    public MatriculaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatriculaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatriculaFragment newInstance(String param1, String param2) {
        MatriculaFragment fragment = new MatriculaFragment();
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
        sharedPreferences = requireActivity().getSharedPreferences("AppGasolineraPrefs", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_matricula, container, false);

        // Inicializar vistas
        editTextMatricula = rootView.findViewById(R.id.editTextMat);
        editTextUsuario = rootView.findViewById(R.id.editTextUsu);
        textContinuar = rootView.findViewById(R.id.textView);

        // Referenciar el botón y asignar el evento de clic
        Button botonRegistrar = rootView.findViewById(R.id.botonLogin);
        botonRegistrar.setOnClickListener(this::clicRegistro);

        return rootView;
    }

    public void clicRegistro(View view) {
        String matricula = editTextMatricula.getText().toString().trim();
        String usuario = editTextUsuario.getText().toString().trim();

        // Validación de matrícula (ejemplo para matrículas españolas: 4 dígitos seguidos de 3 letras)
        Pattern matriculaPattern = Pattern.compile("^[0-9]{4}[A-Z]{3}$");

        if (TextUtils.isEmpty(matricula)) {
            mostrarError(getString(R.string.error_matricula_vacia));
        } else if (!matriculaPattern.matcher(matricula).matches()) {
            mostrarError(getString(R.string.error_matricula_invalida));
        } else if (TextUtils.isEmpty(usuario)) {
            mostrarError(getString(R.string.error_usuario_vacio));
        } else if (!usuario.matches("^[a-zA-Z0-9]+$")) {
            mostrarError(getString(R.string.error_usuario_invalido));
        } else {
            // Guardar datos en SharedPreferences
            guardarDatos(matricula, usuario);

            // Mostrar mensaje de éxito
            textContinuar.setVisibility(View.VISIBLE);
            textContinuar.setText(getString(R.string.datos_guardados));
            ocultarTeclado();
            Toast.makeText(getContext(), getString(R.string.datos_guardados), Toast.LENGTH_SHORT).show();

            // Notificar a MainActivity que el registro fue exitoso
            requireActivity().findViewById(R.id.Tab2).setEnabled(true); // Habilitar Tab2
            requireActivity().findViewById(R.id.Tab3).setEnabled(true); // Habilitar Tab3
        }
    }

    private void mostrarError(String mensaje) {
        textContinuar.setVisibility(View.VISIBLE);
        textContinuar.setText(mensaje);
    }

    private void guardarDatos(String matricula, String usuario) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MATRICULA", matricula);
        editor.putString("USUARIO", usuario);
        editor.apply();
    }

    private void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}