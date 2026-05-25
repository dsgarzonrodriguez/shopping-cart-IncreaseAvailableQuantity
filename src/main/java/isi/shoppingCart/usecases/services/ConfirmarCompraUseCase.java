package isi.shoppingCart.usecases.services;

import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Customer;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.entities.PurchaseItem;
import isi.shoppingCart.usecases.dto.OperationResult;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.CustomerRepository;
import isi.shoppingCart.usecases.ports.IServicioPago;
import isi.shoppingCart.usecases.ports.ProductRepository;
import isi.shoppingCart.usecases.ports.PurchaseRepository;

import java.util.List;

public class ConfirmarCompraUseCase {
    private CartRepository cartRepository;
    private CustomerRepository customerRepository;
    private PurchaseRepository purchaseRepository;
    private ProductRepository productRepository;
    private IServicioPago servicioPago;

    public ConfirmarCompraUseCase(CartRepository cartRepository,
                                  CustomerRepository customerRepository,
                                  PurchaseRepository purchaseRepository,
                                  ProductRepository productRepository,
                                  IServicioPago servicioPago) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.servicioPago = servicioPago;
    }

    public OperationResult execute() {
        Cart cart = cartRepository.getCart();

        if (cart == null || !cart.isReadyForPayment()) {
            return OperationResult.fail("ERROR: No se puede proceder con la compra. El carrito debe contener al menos un producto valido para iniciar el pago.");
        }

        Customer customer = customerRepository.getCustomer();

        if (customer == null) {
            return OperationResult.fail("No hay cliente registrado.");
        }

        List<CartItem> items = cart.getItems();
        int i;

        for (i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Product product = item.getProduct();

            if (item.getQuantity() > product.getAvailableQuantity()) {
                return OperationResult.fail("No hay disponibilidad suficiente para: " + product.getName() + ".");
            }
        }

        double total = cart.getTotal();
        OperationResult resultadoPago = servicioPago.procesarPago(total);

        if (!resultadoPago.isSuccess()) {
            return resultadoPago;
        }

        Purchase purchase = new Purchase(purchaseRepository.getNextId(), customer);

        for (i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Product product = item.getProduct();

            product.decreaseAvailableQuantity(item.getQuantity());
            purchase.addItem(new PurchaseItem(product, item.getQuantity(), product.getPrice()));
            productRepository.save(product);
        }

        purchaseRepository.save(purchase);
        cart.clearCart();
        cartRepository.save(cart);

        return OperationResult.ok("Compra " + purchase.getId() + " confirmada para " + customer.getName() + ". Total: $ " + purchase.getTotal());
    }
}
