package com.prueba.logistica.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.logistica.app.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	public List<Cliente> findByNombreContainingIgnoreCase(String term); 

}
