package edu.uclm.esi.fakeaccountsbe.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.uclm.esi.fakeaccountsbe.services.PagosService;

@RestController
@RequestMapping("pagos")
@CrossOrigin(origins = { "https://localhost:4200" }, allowCredentials = "true")
public class PagosController {
	
	@Autowired
	private PagosService service;

	@PutMapping("/prepararTransaccion")
	public String prepararTransaccion(@RequestBody float importe, @RequestHeader("token") String token) {
			return this.service.prepararTransaccion((long)(importe * 100));
	}
}
