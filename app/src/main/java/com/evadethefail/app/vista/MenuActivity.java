package com.evadethefail.app.vista;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.evadethefail.app.controlador.ControladorPartida;
import com.evadethefail.app.entidades.EstadisticasJugador;
import com.evadethefail.app.entidades.Jugador;
import com.evadethefail.app.entidades.Partida;
import com.evadethefail.app.entidades.Personaje;
import com.evadethefail.app.objectBox.App;
import com.evadethefail.app.objectBox.ObjectBox;
import com.evadethefail.app.objectBox.Utilidades;

import com.evadethefail.app.R;
import com.evadethefail.app.entidades.Partida_;
import com.evadethefail.app.entidades.Personaje_;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
/** @noinspection ALL*/
public class MenuActivity extends AppCompatActivity {
    private Button btnContinuar;
    public static int idPartidaGuardada = -1;
    private final BoxStore boxStore = App.getBoxStore();
    public static EstadisticasJugador estadisticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnContinuar = findViewById(R.id.btnContinuar);
        Button btnNuevaPartida = findViewById(R.id.btnNuevaPartida);
        Button btnEstadisticas = findViewById(R.id.btnEstadisticas);
        Button btnSalir = findViewById(R.id.btnSalir);

        btnContinuar.setEnabled(false);
        verificarPartidaGuardada();

        btnContinuar.setOnClickListener(view -> {
            if (idPartidaGuardada != -1) {
                ControladorPartida.nuevaPartida = true;
                ControladorPartida.cargarPartida(getApplicationContext());
                idPartidaGuardada = -1;
                finish();
            }
        });

        estadisticas = LoginActivity.jugador.getEstadisticas().getTarget();

        if (estadisticas == null) {
            estadisticas = new EstadisticasJugador();
            estadisticas.getJugador().setTarget(LoginActivity.jugador); // Relación ToOne en EstadisticasJugador
            Utilidades.guardarEstadisticasJugador();

            // Asignar las estadísticas al jugador
            LoginActivity.jugador.getEstadisticas().setTarget(estadisticas); // Relación ToOne en Jugador
            ObjectBox.get().boxFor(Jugador.class).put(LoginActivity.jugador); // Guardar los cambios en jugador
        }

        btnNuevaPartida.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, NuevaPartidaActivity.class));
            finish();
        });

        btnEstadisticas.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, EstadisticasJugadorActivity.class));
        });

        btnSalir.setOnClickListener(view -> {
            finishAffinity(); // Cierra todas las actividades
            System.exit(0);   // Termina el proceso de la app
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

    private void verificarPartidaGuardada() {
        Box<Partida> partidaBox = boxStore.boxFor(Partida.class);
        Box<Personaje> personajePartidaBox = boxStore.boxFor(Personaje.class);

        // Obtener la última partida del jugador
        Query<Partida> queryUltimaPartida = partidaBox.query(
                Partida_.jugadorId.equal(LoginActivity.jugador.getId())
        ).orderDesc(Partida_.id).build();

        Partida ultimaPartida = queryUltimaPartida.findFirst();

        if (ultimaPartida == null) {
            return; // No hay partidas registradas
        }

        long ultimaPartidaId = ultimaPartida.getId();

        // Verificar si la última partida es válida (casilla < 15 y VidaRestante > 0)
        Query<Personaje> queryValidarPartida = personajePartidaBox.query(
                Personaje_.idPartida.equal(ultimaPartidaId)
                        .and(Personaje_.vidaRestante.greater(0))
        ).build();

        if (!queryValidarPartida.find().isEmpty() && ultimaPartida.getCasilla() < 15) {
            idPartidaGuardada = (int) ultimaPartidaId;
            btnContinuar.setEnabled(true);
        }
    }

}
