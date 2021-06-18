package com.spectre.node.service;

import com.spectre.node.exception.NodeFormatException;
import com.spectre.node.exception.NodeNotPresent;
import com.spectre.node.model.FamilyTree;
import com.spectre.node.model.Node;
import com.spectre.node.repository.NodeRepository;
import com.spectre.node.validator.NodeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NodeService {

	NodeRepository nodeRepository;
	NodeValidator nodeValidator;
	@Autowired
	public NodeService(NodeRepository nodeRepository, NodeValidator nodeValidator) {
		this.nodeRepository = nodeRepository;
		this.nodeValidator = nodeValidator;
	}

	public Optional<Node> findNode(int id)
		{
			return nodeRepository.findById(id);
		}

	public Node create(Node node) {
		log.info("Creating node {} ...", node);
		nodeValidator.validate(node);
		checkParent(node);
		final Node newNode = nodeRepository.save(node);
		log.info("Created node {} ...", node);
		return newNode;
	}

	public Node update(Node node) {
		log.info("Updating node {} ...", node);
		nodeValidator.validate(node);
		final Optional<Node> originalNode = nodeRepository.findById(node.getId());
		if(originalNode.isEmpty()) {
			log.info("... Updating node {} failed because node do not exists", node);
			throw new NodeNotPresent();
		}
		nodeValidator.validateUpdate(node, originalNode.get());
		checkParent(node);
		final Node updatedNode = nodeRepository.save(node);
		log.info("... Node {} updated", node);
		return updatedNode;
	}

	public FamilyTree getAncestors(int id) {
		log.info("Retrieving node {} ancestors ...", id);
		List<Node> ancestors = new LinkedList<>();
		Optional<Node>  node = nodeRepository.findById(id);
		Node relative;
		if(node.isEmpty()) {
			log.warn("Can't retrieve node {} parents because node do not exists", id);
			throw new NodeNotPresent();
		}
		ancestors.add(0, node.get());
		relative = node.get();
		while (relative.getParentId()!= null)
		{
			final Optional<Node> retrived = nodeRepository.findById(relative.getParentId());
			ancestors.add(0,retrived.get());
      relative = retrived.get();
		}
		final FamilyTree familyTree = new FamilyTree(ancestors);
		log.info("... Retrieved node {} ancestors", id);
		return familyTree;
	}

	private void checkParent(Node node) {
		if( node.getParentId()!= null && Node.EMPTY != node.getParentId() ) {
			Optional<Node> parent = nodeRepository.findById(node.getParentId());
			if (parent.isEmpty()) {
				log.warn("... Unknown parent of node {}", node);
				throw new NodeFormatException("Unknown parent");
			}
		}
	}

}
