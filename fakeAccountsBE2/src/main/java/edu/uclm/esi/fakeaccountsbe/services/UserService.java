package edu.uclm.esi.fakeaccountsbe.services;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.fakeaccountsbe.dao.UserDao;
import edu.uclm.esi.fakeaccountsbe.model.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public void registrar(String ip, User user) {
		if (this.userDao.findById(user.getEmail()).isPresent())
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ya existe un usuario con ese correo electrónico");

		user.setIp(ip);
		user.setCreationTime(System.currentTimeMillis());
		this.userDao.save(user);
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
		return this.userDao.findByToken(token);
	}

	public synchronized void clearOld() {
		
	}
}
