package com.spectre.node.controller;

import com.spectre.node.exception.NodeFormatException;
import com.spectre.node.exception.NodeNotPresent;
import com.spectre.node.exception.OperationNotPermitted;
import com.spectre.node.model.FamilyTree;
import com.spectre.node.model.Node;
import com.spectre.node.service.NodeService;
import com.spectre.node.validator.SimpleNodeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/nodes")
@Slf4j
public class NodeController {

	@Autowired
	private NodeService nodeService;

	@Autowired
	private SimpleNodeValidator simpleNodeValidator;

	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable("id") final int id) {
		log.info("Getting node {}...",id);
		Optional<Node> node = null;
	try{
		     node= nodeService.findNode(id);
	} catch (NodeNotPresent e) {
			log.warn("... node with id {} not found", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
		log.info("Retrieved node {}...",node.get());
		return ResponseEntity.ok().body(node);
	}

	@PostMapping
	public ResponseEntity<?> createNode(@RequestBody final Node node) {
		log.info("Creating node {}...",node);
		Node newNode = null;
		try {
			simpleNodeValidator.validate(node);
			newNode = nodeService.create(node);
		} catch (NodeFormatException e) {
				log.warn(" ... Can't create node {}, because {}",node, e.getMessage());
				return ResponseEntity.badRequest().body(e.getMessage());
		}
		log.info("... Created node {}",node);
		return ResponseEntity.ok().body(newNode);
	}

	@PutMapping
	public ResponseEntity<?> updateNode(@RequestBody final Node node) {
		log.info("Updating node {}...",node);
		Node updated = null;
		try {
			simpleNodeValidator.validate(node);
			updated = nodeService.update(node);
		} catch (NodeFormatException e) {
				log.warn(" ... Can't update node {}, because {}",node, e.getMessage());
				return ResponseEntity.badRequest().body(e.getMessage());
		} catch (OperationNotPermitted e) {
				log.warn(" ... Can't update node {}, because {}",node, e.getMessage());
				return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
		} catch (NodeNotPresent e) {
				log.warn(" ... Can't update because node {} not exists",node);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		log.info("... Updated node {}",node);
		return ResponseEntity.ok().body(updated);
	}

	@GetMapping("ancestors/{id}")
	public ResponseEntity<?> getAncestors(@PathVariable final int id) {
		log.info("Retrieving  node {}'ancestors ...",id);
		FamilyTree ancestors = null;
		try {
			ancestors = nodeService.getAncestors(id);
		} catch (NodeNotPresent e) {
				log.warn(" ... Can't retrieve ancestors because node {} not exists",id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		log.info("... Retrieved node {}'ancestors",id);
		return ResponseEntity.ok().body(ancestors);
	}

}
