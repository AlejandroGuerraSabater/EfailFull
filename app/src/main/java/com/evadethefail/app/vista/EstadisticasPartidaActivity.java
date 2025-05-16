package com.evadethefail.app.vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.evadethefail.app.controlador.ControladorPartida;
import com.evadethefail.app.modelo.PartidaActivity;

import java.util.Random;

import com.evadethefail.app.R;

/** @noinspection ALL*/
public class EstadisticasPartidaActivity extends AppCompatActivity {

    private TextView tvTitulo;
    private TextView tvEnemigosDestruidos;
    private TextView tvVidaPerdida;
    private TextView tvDanoCausado;
    private TextView tvDanoRecibido;
    private TextView tvCartasUsadas;
    private TextView tvCriticosCausados;
    private TextView tvCriticosRecibidos;
    private Button btnSalir;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticaspartida);

        // Inicializar componentes
        tvTitulo = findViewById(R.id.tv_titulo);
        tvEnemigosDestruidos = findViewById(R.id.tv_enemigos_derrotados);
        tvVidaPerdida = findViewById(R.id.tv_vida_perdida);
        tvDanoCausado = findViewById(R.id.tv_dano_causado);
        tvDanoRecibido = findViewById(R.id.tv_dano_recibido);
        tvCartasUsadas = findViewById(R.id.tv_cartas_usadas);
        tvCriticosCausados = findViewById(R.id.tv_criticos_causados);
        tvCriticosRecibidos = findViewById(R.id.tv_criticos_recibidos);
        btnSalir = findViewById(R.id.btn_salir);
        btnVolver = findViewById(R.id.btn_volver);

        // Obtener datos de la partida
        mostrarMensajeAleatorio(ControladorPartida.esVictoria);

        // Mostrar estadísticas de la partida
        tvEnemigosDestruidos.setText(PartidaActivity.estadisticas.getEnemigosDerrotados() +"");
        tvVidaPerdida.setText(PartidaActivity.estadisticas.getVidaPerdida() + "" );
        tvDanoCausado.setText(PartidaActivity.estadisticas.getDanoCausado() + "" );
        tvDanoRecibido.setText(PartidaActivity.estadisticas.getDanoRecibido() + "" );
        tvCartasUsadas.setText(PartidaActivity.estadisticas.getCartasUsadas() + "" );
        tvCriticosCausados.setText(PartidaActivity.estadisticas.getCriticosCausadosJugador() + "" );
        tvCriticosRecibidos.setText(PartidaActivity.estadisticas.getCriticosCausadosContraJugador() + "" );

        // Configurar listeners de botones
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // Cierra todas las actividades y sale de la aplicación
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAlMenu();
            }
        });
    }

    private void mostrarMensajeAleatorio(boolean victoria) {
        String[] mensajesVictoria = {"¡Enhorabuena!", "¡Bien hecho!", "¡Eres increíble!"};
        String[] mensajesDerrota = {"No siempre se gana...", "Game over", "¡Más suerte a la próxima!"};

        Random random = new Random();
        if (victoria) {
            tvTitulo.setText(mensajesVictoria[random.nextInt(mensajesVictoria.length)]);
        } else {
            tvTitulo.setText(mensajesDerrota[random.nextInt(mensajesDerrota.length)]);
        }
    }

    private void volverAlMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}