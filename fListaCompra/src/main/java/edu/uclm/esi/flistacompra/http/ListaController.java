package edu.uclm.esi.flistacompra.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.flistacompra.model.Lista;
import edu.uclm.esi.flistacompra.model.Producto;
import edu.uclm.esi.flistacompra.services.ListaService;
import edu.uclm.esi.flistacompra.services.ProxyBEU;
import edu.uclm.esi.flistacompra.ws.WSListas;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("listas")
@CrossOrigin(origins = { "https://localhost:4200" }, allowCredentials = "true")
public class ListaController {
	@Autowired
	private ListaService listaService;
	@Autowired
	private ProxyBEU proxyBEU;

	@PostMapping("/crearLista")
	public Lista crearLista(@RequestBody String nombre, @RequestHeader("token") String token) {
		nombre = nombre.trim();
		if (nombre.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la lista no puede estar vacío");
		}

		if (nombre.length() > 80) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"El nombre de la lista está limitado a 80 caracteres.");
		}
		System.out.println("Premium" + this.proxyBEU.obtenerUsuarioPremium(token));
		String email = this.proxyBEU.validar(token);

		return this.listaService.crearLista(nombre, token, email);
	}

	@PostMapping("/addProducto")
	public Lista addProducto(HttpServletRequest request, @RequestBody Producto producto,
			@RequestHeader("token") String token) {
		if (producto.getNombre().isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");

		if (producto.getNombre().length() > 80)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"El nombre del producto esta limitado a 80 caracteres.");

		String idLista = request.getHeader("idLista");
		return this.listaService.addProducto(idLista, producto, token);
	}

	@PutMapping("/comprar")
	public Producto comprar(@RequestBody Map<String, Object> compra, @RequestHeader("token") String token) {
		String idProducto = compra.get("idProducto").toString();
		float unidadesCompradas = ((Number) compra.get("unidadesCompradas")).floatValue();
		return this.listaService.comprar(idProducto, unidadesCompradas, token);
	}

	@GetMapping("/obtenerListas")
	public List<Lista> obtenerListas(@RequestHeader("token") String token) {
		String emailLeido = this.proxyBEU.validar(token);
		return this.listaService.obtenerListas(emailLeido, token);
	}
	
	@PutMapping("/editar")
	public Producto editarProducto(@RequestBody Map<String, Object> productoEditado, @RequestHeader("token") String token) {
	    String idProducto = productoEditado.get("idProducto").toString();
	    String nuevoNombre = productoEditado.get("nombre").toString();
	    float nuevasUnidadesPedidas = ((Number) productoEditado.get("unidadesPedidas")).floatValue();
	    return this.listaService.editarProducto(idProducto, nuevoNombre, nuevasUnidadesPedidas, token);
	}


	@DeleteMapping("/eliminarLista{idLista}")
	public void borrarLista(@PathVariable String idLista, @RequestHeader("token") String token) {
		String email = this.proxyBEU.validar(token);
		this.listaService.borrarLista(idLista, email, token);
	}

	@GetMapping("verLista{idLista}")
	public Lista verLista(@PathVariable String idLista, @RequestHeader("token") String token) {
		return this.listaService.verLista(idLista, token);
	}

	@DeleteMapping("/eliminarProducto{idProducto}")
	public void borrarProducto(@PathVariable String idProducto, @RequestHeader("token") String token) {
		this.listaService.borrarProducto(idProducto, token);
	}

	@PostMapping("/invitarUsuario")
	public String invitarUsuario(HttpServletRequest request, @RequestHeader String idLista, @RequestHeader String email,
			@RequestHeader("token") String token) {
		return this.listaService.invitarUsuario(idLista, email, token);
	}

	
	@GetMapping("/aceptarInvitacion")
	public void aceptarInvitacion(HttpServletRequest request, @RequestParam String idLista, @RequestParam String email,
	                              @RequestParam("token") String token, HttpServletResponse response) throws IOException {

	    this.listaService.aceptarInvitacion(idLista, email, token);
	    response.sendRedirect("https://localhost:4200/GestorLista");
	}

	
	@DeleteMapping("/eliminarUsuario")
	public void eliminarUsuario(HttpServletRequest request, @RequestHeader String idLista, @RequestHeader String email,
			@RequestHeader("token") String token) {
		this.listaService.eliminarUsuario(idLista, email, token);
	}

}
