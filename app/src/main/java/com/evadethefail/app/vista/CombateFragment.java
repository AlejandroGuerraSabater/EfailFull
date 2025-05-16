package com.evadethefail.app.vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evadethefail.app.controlador.ControladorCombate;
import com.evadethefail.app.entidades.Carta;
import com.evadethefail.app.entidades.Efecto;
import com.evadethefail.app.entidades.Marca;
import com.evadethefail.app.entidades.Modificador;
import com.evadethefail.app.modelo.PartidaActivity;
import com.evadethefail.app.objectBox.Utilidades;
import com.evadethefail.app.objetos.CartaAdapter;
import com.evadethefail.app.objetos.Estado;
import com.evadethefail.app.objetos.MensajeCombateAdapter;

import java.util.ArrayList;
import java.util.List;

import com.evadethefail.app.R;

/** @noinspection ALL*/
public class CombateFragment extends Fragment {

    public MensajeCombateAdapter adapterMensajes;
    public ConstraintLayout layout;
    public TextView txtNombreJugador, txtNivelJugador, txtEnergiaJugador, txtSaludJugador, txtBloqueoJugador;
    public TextView txtNombreEnemigo, txtNivelEnemigo, txtSaludEnemigo, txtBloqueoEnemigo;
    public ImageView imgJugador, imgEnemigo, intencionEnemigo;
    public LinearLayout panelEfectosJugador, panelEfectosEnemigo;
    public RecyclerView cartasRecyclerView, recyclerMensajes;
    public Button btnUsarCarta, btnFinTurno, btnInfoJugador, btnInfoEnemigo, btnContinuar;
    public CartaAdapter cartaAdapter;
    public static CombateFragment instanciaCombate;
    public boolean cartaSeleccionada = false;
    private ControladorCombate controladorCombate;
    public int cartasEnMano = 6;
    public int cartasPorTurno = 6;
    public int cartaActiva = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combate, container, false);

        instanciaCombate = this;
        controladorCombate = new ControladorCombate();

        // Referencias UI
        layout = view.findViewById(R.id.layout);
        txtNombreJugador = view.findViewById(R.id.txtNombreJugador);
        txtNivelJugador = view.findViewById(R.id.txtNivelJugador);
        txtEnergiaJugador = view.findViewById(R.id.txtEnergiaJugador);
        txtSaludJugador = view.findViewById(R.id.txtSaludJugador);
        txtBloqueoJugador = view.findViewById(R.id.txtBloqueoJugador);
        txtNombreEnemigo = view.findViewById(R.id.txtNombreEnemigo);
        txtNivelEnemigo = view.findViewById(R.id.txtNivelEnemigo);
        txtSaludEnemigo = view.findViewById(R.id.txtSaludEnemigo);
        txtBloqueoEnemigo = view.findViewById(R.id.txtBloqueoEnemigo);
        intencionEnemigo = view.findViewById(R.id.intencionEnemigo);
        imgJugador = view.findViewById(R.id.imgJugador);
        imgEnemigo = view.findViewById(R.id.imgEnemigo);
        panelEfectosJugador = view.findViewById(R.id.panelEfectosJugador);
        panelEfectosEnemigo = view.findViewById(R.id.panelEfectosEnemigo);
        btnUsarCarta = view.findViewById(R.id.btnUsarCarta);
        btnFinTurno = view.findViewById(R.id.btnFinTurno);
        btnInfoJugador = view.findViewById(R.id.btnInfoJugador);
        btnContinuar = view.findViewById(R.id.btnContinuar);
        btnInfoEnemigo = view.findViewById(R.id.btnInfoEnemigo);
        cartasRecyclerView = view.findViewById(R.id.cartasMano);
        recyclerMensajes = view.findViewById(R.id.recyclerMensajes);

        btnUsarCarta.setEnabled(false);  // Inicialmente desactivado
        btnFinTurno.setEnabled(true);    // Inicialmente activo

        switch (PartidaActivity.personaje.getClase().getNombre()) {
            case "Caballero": // Caballero
                imgJugador.setImageResource(R.drawable.caballero);
                break;
            case "Asesino": // Asesino
                imgJugador.setImageResource(R.drawable.asesino);
                break;
            case "Hechicera": // Hechicera
                imgJugador.setImageResource(R.drawable.hechicera);
                break;
        }

        // Configurar RecyclerView
        cartasRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerMensajes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true)); // true para orden descendente

        adapterMensajes = new MensajeCombateAdapter();
        recyclerMensajes.setAdapter(adapterMensajes);
        controladorCombate.combateSetup();

        view.setOnClickListener(v -> {
            // Solo deseleccionar si el click NO fue en el botón de usar carta
            if (cartaSeleccionada && v.getId() != R.id.btnUsarCarta) {
                controladorCombate.deseleccionarCarta();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        PartidaActivity.instanciaPartida.activarModoInmersivo();
    }

    public void actualizarMano() {
        List<Carta> mano = new ArrayList<Carta>();
        for (int i = 0; i < cartasEnMano; i++) {
            int idCarta = Integer.parseInt(PartidaActivity.mano.get(i)); // Obtiene el ID de la carta
            Carta carta = Utilidades.buscaCarta(idCarta);

            mano.add(carta);
        }
        cartaAdapter = new CartaAdapter(mano, true, false);
        cartasRecyclerView.setAdapter(cartaAdapter);
    }

    public void pintaEfectos(boolean esJugador) {
        LinearLayout efectosPanel = esJugador ? panelEfectosJugador : panelEfectosEnemigo;
        List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

        efectosPanel.removeAllViews();

        for (Efecto efecto : efectos) {
            ImageView efectoIcono = new ImageView(getContext());
            int iconoResId = Estado.TRAITCHANGE; // Default para marcas
            String descripcion;

            if (efecto instanceof Modificador) {
                Modificador mod = (Modificador) efecto;
                boolean esDebuff = mod.getVariacionPlana() < 0 || mod.getPorcentaje() < 0;
                switch (mod.getEstadistica()) {
                    case "Ataque": iconoResId = esDebuff ? Estado.ATTACKDOWN : Estado.ATTACKUP; break;
                    case "Defensa": iconoResId = esDebuff ? Estado.DEFENSEDOWN : Estado.DEFENSEUP; break;
                    case "Vida": iconoResId = esDebuff ? Estado.MAXHPDOWN : Estado.MAXHPUP; break;
                    case "Curacion": iconoResId = Estado.HPREGEN; break;
                    case "Probabilidad de Crítico": iconoResId = esDebuff ? Estado.CRITRATEDOWN : Estado.CRITRATEUP; break;
                    case "Daño Crítico": iconoResId = esDebuff ? Estado.CRITDMGDOWN : Estado.CRITDMGUP; break;
                    case "Energia": iconoResId = esDebuff ? Estado.ENERGYDOWN : Estado.ENERGYUP; break;
                    case "Bloqueo": iconoResId = Estado.DEFENSEUP; break;
                }
                descripcion = mod.getEstadistica() + " " + (esDebuff ? "-" : "+") + Math.abs(mod.getVariacionPlana()) +
                        (mod.getPorcentaje() != 0 ? " y " + mod.getPorcentaje() + "%" : "") +
                        " por " + mod.getDuracion()[0] + " " + mod.getDuracion()[1];
            } else if (efecto instanceof Marca) {
                Marca mar = (Marca) efecto;
                iconoResId = Estado.TRAITCHANGE;
                descripcion = mar.getMarcaNombre() + ": " + mar.getDescripcion() + " por " + mar.getDuracion()[0] + " " + mar.getDuracion()[1];
            } else {
                descripcion = "";
            }

            efectoIcono.setImageDrawable(ContextCompat.getDrawable(getContext(), iconoResId));
            efectoIcono.setContentDescription(descripcion);
            efectoIcono.setOnClickListener(v -> controladorCombate.mostrarMensaje(descripcion));
            efectoIcono.setLayoutParams(new LinearLayout.LayoutParams(60, 60)); // Tamaño ajustable
            efectosPanel.addView(efectoIcono);
        }
    }


}
