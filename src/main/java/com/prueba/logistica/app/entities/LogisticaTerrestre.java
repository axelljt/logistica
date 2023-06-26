package com.prueba.logistica.app.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="logistica_terrestre")
@Data
@NoArgsConstructor
public class LogisticaTerrestre implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_logistica_t")
	private Long idLogisticaT;
	
	private Integer cantida;
	
	@NotEmpty(message ="no puede estar vacio")
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_registro")
	private Date fechaRegistro;
	
	@NotEmpty(message ="no puede estar vacio")
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_entrega")
	private Date fechaEntrega;
	
	@Column(name="precio_envio")
	private BigDecimal precioEnvio;
	
	@Column(name="precio_normal")
	private BigDecimal precioNormal;
	
	@Column(name="valor_descuento")
	private BigDecimal descuento;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "LLL###")
	@Column(name="placa_vehiculo")
	private String placaVehiculo;
	
	@Size(max = 10, message = "Numero único alfanumérico de 10 dígitos")
	@Column(name="numero_guia",unique = true)
	private String numeroGuia;
	
	@NotNull()
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_cliente")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Cliente cliente;
	
	@NotNull()
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_producto")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Producto producto;
	
	@NotNull()
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_bodega")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Bodega bodega;
	
	private static final long serialVersionUID = 1L;

}
