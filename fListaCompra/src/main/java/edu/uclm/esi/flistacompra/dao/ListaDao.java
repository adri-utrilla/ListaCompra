package edu.uclm.esi.flistacompra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import edu.uclm.esi.flistacompra.model.Lista;
import jakarta.transaction.Transactional;

public interface ListaDao extends CrudRepository<Lista, String> {

	@Query(value = "select lista_id from lista_emails_usuarios where emails_usuarios =:email", nativeQuery = true)
	List<String> getListasDe(@Param("email") String email);
	
	@Modifying @Transactional
	@Query(value = "update lista_emails_usuarios set confirmado = true where lista_id =:idLista and emails_usuarios =:email", nativeQuery = true)
	void aceptarInvitacion(@Param("idLista") String idLista, @Param("email") String email);
	
	@Query(value = "select count(id) from lista where propietario = :email", nativeQuery = true)
	int contarListasDePropietario(@Param("email") String email);
	
	@Modifying @Transactional
	@Query(value = "DELETE FROM lista_emails_usuarios WHERE lista_id = :idLista AND emails_usuarios = :email", nativeQuery = true)
	int eliminarEmailDeLista(@Param("idLista") String idLista, @Param("email") String email);

	
	



}
