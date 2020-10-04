package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CartControllerTest {

    private CartController cartController;

    private CartRepository cartRepo = mock(CartRepository.class);

    private UserRepository userRepo = mock(UserRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    private Item item;

    private User user;

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
        user = new User();
        user.setId(0L);
        user.setUsername("Rob");
        user.setPassword("testPassword");
        user.setCart(new Cart());
        when(userRepo.findByUsername("Rob")).thenReturn(user);
        item = new Item();
        item.setId(1L);
        item.setName("Hexagonal Widget");
        item.setDescription("A widget that is hexagonal");
        item.setPrice(new BigDecimal("3.99"));
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));
    }

    @Test
    public void add_to_cart_happy_path() {
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("Rob");
        r.setItemId(1);
        r.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_user_not_found() {
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("John");
        r.setItemId(1);
        r.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_item_not_found() {
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("Rob");
        r.setItemId(2);
        r.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_happy_path() {
        user.getCart().addItem(item);
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("Rob");
        r.setItemId(1);
        r.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_user_not_found() {
        user.getCart().addItem(item);
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("John");
        r.setItemId(1);
        r.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(r);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_item_not_found() {
        user.getCart().addItem(item);
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("Rob");
        r.setItemId(2);
        r.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(r);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
