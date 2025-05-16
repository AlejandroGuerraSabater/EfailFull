package com.evadethefail.app.objetos;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.evadethefail.app.R;

public class MensajeCombateAdapter extends RecyclerView.Adapter<MensajeCombateAdapter.MensajeViewHolder> {

    private final List<MensajeCombate> mensajes = new ArrayList<>();

    public void agregarMensaje(MensajeCombate mensaje) {
        mensajes.add(0, mensaje); // Agregar al inicio
        notifyItemInserted(0);
        eliminarTrasDelay(mensaje);
    }

    private void eliminarTrasDelay(MensajeCombate mensaje) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            int index = mensajes.indexOf(mensaje);
            if (index != -1) {
                mensajes.remove(index);
                notifyItemRemoved(index);
            }
        }, 7000);
    }

    @NonNull
    @Override
    public MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensaje_combate, parent, false);
        return new MensajeViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeViewHolder holder, int position) {
        MensajeCombate msg = mensajes.get(position);
        TextView texto = holder.textoMensaje;
        texto.setText(msg.texto);
        texto.setTextColor(msg.esDelJugador ? Color.GREEN : Color.RED);

        long tiempoPasado = System.currentTimeMillis() - msg.timestamp;
        float alpha = 1f - (tiempoPasado / 3000f);
        texto.setAlpha(Math.max(0.2f, alpha)); // mínimo visible
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    static class MensajeViewHolder extends RecyclerView.ViewHolder {
        TextView textoMensaje;

        public MensajeViewHolder(@NonNull View itemView) {
            super(itemView);
            textoMensaje = itemView.findViewById(R.id.textoMensaje);
        }
    }
}
