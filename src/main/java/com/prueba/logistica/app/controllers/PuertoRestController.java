package com.prueba.logistica.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.logistica.app.entities.Puerto;
import com.prueba.logistica.app.services.IPuertoService;

@RestController
@RequestMapping("/api")
public class PuertoRestController {

	@Autowired
	private IPuertoService puertoService;
	
	@GetMapping("/puertos")
	public List<Puerto>show() {
		return puertoService.findAllPuertos();
	}

	/*Metodo para obtener un Puerto*/
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/puertos/{id}")
	public ResponseEntity<?> getPuerto(@PathVariable Long id) {
		
		Puerto Puerto = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			Puerto = puertoService.findPuertoById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(Puerto == null) {
			response.put("mensaje", "El Puerto con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Puerto>(Puerto, HttpStatus.OK);
	}
	
	/*Metodo para guardar Puerto*/
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/Puertos")
	public ResponseEntity<?> savePuerto(@Valid @RequestBody Puerto puerto,BindingResult result){
		
		Puerto PuertoNew = null;
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El Campo '" + err.getField() +"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		try {
			  PuertoNew = puertoService.savePuerto(puerto);
			} 
		catch (DataAccessException e) {
			response.put("mensaje","Error al guardar el Puerto en la base de datos");
			response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El Puerto ha sido guardado con éxito!");
		response.put("Puerto", PuertoNew);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}

	/*Metodo para actualizar Puerto*/
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/Puertos/{id}")
	public ResponseEntity<?> updatePuerto(@Valid @RequestBody Puerto puerto,@PathVariable Long id,BindingResult result){
	
		Puerto currentPuerto = puertoService.findPuertoById(id);
		Puerto updatedPuerto = null;
		Map<String,Object> response= new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El Campo '" + err.getField() +"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		if(currentPuerto == null) {
			response.put("mensaje", "Error: no se pudo editar, el Puerto con ID:"
					.concat(id.toString().concat(" No existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND); 
		}
		
		try 
		{
			currentPuerto.setNombre_puerto(puerto.getNombre_puerto());
			currentPuerto.setUbicacion(puerto.getUbicacion());
			updatedPuerto = puertoService.savePuerto(currentPuerto);
		} 
		catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el Puerto de la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	  	
		response.put("mensaje", "El Puerto ha sido actualizado con éxito!");
		response.put("Puerto",updatedPuerto );
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}

	/*Metodo para eliminar Puerto*/
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/Puertos/{id}")
	public ResponseEntity<?> deletePuerto(@PathVariable Long id){
		
		Map<String,Object> response = new HashMap<>();
		
		Puerto currentPuerto = puertoService.findPuertoById(id);
		
		if(currentPuerto == null) {
			response.put("mensaje", "Error: no se pudo eliminar, el Puerto con ID:"
					.concat(id.toString().concat(" No existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND); 
		}
		
		try {
				puertoService.deletePuerto(currentPuerto);
			} 
		catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el Puerto de la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Puerto eliminado con éxito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
	
}
