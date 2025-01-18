package edu.uclm.esi.flistacompra.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import edu.uclm.esi.flistacompra.model.Producto;

public interface ProductoDao extends CrudRepository<Producto, String> {
	
	@Query("SELECT COUNT(p) FROM Producto p WHERE p.lista.id = :idLista")
	int contarProductosDeLista(@Param("idLista") String idLista);

}
