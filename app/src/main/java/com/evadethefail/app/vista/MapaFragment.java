package com.evadethefail.app.vista;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evadethefail.app.controlador.ControladorPartida;
import com.evadethefail.app.modelo.PartidaActivity;

import com.evadethefail.app.R;

/** @noinspection ALL*/
public class MapaFragment extends Fragment {

    public static int casilla = 1;

    public static int totalCasillas = 4;
    public static MapaFragment instanciaMapa;

    /* Version Final
    public static String[] tipoCasilla = new String[]{ "Combate", "Combate", "Combate", "Descanso", "Jefe",
            "Combate", "Combate", "Combate", "Descanso", "Recompensa",
            "Combate", "Combate", "Combate", "Descanso", "Jefe"};
     */

    //Version entregada
    public static String[] tipoCasilla = new String[]{ "Combate", "Combate", "Combate", "Jefe" };
    private ImageView imgCiudad, imgBosque, imgIglesia, imgCastillo;
    private Button continuarButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        instanciaMapa = this;
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        imgCiudad = view.findViewById(R.id.imgCiudad);
        imgBosque = view.findViewById(R.id.imgBosque);
        imgIglesia = view.findViewById(R.id.imgIglesia);
        imgCastillo = view.findViewById(R.id.imgCastillo);
        continuarButton = view.findViewById(R.id.continuarButton);

        ImageView[] casillas = {imgCiudad, imgBosque, imgIglesia, imgCastillo};

        // Primero oscurecemos todas
        for (ImageView casilla : casillas) {
            casilla.setColorFilter(Color.argb(140, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
        }

        continuarButton.setOnClickListener(v -> {
            String tipo = tipoCasilla[casilla - 1];
            if (tipo.equals("Combate") || tipo.equals("Jefe")) {
                ((PartidaActivity) getActivity()).iniciarCombate();
            } else {
                // A futuro: Casillas de no combate (Descansos, recompensas, eventos...)
                avanzarCasilla();
            }
            continuarButton.setEnabled(false); // Desactiva el botón hasta avanzar de nuevo
        });

        imgCiudad.clearColorFilter();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        PartidaActivity.instanciaPartida.activarModoInmersivo();
    }

    public void avanzarCasilla() {
        ControladorPartida.guardarProgreso(PartidaActivity.instanciaPartida);
        casilla++;
        ControladorPartida.partida.incrementarCasilla();
        actualizarPosicion(casilla);
        continuarButton.setEnabled(true); // Activar el botón de continuar
    }

    /*
    public void avanzarCasillaInterfaz() {
        ConstraintLayout layout = (ConstraintLayout) imgJugador.getParent();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        float biasX = 0.5f;
        float biasY = 0.5f;

        if (casilla % 5 == 0) {
            biasY += 0.35f;
            if (casilla != 5) {
                biasX = 0.23f;
            }
        } else {
            if (casilla > 5 && casilla < 10) {
                biasX -= 0.23f;
            } else {
                biasX += 0.23f;
            }
        }

        constraintSet.setHorizontalBias(imgJugador.getId(), biasX);
        constraintSet.setVerticalBias(imgJugador.getId(), biasY);
        constraintSet.applyTo(layout);
    }
    */

    private void actualizarPosicion(int destino) {

        ImageView[] casillas = {imgCiudad, imgBosque, imgIglesia, imgCastillo};

        // Primero oscurecemos todas
        for (ImageView casilla : casillas) {
            casilla.setColorFilter(Color.argb(120, 0, 0, 0), PorterDuff.Mode.SRC_ATOP); // 30% brillo
        }

        // Luego aclaramos la que toca
        switch (destino) {
            case 1:
                imgCiudad.clearColorFilter();
                break;
            case 2:
                imgBosque.clearColorFilter();
                break;
            case 3:
                imgIglesia.clearColorFilter();
                break;
            case 4:
                imgCastillo.clearColorFilter();
                break;
        }
    }


}
