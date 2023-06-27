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

import com.prueba.logistica.app.entities.Producto;
import com.prueba.logistica.app.services.IProductoService;

@RestController
@RequestMapping("/api")
public class ProductoRestController {

	@Autowired
	private IProductoService productoService;
	
	@GetMapping("/productos")
	public List<Producto>show() {
		return productoService.findAllProductos();
	}

	/*Metodo para obtener un Producto*/
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/productos/{id}")
	public ResponseEntity<?> getProducto(@PathVariable Long id) {
		
		Producto Producto = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			Producto = productoService.findProductoById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(Producto == null) {
			response.put("mensaje", "El Producto con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Producto>(Producto, HttpStatus.OK);
	}
	
	/*Metodo para guardar Producto*/
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/productos")
	public ResponseEntity<?> saveProducto(@Valid @RequestBody Producto Producto,BindingResult result){
		
		Producto ProductoNew = null;
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
			  ProductoNew = productoService.saveProducto(Producto);
			} 
		catch (DataAccessException e) {
			response.put("mensaje","Error al guardar el Producto en la base de datos");
			response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El Producto ha sido guardado con éxito!");
		response.put("Producto", ProductoNew);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}

	/*Metodo para actualizar Producto*/
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/Productos/{id}")
	public ResponseEntity<?> updateProducto(@Valid @RequestBody Producto Producto,@PathVariable Long id,BindingResult result){
	
		Producto currentProducto = productoService.findProductoById(id);
		Producto updatedProducto = null;
		Map<String,Object> response= new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El Campo '" + err.getField() +"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		if(currentProducto == null) {
			response.put("mensaje", "Error: no se pudo editar, el Producto con ID:"
					.concat(id.toString().concat(" No existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND); 
		}
		
		try 
		{
			currentProducto.setNombre_producto(Producto.getNombre_producto());
			currentProducto.setDescripcion(Producto.getDescripcion());
			updatedProducto = productoService.saveProducto(currentProducto);
		} 
		catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el Producto de la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	  	
		response.put("mensaje", "El Producto ha sido actualizado con éxito!");
		response.put("Producto",updatedProducto );
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}

	/*Metodo para eliminar Producto*/
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/Productos/{id}")
	public ResponseEntity<?> deleteProducto(@PathVariable Long id){
		
		Map<String,Object> response = new HashMap<>();
		
		Producto currentProducto = productoService.findProductoById(id);
		
		if(currentProducto == null) {
			response.put("mensaje", "Error: no se pudo eliminar, el Producto con ID:"
					.concat(id.toString().concat(" No existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND); 
		}
		
		try {
				productoService.deleteProducto(currentProducto);
			} 
		catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el Producto de la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Producto eliminado con éxito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
	
}
