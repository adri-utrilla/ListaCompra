package edu.uclm.esi.fakeaccountsbe.http;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import edu.uclm.esi.fakeaccountsbe.services.UserService;

@RestController
@RequestMapping("tokens")
@CrossOrigin(origins = { "https://localhost:4200" }, allowCredentials = "true" )
public class TokenController {
	
	@Autowired
	private UserService userService;

	@PutMapping("/validar")
	public void validar(@RequestBody String token) {
		try {
			userService.findByToken(token);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token no válido");
		}
	}
}
















