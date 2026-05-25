package isi.shoppingCart.usecases.ports;

import isi.shoppingCart.usecases.dto.OperationResult;

public interface IServicioPago {
    OperationResult procesarPago(double monto);
}
