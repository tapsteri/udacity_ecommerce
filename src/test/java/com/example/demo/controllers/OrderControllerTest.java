package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepo = mock(OrderRepository.class);

    private UserRepository userRepo = mock(UserRepository.class);

    private UserOrder order;

    private Item item;

    private User user;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
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
        user.getCart().addItem(item);

    }

    @Test
    public void submit_user_order_happy_path() {
        ResponseEntity<UserOrder> response = orderController.submit("Rob");
        assertNotNull(response);
        assertEquals(item, response.getBody().getItems().get(0));
    }

    @Test
    public void submit_user_order_user_not_found() {
        ResponseEntity<UserOrder> response = orderController.submit("John");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_user_orders_happy_path() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Rob");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get_user_orders_user_not_found() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("John");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
