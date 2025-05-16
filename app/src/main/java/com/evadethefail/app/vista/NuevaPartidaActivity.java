package com.evadethefail.app.vista;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.evadethefail.app.controlador.ControladorPartida;
import com.evadethefail.app.modelo.PartidaActivity;

import com.evadethefail.app.R;

import java.util.ArrayList;
import java.util.List;

/** @noinspection ALL*/
public class NuevaPartidaActivity extends AppCompatActivity {

    private ImageView[] personajes = new ImageView[3];
    private String[] nombrePersonajes = new String[3];
    private boolean[] selected = {false, false, false};
    private Button selectButton;
    private TextView characterLabel;
    public static String personajeSeleccionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_partida);

        personajes[0] = findViewById(R.id.Caballero);
        nombrePersonajes[0] = "Caballero";
        personajes[1] = findViewById(R.id.Asesino);
        nombrePersonajes[1] = "Asesino";
        personajes[2] = findViewById(R.id.Hechicera);
        nombrePersonajes[2] = "Hechicera";
        selectButton = findViewById(R.id.btnSeleccionar);
        characterLabel = findViewById(R.id.lblPersonaje);

        for (int i = 0; i < personajes.length; i++) {
            int index = i;
            personajes[i].setOnClickListener(v -> selectCharacter(index));
        }

        selectButton.setOnClickListener(v -> {
            if (personajeSeleccionado != "") {
                iniciarPartida();
                MapaFragment.casilla = 1;
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 1) Desactiva el ajuste automático de insets
            window.setDecorFitsSystemWindows(false);

            // 2) Consigue el controller
            final WindowInsetsController insetsController = window.getInsetsController();
            if (insetsController != null) {
                // 3) Oculta BOTH systemBars (status + navigation)
                insetsController.hide(WindowInsets.Type.systemBars());

                // 4) Comportamiento: swipe para mostrar temporalmente
                insetsController.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        } else {
            View decorView = window.getDecorView();
            int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(flags);
        }

    }

    private void selectCharacter(int index) {
        for (int i = 0; i < personajes.length; i++) {
            personajes[i].setAlpha(1.0f);
            selected[i] = false;
        }

        selected[index] = true;
        personajes[index].setAlpha(0.5f);
        personajeSeleccionado = nombrePersonajes[index];

        characterLabel.setText(nombrePersonajes[index]);
        selectButton.setEnabled(true);
    }

    private void iniciarPartida() {
        ControladorPartida.setupInicial();
        Intent intent = new Intent(this, PartidaActivity.class);
        MenuActivity.estadisticas.partidasJugadasSumar(1);
        startActivity(intent);
        finish();
    }
}
