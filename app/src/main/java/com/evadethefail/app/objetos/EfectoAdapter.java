package com.evadethefail.app.objetos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evadethefail.app.entidades.Efecto;
import com.evadethefail.app.entidades.Marca;
import com.evadethefail.app.entidades.Modificador;

import java.util.List;

import com.evadethefail.app.R;

public class EfectoAdapter extends RecyclerView.Adapter<EfectoAdapter.EfectoViewHolder> {
    private List<Efecto> efectos;

    public EfectoAdapter(List<Efecto> efectos) {
        this.efectos = efectos;
    }

    @NonNull
    @Override
    public EfectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_efecto, parent, false);
        return new EfectoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EfectoViewHolder holder, int position) {
        Efecto efecto = efectos.get(position);

        if (efecto instanceof Modificador) {
            Modificador mod = (Modificador) efecto;
            String textoValor = "";
            holder.txtNombre.setText(mod.getEstadistica());
            if (mod.getVariacionPlana() != 0) {
                textoValor += mod.getVariacionPlana();
            }

            if (mod.getPorcentaje() != 0) {
                if (!textoValor.isEmpty()) textoValor += " + ";
                textoValor += mod.getPorcentaje() + "%";
            }
            holder.txtValor.setText(textoValor);
            holder.txtDuracion.setText("Duración: " + mod.getDuracion()[0] + " " + mod.getDuracion()[1]);
        } else if (efecto instanceof Marca) {
            Marca marca = (Marca) efecto;
            holder.txtNombre.setText(marca.getMarcaNombre());
            holder.txtValor.setText(marca.getDescripcion());
            holder.txtDuracion.setText("Duración: " + marca.getDuracion()[0] + " " + marca.getDuracion()[1]);
        }
    }

    @Override
    public int getItemCount() {
        return efectos.size();
    }

    public static class EfectoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtValor, txtDuracion;

        public EfectoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreEfecto);
            txtValor = itemView.findViewById(R.id.txtValorEfecto);
            txtDuracion = itemView.findViewById(R.id.txtDuracionEfecto);
        }
    }
}
