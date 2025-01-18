package edu.uclm.esi.flistacompra.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.flistacompra.dao.ListaDao;
import edu.uclm.esi.flistacompra.dao.ProductoDao;
import edu.uclm.esi.flistacompra.model.Lista;
import edu.uclm.esi.flistacompra.model.Producto;
import edu.uclm.esi.flistacompra.ws.WSListas;

@Service
public class ListaService {

	@Autowired
	private ListaDao listaDao;
	@Autowired
	private ProductoDao productoDao;
	@Autowired
	private ProxyBEU proxy;
	@Autowired
	private WSListas wsListas;

	public Lista crearLista(String nombre, String token, String email) {
		String emailLeido = this.proxy.validar(token);
		if (emailLeido==null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");
		if (this.proxy.obtenerUsuarioPremium(token) == false && this.listaDao.contarListasDePropietario(emailLeido) >= 2) {
			throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED);
			
		}
        
		Lista lista = new Lista();
		lista.setNombre(nombre);
		lista.addEmailUsuario(emailLeido);
		lista.setPropietario(emailLeido);
		this.listaDao.save(lista);
		return lista;
	}

	public Lista addProducto(String idLista, Producto producto, String token) {
		if (this.proxy.validar(token) == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");
		if(this.proxy.obtenerUsuarioPremium(token)== false && this.productoDao.contarProductosDeLista(idLista) >= 10) {
			throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED);
		}
		Optional<Lista> optLista = this.listaDao.findById(idLista);
		if (optLista.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista");

		Lista lista = optLista.get();
		lista.add(producto);

		producto.setLista(lista);
		this.productoDao.save(producto);
		this.wsListas.notificarAdd(idLista, producto);
		return lista;
	}

	public Producto comprar(String idProducto, float unidadesCompradas, String token) {
		if(this.proxy.validar(token)== null) 
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");
	
		Optional<Producto> optProducto = this.productoDao.findById(idProducto);
		if (idProducto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha ingresado producto");
		
		Producto producto = optProducto.get();
		producto.setUnidadesCompradas(unidadesCompradas);
		String idLista = producto.getLista().getId();
		
		this.productoDao.save(producto);
		this.wsListas.notificarCompra(idLista, producto);

		return producto;
	}

	public void borrarLista(String idLista, String usuario, String token) {
		if (this.proxy.validar(token) == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");
		
		Optional<Lista> optLista = this.listaDao.findById(idLista);
		if (optLista.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista");
		Lista lista = optLista.get();
		if (!lista.getPropietario().equals(usuario))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No eres el propietario de la lista");
		else
			this.listaDao.deleteById(idLista);
	}

	public Lista verLista(String idLista, String token) {
		if (this.proxy.validar(token) == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");

		Optional<Lista> optLista = this.listaDao.findById(idLista);
		if (optLista.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista");
		return optLista.get();
	}
	
	public void borrarProducto(String idProducto, String token) {
	    if (this.proxy.validar(token) == null) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");
	    }
	    
	    Optional<Producto> optProducto = this.productoDao.findById(idProducto);
	    if (optProducto.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el producto");
	    }

	    Producto producto = optProducto.get();
	    String idLista = producto.getLista().getId();
	    this.productoDao.deleteById(idProducto);

	    this.wsListas.notificarEliminar(idLista, producto);
	}

	public List<Lista> obtenerListas(String email, String token) {
		if (this.proxy.validar(token) == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");
		
		List<Lista> result = new ArrayList<>();
		List<String> ids = this.listaDao.getListasDe(email);
		for (String id : ids) {
			result.add(this.listaDao.findById(id).get());
		}
		return result;
	}

	public String invitarUsuario(String idLista, String email, String token) {
	    String emailUsuario = this.proxy.validar(token);
		if (emailUsuario == null)
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");
		else if(!emailUsuario.equals(this.listaDao.findById(idLista).get().getPropietario()))
        	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No eres el propietario de la lista, no puedes invitar a nadie");

	    Optional<Lista> optLista = this.listaDao.findById(idLista);
	    if (optLista.isEmpty())
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista");

	    Lista lista = optLista.get();
	    lista.addEmailUsuario(email);
	    this.listaDao.save(lista);

	    String url = "https://localhost:80/listas/aceptarInvitacion?idLista=" + idLista +
	            "&email=" + email + "&token=" + token;
	    return url;
	}


	public void aceptarInvitacion(String idLista, String email, String token) {
		if (this.proxy.validar(token) == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");
	}
	
	public void eliminarUsuario(String idLista, String email, String token) {
		String emailUsuario = this.proxy.validar(token);
		if (emailUsuario == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");
		else if (this.listaDao.eliminarEmailDeLista(idLista, email) == 0)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el email en la lista");
		else if (!emailUsuario.equals(this.listaDao.findById(idLista).get().getPropietario()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No eres el propietario de la lista");
		else if(emailUsuario.equals(email))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No puedes eliminarte de tu propia lista");
		this.listaDao.eliminarEmailDeLista(idLista, email);
	}

	public Producto editarProducto(String idProducto, String nuevoNombre, float nuevasUnidadesPedidas, String token) {
	    if (this.proxy.validar(token) == null) 
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o sesión caducada");

	    Optional<Producto> optProducto = this.productoDao.findById(idProducto);
	    if (optProducto.isEmpty())
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");

	    Producto producto = optProducto.get();
	    producto.setNombre(nuevoNombre);
	    producto.setUnidadesPedidas(nuevasUnidadesPedidas);

	    this.productoDao.save(producto);
	    
	    this.wsListas.notificarEditar(producto.getLista().getId(), producto);
	    
	    return producto;
	}


}
