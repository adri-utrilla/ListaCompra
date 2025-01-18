package edu.uclm.esi.fakeaccountsbe.services;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.fakeaccountsbe.auxi.HttpClient;
import edu.uclm.esi.fakeaccountsbe.auxi.Manager;
import edu.uclm.esi.fakeaccountsbe.dao.UserDao;
import edu.uclm.esi.fakeaccountsbe.model.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	private Map<String, User> usersByToken = new ConcurrentHashMap<>();

	public void registrar(String ip, User user) throws IOException {
		if (this.userDao.findById(user.getEmail()).isPresent())
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ya existe un usuario con ese correo electrónico");

		user.setIp(ip);
		user.setCreationTime(System.currentTimeMillis());
		user.setPremium(false);
		user.setValidationToken(UUID.randomUUID().toString());
		this.userDao.save(user);

		this.sendEmail(user,"Bienvenida.html.txt","brevo.parameters.txt");
	}

	public void login(User tryingUser) {
		this.find(tryingUser.getEmail(), tryingUser.getPwd());
	}

	public void clearAll() {
		this.userDao.deleteAll();
	}

	public Iterable<User> getAllUsers() {
		return this.userDao.findAll();
	}

	public User find(String email, String pwd) {
		Optional<User> optUser = this.userDao.findById(email);
		if (!optUser.isPresent())
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales incorrectas");
		User user = optUser.get();
		if (!user.getPwd().equals(pwd))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales incorrectas");

		return user;
	}

	public void delete(String email) {
		this.userDao.deleteById(email);
	}

	public User findByToken(String token) {
		return this.usersByToken.get(token);
	}
	
	public User findByRecoveryToken(String RecoveryToken) {
		return this.userDao.findByRecoveryToken(RecoveryToken);
	}
	
	public synchronized void clearOld() {

	}
	
	public void confirm(String tokenId) {
		User optUser = this.userDao.findByValidationToken(tokenId);
		if (optUser==null)
		throw new ResponseStatusException(HttpStatus.NOT_FOUND,	"No se encuentra el token o ha caducado");

		long time = System.currentTimeMillis();
		if (time-optUser.getCreationTime()<=24*60*60*1000){
			optUser.setCreationTime(time);
        } else {
        	this.userDao.delete(optUser);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El token ha caducado");
        }
		optUser.setValidationDate(time);
		this.userDao.save(optUser);
		
	}
	
	public void correoCambioContrasena(User user) throws IOException {
	    this.userDao.save(user);
	    this.sendEmail(user,"cambioContraseña.html.txt","contraseñas.parameters.txt");
	}
	
	public void cambioContrasena(User user) throws IOException {
		user.setCaducidadRecoveryToken(null);
		user.setRecoveryToken(null);
	    this.userDao.save(user);
	}

	private void sendEmail(User user, String File1, String File2) throws IOException {
		String body = Manager.get().readFile(File1);
		if(user.getRecoveryToken()!=null) {
			body = body.replace("#TOKEN#", user.getRecoveryToken());
		}else if(user.getValidationToken()!=null) {
			body = body.replace("#TOKEN#", user.getValidationToken());
		}
		
		JSONObject emailParameters = new JSONObject(Manager.get().readFile(File2));

		String endPoint = emailParameters.getString("endpoint");

		JSONArray headers = emailParameters.getJSONArray("headers");

		JSONObject payload = new JSONObject();
		payload.put("sender", emailParameters.getJSONObject("sender"));

		JSONArray to = new JSONArray();
		to.put(new JSONObject().put("email", user.getEmail()));
		payload.put("to", to);

		payload.put("subject", emailParameters.getString("subject"));
		payload.put("htmlContent", body);

		HttpClient client = new HttpClient();
		client.sendPost(endPoint, headers, payload);
	}
	
	public boolean usuarioValido(String email) {
		User user = this.userDao.findById(email).get();
		return user != null && user.getValidationDate() != null;
	}
}
