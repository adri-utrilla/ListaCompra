package edu.uclm.esi.flistacompra.ws;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WSChat extends TextWebSocketHandler {

	private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	private Map<String, WebSocketSession> sessionsByNombre = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println(session.getId() + " conectado");
		String nombreUsuario = this.getNombreParameter(session);
		this.sessions.put(session.getId(), session);
		this.sessionsByNombre.put(nombreUsuario, session);

		JSONObject jso = new JSONObject();
		jso.put("tipo", "nuevoUsuario");
		jso.put("contenido", nombreUsuario);
		this.difundir(jso);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		JSONObject jso = new JSONObject(message.getPayload());
		if (jso.getString("tipo").equalsIgnoreCase("difusion")) {
			jso.put("tipo", "mensajeDeTexto");
			jso.put("contenido",jso.getString("contenido"));
			this.difundir(jso);
		} else if (jso.getString("tipo").equalsIgnoreCase("mensajePrivado")) {
			String destinatario = jso.getString("destinatario");
			WebSocketSession sessionDestinatario = this.sessionsByNombre.get(destinatario);
			if (sessionDestinatario != null) {
				sessionDestinatario.sendMessage(message);
			}
		}

	}

	private void difundir(JSONObject jso) throws IOException {
		TextMessage message = new TextMessage(jso.toString());
		for (WebSocketSession target : this.sessions.values()) {

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						target.sendMessage(message);
					} catch (IOException e) {
						WSChat.this.sessions.remove(target.getId());
					}
				}
			}).start();
		}
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		this.sessions.remove(session.getId());
	}

	private String getNombreParameter(WebSocketSession session) {
		URI uri = session.getUri();
		String query = uri.getQuery();
		for (String param : query.split("&")) {
			String[] parts = param.split("=");
			if (parts.length > 1 && "nombre".equals(parts[0]))
				return parts[1];
		}
		return null;
	}
}
