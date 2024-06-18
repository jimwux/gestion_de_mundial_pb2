package ar.edu.unlam.pb2.dominio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Torneo {

	private String nombre;
	private HashSet<Equipo> equipos;
	private HashSet<Partido> partidos;
	private TreeMap<Grupo, TreeSet<Equipo>> equiposPorGrupo;
	private HashSet<Equipo> equiposEnEliminatorias;

	public Torneo(String nombre) {
		this.nombre = nombre;
		this.equipos = new HashSet<>();
		this.partidos = new HashSet<>();
		this.equiposPorGrupo = this.obtenerEquiposPorGrupo();
		this.equiposEnEliminatorias = new HashSet<>();

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public HashSet<Equipo> getEquipos() {
		return equipos;
	}

	public void setEquipos(HashSet<Equipo> equipos) {
		this.equipos = equipos;
	}

	public Equipo agregarEquipo(Integer id, String nombrePais, Grupo grupo) throws GrupoLlenoException {

		this.verificarQueHayaEspacioEnEseGrupo(grupo);
		Equipo equipo = new Equipo(id, nombrePais, grupo);
		this.equipos.add(equipo);
		return equipo;
	}
	
	public Equipo obtenerEquipo (Integer idEquipo) {

		for (Equipo equipo : equipos) {
			if (equipo.getId().equals(idEquipo)) {
				return equipo;
			}
		}

		return null;

	}

	private boolean verificarQueHayaEspacioEnEseGrupo(Grupo grupo) throws GrupoLlenoException {

		Integer contador = 0;

		for (Equipo equipo : equipos) {
			if (equipo.getGrupo().equals(grupo)) {
				contador++;
			}
		}

		if (contador < 4) {
			return true;
		}

		throw new GrupoLlenoException("El grupo ya esta lleno");
	}

	public Boolean registrarPartido(Integer idPartido, Equipo local, Equipo rival)
			throws EquipoDuplicadoException, PartidoJugadoException {

		if (local.equals(rival)) {
			throw new EquipoDuplicadoException("El local y el rival son el mismo equipo");
		}

		this.verificarSiElPartidoYaSeJugo(local, rival);

		Partido partido = null;

		if (local.getGrupo().equals(rival.getGrupo())) {
			partido = new FaseDeGrupos(idPartido, local, rival);
		} else {
			partido = new Eliminatoria(idPartido, local, rival);
		}

		return this.partidos.add(partido);
	}

	private Boolean verificarSiElPartidoYaSeJugo(Equipo equipo1, Equipo equipo2) throws PartidoJugadoException {

		for (Partido partido : partidos) {
			if ((partido.getLocal().equals(equipo1) && partido.getRival().equals(equipo2))
					|| (partido.getLocal().equals(equipo2) && partido.getRival().equals(equipo1))) {
				throw new PartidoJugadoException("El partido ya se jugó");
			}
		}

		return true;
	}

	public Partido obtenerPartido(Integer idPartido) {

		for (Partido partido : partidos) {
			if (partido.getId().equals(idPartido)) {
				return partido;
			}
		}

		return null;

	}

	public void registrarResultado(Integer idPartido, Integer golesLocal, Integer golesVisitantes) throws TipoDePartidoIncorrectoException {

		Partido partido = this.obtenerPartido(idPartido);		
		
		Equipo local = partido.getLocal();
		Equipo rival = partido.getRival();
		
		local.registrarGolesAFavor(golesLocal);
		local.registrarGolesAEnContra(golesVisitantes);
		
		rival.registrarGolesAFavor(golesVisitantes);
		rival.registrarGolesAEnContra(golesLocal);
		
		if (golesLocal > golesVisitantes) {
			partido.setResultado(Resultado.GANA_LOCAL);
			local.acumularPuntos(3);
		} else if (golesVisitantes > golesLocal) {
			partido.setResultado(Resultado.GANA_VISITANTE);
			rival.acumularPuntos(3);
		} else {
			partido.setResultado(Resultado.EMPATE);
			local.acumularPuntos(1);
			rival.acumularPuntos(1);
		}	
	}
	
	public Resultado obtenerResultado(Integer idPartido) {
		Partido partido = this.obtenerPartido(idPartido);
		return partido.getResultado();
	}
	
	public Integer obtenerPuntajeDeUnEquipo (Integer idEquipo) {
		
		Equipo equipo = this.obtenerEquipo(idEquipo);		
		return equipo.getPuntaje();
		
	}

	public void registrarResultado(Integer idPartido, Integer golesLocal, Integer golesVisitantes, Integer penalesConvertidosLocal, Integer penalesConvertidosVisitante) throws EmpateEnPenalesException, TipoDePartidoIncorrectoException {
		
		Partido partido = this.obtenerPartido(idPartido);
		
		if (!(partido instanceof Eliminatoria)) {
			throw new TipoDePartidoIncorrectoException("El partido es de fase de grupos, no se puede ir a penales");
		}
		
		this.registrarResultado(idPartido, golesLocal, golesVisitantes);
		
		if (partido.getResultado().equals(Resultado.EMPATE)) {
			this.registrarResultado(idPartido, penalesConvertidosLocal, penalesConvertidosVisitante);
			if (partido.getResultado().equals(Resultado.EMPATE)) {
				throw new EmpateEnPenalesException("No es posible empatar en penales");
			}
		}
		
	}

	public TreeSet<Integer> obtenerPuntajeDeLosEquipos() {

		TreeSet<Integer> puntajeEquipos = new TreeSet<>();

		for (Map.Entry<Grupo, TreeSet<Equipo>> entry : equiposPorGrupo.entrySet()) {					
			for (Equipo equipo : entry.getValue()) {
				puntajeEquipos.add(equipo.getPuntaje());
			}
		}
		
		return puntajeEquipos;
	}
	
	public TreeMap<Grupo, TreeSet<Equipo>> obtenerEquiposPorGrupo () {
		
		TreeMap<Grupo, TreeSet<Equipo>> equiposPorGrupo = new TreeMap<>();
		
		equiposPorGrupo.put(Grupo.A, new TreeSet<Equipo>());
		equiposPorGrupo.put(Grupo.B, new TreeSet<Equipo>());
		equiposPorGrupo.put(Grupo.C, new TreeSet<Equipo>());
		equiposPorGrupo.put(Grupo.D, new TreeSet<Equipo>());
		equiposPorGrupo.put(Grupo.E, new TreeSet<Equipo>());
		equiposPorGrupo.put(Grupo.F, new TreeSet<Equipo>());
		equiposPorGrupo.put(Grupo.G, new TreeSet<Equipo>());
		equiposPorGrupo.put(Grupo.H, new TreeSet<Equipo>());
			
		for (Map.Entry<Grupo, TreeSet<Equipo>> entry : equiposPorGrupo.entrySet()) {					
			for (Equipo equipo : equipos) {
				if (equipo.getGrupo().equals(entry.getKey())) {
					entry.getValue().add(equipo);
				}
			}
		}
		
		return equiposPorGrupo;
		
	}
	
	public void agregarMejoresEquiposAFaseEliminatoria() {
		
		HashSet<Equipo> equiposEnEliminatorias = new HashSet<>();
		
		for (Map.Entry<Grupo, TreeSet<Equipo>> entry : equiposPorGrupo.entrySet()) {
			
	        List<Equipo> equiposDelGrupo = new ArrayList<>(entry.getValue());
	        if (equiposDelGrupo.size() >= 2) { // se fija si al menos hay dos equipos
	            equiposEnEliminatorias.add(equiposDelGrupo.get(0)); 
	            equiposEnEliminatorias.add(equiposDelGrupo.get(1));
	        }
		}
		
		this.equiposEnEliminatorias = equiposEnEliminatorias;
		
	}

	public HashSet<Equipo> obtenerEquiposEnFaseEliminatoria() {
		return this.equiposEnEliminatorias;
	}

}
