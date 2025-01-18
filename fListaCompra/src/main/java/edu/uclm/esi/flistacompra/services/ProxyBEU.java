package edu.uclm.esi.flistacompra.services;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;

@Service
public class ProxyBEU {

	public String validar(String token) {
		String url = "http://localhost:8000/tokens/validar";

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPut httpPut = new HttpPut(url);
			httpPut.setEntity(new StringEntity(token));
			httpPut.setHeader("Content-Type", "text/plain");

			HttpContext context = new BasicHttpContext();
			try (CloseableHttpResponse response = httpClient.execute(httpPut, context)) {
				System.out.println("Response Status: " + response.getCode());
				String r =  EntityUtils.toString(response.getEntity());
				return r;
			}
		} catch (Exception e) {
			
			return null;
			
		}
	}

	
	public Boolean obtenerUsuarioPremium(String token) {
	    String url = "http://localhost:8000/tokens/obtenerUsuarioPremium";

	    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
	        HttpGet httpGet = new HttpGet(url);
	        httpGet.setHeader("Content-Type", "application/json");
	        httpGet.setHeader("token", token);

	        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
	            int statusCode = response.getCode();

	            if (statusCode == 200) {
	                String responseBody = EntityUtils.toString(response.getEntity());
	                System.out.println("Respuesta del servidor: " + responseBody);
	                return Boolean.parseBoolean(responseBody);
	            } else if (statusCode == 403) {
	                System.out.println("El usuario no es premium o el token no es válido.");
	                return false;
	            } else {
	                System.out.println("Error en la solicitud. Código: " + statusCode);
	                return false;
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Error al obtener el estado premium del usuario: " + e.getMessage());
	        return false;
	    }
	}

}
