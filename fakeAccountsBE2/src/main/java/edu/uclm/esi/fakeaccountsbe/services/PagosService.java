package edu.uclm.esi.fakeaccountsbe.services;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PagosService {
	
	static {
		Stripe.apiKey = "sk_test_51Q7a7MAXrAwE3n4IbqTbR6peOuwgpjz5lMi0Vj6TBQD1f5SyKfFqAnyx8aGLl1LBA8ydfAeWrkJb36QUcg92RJx3001Pq7xt5S";
	}

	

	public String prepararTransaccion(long importe) {
		PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
				.setCurrency("eur")
				.setAmount(importe)
				.build();
		
		PaymentIntent intent;
		try {
			intent = PaymentIntent.create(params);
			JSONObject jso = new JSONObject(intent.toJson());
			String clientSecret = jso.getString("client_secret");
			return clientSecret;	
		}catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}	

	}

}
