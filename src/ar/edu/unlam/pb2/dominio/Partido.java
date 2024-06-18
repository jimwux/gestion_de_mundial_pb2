package ar.edu.unlam.pb2.dominio;

public class Partido {

	private Integer id;
	private Equipo local;
	private Equipo rival;
	private Resultado resultado;

	public Partido(Integer id, Equipo local, Equipo rival) {
		this.id = id;
		this.local = local;
		this.rival = rival;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Equipo getLocal() {
		return local;
	}

	public void setLocal(Equipo local) {
		this.local = local;
	}

	public Equipo getRival() {
		return rival;
	}

	public void setRival(Equipo rival) {
		this.rival = rival;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}

	
	
	
	
}
