package com.prueba.logistica.app.controllers;

import java.math.BigDecimal;
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

import com.prueba.logistica.app.entities.LogisticaTerrestre;
import com.prueba.logistica.app.services.ILogisticaTerrestreService;
import com.prueba.logistica.app.utilities.Common;

@RestController
@RequestMapping("/api")
public class LogisticaTerrestreRestController {

	@Autowired
	private ILogisticaTerrestreService logisticaTservice;
	
	public final double DESCUENTO_LOGISTICA_T = 0.05;
	
	@GetMapping("/logistica-terrestre")
	public List<LogisticaTerrestre>show() {
		return logisticaTservice.findAllLogisticaT();
	}
	
	/*Metodo para obtener una Logistica Terrestre*/
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/logistica-terrestre/{id}")
	public ResponseEntity<?> getLogisticaTerrestre(@PathVariable Long id) {
		
		LogisticaTerrestre logisticaTerrestre = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			logisticaTerrestre = logisticaTservice.findLogisticaTById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(logisticaTerrestre == null) {
			response.put("mensaje", "Logistica terrestre con  ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<LogisticaTerrestre>(logisticaTerrestre, HttpStatus.OK);
	}
	
	/*Metodo para guardar logistica-terrestre*/
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/logistica-terrestre")
	public ResponseEntity<?> saveLogisticaT(@Valid @RequestBody LogisticaTerrestre logisticaT,BindingResult result){
		
		LogisticaTerrestre logisticaTNew = null;
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
			if(logisticaT.getCantidad() > 10) {
				BigDecimal precioNormal = logisticaT.getPrecioEnvio();
				logisticaT.setPrecioNormal(precioNormal);
				logisticaT.setPrecioEnvio(Common.aplicarDescuento(precioNormal,DESCUENTO_LOGISTICA_T));
				logisticaT.setDescuento(Common.calcularDescuentoLogisticaT(precioNormal,DESCUENTO_LOGISTICA_T));
			}else {
				logisticaT.setPrecioNormal(logisticaT.getPrecioEnvio());
				logisticaT.setPrecioEnvio(logisticaT.getPrecioEnvio());
				logisticaT.setDescuento(new BigDecimal(0.00));
			}
			logisticaTNew = logisticaTservice.saveLogisticaT(logisticaT);
			} 
		catch (DataAccessException e) {
			response.put("mensaje","Error al guardar Logistica terrestre en la base de datos");
			response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","Logistica terrestre guardada con éxito!");
		response.put("logisticaT", logisticaTNew);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
/*Metodo para actualizar cliente*/
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/logistica-terrestre/{id}")
	public ResponseEntity<?> updateLogisticaT(@Valid @RequestBody LogisticaTerrestre logisticaT,@PathVariable Long id,BindingResult result){
	
		LogisticaTerrestre currentLogisticaTerrestre = logisticaTservice.findLogisticaTById(id);
		LogisticaTerrestre updatedLogisticaTerrestre = null;
		Map<String,Object> response= new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El Campo '" + err.getField() +"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		if(currentLogisticaTerrestre == null) {
			response.put("mensaje", "Error: no se pudo editar, la logistica con ID :"
					.concat(id.toString().concat(" No existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND); 
		}
		
		try 
		{
			currentLogisticaTerrestre.setCantidad(logisticaT.getCantidad());
			if(logisticaT.getCantidad() > 10) {
				BigDecimal precioNormal = logisticaT.getPrecioEnvio();
				currentLogisticaTerrestre.setPrecioNormal(precioNormal);
				currentLogisticaTerrestre.setPrecioEnvio(Common.aplicarDescuento(precioNormal,DESCUENTO_LOGISTICA_T));
				currentLogisticaTerrestre.setDescuento(Common.calcularDescuentoLogisticaT(precioNormal,DESCUENTO_LOGISTICA_T));
			}else {
				currentLogisticaTerrestre.setPrecioNormal(logisticaT.getPrecioEnvio());
				currentLogisticaTerrestre.setPrecioEnvio(logisticaT.getPrecioEnvio());
				currentLogisticaTerrestre.setDescuento(new BigDecimal(0.00));
			}
			currentLogisticaTerrestre.setFechaRegistro(logisticaT.getFechaRegistro());
			currentLogisticaTerrestre.setFechaEntrega(logisticaT.getFechaEntrega());
			currentLogisticaTerrestre.setPlacaVehiculo(logisticaT.getPlacaVehiculo());
			currentLogisticaTerrestre.setNumeroGuia(logisticaT.getNumeroGuia());
			currentLogisticaTerrestre.setCliente(logisticaT.getCliente());
			currentLogisticaTerrestre.setProducto(logisticaT.getProducto());
			currentLogisticaTerrestre.setBodega(logisticaT.getBodega());
			updatedLogisticaTerrestre = logisticaTservice.saveLogisticaT(currentLogisticaTerrestre);
		} 
		catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el cliente de la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	  	
		response.put("mensaje", "Logistica terrestre actualizada con éxito!");
		response.put("logisticaT",updatedLogisticaTerrestre );
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
/*Metodo para eliminar cliente*/
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/logistica-terrestre/{id}")
	public ResponseEntity<?> deleteLogisticaT(@PathVariable Long id){
		
		Map<String,Object> response = new HashMap<>();
		
		LogisticaTerrestre currentLogisticaTerrestre = logisticaTservice.findLogisticaTById(id);
		
		if(currentLogisticaTerrestre == null) {
			response.put("mensaje", "Error: no se pudo eliminar, logistica con ID:"
					.concat(id.toString().concat(" No existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND); 
		}
		
		try {
			logisticaTservice.deleteLogisticaT(currentLogisticaTerrestre);
			} 
		catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la logistica terrestre de la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Logistica terrestre eliminada con éxito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
	
	
	
}
