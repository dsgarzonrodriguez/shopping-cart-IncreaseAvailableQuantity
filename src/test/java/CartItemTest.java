package isi.shoppingCart.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartItemTest {

    @Test
    public void testGetProduct() {
        Product product = new Product(1, "Leche", 2000.0, 10);
        CartItem item = new CartItem(product, 1);

        assertEquals(product, item.getProduct());
    }

    @Test
    public void testGetQuantity() {
        Product product = new Product(1, "Leche", 2000.0, 10);
        CartItem item = new CartItem(product, 3);

        assertEquals(3, item.getQuantity());
    }

    @Test
    public void testIncreaseQuantity() {
        Product product = new Product(1, "Leche", 2000.0, 10);
        CartItem item = new CartItem(product, 1);

        item.increaseQuantity();

        assertEquals(2, item.getQuantity());
    }

    @Test
    public void testGetSubtotal() {
        Product product = new Product(1, "Leche", 2000.0, 10);
        CartItem item = new CartItem(product, 3);

        assertEquals(6000.0, item.getSubtotal());
    }

    @Test
    public void testIsValidForPayment() {
        Product product = new Product(1, "Leche", 2000.0, 10);

        assertTrue(new CartItem(product, 1).isValidForPayment());
        assertFalse(new CartItem(product, 0).isValidForPayment());
        assertFalse(new CartItem(null, 1).isValidForPayment());
    }
}
