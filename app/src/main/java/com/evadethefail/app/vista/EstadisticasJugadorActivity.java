package com.evadethefail.app.vista;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.evadethefail.app.R;
import com.evadethefail.app.entidades.EstadisticasJugador;
import android.view.View;
import android.widget.Button;

public class EstadisticasJugadorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas_jugador);

        // Obtenemos las estadísticas del jugador desde MenuActivity
        EstadisticasJugador estadisticas = MenuActivity.estadisticas;

        // Inicializamos las vistas
        TextView tvPersonajeFavorito = findViewById(R.id.tv_personaje_favorito);
        TextView tvVecesUsadoCaballero = findViewById(R.id.tv_veces_usado_caballero);
        TextView tvVecesUsadoAsesino = findViewById(R.id.tv_veces_usado_asesino);
        TextView tvVecesUsadoHechicera = findViewById(R.id.tv_veces_usado_hechicera);
        TextView tvEnemigosDerrotados = findViewById(R.id.tv_enemigos_derrotados);
        TextView tvVidaPerdida = findViewById(R.id.tv_vida_perdida);
        TextView tvDanoCausado = findViewById(R.id.tv_dano_causado);
        TextView tvDanoRecibido = findViewById(R.id.tv_dano_recibido);
        TextView tvCartasUsadas = findViewById(R.id.tv_cartas_usadas);
        TextView tvCriticosCausados = findViewById(R.id.tv_criticos_causados);
        TextView tvCriticosRecibidos = findViewById(R.id.tv_criticos_recibidos);
        TextView tvPartidasJugadas = findViewById(R.id.tv_partidas_jugadas);
        TextView tvDanoMaximo = findViewById(R.id.tv_dano_maximo);
        Button btnVolver = findViewById(R.id.btn_volver);

        // Actualizamos las vistas con los datos
        estadisticas.setPersonajeFavorito();
        tvPersonajeFavorito.setText("Personaje favorito: " + estadisticas.getPersonajeFavorito());
        tvVecesUsadoCaballero.setText("Veces usado Caballero: " + estadisticas.getVecesUsadoCaballero());
        tvVecesUsadoAsesino.setText("Veces usado Asesino: " + estadisticas.getVecesUsadoAsesino());
        tvVecesUsadoHechicera.setText("Veces usado Hechicera: " + estadisticas.getVecesUsadoHechicera());
        tvEnemigosDerrotados.setText("Enemigos derrotados: " + estadisticas.getEnemigosDerrotados());
        tvVidaPerdida.setText("Vida perdida: " + estadisticas.getVidaPerdida());
        tvDanoCausado.setText("Daño causado: " + estadisticas.getDanoCausado());
        tvDanoRecibido.setText("Daño recibido: " + estadisticas.getDanoRecibido());
        tvCartasUsadas.setText("Cartas usadas: " + estadisticas.getCartasUsadas());
        tvCriticosCausados.setText("Críticos causados: " + estadisticas.getCriticosCausadosJugador());
        tvCriticosRecibidos.setText("Críticos recibidos: " + estadisticas.getCriticosCausadosContraJugador());
        tvPartidasJugadas.setText("Partidas jugadas: " + estadisticas.getPartidasJugadas());
        tvDanoMaximo.setText("Daño máximo: " + estadisticas.getDanoMaximo());

        // Configurar el botón para volver al MenuActivity
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra esta activity y vuelve a la anterior (MenuActivity)
            }
        });
    }
}