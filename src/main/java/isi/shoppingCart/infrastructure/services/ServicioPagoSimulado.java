package isi.shoppingCart.infrastructure.services;

import isi.shoppingCart.usecases.dto.OperationResult;
import isi.shoppingCart.usecases.ports.IServicioPago;

public class ServicioPagoSimulado implements IServicioPago {
    private double creditoDisponible;

    public ServicioPagoSimulado(double creditoDisponible) {
        this.creditoDisponible = creditoDisponible;
    }

    public OperationResult procesarPago(double monto) {
        if (monto <= 0) {
            return OperationResult.fail("PAGO RECHAZADO: el monto debe ser mayor a cero.");
        }

        if (monto > creditoDisponible) {
            return OperationResult.fail("PAGO RECHAZADO: el monto total supera su credito.");
        }

        return OperationResult.ok("PAGO APROBADO: pago procesado correctamente.");
    }
}
