package edu.uclm.esi.flistacompra.ws;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.flistacompra.dao.ListaDao;
import edu.uclm.esi.flistacompra.model.Producto;

@Component
public class WSListas extends TextWebSocketHandler {

	@Autowired
	private static ListaDao listaDao;

	private static Map<String, List<WebSocketSession>> sessionsByIdLista = new ConcurrentHashMap<>();
	
	@Autowired
	public void setListaDao(ListaDao listaDao) {
		WSListas.listaDao = listaDao;
	}


	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	    String email = this.getParameter(session, "email");
	    List<String> listas = this.listaDao.getListasDe(email); 
	    
	    for (String idLista : listas) {
	        idLista = idLista.trim().toLowerCase(); 
	        this.sessionsByIdLista.computeIfAbsent(idLista, k -> new ArrayList<>()).add(session);
	    }
	    

	    System.out.println("Contenido actualizado de sessionsByIdLista: " + this.sessionsByIdLista);
	}

	public void notificarAdd(String idLista, Producto producto) {
	    idLista = idLista.trim().toLowerCase(); 
	    System.out.println("Buscando sesiones para idLista: '" + idLista + "'");
	    
	    System.out.println("Claves disponibles en sessionsByIdLista:");
	    for (String key : this.sessionsByIdLista.keySet()) {
	        System.out.println("Clave: '" + key + "'");
	    }

	    List<WebSocketSession> interesados = this.sessionsByIdLista.get(idLista);
	    
	    if (interesados == null || interesados.isEmpty()) {
	        System.out.println("No hay sesiones asociadas a la lista: " + idLista);
	        return;
	    }

	    JSONObject jso = new JSONObject();
	    jso.put("tipo", "actualizacionDeLista");
	    jso.put("idLista", idLista);
	    jso.put("unidadesCompradas", producto.getUnidadesCompradas());
	    jso.put("unidadesPedidas", producto.getUnidadesPedidas());
	    jso.put("nombre", producto.getNombre());
	    TextMessage message = new TextMessage(jso.toString());

	    for (WebSocketSession target : interesados) {
	        System.out.println("Enviando mensaje a sesión: " + target.getId());
	        try {
	            target.sendMessage(message);
	        } catch (IOException e) {
	            System.err.println("Error al enviar mensaje a sesión: " + target.getId());
	        }
	    }
	}
	
	
	
	public void notificarEliminar(String idLista, Producto producto) {
	    idLista = idLista.trim().toLowerCase();
	    System.out.println("Buscando sesiones para idLista: '" + idLista + "'");

	    List<WebSocketSession> interesados = this.sessionsByIdLista.get(idLista);

	    if (interesados == null || interesados.isEmpty()) {
	        System.out.println("No hay sesiones asociadas a la lista: " + idLista);
	        return;
	    }

	    JSONObject jso = new JSONObject();
	    jso.put("tipo", "actualizacionDeLista");
	    jso.put("idLista", idLista);
	    jso.put("idProducto", producto.getId());
	    TextMessage message = new TextMessage(jso.toString());

	    for (WebSocketSession target : interesados) {
	        System.out.println("Enviando mensaje de eliminación a sesión: " + target.getId());
	        try {
	            target.sendMessage(message);
	        } catch (IOException e) {
	            System.err.println("Error al enviar mensaje a sesión: " + target.getId());
	        }
	    }
	}


	public void notificarEditar(String idLista, Producto producto) {
	    idLista = idLista.trim().toLowerCase();
	    List<WebSocketSession> interesados = this.sessionsByIdLista.get(idLista);
	    
	    if (interesados == null || interesados.isEmpty()) {
	        System.out.println("No hay sesiones asociadas a la lista: " + idLista);
	        return;
	    }
	    
	    JSONObject jso = new JSONObject();
	    jso.put("tipo", "actualizacionDeLista");
	    jso.put("idLista", idLista);
	    jso.put("nombre", producto.getId());
	    jso.put("unidadesCompradas", producto.getNombre());
	    jso.put("unidadesPedidas", producto.getUnidadesPedidas());
	    TextMessage message = new TextMessage(jso.toString());

	    for (WebSocketSession target : interesados) {
	        try {
	            target.sendMessage(message);
	        } catch (IOException e) {
	            System.err.println("Error al enviar mensaje a sesión: " + target.getId());
	        }
	    }
	}
	
	
	
	public void notificarCompra(String idLista, Producto producto) {
	    idLista = idLista.trim().toLowerCase();
	    List<WebSocketSession> interesados = this.sessionsByIdLista.get(idLista);
	    
	    if (interesados == null || interesados.isEmpty()) {
	        System.out.println("No hay sesiones asociadas a la lista: " + idLista);
	        return;
	    }

	    JSONObject jso = new JSONObject();
	    jso.put("tipo", "actualizacionDeLista");
	    jso.put("idLista", idLista);
	    jso.put("nombre", producto.getId());
	    jso.put("unidadesCompradas", producto.getUnidadesCompradas());
	    TextMessage message = new TextMessage(jso.toString());

	    for (WebSocketSession target : interesados) {
	        try {
	            target.sendMessage(message);
	        } catch (IOException e) {
	            System.err.println("Error al enviar mensaje a sesión: " + target.getId());
	        }
	    }
	}
	



	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	    this.sessionsByIdLista.values().forEach(sessions -> sessions.remove(session));
	}

	
	private String getParameter(WebSocketSession session, String paramName) {
	    URI uri = session.getUri();
	    String query = uri.getQuery();
	    System.out.println("Consulta URI: " + query);
	    for (String param : query.split("&")) {
	        String[] parts = param.split("=");
	        if (parts.length > 1 && paramName.equals(parts[0])) {
	            return parts[1];
	        }
	    }
	    return null;
	}

	
}
