package edu.uclm.esi.flistacompra.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Lista {
	@Id @Column(length = 36)
	private String id;
	@Column(length = 80)
	private String nombre;
	
	private String propietario;
	
	@OneToMany(mappedBy = "lista")
	private List<Producto> productos;
	
	@ElementCollection
	private List<String> emailsUsuarios;
	
	public Lista() {
		this.id = UUID.randomUUID().toString();
		this.productos = new ArrayList<>();
		this.emailsUsuarios = new ArrayList<>();
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void add(Producto producto) {
		this.productos.add(producto);
	}

	public List<String> getEmailsUsuarios() {
		return emailsUsuarios;
	}

	public void setEmailsUsuarios(List<String> emailsUsuarios) {
		this.emailsUsuarios = emailsUsuarios;
	}
	
	public void addEmailUsuario(String email) {
		this.emailsUsuarios.add(email);
	}

	public String getPropietario() {
		return propietario;
	}

	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}
	
}
