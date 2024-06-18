package ar.edu.unlam.pb2.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unlam.pb2.dominio.*;

public class TestMundial {
	
	private Torneo torneo;
	
	@Before
	public void init () {
		this.torneo = new Torneo("Qatar 2024");
	}

	@Test
	public void queSePuedaCrearUnTorneoCon32Equipos() throws GrupoLlenoException {
		
		assertNotNull(this.torneo);
		assertEquals("Qatar 2024", this.torneo.getNombre());
				
		String[] nombresEquipos = {"Catar", "Ecuador", "Senegal", "Países Bajos", 
			"Inglaterra", "Irán", "Estados Unidos", "Gales", "Argentina", "Arabia Saudita",
			"México", "Polonia", "Francia", "Australia", "Dinamarca", "Túnez", "España", "Costa Rica",
			"Alemania", "Japón", "Bélgica", "Canadá", "Marruecos", "Croacia", "Brasil", "Serbia", "Suiza",
			"Camerún", "Portugal", "Ghana", "Uruguay", "Corea del Sur"};
		
		Integer iteradorGrupo = 0;
		
		for (int i = 0; i < nombresEquipos.length; i+=4) {
			this.torneo.agregarEquipo(i, nombresEquipos[i], Grupo.values()[iteradorGrupo]);
			this.torneo.agregarEquipo(i+1, nombresEquipos[i+1], Grupo.values()[iteradorGrupo]);
			this.torneo.agregarEquipo(i+2, nombresEquipos[i+2], Grupo.values()[iteradorGrupo]);
			this.torneo.agregarEquipo(i+3, nombresEquipos[i+3], Grupo.values()[iteradorGrupo]);		
			iteradorGrupo++;
		}
						
		assertEquals(32, this.torneo.getEquipos().size());
	}
	
	@Test
	public void queSePuedaCrearUnPartidoDeGruposConDosEquiposDelMismoGrupo() throws GrupoLlenoException, EquipoDuplicadoException, PartidoJugadoException {
		
		Equipo local = this.torneo.agregarEquipo(1, "Uruguay", Grupo.A);	
		Equipo rival = this.torneo.agregarEquipo(2, "México", Grupo.A);
		
		Integer idPartido = 1;
		assertTrue(this.torneo.registrarPartido(idPartido, local, rival));
		
		Partido partidoObtenido = this.torneo.obtenerPartido(idPartido);
		assertTrue(partidoObtenido instanceof FaseDeGrupos);
		
	}

	@Test
	public void queSePuedaCrearUnPartidoDeEliminatoriasConDosEquiposPertenecientesALaListaDeEquiposEnEliminatorias() throws EquipoDuplicadoException, GrupoLlenoException, PartidoJugadoException {
		
		Equipo local = this.torneo.agregarEquipo(1, "Uruguay", Grupo.A);
		Equipo rival = this.torneo.agregarEquipo(2, "México", Grupo.B);
		
		Integer idPartido = 1;
		assertTrue(this.torneo.registrarPartido(idPartido, local, rival));
		
		Partido partidoObtenido = this.torneo.obtenerPartido(idPartido);
		assertTrue(partidoObtenido instanceof Eliminatoria);
	}

	@Test (expected = PartidoJugadoException.class)
	public void queAlCrearUnPartidoDondeLosEquiposYaJugaronSeLanceUnaPartidoJugadoException() throws EquipoDuplicadoException, PartidoJugadoException, GrupoLlenoException {
		
		Equipo local = this.torneo.agregarEquipo(1, "Uruguay", Grupo.A);	
		Equipo rival = this.torneo.agregarEquipo(2, "México", Grupo.B);
		
		assertTrue(this.torneo.registrarPartido(1, local, rival));

		this.torneo.registrarPartido(2, rival, local);
		
	}

	@Test (expected = EquipoDuplicadoException.class)
	public void queAlCrearUnPartidoDeGruposDondeElEquipoLocalEsElMismoQueElEquipoRivalSeLanceUnaEquipoDuplicadoException() throws GrupoLlenoException, EquipoDuplicadoException, PartidoJugadoException {
		
		Equipo local = this.torneo.agregarEquipo(1, "Uruguay", Grupo.A);	
		Equipo rival = this.torneo.agregarEquipo(1, "Uruguay", Grupo.A);
		
		this.torneo.registrarPartido(1, local, rival);

	}

	@Test
	public void queAlRegistrarElResultadoDeUnPartidoDeGruposSeAcumulenLosPuntosCorrespondientesACadaEquipo() throws GrupoLlenoException, EquipoDuplicadoException, PartidoJugadoException, TipoDePartidoIncorrectoException {
		
		Equipo local = this.torneo.agregarEquipo(1, "Uruguay", Grupo.A);	
		Equipo rival = this.torneo.agregarEquipo(2, "México", Grupo.A);
		
		this.torneo.registrarPartido(1, local, rival);
		
		Integer golesLocal = 1;
		Integer golesVisitantes = 3;
		this.torneo.registrarResultado(1, golesLocal, golesVisitantes);
		
		Integer puntajeLocalEsperado = 0;
		Integer puntajeRivalEsperado = 3;
		Integer puntajeLocal = this.torneo.obtenerPuntajeDeUnEquipo(1);
		Integer puntajeRival = this.torneo.obtenerPuntajeDeUnEquipo(2);

		assertEquals(puntajeLocalEsperado, puntajeLocal);
		assertEquals(puntajeRivalEsperado, puntajeRival);
		
		Equipo nuevoRival = this.torneo.agregarEquipo(3, "Irán", Grupo.A);
		this.torneo.registrarPartido(2, local, nuevoRival);
		
		golesLocal = 1;
		golesVisitantes = 1;
		this.torneo.registrarResultado(2, golesLocal, golesVisitantes);
		
		puntajeLocalEsperado = 1;
		puntajeRivalEsperado = 3;
		Integer puntajeNuevoRivalEsperado = 1;

		puntajeLocal = this.torneo.obtenerPuntajeDeUnEquipo(1);
		puntajeRival = this.torneo.obtenerPuntajeDeUnEquipo(2);
		Integer puntajeNuevoRival = this.torneo.obtenerPuntajeDeUnEquipo(3);

		assertEquals(puntajeLocalEsperado, puntajeLocal);
		assertEquals(puntajeRivalEsperado, puntajeRival);
		assertEquals(puntajeNuevoRivalEsperado, puntajeNuevoRival);
			
	}

	@Test
	public void queAlObtenerElResultadoDeUnPartidoDeGruposSeaElElementoEmpateDelEnum() throws EquipoDuplicadoException, PartidoJugadoException, GrupoLlenoException, TipoDePartidoIncorrectoException {
		
		Equipo local = this.torneo.agregarEquipo(1, "Uruguay", Grupo.A);	
		Equipo rival = this.torneo.agregarEquipo(2, "México", Grupo.A);
		
		Integer idPartido = 1;
		this.torneo.registrarPartido(idPartido, local, rival);
		
		Integer golesLocal = 3;
		Integer golesVisitantes = 3;
		this.torneo.registrarResultado(idPartido, golesLocal, golesVisitantes);
		
		Resultado resultadoObtenido = this.torneo.obtenerResultado(idPartido);
		assertEquals(Resultado.EMPATE, resultadoObtenido);
	}

	@Test
	public void queAlObtenerElResultadoDeUnPartidoDeEliminatoriasEnCasoDeEmpateSeObtengaElEnumDelGanadorPorPenales() throws GrupoLlenoException, EquipoDuplicadoException, PartidoJugadoException, EmpateEnPenalesException, TipoDePartidoIncorrectoException {
		
		Equipo local = this.torneo.agregarEquipo(1, "Uruguay", Grupo.A);	
		Equipo rival = this.torneo.agregarEquipo(2, "México", Grupo.B);
		
		Integer idPartido = 1;
		this.torneo.registrarPartido(idPartido, local, rival);
		
		Integer golesLocal = 3;
		Integer golesVisitantes = 3;
		Integer penalesConvertidosLocal = 4;
		Integer penalesConvertidosVisitante = 5;
			
		this.torneo.registrarResultado(idPartido, golesLocal, golesVisitantes, penalesConvertidosLocal, penalesConvertidosVisitante);
		Resultado resultadoObtenido = this.torneo.obtenerResultado(idPartido);
		
		assertEquals(Resultado.GANA_VISITANTE, resultadoObtenido);
		
	}

	@Test
	public void queAlConsultarPuntajeDeEquiposDeLosGrupoSeObtenganLosEquiposOrdenadosPorGrupoAscendenteYPorPuntosDescendentemente() throws GrupoLlenoException, EquipoDuplicadoException, PartidoJugadoException {
		
		Equipo uruguay = this.torneo.agregarEquipo(1, "Uruguay", Grupo.A);
		uruguay.acumularPuntos(5);
		
		Equipo mexico = this.torneo.agregarEquipo(2, "México", Grupo.B);
		mexico.acumularPuntos(2);
		
		Equipo españa = this.torneo.agregarEquipo(3, "España", Grupo.B);
		españa.acumularPuntos(4);

		Equipo marruecos = this.torneo.agregarEquipo(4, "Marruecos", Grupo.C);
		marruecos.acumularPuntos(8);

		Equipo croacia = this.torneo.agregarEquipo(5, "Croacia", Grupo.F);	
		croacia.acumularPuntos(3);

		Equipo polonia = this.torneo.agregarEquipo(6, "Polonia", Grupo.A);
		polonia.acumularPuntos(6);

		Equipo paisesBajos = this.torneo.agregarEquipo(7, "Países Bajos", Grupo.E);	
		paisesBajos.acumularPuntos(1);

		Equipo portugal = this.torneo.agregarEquipo(8, "Portugal", Grupo.F);
		portugal.acumularPuntos(0);

		TreeSet<Integer> puntajeEquiposOrdenados = this.torneo.obtenerPuntajeDeLosEquipos();
		Integer i = 0;
		
		for (Integer puntaje : puntajeEquiposOrdenados) {
			switch (i) {
			case 0:
				assertEquals(polonia.getPuntaje(), puntaje);
			break;
			case 1:
				assertEquals(uruguay.getPuntaje(), puntaje);
			break;
			case 2:
				assertEquals(españa.getPuntaje(), puntaje);
			break;
			case 3:
				assertEquals(mexico.getPuntaje(), puntaje);
			break;
			case 4:
				assertEquals(marruecos.getPuntaje(), puntaje);
			break;
			case 5:
				assertEquals(paisesBajos.getPuntaje(), puntaje);
			break;
			case 6:
				assertEquals(croacia.getPuntaje(), puntaje);
			break;
			case 7:
				assertEquals(portugal.getPuntaje(), puntaje);
			break;
			} i++;
		}

	}

	@Test
	public void queAlFinalizarLaFaseDeGruposSeAgreguenLosMejores2EquiposDeCadaGrupoALaColeccionDeEquiposEnEliminatorias() throws GrupoLlenoException {
		
		// GRUPO A

		Equipo uruguay = this.torneo.agregarEquipo(1, "Uruguay", Grupo.A);
		uruguay.acumularPuntos(5);
		
		Equipo mexico = this.torneo.agregarEquipo(2, "México", Grupo.A);
		mexico.acumularPuntos(2);
		
		Equipo españa = this.torneo.agregarEquipo(3, "España", Grupo.A);
		españa.acumularPuntos(4);

		Equipo marruecos = this.torneo.agregarEquipo(4, "Marruecos", Grupo.A);
		marruecos.acumularPuntos(8);

		// GRUPO B
		
		Equipo croacia = this.torneo.agregarEquipo(5, "Croacia", Grupo.B);	
		croacia.acumularPuntos(3);

		Equipo polonia = this.torneo.agregarEquipo(6, "Polonia", Grupo.B);
		polonia.acumularPuntos(6);

		Equipo paisesBajos = this.torneo.agregarEquipo(7, "Países Bajos", Grupo.B);	
		paisesBajos.acumularPuntos(1);

		Equipo portugal = this.torneo.agregarEquipo(8, "Portugal", Grupo.B);
		portugal.acumularPuntos(0);
		
		this.torneo.agregarMejoresEquiposAFaseEliminatoria();
		
		HashSet<Equipo> puntajeEquiposOrdenados = this.torneo.obtenerEquiposEnFaseEliminatoria();
		Integer i = 0;
		
		for (Equipo equipo : puntajeEquiposOrdenados) {
			switch (i) {
			case 0:
				assertEquals(marruecos, equipo);
			break;
			case 1:
				assertEquals(uruguay, equipo);
			break;
			case 2:
				assertEquals(polonia, equipo);
			break;
			case 3:
				assertEquals(croacia, equipo);
			} i++;
		}
	}

	
	
}
