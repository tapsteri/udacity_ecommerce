package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		log.info("Item get all");
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Optional<Item> item = itemRepository.findById(id);
		if(!item.isPresent()) {
			log.info("Item finding by id failed, id {} does not exist", id);
			return ResponseEntity.notFound().build();
		}
		log.info("Item finding by id succeeded for id {}", id);
		return ResponseEntity.of(item);

		//return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		if(items == null || items.isEmpty()) {
			log.info("Items finding by name failed, item by name {} does not exist", name);
			return ResponseEntity.notFound().build();
		}
		log.info("Items finding by name succeeded for item {}", name);
		return ResponseEntity.ok(items);


		//return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
		//		: ResponseEntity.ok(items);
			
	}
	
}
