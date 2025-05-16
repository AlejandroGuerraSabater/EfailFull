package com.evadethefail.app.controlador;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Method;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.evadethefail.app.R;
import com.evadethefail.app.entidades.Ataque;
import com.evadethefail.app.entidades.AtaqueEfecto;
import com.evadethefail.app.entidades.Carta;
import com.evadethefail.app.entidades.Efecto;
import com.evadethefail.app.entidades.Enemigo;
import com.evadethefail.app.entidades.EstadisticasPartida;
import com.evadethefail.app.entidades.Marca;
import com.evadethefail.app.entidades.Modificador;
import com.evadethefail.app.entidades.Personaje;
import com.evadethefail.app.modelo.PartidaActivity;
import com.evadethefail.app.objectBox.App;
import com.evadethefail.app.objectBox.Utilidades;
import com.evadethefail.app.objetos.EfectoAdapter;
import com.evadethefail.app.objetos.Estado;
import com.evadethefail.app.objetos.MensajeCombate;
import com.evadethefail.app.vista.CartasFragment;
import com.evadethefail.app.vista.CombateFragment;
import com.evadethefail.app.vista.LoginActivity;
import com.evadethefail.app.vista.MapaFragment;
import com.evadethefail.app.vista.MenuActivity;
import io.objectbox.Box;
import io.objectbox.BoxStore;
/** @noinspection ALL*/
public class ControladorCombate {
	public static boolean inconveniente;
	private CartasFragment cartas = CartasFragment.instanciaCartas;
	private CombateFragment combate = CombateFragment.instanciaCombate;
	public static Ataque siguienteAtaque;
	public static Carta cartaUsada;
	public static boolean esCritico = false, reaccion = false;
	public static ControladorCombate instanciaControlador;
	public AlertDialog descripcionCartaDialog;
	private Enemigo enemigo;
	private Personaje personaje;
	public List<String> traitsEnemigo = new ArrayList<String>();
	public List<String> traitsPersonaje = new ArrayList<String>();
	public static double multiplicadorEspecial = 1;
	private static final BoxStore boxStore = App.getBoxStore();
	private static final Box<EstadisticasPartida> estadisticasPartidaBox = boxStore.boxFor(EstadisticasPartida.class);

	// Método para preparar el combate, asignar mazo y repartir cartas
	public void combateSetup() {
		instanciaControlador = this;

		ControladorPartida.eligeEnemigo();
		eleccionAtaque();

		// Obtener el enemigo y el personaje
		enemigo = PartidaActivity.enemigo;
		personaje = PartidaActivity.personaje;

		if (enemigo.getNombre().equals("Héroe Caído")){
			siguienteAtaque = Utilidades.buscaAtaque(33);
			Drawable icono = ContextCompat.getDrawable(combate.getContext(), Estado.GUTSSTATUS);
			combate.intencionEnemigo.setOnClickListener(v -> mostrarMensaje("El héroe caído prepara una acción legendaria...", false));
			combate.intencionEnemigo.setImageDrawable(icono);
		}

		// Actualizar UI
		combate.txtSaludEnemigo.setText(enemigo.getVidaRestante() + "/" + enemigo.getVida());
		combate.txtSaludEnemigo.setTextColor(Color.GREEN);
		combate.txtBloqueoEnemigo.setText(enemigo.getBloqueo() == 0 ? "" : String.valueOf(enemigo.getBloqueo()));
		combate.txtNombreEnemigo.setText(enemigo.getNombre());
		combate.txtNivelEnemigo.setText("Nv. " + enemigo.getNivel());

		combate.txtSaludJugador.setText(personaje.getVidaRestante() + "/" + personaje.getVida());
		combate.txtSaludJugador.setTextColor(Color.GREEN);
		combate.txtEnergiaJugador.setText(personaje.getEnergiaRestante() + "/" + personaje.getEnergia());
		combate.txtBloqueoJugador.setText(personaje.getBloqueo() == 0 ? "" : String.valueOf(personaje.getBloqueo()));
		combate.txtNivelJugador.setText("Nv. " + personaje.getNivel());
		combate.txtNombreJugador.setText(LoginActivity.jugador.getNombre());

		switch (MapaFragment.casilla){
			case 1:
				combate.layout.setBackground(ContextCompat.getDrawable(combate.getContext(), R.drawable.bgpueblo));
				aplicarContrasteTexto(combate.getView(), true); // true para fondo claro, false para fondo oscuro
				break;
			case 2:
				combate.layout.setBackground(ContextCompat.getDrawable(combate.getContext(), R.drawable.bgbosque));
				aplicarContrasteTexto(combate.getView(), true); // true para fondo claro, false para fondo oscuro
				break;

			case 3:
				combate.layout.setBackground(ContextCompat.getDrawable(combate.getContext(), R.drawable.bgiglesia));
				aplicarContrasteTexto(combate.getView(), false); // true para fondo claro, false para fondo oscuro
				break;
			case 4:
				combate.layout.setBackground(ContextCompat.getDrawable(combate.getContext(), R.drawable.bgcastillo));
				aplicarContrasteTexto(combate.getView(), false); // true para fondo claro, false para fondo oscuro
				break;
		}

		cargarImagenEnemigo(enemigo.getNombre());

		// Asignar mazo para el combate
		List<String> mazoJugador = new ArrayList<>(PartidaActivity.mazo);
		Collections.shuffle(mazoJugador);
		PartidaActivity.porRobar = mazoJugador;
		PartidaActivity.mano = new ArrayList<>();

		for (int i = 0; i < combate.cartasPorTurno; i++) { // Número de cartas iniciales
			if (!PartidaActivity.porRobar.isEmpty()) {
				PartidaActivity.mano.add(PartidaActivity.porRobar.remove(0));
			}
		}
		cartas.refrescaPorRobar();


		// Botón de estadísticas del jugador
		combate.btnInfoJugador.setOnClickListener(v -> mostrarInfoJugador());

		// Botón de estadísticas del enemigo
		combate.btnInfoEnemigo.setOnClickListener(v -> mostrarInfoEnemigo());

		// Botón de fin de turno
		combate.btnFinTurno.setOnClickListener(v -> turnoEnemigo());

		combate.actualizarMano();
	}

	private void mostrarInfoJugador() {
		Personaje personaje = PartidaActivity.personaje;
		String mensaje = String.format("Energía: %d/%d\nVida: %d/%d\nAtaque: %d\nDefensa: %d\nBloqueo: %d\nCrit: %.2f%%\nDmg Crit: %.2f%%",
				personaje.getEnergiaRestante(), personaje.getEnergia(),
				personaje.getVidaRestante(), personaje.getVida(),
				personaje.getAtaqueReal(), personaje.getDefensaReal(),
				personaje.getBloqueo(), personaje.getProbabilidadCriticoReal(), personaje.getDanoCriticoReal());


		mostrarMensaje(mensaje);
	}

	private void mostrarInfoEnemigo() {
		String mensaje = String.format("Vida: %d/%d\nAtaque: %d\nDefensa: %d\nBloqueo: %d\nCrit: %.2f%%\nDmg Crit: %.2f%%",
				enemigo.getVidaRestante(), enemigo.getVida(),
				enemigo.getAtaqueReal(), enemigo.getDefensaReal(),
				enemigo.getBloqueo(), enemigo.getProbabilidadCriticoReal(), enemigo.getDanioCriticoReal());

		mostrarMensaje(mensaje);
	}

	// Método para reiniciar el turno (actualizar duraciones de efectos...)
	public void nuevoTurno() {

		for (int i = 0; i < combate.cartasPorTurno; i++) {
			cartas.moverCartaAMano(combate);
		}

		cartas.refrescaDescartadas();
		cartas.refrescaPorRobar();
		combate.actualizarMano();


		controladorEfectosTurno(false);
		limpiaEfectos(false);
		traitsEnemigo = devuelveTraits(false);

		controladorEfectosTurno(true);
		limpiaEfectos(true);
		traitsPersonaje = devuelveTraits(true);

		PartidaActivity.personaje.setEnergiaRestante(PartidaActivity.personaje.getEnergia());
		personaje.setBloqueo(0);

		revisarMarcasInicioTurno(true);

		combate.txtEnergiaJugador
				.setText(PartidaActivity.personaje.getEnergiaRestante() + "/" + PartidaActivity.personaje.getEnergia());
		eleccionAtaque();
		combate.txtBloqueoJugador.setText(personaje.getBloqueo() == 0 ? "" : String.valueOf(personaje.getBloqueo()));

	}

	// En ControladorCombate.java
	public void seleccionarCarta(View cartaView, int posicion) {
		// Si ya hay una carta seleccionada, primero la deseleccionamos
		if (combate.cartaSeleccionada) {
			deseleccionarCarta();
		}

		// Seleccionar la nueva carta
		combate.cartaActiva = posicion;
		combate.cartaSeleccionada = true;
		elevarCarta(cartaView);

		// Actualizar estado de los botones
		combate.btnUsarCarta.setEnabled(true);
		combate.btnFinTurno.setEnabled(false);

		// Configurar listener para usar la carta
		combate.btnUsarCarta.setOnClickListener(v -> {
			if (lanzarCarta(PartidaActivity.mano.get(combate.cartaActiva))) {
				if (!combate.btnContinuar.isEnabled())
					deseleccionarCarta();
				combate.txtEnergiaJugador.setText(personaje.getEnergiaRestante() + "/" + personaje.getEnergia());
				combate.actualizarMano();
			}
		});
	}

	public void deseleccionarCarta() {
		if (combate.cartaActiva != -1) {
			// Obtener la vista de la carta seleccionada
			RecyclerView.ViewHolder viewHolder = combate.cartasRecyclerView
					.findViewHolderForAdapterPosition(combate.cartaActiva);

			if (viewHolder != null) {
				resetearPosicionCarta(viewHolder.itemView);
			}
		}

		// Resetear selección
		combate.cartaActiva = -1;
		combate.cartaSeleccionada = false;

		// Actualizar estado de los botones
		combate.btnUsarCarta.setEnabled(false);
		combate.btnFinTurno.setEnabled(true);
	}

	private void elevarCarta(View cartaView) {
		float elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -10, combate.getContext().getResources().getDisplayMetrics());
		TranslateAnimation animation = new TranslateAnimation(0, 0, 0, elevation);
		animation.setDuration(200);
		animation.setFillAfter(true);
		cartaView.startAnimation(animation);
	}

	private void resetearPosicionCarta(View cartaView) {
		if (cartaView != null) {
			TranslateAnimation animation = new TranslateAnimation(0, 0, cartaView.getTranslationY(), 0);
			animation.setDuration(200);
			animation.setFillAfter(true);
			cartaView.startAnimation(animation);
		}
	}

	public void mostrarDescripcionCarta(Carta carta) {
		AlertDialog.Builder builder = new AlertDialog.Builder(combate.getContext());
		LayoutInflater inflater = LayoutInflater.from(combate.getContext());
		View descripcionView = inflater.inflate(R.layout.dialog_descripcion_carta, null);

		// Obtener referencias a las vistas
		TextView txtNombre = descripcionView.findViewById(R.id.txtNombreCarta);
		TextView txtCoste = descripcionView.findViewById(R.id.txtCosteCarta);
		TextView txtElemento = descripcionView.findViewById(R.id.txtElementoCarta);
		TextView txtDescripcion = descripcionView.findViewById(R.id.txtDescripcionCarta);
		TextView txtPotencia = descripcionView.findViewById(R.id.txtPotenciaCarta);
		TextView txtTituloEfectos = descripcionView.findViewById(R.id.textView);
		RecyclerView rvEfectos = descripcionView.findViewById(R.id.rvEfectosCarta);
		Button btnCerrar = descripcionView.findViewById(R.id.btnCerrarDescripcion);

		// Establecer los datos básicos de la carta
		txtNombre.setText(carta.getNombre());
		txtCoste.setText("Coste: " + carta.getCosto());
		txtElemento.setText("Elemento: " + carta.getElemento());

		// Mostrar descripción solo si no está vacía
		if (carta.getDescripcion() != null && !carta.getDescripcion().isEmpty()) {
			txtDescripcion.setText(carta.getDescripcion());
			txtDescripcion.setVisibility(View.VISIBLE);
		} else {
			txtDescripcion.setVisibility(View.GONE);
		}

		// Mostrar potencia solo si es mayor que cero
		if (carta.getPotencia() > 0) {
			txtPotencia.setText("Potencia: " + carta.getPotencia());
			txtPotencia.setVisibility(View.VISIBLE);
		} else {
			txtPotencia.setVisibility(View.GONE);
		}

		// Configurar el RecyclerView para los efectos
		List<Efecto> efectos = carta.getEfectos();
		if (efectos != null && !efectos.isEmpty()) {
			txtTituloEfectos.setVisibility(View.VISIBLE);
			rvEfectos.setVisibility(View.VISIBLE);
			rvEfectos.setLayoutManager(new LinearLayoutManager(combate.getContext()));
			rvEfectos.setAdapter(new EfectoAdapter(efectos));

			// Agregar un divisor entre elementos
			DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
					rvEfectos.getContext(), LinearLayoutManager.VERTICAL);
			rvEfectos.addItemDecoration(dividerItemDecoration);
		} else {
			txtTituloEfectos.setVisibility(View.GONE);
			rvEfectos.setVisibility(View.GONE);
		}

		// Configurar el botón para cerrar el diálogo
		btnCerrar.setOnClickListener(v -> {
			if (descripcionCartaDialog != null && descripcionCartaDialog.isShowing()) {
				descripcionCartaDialog.dismiss();
			}
		});

		// Crear y mostrar el diálogo
		builder.setView(descripcionView);
		descripcionCartaDialog = builder.create();

		// Prevenir cierre al tocar fuera del diálogo
		descripcionCartaDialog.setCanceledOnTouchOutside(false);

		descripcionCartaDialog.show();
	}

	public boolean lanzarCarta(String id) {
		cartaUsada = Utilidades.buscaCarta(Integer.parseInt(id));

		if (personaje.getEnergiaRestante() < cartaUsada.getCosto()) {
			mostrarMensaje("¡Energía insuficiente!", false);
			return false;
		}

		PartidaActivity.estadisticas.cartasUsadasSumar(1);
		MenuActivity.estadisticas.cartasUsadasSumar(1);

		if ( cartaUsada.getIdCarta() == 411 ){
			int roll = (int) (Math.random() * 100) + 1;

			if (roll < 25)
				cartaUsada = Utilidades.buscaCarta(403);
			else if (roll < 50)
				cartaUsada = Utilidades.buscaCarta(405);
			else if (roll < 75)
				cartaUsada = Utilidades.buscaCarta(406);
			else
				cartaUsada = Utilidades.buscaCarta(407);
		}

		revisarMarcasPreAccion(true);
		revisarDerrota();

		if (!inconveniente){
			inconveniente = false;
			usarCarta(cartaUsada);

			revisarMarcasAccion(true);

			personaje.setEnergiaRestante(personaje.getEnergiaRestante() - cartaUsada.getCosto());
			controladorEfectosAccion(true);
			cartas.moverCartaADescartes(combate, combate.cartaActiva);
		}

		revisarDerrota();
		revisarVictoria();

		return true;
	}

	public void usarCarta(Carta carta){
		List<Efecto> efectos = carta.getEfectos();
		int dano = 0;

		if (carta.getObjetivo() == 0) {
			for (Efecto efecto : efectos) {
				personaje.agregarEfecto(combate, efecto);
			}
			combate.pintaEfectos(true);
			traitsPersonaje = devuelveTraits(true);
		} else {
			if (carta.getPotencia() > 0) {

				if (carta.getIdCarta() == 212){
					enemigo.setBloqueo(0);
				}

				dano = calcularDano(carta.getPotencia(), personaje.getNivel(), personaje.getAtaqueReal(),
						enemigo.getDefensaReal(), personaje.getProbabilidadCriticoReal(),
						personaje.getDanoCriticoReal(), carta.getTiposCompleto(), true);

				if (carta.getIdCarta() == 203) {
					dano = calcularDano(carta.getPotencia(), personaje.getNivel(), personaje.getAtaqueReal(),
							enemigo.getDefensaReal(), personaje.getProbabilidadCriticoReal() + 100,
							personaje.getDanoCriticoReal(), carta.getTiposCompleto(), true);
				}

				danar(false, dano);

				mostrarMensaje("¡Infligiste " + dano + " puntos de daño!", true);
				if (esCritico) {
					mostrarMensaje("¡Un golpe crítico!", true);
					PartidaActivity.estadisticas.criticosCausadosJugadorSumar(1);
					MenuActivity.estadisticas.criticosCausadosJugadorSumar(1);
					esCritico = false;
				}

				controladorEfectosAtaque(true);
				controladorEfectosAtaqueRecibido(false);
			}

			revisarMarcasReaccion(false);

			for (Efecto efecto : efectos) {
				enemigo.agregarEfecto(combate, efecto);
			}

			if (reaccion){
				MetodosMarcas.limpiaElementos(false);
				reaccion = false;
			}

			combate.pintaEfectos(false);
			traitsEnemigo = devuelveTraits(false);
		}

		combate.txtSaludJugador.setText(personaje.getVidaRestante() + "/" + personaje.getVida());
	}

	/**
	 * Carga una imagen de enemigo basada en el nombre proporcionado.
	 * El método convierte el nombre a minúsculas, elimina espacios,
	 * quita tildes y busca una imagen con ese nombre en la carpeta drawable.
	 *
	 * @param nombre Nombre del enemigo cuya imagen se quiere cargar
	 * @return true si la imagen se cargó correctamente, false en caso contrario
	 */
	public boolean cargarImagenEnemigo(String nombre) {
		try {
			// Procesar el nombre: convertir a minúsculas, eliminar espacios y quitar tildes
			String nombreProcesado = quitarTildes(nombre.toLowerCase().replace(" ", ""));

			// Obtener el ID del recurso usando reflection
			int idRecurso = combate.getContext().getResources().getIdentifier(
					nombreProcesado,
					"drawable",
					combate.getContext().getPackageName()
			);

			// Verificar si se encontró el recurso
			if (idRecurso == 0) {
				Log.e("CargarImagen", "No se encontró la imagen: " + nombreProcesado);
				return false;
			}

			// Cargar la imagen en combate.imgEnemigo (asumiendo que es un ImageView)
			combate.imgEnemigo.setImageResource(idRecurso);
			return true;

		} catch (Exception e) {
			Log.e("CargarImagen", "Error al cargar la imagen: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Método auxiliar para quitar tildes y caracteres especiales de una cadena
	 *
	 * @param texto Texto con posibles tildes
	 * @return Texto sin tildes ni caracteres especiales
	 */
	private String quitarTildes(String texto) {
		String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
		return normalized.replaceAll("[^\\p{ASCII}]", "");
	}

	// El turno del enemigo empieza cuando el jugador acaba el turno y termina
	// después de haber ejecutado el ataque y haber elegido el siguiente
	private void turnoEnemigo() {

		revisarMarcasFinTurno(true);
		revisarMarcasInicioTurno(false);
		revisarDerrota();
		revisarVictoria();

		enemigo.setBloqueo(0);
		combate.txtBloqueoEnemigo.setText(enemigo.getBloqueo() == 0 ? "" : String.valueOf(enemigo.getBloqueo()));

		while (combate.cartasEnMano > 0) {
			cartas.moverCartaADescartes(combate, 0);
		}

		combate.actualizarMano();

		revisarMarcasPreAccion(false);
		revisarVictoria();

		if ( siguienteAtaque.getIdAtaque() == 27 ){
			mostrarMensaje("¡El enemigo usó oración natural negra!", false);
			int roll = (int) (Math.random() * 100) + 1;

			if (roll < 25)
				siguienteAtaque = Utilidades.buscaAtaque(29);
			else if (roll < 50)
				siguienteAtaque = Utilidades.buscaAtaque(30);
			else if (roll < 75)
				siguienteAtaque = Utilidades.buscaAtaque(31);
			else
				siguienteAtaque = Utilidades.buscaAtaque(32);
		}

		atacar(siguienteAtaque);

		revisarMarcasAccion(false);
		revisarMarcasReaccion(true);

		revisarDerrota();
		revisarVictoria();

		revisarMarcasFinTurno(false);
		revisarDerrota();
		revisarVictoria();

		controladorEfectosAccion(false);
		inconveniente = false;
		nuevoTurno();
	}

	public void atacar (Ataque ataque){

		StringBuilder mensaje = new StringBuilder();
		mensaje.append(enemigo.getNombre()).append(" usó ").append(ataque.getNombre()).append(".\n");

		int dano = 0;
		if (ataque.getObjetivo() == 0) {
			for (Efecto efecto : ataque.getEfectos()) {
				enemigo.agregarEfecto(combate, efecto);
				if (efecto instanceof Modificador) {
					Modificador mod = (Modificador) efecto;
					mensaje.append("¡").append(enemigo.getNombre()).append(" se infligió una subida de ").append(mod.getEstadistica()).append("!\n");
				} else if (efecto instanceof Marca) {
					Marca mar = (Marca) efecto;
					mensaje.append("¡").append(enemigo.getNombre()).append(" se infligió ").append(mar.getMarcaNombre()).append("!\n");
				}
			}

			traitsEnemigo = devuelveTraits(false);
			combate.txtSaludEnemigo.setText(enemigo.getVidaRestante() + "/" + enemigo.getVida());
		} else {
			if (ataque.getPotencia() > 0) {
				dano = calcularDano(ataque.getPotencia(), enemigo.getNivel(), enemigo.getAtaqueReal(),
						personaje.getDefensaReal(), enemigo.getProbabilidadCriticoReal(),
						enemigo.getDanioCriticoReal(), ataque.getTiposCompleto(), false);

				mensaje.append("¡").append(enemigo.getNombre()).append(" te infligió ").append(dano).append(" de daño!\n");
				if (esCritico) {
					mensaje.append("¡Un golpe crítico!\n");
					PartidaActivity.estadisticas.criticosCausadosContraJugadorSumar(1);
					MenuActivity.estadisticas.criticosCausadosContraJugadorSumar(1);
					esCritico = false;
				}

				danar(true, dano);

				controladorEfectosAtaque(false);
				controladorEfectosAtaqueRecibido(true);
			}

			for (Efecto efecto : ataque.getEfectos()) {
				personaje.agregarEfecto(combate, efecto);
				if (efecto instanceof Modificador) {
					Modificador mod = (Modificador) efecto;
					mensaje.append("¡").append(enemigo.getNombre()).append(" te infligió una bajada de ").append(mod.getEstadistica()).append("!\n");
				} else if (efecto instanceof Marca) {
					Marca mar = (Marca) efecto;
					mensaje.append("¡").append(enemigo.getNombre()).append(" te infligió ").append(mar.getMarcaNombre()).append("!\n");
				}
			}

			if (reaccion){
				MetodosMarcas.limpiaElementos(true);
				reaccion = false;
			}

			traitsPersonaje = devuelveTraits(true);

			estadisticasPartidaBox.put(PartidaActivity.estadisticas);

			combate.txtSaludJugador.setText(personaje.getVidaRestante() + "/" + personaje.getVida());
		}

		mostrarMensaje(mensaje.toString(), false);
	}

	public void mostrarMensaje(String mensaje) {
		new AlertDialog.Builder(combate.getContext())
				.setMessage(mensaje)
				.setPositiveButton("OK", null)
				.show();
	}

	public void mostrarMensaje(String mensaje, boolean esDelJugador) {
		if (combate.adapterMensajes != null) {
			combate.adapterMensajes.agregarMensaje(new MensajeCombate(mensaje, esDelJugador));
		}
	}

	public void continuar() {
		PartidaActivity.batallaActiva = false;
		combate.btnContinuar.setVisibility(View.VISIBLE);
		combate.btnContinuar.setEnabled(true);
		combate.btnUsarCarta.setEnabled(false);
		combate.btnFinTurno.setEnabled(false);
	}

	public void revisarDerrota() {
		if (personaje.getVidaRestante() == 0){
			mostrarMensaje("Has sido derrotado...", false);
			continuar();
			combate.btnContinuar.setOnClickListener(d -> ControladorPartida.derrota(combate.getContext()));
		}
	}

	public void revisarVictoria() {
		if (enemigo.getVidaRestante() == 0){
			mostrarMensaje("¡Has ganado!", true);
			continuar();
			combate.btnContinuar.setOnClickListener(d ->ControladorPartida.victoria(combate.getContext()));
		}
	}

	private void eleccionAtaque() {
		int ataqueElegido = decideAtaque(true);
		siguienteAtaque = new Ataque(PartidaActivity.enemigo.getAtaques().get(ataqueElegido));

		for (AtaqueEfecto ae : PartidaActivity.enemigo.getAtaques().get(ataqueElegido).getAtaqueEfectos()){
			siguienteAtaque.getAtaqueEfectos().add(ae);
		}

		int iconoResId;

		if (siguienteAtaque.getObjetivo() == 1 ) {
			iconoResId = (siguienteAtaque.getPotencia() == 0) ? Estado.BUFFRATEDOWN : Estado.ATTACK;
		} else {
			iconoResId = Estado.BUFFRATEUP;
		}

		Drawable icono = ContextCompat.getDrawable(combate.getContext(), iconoResId);
		combate.intencionEnemigo.setImageDrawable(icono);

		String mensaje = siguienteAtaque.getObjetivo() == 0
				? "El enemigo planea aplicarse un estado positivo"
				: (siguienteAtaque.getPotencia() == 0)
				? "El enemigo te aplicará un estado negativo"
				: "El enemigo te atacará con una potencia de " + siguienteAtaque.getPotencia();

		combate.intencionEnemigo.setOnClickListener(v -> mostrarMensaje(mensaje, false));
	}

	private static int decideAtaque(Boolean normal) {
		if (normal) {
			int roll = (int) (Math.random() * 100) + 1;
			int acumulado = 0;

			for (int i = 0; i < PartidaActivity.enemigo.getAtaques().size(); i++) {
				acumulado += PartidaActivity.enemigo.getAtaques().get(i).getProbabilidad();
				if (roll <= acumulado) {
					return i;
				}

			}
		}
		//En la version entregada nunca se da el caso
		return 1;
	}

	//No valida los efectos con ataque recibido a propósito ya que no pueden durar de un turno a otro
	public void limpiaEfectos(boolean esJugador) {
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

		Iterator<Efecto> iterator = efectos.iterator();
		while (iterator.hasNext()) {
			Efecto efecto = iterator.next();
			if (efecto.getDuracionTurnos() == 0 && efecto.getDuracionAtaques() == 0
					&& efecto.getDuracionAcciones() == 0) {
				iterator.remove();
			}
		}

		combate.pintaEfectos(esJugador);
	}

	public void controladorEfectosTurno(boolean esJugador) {
		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

		for (int i = efectos.size() - 1; i >= 0; i--) {
			Efecto efecto = efectos.get(i);

			if (efecto.getDuracionTurnos() > 0) {
				efecto.setDuracionTurnos(efecto.getDuracionTurnos() - 1);
			}

			if (efecto.getDuracionAcciones() == 0 && efecto.getDuracionAtaques() == 0 && efecto.getDuracionTurnos() == 0 && efecto.getDuracionAtaquesRecibidos() == 0) {
				if (esJugador) {
					PartidaActivity.personaje.removerEfecto(combate, efecto);
				} else {
					PartidaActivity.enemigo.removerEfecto(combate, efecto);
				}
			}
		}

	}

	public void controladorEfectosAtaque(boolean esJugador) {
		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

		for (int i = efectos.size() - 1; i >= 0; i--) {
			Efecto efecto = efectos.get(i);

			if (efecto.getDuracionAtaques() > 0) {
				efecto.setDuracionAtaques(efecto.getDuracionAtaques() - 1);
			}

			if (efecto.getDuracionAcciones() == 0 && efecto.getDuracionAtaques() == 0 && efecto.getDuracionTurnos() == 0 && efecto.getDuracionAtaquesRecibidos() == 0) {
				if (esJugador) {
					PartidaActivity.personaje.removerEfecto(combate, efecto);
				} else {
					PartidaActivity.enemigo.removerEfecto(combate, efecto);
				}
			}
		}

	}

	public void controladorEfectosAtaqueRecibido(boolean esJugador) {
		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

		for (int i = efectos.size() - 1; i >= 0; i--) {
			Efecto efecto = efectos.get(i);

			if (efecto.getDuracionAtaquesRecibidos() > 0) {
				efecto.setDuracionAtaquesRecibidos(efecto.getDuracionAtaquesRecibidos() - 1);
			}

			if (efecto.getDuracionAcciones() == 0 && efecto.getDuracionAtaques() == 0 && efecto.getDuracionTurnos() == 0 && efecto.getDuracionAtaquesRecibidos() == 0) {
				if (esJugador) {
					PartidaActivity.personaje.removerEfecto(combate, efecto);
				} else {
					PartidaActivity.enemigo.removerEfecto(combate, efecto);
				}
			}
		}

	}

	public void controladorEfectosAccion(boolean esJugador) {
		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

		for (int i = efectos.size() - 1; i >= 0; i--) {
			Efecto efecto = efectos.get(i);

			if (efecto.getDuracionAcciones() > 0) {
				efecto.setDuracionAcciones(efecto.getDuracionAcciones() - 1);
			}

			if (efecto.getDuracionAcciones() == 0 && efecto.getDuracionAtaques() == 0 && efecto.getDuracionTurnos() == 0 && efecto.getDuracionAtaquesRecibidos() == 0) {
				if (esJugador) {
					PartidaActivity.personaje.removerEfecto(combate, efecto);
				} else {
					PartidaActivity.enemigo.removerEfecto(combate, efecto);
				}
			}
		}

	}

	/*
	 * Fórmula de daño: Variación * (Ataque * potencia / (defensa * 20 + 10) + 2)
	 */
	public int calcularDano(int potencia, int nivel, int ataque, int defensa, double probCrit, double danoCrit, List<String> tipos, boolean esJugador) {
		double variacion = 0.9 + (Math.random() * 0.1);
		double danoBase = variacion * ((1 + nivel * 0.1) * ataque * potencia / (defensa * 25) + 2);

		if (tipos != null){
			multiplicadorEspecial =  MetodosMarcas.revisaTraits(esJugador, traitsPersonaje, traitsEnemigo, tipos);
		}

		// Verificar crítico
		if (Math.random() * 100 < probCrit) {
			danoBase += danoBase * danoCrit / 100;
			esCritico = true;
		} else {
			esCritico = false;
		}

		return (int) (danoBase * multiplicadorEspecial);
	}

	public void danar(boolean esJugador, int dano) {
		int bloqueo, vidaRestante, vidaMaxima;

		dano = (int) (dano * multiplicadorEspecial);

		if (esJugador) {
			bloqueo = personaje.getBloqueo();
			vidaRestante = personaje.getVidaRestante();
			vidaMaxima = personaje.getVida();
			PartidaActivity.estadisticas.danoRecibidoSumar(dano);
			MenuActivity.estadisticas.danoRecibidoSumar(dano);
		} else {
			bloqueo = enemigo.getBloqueo();
			vidaRestante = enemigo.getVidaRestante();
			vidaMaxima = enemigo.getVida();
			if (dano > MenuActivity.estadisticas.getDanoMaximo()){
				MenuActivity.estadisticas.setDanoMaximo(dano);
			}
			PartidaActivity.estadisticas.danoCausadoSumar(dano);
			MenuActivity.estadisticas.danoCausadoSumar(dano);
		}

		// Calcular daño considerando bloqueo
		if (bloqueo > 0) {
			if (bloqueo > dano) {
				bloqueo -= dano;
			} else {
				int danoAux = dano - bloqueo;
				bloqueo = 0;
				vidaRestante -= danoAux;
			}
		} else {
			vidaRestante -= dano;
			if (esJugador){
				PartidaActivity.estadisticas.vidaPerdidaSumar(dano);
				MenuActivity.estadisticas.vidaPerdidaSumar(dano);
			}
		}

		// Asegurar que la vida no sea negativa
		vidaRestante = Math.max(vidaRestante, 0);

		int color = Color.GREEN;

		// Actualizar valores del objetivo
		if (esJugador) {
			personaje.setBloqueo(bloqueo);
			personaje.setVidaRestante(vidaRestante);
			combate.txtBloqueoJugador.setText(bloqueo == 0 ? "" : String.valueOf(bloqueo));
			combate.txtSaludJugador.setText(vidaRestante + "/" + vidaMaxima);

			if (vidaRestante < vidaMaxima * 0.2){
				color = Color.RED;
			} else if (vidaRestante < vidaMaxima * 0.5){
				color = Color.parseColor("#FF9913"); //Naranja
			}
			combate.txtSaludJugador.setTextColor(color);
		} else {
			enemigo.setBloqueo(bloqueo);
			enemigo.setVidaRestante(vidaRestante);
			combate.txtBloqueoEnemigo.setText(bloqueo == 0 ? "" : String.valueOf(bloqueo));
			combate.txtSaludEnemigo.setText(vidaRestante + "/" + vidaMaxima);

			if (vidaRestante < vidaMaxima * 0.2){
				color = Color.RED;
			} else if (vidaRestante < vidaMaxima * 0.5){
				color = Color.parseColor("#FF9913"); //Naranja
			}
			combate.txtSaludEnemigo.setTextColor(color);
		}
	}
	
	private List<String> devuelveTraits(boolean esJugador) {

		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();
		List<String> traits = new ArrayList<String>();

		for (int i = efectos.size() - 1; i >= 0; i--) {
			Efecto efecto = efectos.get(i);

			if (efecto instanceof Marca) {
				Marca mar = (Marca) efecto;
				if (mar.getMomento().equals("trait")){
					traits.add(mar.getNombre());
				}

			}
		}

		return traits;

	}
	
	private void revisarMarcasPreAccion(boolean esJugador) {

		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

		for (int i = efectos.size() - 1; i >= 0; i--) {
			Efecto efecto = efectos.get(i);

			if (efecto instanceof Marca) {
				Marca mar = (Marca) efecto;
				if (mar.getMomento().equals("preaccion")){
					revisaMarcas(mar, esJugador);
				}

			}
		}

	}
	
	private void revisarMarcasReaccion(boolean esJugador) {

		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

		for (int i = efectos.size() - 1; i >= 0; i--) {
			Efecto efecto = efectos.get(i);

			if (efecto instanceof Marca) {
				Marca mar = (Marca) efecto;
				if (mar.getMomento().equals("reaccion")) {
					revisaMarcas(mar, esJugador);
				}
			}
		}

	}
	
	private void revisarMarcasAccion(boolean esJugador) {

		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

		for (int i = efectos.size() - 1; i >= 0; i--) {
			Efecto efecto = efectos.get(i);

			if (efecto instanceof Marca) {
				Marca mar = (Marca) efecto;
				if (mar.getMomento().equals("accion")){
					revisaMarcas(mar, esJugador);
				}

			}
		}

	}

	private void revisarMarcasFinTurno(boolean esJugador) {

		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

		for (int i = efectos.size() - 1; i >= 0; i--) {
			Efecto efecto = efectos.get(i);

			if (efecto instanceof Marca) {
				Marca mar = (Marca) efecto;
				if (mar.getMomento().equals("finTurno")){
					revisaMarcas(mar, esJugador);
				}

			}
		}

	}
	private void revisarMarcasInicioTurno(boolean esJugador) {

		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

		for (int i = efectos.size() - 1; i >= 0; i--) {
			Efecto efecto = efectos.get(i);

			if (efecto instanceof Marca) {
				Marca mar = (Marca) efecto;
				if (mar.getMomento().equals("inicioTurno")){
					revisaMarcas(mar, esJugador);
				}

			}
		}

	}


	private void revisaMarcas(Marca mar, boolean esJugador) {
		try {
			MetodosMarcas obj = new MetodosMarcas();

			String nombreMetodo = mar.getMetodosAsociados();

			Class<?>[] tiposParametros = new Class<?>[]{boolean.class};
			Method metodo = MetodosMarcas.class.getMethod(nombreMetodo, tiposParametros);

			metodo.invoke(obj, esJugador);


		} catch (Exception e) {
			//Toast.makeText(combate.getContext(), "prueba", Toast.LENGTH_SHORT).show();
		}
		
	}

	private void aplicarContrasteTexto(View view, boolean modoClaro) {
		int color = ContextCompat.getColor(combate.requireContext(),
				modoClaro ? R.color.contrast_dark_gray : R.color.contrast_light_gray);

		cambiarColorTexto(view, color);
	}

	private void cambiarColorTexto(View view, int color) {
		if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;
			for (int i = 0; i < group.getChildCount(); i++) {
				cambiarColorTexto(group.getChildAt(i), color);
			}
		} else if (view instanceof TextView) {
			((TextView) view).setTextColor(color);
		}
	}


}
