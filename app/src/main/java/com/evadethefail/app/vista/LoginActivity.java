package com.evadethefail.app.vista;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.evadethefail.app.entidades.Jugador;
import com.evadethefail.app.objectBox.ObjectBox;
import com.evadethefail.app.objectBox.Utilidades;

import com.evadethefail.app.R;
import com.evadethefail.app.entidades.Jugador_;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
/** @noinspection ALL*/
public class LoginActivity extends AppCompatActivity {

    private EditText txtUsuario, txtContrasena;
    private Button btnInicioSesion, btnRegistro;
    public static Jugador jugador;
    private BoxStore boxStore = ObjectBox.get();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnInicioSesion = findViewById(R.id.btnInicioSesion);
        btnRegistro = findViewById(R.id.btnRegistro);

        Utilidades.revisaEIniciaDatos();
        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
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

    private void iniciarSesion() {
        Box<Jugador> jugadorBox = boxStore.boxFor(Jugador.class);

        Query<Jugador> query = jugadorBox.query(
                Jugador_.nombre.equal(txtUsuario.getText().toString())
                        .and(Jugador_.contrasena.equal(txtContrasena.getText().toString()))
        ).build();

        jugador = query.findFirst();

        if (jugador != null) {
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}