package com.evadethefail.app.vista;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evadethefail.app.entidades.Carta;
import com.evadethefail.app.modelo.PartidaActivity;
import com.evadethefail.app.objectBox.Utilidades;
import com.evadethefail.app.objetos.CartaAdapter;

import java.util.ArrayList;
import java.util.List;

import com.evadethefail.app.R;

/** @noinspection ALL*/
public class RecompensaDialog extends Dialog {
    private Carta cartaSeleccionada = null;
    private Button btnConfirmar;
    private long idSeleccionado;
    public static RecompensaDialog instanciaRecompensa;
    private RecyclerView recyclerCartas;
    public int cartaActiva = -1;
    private List<Carta> cartas;

    public RecompensaDialog(Context context, List<Integer> opciones) {
        super(context);
        instanciaRecompensa = this;
        setContentView(R.layout.dialog_recompensa);
        setTitle("¡Victoria! ¡Selecciona tu recompensa!");

        this.cartas = new ArrayList<Carta>();

        for(int i = 0; i<opciones.size(); i++){
            cartas.add(Utilidades.buscaCarta(opciones.get(i)));
        }

        recyclerCartas = findViewById(R.id.recyclerCartas);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        Button btnOmitir = findViewById(R.id.btnOmitir);

        recyclerCartas.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        CartaAdapter adapter = new CartaAdapter(cartas, false, true);
        recyclerCartas.setAdapter(adapter);

        btnConfirmar.setEnabled(false);
        btnConfirmar.setOnClickListener(v -> {
            PartidaActivity.mazo.add(idSeleccionado + "");
            PartidaActivity.personaje.addCarta(idSeleccionado);
            PartidaActivity.personaje.setEnergiaRestante(PartidaActivity.personaje.getEnergia());
            PartidaActivity.personaje.getEfectos().clear();
            CartasFragment.instanciaCartas.reiniciaMazo();

            PartidaActivity.estadisticas.enemigosDerrotadosSumar(1);
            MenuActivity.estadisticas.enemigosDerrotadosSumar(1);
            Utilidades.guardarEstadisticasPartida();
            Utilidades.guardarEstadisticasJugador();

            PartidaActivity.viewPager.setCurrentItem(0, true);
            PartidaActivity.pagerAdapter.desactivarCombate();
            MapaFragment.instanciaMapa.avanzarCasilla();
            dismiss();
        });

        btnOmitir.setOnClickListener(v -> {
            PartidaActivity.personaje.setEnergiaRestante(PartidaActivity.personaje.getEnergia());
            PartidaActivity.personaje.getEfectos().clear();
            CartasFragment.instanciaCartas.reiniciaMazo();

            PartidaActivity.estadisticas.enemigosDerrotadosSumar(1);
            MenuActivity.estadisticas.enemigosDerrotadosSumar(1);
            Utilidades.guardarEstadisticasPartida();
            Utilidades.guardarEstadisticasJugador();

            PartidaActivity.viewPager.setCurrentItem(0, true);
            PartidaActivity.pagerAdapter.desactivarCombate();
            MapaFragment.instanciaMapa.avanzarCasilla();
            dismiss();
        });
    }

    public void seleccionarCarta(View cartaView, int posicion) {
        elevarCarta(cartaView);
        btnConfirmar.setEnabled(true);
        cartaActiva = posicion;
        idSeleccionado = this.cartas.get(posicion).getIdCarta();
    }

    public void elevarCarta(View cartaView) {
        float elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -10, this.getContext().getResources().getDisplayMetrics());
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, elevation);
        animation.setDuration(200);
        animation.setFillAfter(true);
        cartaView.startAnimation(animation);
    }

    public void deseleccionarCarta() {
            // Obtener la vista de la carta seleccionada
            RecyclerView.ViewHolder viewHolder = recyclerCartas
                    .findViewHolderForAdapterPosition(cartaActiva);

            if (viewHolder != null) {
                resetearPosicionCarta(viewHolder.itemView);
            }

            cartaActiva = -1;
            btnConfirmar.setEnabled(false);
            idSeleccionado = -1;

    }

    public void resetearPosicionCarta(View cartaView) {
        if (cartaView != null) {
            TranslateAnimation animation = new TranslateAnimation(0, 0, cartaView.getTranslationY(), 0);
            animation.setDuration(200);
            animation.setFillAfter(true);
            cartaView.startAnimation(animation);
        }
    }

    public Carta getRecompensaSeleccionada() {
        return cartaSeleccionada;
    }
}
