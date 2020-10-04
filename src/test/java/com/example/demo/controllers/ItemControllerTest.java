package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ItemControllerTest {

    private ItemController itemController;

    private Item item;

    private List<Item> items;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
        item = new Item();
        item.setId(1L);
        item.setName("Hexagonal Widget");
        item.setDescription("A widget that is hexagonal");
        item.setPrice(new BigDecimal("3.99"));
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));
        items = new LinkedList<>();
        items.add(item);
        when(itemRepo.findByName("Hexagonal Widget")).thenReturn(items);
        when(itemRepo.findAll()).thenReturn(items);
    }

    @Test
    public void get_all_items_happy_path() {
        Item i = Objects.requireNonNull(itemController.getItems().getBody()).get(0);
        assertNotNull(i);
        assertEquals(i, item);
    }

    @Test
    public void get_item_by_id_happy_path() {
        Item i = itemController.getItemById(1L).getBody();
        assertNotNull(i);
        assertEquals(i, item);
    }

    @Test
    public void get_item_by_id_not_found() {
        ResponseEntity response = itemController.getItemById(2L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name_happy_path() {
        Item i = itemController.getItemsByName("Hexagonal Widget").getBody().get(0);
        assertNotNull(i);
        assertEquals(i, item);
    }

    @Test
    public void get_items_by_name_not_found() {
        ResponseEntity response = itemController.getItemsByName("Octagonal Widget");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
