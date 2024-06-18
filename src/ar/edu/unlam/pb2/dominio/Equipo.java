package ar.edu.unlam.pb2.dominio;

import java.util.Objects;

public class Equipo implements Comparable<Equipo> {
	private String nombrePais;
	private Grupo grupo;
	private Integer id;
	private Integer puntaje;
	private Integer golesAFavor;
	private Integer golesEnContra;

	public Equipo(Integer id, String nombrePais, Grupo grupo) {
		this.id = id;
		this.nombrePais = nombrePais;
		this.grupo = grupo;
		this.puntaje = 0;
		this.golesAFavor = 0;
		this.golesEnContra = 0;
	}

	public String getNombrePais() {
		return nombrePais;
	}

	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombrePais);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Equipo other = (Equipo) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombrePais, other.nombrePais);
	}
	
	public void acumularPuntos (Integer puntos) {
		this.puntaje += puntos;
	}
	
	public void registrarGolesAFavor (Integer golesAFavor) {
		this.golesAFavor += golesAFavor;
	}
	
	public void registrarGolesAEnContra (Integer golesEnContra) {
		this.golesEnContra += golesEnContra;
	}

	public Integer getPuntaje() {
		return puntaje;
	}

	public Integer getGolesAFavor() {
		return golesAFavor;
	}

	public Integer getGolesEnContra() {
		return golesEnContra;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public int compareTo(Equipo o) {
		return this.getPuntaje().compareTo(o.getPuntaje());
	}
	
	
	
	
}
