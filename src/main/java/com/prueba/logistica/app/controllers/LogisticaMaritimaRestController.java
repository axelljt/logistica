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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.logistica.app.entities.LogisticaMaritima;
import com.prueba.logistica.app.services.ILogisticaMaritimaService;

@RestController
@RequestMapping("/api")
public class LogisticaMaritimaRestController {

	@Autowired
	private ILogisticaMaritimaService logisticaMservice;
	
	@GetMapping("/logistica-maritima")
	public List<LogisticaMaritima>show() {
		return logisticaMservice.findAllLogisticaM();
	}
	
	/*Metodo para guardar logistica-maritima*/
	
	@PostMapping("/logistica-maritima")
	public ResponseEntity<?> saveLogisticaT(@Valid @RequestBody LogisticaMaritima logisticaM,BindingResult result){
		
		LogisticaMaritima logisticaMNew = null;
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
			logisticaMNew = logisticaMservice.saveLogisticaM(logisticaM);
			} 
		catch (DataAccessException e) {
			response.put("mensaje","Error al guardar el cliente en la base de datos");
			response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","Logistica maritima guardada con éxito!");
		response.put("logisticaM", logisticaMNew);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
/*Metodo para actualizar logistica-maritima*/
	
	@PutMapping("/logistica-maritima/{id}")
	public ResponseEntity<?> updateLogisticaM(@Valid @RequestBody LogisticaMaritima logisticaM,@PathVariable Long id,BindingResult result){
	
		LogisticaMaritima currentLogisticaMaritima = logisticaMservice.findLogisticaMById(id);
		LogisticaMaritima updatedLogisticaMaritima = null;
		Map<String,Object> response= new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El Campo '" + err.getField() +"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		if(currentLogisticaMaritima == null) {
			response.put("mensaje", "Error: no se pudo editar, la logistica con ID :"
					.concat(id.toString().concat(" No existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND); 
		}
		
		try 
		{
			currentLogisticaMaritima.setCantida(logisticaM.getCantida());
			currentLogisticaMaritima.setFechaRegistro(logisticaM.getFechaRegistro());
			currentLogisticaMaritima.setFechaEntrega(logisticaM.getFechaEntrega());
			currentLogisticaMaritima.setPrecioNormal(logisticaM.getPrecioNormal());
			currentLogisticaMaritima.setDescuento(logisticaM.getDescuento());
			currentLogisticaMaritima.setPrecioEnvio(logisticaM.getPrecioEnvio());
			currentLogisticaMaritima.setNumeroFlota(logisticaM.getNumeroFlota());
			currentLogisticaMaritima.setNumeroGuia(logisticaM.getNumeroGuia());
			currentLogisticaMaritima.setCliente(logisticaM.getCliente());
			currentLogisticaMaritima.setProducto(logisticaM.getProducto());
			currentLogisticaMaritima.setPuerto(logisticaM.getPuerto());
			updatedLogisticaMaritima = logisticaMservice.saveLogisticaM(currentLogisticaMaritima);
		} 
		catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la logistica maritima de la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	  	
		response.put("mensaje", "Logistica maritima actualizada con éxito!");
		response.put("logisticaM",updatedLogisticaMaritima );
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
/*Metodo para eliminar cliente*/
	
	@DeleteMapping("/logistica-terrestre/{id}")
	public ResponseEntity<?> deleteLogisticaT(@PathVariable Long id){
		
		Map<String,Object> response = new HashMap<>();
		
		LogisticaMaritima currentLogisticaMaritima = logisticaMservice.findLogisticaMById(id);
		
		if(currentLogisticaMaritima == null) {
			response.put("mensaje", "Error: no se pudo eliminar, logistica con ID:"
					.concat(id.toString().concat(" No existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND); 
		}
		
		try {
			logisticaMservice.deleteLogisticaM(currentLogisticaMaritima);
			} 
		catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la logistica maritima de la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Logistica maritima eliminada con éxito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
}
