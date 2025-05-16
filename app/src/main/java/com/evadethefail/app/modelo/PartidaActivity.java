package com.evadethefail.app.modelo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.evadethefail.app.controlador.ControladorPartida;
import com.evadethefail.app.entidades.Enemigo;
import com.evadethefail.app.entidades.EstadisticasPartida;
import com.evadethefail.app.entidades.Personaje;
import com.evadethefail.app.vista.CartasFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import com.evadethefail.app.R;

import com.evadethefail.app.objetos.PartidaPagerAdapter;
import com.evadethefail.app.vista.MenuActivity;
/** @noinspection ALL*/
public class PartidaActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    public static ViewPager2 viewPager;
    public static PartidaPagerAdapter pagerAdapter;
    public static EstadisticasPartida estadisticas;
    public static boolean batallaActiva = false;
    public static Personaje personaje;
    public static String tipoCasillaActual;
    public static List<String> mazo = new ArrayList<>();
    public static List<String> mano = new ArrayList<>();
    public static List<String> porRobar = new ArrayList<>();
    public static List<String> descartadas = new ArrayList<>();
    public static List<String> desterradas = new ArrayList<>();
    public static Enemigo enemigo;
    public static int personajeSeleccionado;
    public static PartidaActivity instanciaPartida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instanciaPartida = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        PartidaActivity.mazo = new ArrayList<>();
        PartidaActivity.mano = new ArrayList<>();
        PartidaActivity.porRobar = new ArrayList<>();
        PartidaActivity.descartadas = new ArrayList<>();
        PartidaActivity.desterradas = new ArrayList<>();

        // Configurar el adaptador del ViewPager2
        pagerAdapter = new PartidaPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ (API 30+)
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.navigationBars());
                insetsController.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        } else {
            // Android 10 o menor (API <30)
            View decorView = getWindow().getDecorView();
            int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(flags);
        }


        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View tabView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            TextView tabText = tabView.findViewById(R.id.tab_text);
            switch (position) {
                case 0:
                    tabText.setText("Mapa");
                    break;
                case 1:
                    tabText.setText("Cartas");
                    break;
                case 2:
                    tabText.setText("Combate");
                    break;
            }

            tab.setCustomView(tabView);
        }).attach();


        viewPager.setUserInputEnabled(true); // Se puede deslizar, pero Combate no está aún


        switch (personaje.getClase().getNombre()) {
            case "Caballero":
                MenuActivity.estadisticas.vecesUsadoCaballeroSumar(1);
                break;
            case "Asesino":
                MenuActivity.estadisticas.vecesUsadoAsesinoSumar(1);
                break;
            case "Hechicera":
                MenuActivity.estadisticas.vecesUsadoHechiceraSumar(1);
                break;
        }

        // Preguntar antes de salir
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                confirmarSalida(PartidaActivity.this);
            }
        });
        ControladorPartida.guardarProgreso(this);
    }

    public void activarModoInmersivo() {
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 1) Desactiva el ajuste automático de insets
            window.setDecorFitsSystemWindows(false);

            // 2) Consigue el controller
            final WindowInsetsController insetsController = window.getInsetsController();
            if (insetsController != null) {
                // 3) Oculta BOTH systemBars (status + navigation)
                insetsController.hide(WindowInsets.Type.systemBars());

                // 4) Comportamiento: swipe para mostrar temporalmente
                insetsController.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        } else {
            View decorView = window.getDecorView();
            int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(flags);
        }
    }


    /**
     * Método público para que MapaFragment active el combate.
     */
    public void iniciarCombate() {
        batallaActiva = true;
        pagerAdapter.activarCombate();
        viewPager.setCurrentItem(2, true); // Ir al Combate con animación
    }

    private void confirmarSalida(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Salir del juego")
                .setMessage("¿Quieres guardar tu progreso antes de salir?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    ControladorPartida.guardarProgreso(context);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    ControladorPartida.establecerVidaCero();
                    finish();
                })
                .setNeutralButton("Cancelar", null)
                .show();
    }

}
