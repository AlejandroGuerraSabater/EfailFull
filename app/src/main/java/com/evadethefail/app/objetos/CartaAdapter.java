package com.evadethefail.app.objetos;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evadethefail.app.controlador.ControladorCombate;
import com.evadethefail.app.entidades.Carta;
import com.evadethefail.app.modelo.PartidaActivity;

import java.util.List;

import com.evadethefail.app.R;

import com.evadethefail.app.vista.RecompensaDialog;

public class CartaAdapter extends RecyclerView.Adapter<CartaAdapter.CartaViewHolder> {

    private final List<Carta> listaCartas;
    private boolean esCombate = false;
    private boolean esRecompensa = false;
    private ControladorCombate controladorCombate = ControladorCombate.instanciaControlador;
    private RecompensaDialog recompensa = RecompensaDialog.instanciaRecompensa;

    // Constructor modificado para aceptar contexto de uso
    public CartaAdapter(List<Carta> listaCartas, boolean esCombate, boolean esRecompensa) {
        this.listaCartas = listaCartas;
        this.esCombate = esCombate;
        this.esRecompensa = esRecompensa;

    }

    @NonNull
    @Override
    public CartaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carta, parent, false);
        return new CartaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CartaViewHolder holder, int position) {

        Carta carta = listaCartas.get(position);

        ajustarTamanioCarta(holder.itemView, listaCartas.size());

        // Asignar datos a las vistas
        holder.txtNombre.setText(carta.getNombre());
        holder.txtCoste.setText(String.valueOf(carta.getCosto()));
        holder.txtElemento.setText(carta.getElemento());

        // Manejo seguro de la clase (ObjectBox)
        String nombreClase = "Sin clase";
        if (carta.getClase() != null && carta.getClase().getTarget() != null) {
            nombreClase = carta.getClase().getTarget().getNombre();
        }
        holder.txtClase.setText(nombreClase);

        holder.txtDescripcion.setText(carta.getDescripcion());
        holder.txtPotencia.setText("Potencia: " + carta.getPotencia());

        if (esCombate) {

            // Al hacer clic normal, selecciona la carta
            holder.itemView.setOnClickListener(v -> {
                if (PartidaActivity.batallaActiva)
                    controladorCombate.seleccionarCarta(holder.itemView, position);
            });


            // Al mantener pulsado, muestra la descripción de la carta
            holder.itemView.setOnLongClickListener(v -> {
                controladorCombate.mostrarDescripcionCarta(carta);
                return true; // Devuelve true para indicar que el evento se ha consumido
            });

            holder.txtNombre.setText("");
            holder.txtClase.setText(carta.getNombre());
            holder.txtDescripcion.setText("");
            holder.txtPotencia.setText(carta.getPotencia() + "");

        } else if (esRecompensa) {
            // Al hacer clic normal, selecciona la carta
            holder.itemView.setOnClickListener(v -> {

                if (recompensa.cartaActiva == -1){
                    recompensa.seleccionarCarta(holder.itemView, position);

                } else if (recompensa.cartaActiva != position){

                    recompensa.deseleccionarCarta();
                    recompensa.seleccionarCarta(holder.itemView, position);

                } else {
                    recompensa.deseleccionarCarta();
                }
            });


            // Al mantener pulsado, muestra la descripción de la carta
            holder.itemView.setOnLongClickListener(v -> {
                controladorCombate.mostrarDescripcionCarta(carta);
                return true; // Devuelve true para indicar que el evento se ha consumido
            });

            holder.txtNombre.setText("");
            holder.txtClase.setText(carta.getNombre());
            holder.txtDescripcion.setText("");
            holder.txtPotencia.setText(carta.getPotencia() + "");
            holder.txtPotencia.setGravity(Gravity.TOP);

        } else if (carta.getEfectos() != null && !carta.getEfectos().isEmpty()) {
            holder.recyclerEfectos.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            EfectoAdapter efectoAdapter = new EfectoAdapter(carta.getEfectos());
            holder.recyclerEfectos.setAdapter(efectoAdapter);
        }
    }

    private void ajustarTamanioCarta(View itemView, int numCartas) {
        DisplayMetrics metrics = itemView.getContext().getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        ViewGroup.LayoutParams params = itemView.getLayoutParams();

        if (esCombate) {
            int anchoRecyclerView = (int)(screenWidth * 0.5);
            int margen = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 16, metrics);
            int anchoDisponible = anchoRecyclerView - margen;

            int divisor = (numCartas <= 4) ? 5 : numCartas;
            params.width = anchoDisponible / divisor;
            params.height = (int)(screenHeight * 0.3);
        } else if (esRecompensa) {
            int anchoRecyclerView = (int)(screenWidth * 0.3);
            int margen = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 8, metrics);
            int anchoDisponible = anchoRecyclerView - margen;

            params.width = anchoDisponible / Math.max(numCartas, 1);
            params.height = (int)(screenHeight * 0.3);
        } else {
            params.width = (int)(screenWidth * 0.2);
            params.height = (int)(screenHeight * 0.3);
        }

        itemView.setLayoutParams(params);
    }


    @Override
    public int getItemCount() {
        return listaCartas.size();
    }

    public static class CartaViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txtNombre, txtCoste, txtElemento, txtClase, txtDescripcion, txtPotencia;
        ImageView imgFondo;
        RecyclerView recyclerEfectos;

        public CartaViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFondo = itemView.findViewById(R.id.imgCarta);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCoste = itemView.findViewById(R.id.txtCoste);
            txtElemento = itemView.findViewById(R.id.txtElemento);
            txtClase = itemView.findViewById(R.id.txtClase);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtPotencia = itemView.findViewById(R.id.txtPotencia);
            recyclerEfectos = itemView.findViewById(R.id.recyclerEfectos);

        }
    }

    public void setCartas(List<Carta> nuevasCartas) {
        this.listaCartas.clear();
        this.listaCartas.addAll(nuevasCartas);
    }
    public List<Carta> getListaCartas() {
        return listaCartas;
    }
}
