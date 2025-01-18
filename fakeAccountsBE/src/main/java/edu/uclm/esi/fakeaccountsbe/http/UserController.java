package edu.uclm.esi.fakeaccountsbe.http;

import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.esi.fakeaccountsbe.dao.UserDao;
import edu.uclm.esi.fakeaccountsbe.model.CredencialesRegistro;
import edu.uclm.esi.fakeaccountsbe.model.User;
import edu.uclm.esi.fakeaccountsbe.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = { "https://localhost:4200" }, allowCredentials = "true")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;

	@PostMapping("/registrar1")
	public void registrar1(HttpServletRequest req, @RequestBody CredencialesRegistro cr) throws IOException {
		cr.comprobar();
		User user = new User();
		user.setEmail(cr.getEmail());
		user.setPwd(cr.getPwd1());

		this.userService.registrar(req.getRemoteAddr(), user);
	}

	@PostMapping("/hacersePremium")
	public void hacerPremium(HttpServletRequest req, @RequestHeader("token") String token) {
		User user = this.userDao.findByToken(token);
		if(user == null)
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token no válido");

		if (user.isPremium())
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ya eres premium");

		user.setPremium(true);
		this.userDao.save(user);
	}

	@GetMapping("/verificarPremium")
	public Map<String, Boolean> verificarPremium(@RequestHeader("token") String token) {
	    User user = this.userDao.findByToken(token);
	    if (user == null) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token no válido");
	    }

	    Map<String, Boolean> response = new HashMap<>();
	    response.put("isPremium", user.isPremium());
	    return response;
	}

	@GetMapping("/validarCuenta/{validationToken}")
	public void validar(HttpServletResponse response, @PathVariable String validationToken) {
		try {
			this.userService.confirm(validationToken);
			response.sendRedirect("https://localhost:4200/");			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
	
	@PostMapping("/correoCambioContrasena")
	public void correoCambioContrasena(HttpServletRequest req, @RequestBody String email) throws IOException {
	    if (this.userDao.findById(email).isPresent()) {
	        User user = new User();
	        
	        Optional<User> optUser = this.userDao.findById(email);
			user = optUser.get();

	        user.setRecoveryToken(UUID.randomUUID().toString());

	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(new Date());
	        calendar.add(Calendar.HOUR_OF_DAY, 1);
	        Date date = calendar.getTime();

	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String formattedDate = sdf.format(date);

	        System.out.println("Formatted Date: " + formattedDate);

	        user.setCaducidadRecoveryToken(formattedDate);
	        this.userService.correoCambioContrasena(user);
	    } else {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este correo no está registrado.");
	    }
	}
	
	@PostMapping("/cambioContrasena")
	public void cambioContrasena(HttpServletRequest req, @RequestBody String body) throws IOException {
	    
	    ObjectMapper objectMapper = new ObjectMapper();

	    @SuppressWarnings("unchecked")
	    Map<String, Object> map = objectMapper.readValue(body, Map.class);

	    String pwd = (String) map.get("password");
	    String recToken = (String) map.get("token");

	    User user = this.userDao.findByRecoveryToken(recToken);

	    if (user != null) {
	        String caducidadTokenStr = user.getCaducidadRecoveryToken();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        LocalDateTime caducidadToken = LocalDateTime.parse(caducidadTokenStr, formatter);

	        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

	        if (caducidadToken.isAfter(now)) {
	            user.setPwd(pwd);
	            this.userService.cambioContrasena(user);
	        } else {
	            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El token ha caducado");
	        }
	    } else {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existe el Token de Recuperacion");
	    }
	}

	@GetMapping("/registrar2")
	public void registrar2(HttpServletRequest req, @RequestParam String email, @RequestParam String pwd1,
			@RequestParam String pwd2) throws IOException {
		CredencialesRegistro cr = new CredencialesRegistro();
		cr.setEmail(email);
		cr.setPwd1(pwd1);
		cr.setPwd2(pwd2);
		cr.comprobar();
		User user = new User();
		user.setEmail(cr.getEmail());
		user.setPwd(cr.getPwd1());

		this.userService.registrar(req.getRemoteAddr(), user);
	}

	@GetMapping("/registrarMuchos")
	public void registrarMuchos(HttpServletRequest req, @RequestParam String name, @RequestParam Integer n) throws IOException {
		for (int i = 0; i < n; i++)
			this.registrar2(req, name + i + "@pepe.com", "Pepe1234", "Pepe1234");
	}

	@PutMapping("/login1")
	public String login1(HttpServletResponse response, HttpServletRequest request, @RequestBody(required = false) User user){
		String fakeUserId = this.findCookie(request, "fakeUserId");
		if (user!=null || fakeUserId == null) {
			user = this.userService.find(user.getEmail(), user.getPwd());
			fakeUserId = UUID.randomUUID().toString();
			
			Cookie cookie = new Cookie("fakeUserId", fakeUserId);
			cookie.setMaxAge(3600*24*365);
			cookie.setPath("/");
			cookie.setAttribute("SameSite", "None");
			cookie.setSecure(true);
			response.addCookie(cookie);

			user.setCookie(fakeUserId);
			user.setToken(UUID.randomUUID().toString());
			this.userDao.save(user);
		} else {
			System.out.println("Cookie: "+ fakeUserId);
			user = this.userDao.findByCookie(fakeUserId);
			if(user != null && user.getValidationDate()!= null ) {
				user.setToken(UUID.randomUUID().toString());
				this.userDao.save(user);
			}else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cookie caducada");
			}
		}
		
		
		return user.getToken();
	}

	private String findCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (int i = 0; i < cookies.length; i++)
			if (cookies[i].getName().equals(cookieName))
				return cookies[i].getValue();
		return null;
	}
	

	@GetMapping("/checkCookie")
	public ResponseEntity<Map<String, String>> checkCookie(HttpServletRequest request) {
	    String fakeUserId = this.findCookie(request, "fakeUserId");
	    if (fakeUserId != null) {
	        User user = this.userDao.findByCookie(fakeUserId);
	        if (user != null) {
	            String email = user.getEmail();
	            Map<String, String> response = new HashMap<>();
	            response.put("token", user.getToken());
	            response.put("email", email);
	            return ResponseEntity.ok(response);
	        }
	    }
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}


	@GetMapping("/login2")
	public User login2(HttpServletResponse response, @RequestParam String email, @RequestParam String pwd) {
		User user = this.userService.find(email, pwd);
		user.setToken(UUID.randomUUID().toString());
		response.setHeader("token", user.getToken());
		return user;
	}

	@GetMapping("/login3/{email}")
	public User login3(HttpServletResponse response, @PathVariable String email, @RequestParam String pwd) {
		return this.login2(response, email, pwd);
	}

	@GetMapping("/getAllUsers")
	public Iterable<User> getAllUsers() {
		return this.userService.getAllUsers();
	}

	@DeleteMapping("/delete")
	public void delete(HttpServletRequest request, @RequestParam String email, @RequestParam String pwd) {
		User user = this.userService.find(email, pwd);

		String token = request.getHeader("token");
		if (!token.equals(user.getToken()))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token " + token + " inválido");

		this.userService.delete(email);
	}

	@DeleteMapping("/clearAll")
	public void clearAll(HttpServletRequest request) {
		String sToken = request.getHeader("prime");
		Integer token = Integer.parseInt(sToken);
		if (!isPrime(token.intValue()))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Debes pasar un número primo en la cabecera");
		if (sToken.length() != 3)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El nº primo debe tener tres cifras");
		this.userService.clearAll();
	}

	private boolean isPrime(int n) {
		if (n <= 1)
			return false;
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}
}
