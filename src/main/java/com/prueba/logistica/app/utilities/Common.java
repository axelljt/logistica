package com.prueba.logistica.app.utilities;

import java.math.BigDecimal;

public class Common {
	
	/*Metodo para calcular el descuento al monto de envio si la cantidad > 10*/
	
	public static BigDecimal calcularDescuentoLogisticaT(BigDecimal precioEnvio,double montoDescuento) {
		BigDecimal descuento = new BigDecimal(montoDescuento);
		return (precioEnvio.multiply(descuento)) ;
		
	}
	
	/*Metodo para aplicar el descuento al monto enviado*/
	
	public static BigDecimal aplicarDescuento(BigDecimal precioEnvio,double montoDescuento) {
		return precioEnvio.subtract(calcularDescuentoLogisticaT(precioEnvio,montoDescuento));
	}

}
