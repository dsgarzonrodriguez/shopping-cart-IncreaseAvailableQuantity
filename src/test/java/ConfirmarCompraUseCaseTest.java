import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.entities.Customer;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.infrastructure.repositories.InMemoryCartRepository;
import isi.shoppingCart.infrastructure.repositories.InMemoryCustomerRepository;
import isi.shoppingCart.infrastructure.repositories.InMemoryProductRepository;
import isi.shoppingCart.infrastructure.repositories.InMemoryPurchaseRepository;
import isi.shoppingCart.infrastructure.services.ServicioPagoSimulado;
import isi.shoppingCart.usecases.dto.OperationResult;
import isi.shoppingCart.usecases.services.ConfirmarCompraUseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfirmarCompraUseCaseTest {

    @Test
    public void pagoAprobadoRegistraCompraDescuentaInventarioYLimpiaCarrito() {
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        InMemoryCartRepository cartRepository = new InMemoryCartRepository();
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryPurchaseRepository purchaseRepository = new InMemoryPurchaseRepository();

        Product product = new Product(1, "Mouse", 80.0, 2);
        productRepository.save(product);
        customerRepository.save(new Customer(1, "Cliente"));

        Cart cart = new Cart();
        cart.addProduct(product);
        cartRepository.save(cart);

        ConfirmarCompraUseCase useCase = new ConfirmarCompraUseCase(
                cartRepository,
                customerRepository,
                purchaseRepository,
                productRepository,
                new ServicioPagoSimulado(100.0));

        OperationResult result = useCase.execute();

        assertTrue(result.isSuccess());
        assertEquals(1, purchaseRepository.findAll().size());
        assertEquals(1, productRepository.findById(1).getAvailableQuantity());
        assertTrue(cartRepository.getCart().getItems().isEmpty());
    }

    @Test
    public void pagoRechazadoNoRegistraCompraNoDescuentaInventarioYConservaCarrito() {
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        InMemoryCartRepository cartRepository = new InMemoryCartRepository();
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryPurchaseRepository purchaseRepository = new InMemoryPurchaseRepository();

        Product product = new Product(1, "Teclado", 150.0, 3);
        productRepository.save(product);
        customerRepository.save(new Customer(1, "Cliente"));

        Cart cart = new Cart();
        cart.addProduct(product);
        cartRepository.save(cart);

        ConfirmarCompraUseCase useCase = new ConfirmarCompraUseCase(
                cartRepository,
                customerRepository,
                purchaseRepository,
                productRepository,
                new ServicioPagoSimulado(100.0));

        OperationResult result = useCase.execute();

        assertFalse(result.isSuccess());
        assertEquals("PAGO RECHAZADO: el monto total supera su credito.", result.getMessage());
        assertTrue(purchaseRepository.findAll().isEmpty());
        assertEquals(3, productRepository.findById(1).getAvailableQuantity());
        assertFalse(cartRepository.getCart().getItems().isEmpty());
    }
}
