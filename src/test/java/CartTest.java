package isi.shoppingCart.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    @Test
    public void testGetItems() {
        Cart cart = new Cart();
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testAddProduct() {
        Cart cart = new Cart();
        Product p1 = new Product(1, "Leche", 2000.0, 10);
        Product p2 = new Product(2, "Pan", 1500.0, 5);

        cart.addProduct(p1);
        cart.addProduct(p1);
        cart.addProduct(p2);

        assertEquals(2, cart.getItems().size());
        assertEquals(2, cart.getQuantityByProductId(1));
    }

    @Test
    public void testGetQuantityByProductId() {
        Cart cart = new Cart();
        Product product = new Product(1, "Leche", 2000.0, 10);

        cart.addProduct(product);

        assertEquals(1, cart.getQuantityByProductId(1));
        assertEquals(0, cart.getQuantityByProductId(99));
    }

    @Test
    public void testGetTotal() {
        Cart cart = new Cart();
        Product p1 = new Product(1, "Leche", 2000.0, 10);
        Product p2 = new Product(2, "Pan", 1500.0, 5);

        cart.addProduct(p1);
        cart.addProduct(p2);

        assertEquals(3500.0, cart.getTotal());
    }

    @Test
    public void testClearCart() {
        Cart cart = new Cart();
        Product product = new Product(1, "Leche", 2000.0, 10);

        cart.addProduct(product);
        cart.clearCart();

        assertTrue(cart.getItems().isEmpty());
        assertEquals(0.0, cart.getTotal());
    }

    @Test
    public void testIsReadyForPayment() {
        Cart cart = new Cart();
        Product product = new Product(1, "Leche", 2000.0, 10);

        assertFalse(cart.isReadyForPayment());

        cart.addProduct(product);

        assertTrue(cart.isReadyForPayment());
    }
}
