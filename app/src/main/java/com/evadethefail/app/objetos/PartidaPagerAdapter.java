package com.evadethefail.app.objetos;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.evadethefail.app.vista.CartasFragment;
import com.evadethefail.app.vista.CombateFragment;
import com.evadethefail.app.vista.MapaFragment;

public class PartidaPagerAdapter extends FragmentStateAdapter {

    private boolean batallaActiva = false;

    public PartidaPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void activarCombate() {
        batallaActiva = true;
        notifyDataSetChanged();
    }

    public void desactivarCombate() {
        batallaActiva = false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new MapaFragment();
            case 1: return new CartasFragment();
            case 2: return new CombateFragment(); // Solo se accede si batallaActiva es true
            default: throw new IllegalStateException("Posición inválida: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return batallaActiva ? 3 : 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean containsItem(long itemId) {
        return itemId < getItemCount();
    }
}
