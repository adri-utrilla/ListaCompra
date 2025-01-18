package edu.uclm.esi.fakeaccountsbe.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.fakeaccountsbe.model.User;
import edu.uclm.esi.fakeaccountsbe.services.UserService;

@RestController
@RequestMapping("tokens")
@CrossOrigin(origins = { "https://localhost:4200" }, allowCredentials = "true" )
public class TokenController {
	
	@Autowired
	private UserService userService;

	@PutMapping("/validar")
	public String validar(@RequestBody String token) {
		try {
			String email = userService.findByToken(token).getEmail();
			return email;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token no válido");
		}
	}
	
	@GetMapping("/obtenerUsuarioPremium")
	public Boolean obtenerUsuarioPremium(@RequestHeader("token") String token) {
		try {
			User user = userService.findByToken(token);
			if (user.isPremium()) {
				return user.isPremium();
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No es usuario premium");
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token no válido");
		}
	}
}