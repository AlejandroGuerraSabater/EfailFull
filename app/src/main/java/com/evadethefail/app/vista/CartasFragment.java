package com.evadethefail.app.vista;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evadethefail.app.entidades.Carta;
import com.evadethefail.app.modelo.PartidaActivity;
import com.evadethefail.app.objectBox.Utilidades;
import com.evadethefail.app.objetos.CartaAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.evadethefail.app.R;

/** @noinspection ALL*/
public class CartasFragment extends Fragment {

    private RecyclerView inventarioRecyclerView;
    private RecyclerView porRobarRecyclerView;
    private RecyclerView descartadasRecyclerView;

    public List<Carta> inventario = new ArrayList<>();
    public List<Carta> porRobar = new ArrayList<>();
    public List<Carta> descartadas = new ArrayList<>();

    public static CartasFragment instanciaCartas;
    private static final int MAX_COLUMNS = 4;
    private static final int CARD_SPACING = 8; // en dp

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cartas, container, false);
        Context context = requireContext();
        instanciaCartas = this;

        // Inicializar RecyclerViews
        inventarioRecyclerView = view.findViewById(R.id.inventarioRecyclerView);
        porRobarRecyclerView = view.findViewById(R.id.porRobarRecyclerView);
        descartadasRecyclerView = view.findViewById(R.id.descartadasRecyclerView);

        // Configurar listas y adaptadores
        iniciaInventario(context);
        refrescaPorRobar();
        refrescaDescartadas();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        PartidaActivity.instanciaPartida.activarModoInmersivo();
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Carta> cartas) {
        int columnCount = MAX_COLUMNS;

        while (recyclerView.getItemDecorationCount() > 0) {
            recyclerView.removeItemDecorationAt(0);
        }

        recyclerView.setLayoutManager(null);
        recyclerView.setAdapter(null);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), columnCount);
        recyclerView.setLayoutManager(layoutManager);

        CartaAdapter adapter = new CartaAdapter(cartas, false, false) {
            @Override
            public void onBindViewHolder(@NonNull CartaViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                adjustCardSize(holder, columnCount);
            }
        };

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(columnCount, CARD_SPACING, true));
    }



    private void adjustCardSize(CartaAdapter.CartaViewHolder holder, int columnCount) {
        DisplayMetrics displayMetrics = holder.itemView.getContext().getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int spacing = (int) (CARD_SPACING * displayMetrics.density);

        int cardWidth = (screenWidth - (spacing * (columnCount + 1))) / columnCount;

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        params.width = cardWidth;
        params.height = (int) (cardWidth * 1.5);
        holder.itemView.setLayoutParams(params);
    }

    private void iniciaInventario(Context context) {
        inventario.clear();
        for (String id : PartidaActivity.mazo) {
            inventario.add(Utilidades.buscaCarta(Integer.parseInt(id)));
        }
        setupRecyclerView(inventarioRecyclerView, inventario);
    }

    public void refrescaInventario() {
        inventario.clear();
        for (String id : PartidaActivity.mazo) {
            inventario.add(Utilidades.buscaCarta(Integer.parseInt(id)));
        }
        setupRecyclerView(inventarioRecyclerView, inventario);
    }

    public void refrescaPorRobar() {
        porRobar.clear();
        for (String id : PartidaActivity.porRobar) {
            porRobar.add(Utilidades.buscaCarta(Integer.parseInt(id)));
        }
        setupRecyclerView(porRobarRecyclerView, porRobar);
    }

    public void refrescaDescartadas() {
        descartadas = new ArrayList<Carta>();
        for (String id : PartidaActivity.descartadas) {
            descartadas.add(Utilidades.buscaCarta(Integer.parseInt(id)));
        }
        setupRecyclerView(descartadasRecyclerView, descartadas);
    }

    public void moverCartaADescartes(CombateFragment combate, int posicion) {
        PartidaActivity.descartadas.add(PartidaActivity.mano.remove(posicion));
        refrescaDescartadas();
        combate.cartasEnMano--;
    }

    public void moverCartaAMano(CombateFragment combate) {
        if (PartidaActivity.porRobar.isEmpty()) {
            PartidaActivity.porRobar.addAll(PartidaActivity.descartadas);
            PartidaActivity.descartadas.clear();
            Collections.shuffle(PartidaActivity.porRobar);
        }
        PartidaActivity.mano.add(PartidaActivity.porRobar.remove(0));
        refrescaPorRobar();
        refrescaDescartadas();
        combate.cartasEnMano++;
    }

    public void reiniciaMazo() {
        inventario.clear();
        PartidaActivity.porRobar.clear();
        PartidaActivity.descartadas.clear();
        PartidaActivity.mano.clear();

        refrescaDescartadas();
        refrescaPorRobar();

        for (String id : PartidaActivity.mazo) {
            inventario.add(Utilidades.buscaCarta(Integer.parseInt(id)));
        }
        refrescaInventario();
    }

    // Clase interna para el espaciado entre items
    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;
                if (position < spanCount) outRect.top = spacing;
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) outRect.top = spacing;
            }
        }
    }
}