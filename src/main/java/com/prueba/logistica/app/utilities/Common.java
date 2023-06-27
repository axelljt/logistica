package com.prueba.logistica.app.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Common {
	
	/*Metodo para calcular el descuento al monto de envio si la cantidad > 10*/
	
	public static BigDecimal calcularDescuentoLogisticaT(BigDecimal precioEnvio,double montoDescuento) {
		BigDecimal descuento = new BigDecimal(montoDescuento);
		return (precioEnvio.multiply(descuento)).setScale(2,RoundingMode.HALF_UP) ;
		
	}
	
	/*Metodo para aplicar el descuento al monto enviado*/
	
	public static BigDecimal aplicarDescuento(BigDecimal precioEnvio,double montoDescuento) {
		return precioEnvio.subtract(calcularDescuentoLogisticaT(precioEnvio,montoDescuento)).setScale(2,RoundingMode.HALF_UP);
	}

}
