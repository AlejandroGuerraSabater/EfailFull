package com.evadethefail.app.vista;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.evadethefail.app.entidades.Jugador;

import com.evadethefail.app.R;
import com.evadethefail.app.entidades.Jugador_;
import com.evadethefail.app.objectBox.ObjectBox;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtUsuarioRegistro, txtContrasenaRegistro;
    private Button btnConfirmar, btnVolver;
    private TextView lblError;
    private ImageView imgTitulo;
    private BoxStore boxStore = ObjectBox.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar componentes
        txtUsuarioRegistro = findViewById(R.id.txtUsuarioRegistro);
        txtContrasenaRegistro = findViewById(R.id.txtContrasenaRegistro);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        btnVolver = findViewById(R.id.btnVolver);
        lblError = findViewById(R.id.lblError);

        // Configurar listeners
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.navigationBars());
                insetsController.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        } else {
            View decorView = getWindow().getDecorView();
            int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(flags);
        }

    }

    private void registrarUsuario() {
        String usuario = txtUsuarioRegistro.getText().toString();
        String contrasena = txtContrasenaRegistro.getText().toString();

        // Verificar si la contraseña está vacía o son solo espacios en blanco
        if (TextUtils.isEmpty(contrasena) || contrasena.trim().isEmpty()) {
            mostrarError();
            return;
        }

        // Verificar si el usuario ya existe
        Box<Jugador> jugadorBox = boxStore.boxFor(Jugador.class);
        Query<Jugador> query = jugadorBox.query(
                Jugador_.nombre.equal(usuario)
        ).build();

        Jugador jugadorExistente = query.findFirst();

        if (jugadorExistente != null) {
            // El usuario ya existe
            mostrarError();
        } else {
            // Crear nuevo usuario
            Jugador nuevoJugador = new Jugador();
            nuevoJugador.setNombre(usuario);
            nuevoJugador.setContrasena(contrasena);

            // Guardar en la base de datos
            jugadorBox.put(nuevoJugador);

            // Volver a la pantalla de login
            finish();
        }
    }

    private void mostrarError() {
        // Mostrar mensaje de error
        lblError.setVisibility(View.VISIBLE);

        // Limpiar los campos
        txtUsuarioRegistro.setText("");
        txtContrasenaRegistro.setText("");
    }
}