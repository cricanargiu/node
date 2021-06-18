package com.spectre.node.infrastructure.controller;


import com.spectre.node.model.FamilyTree;
import com.spectre.node.model.Node;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NodeControllerTestIT {
	@Autowired
	private TestRestTemplate restTemplate;

  @Test
	public void create_and_get_a_node() {
		HttpEntity<Node> request = new HttpEntity<>(new Node("pretty_node","a pretty test node"), new HttpHeaders());

		final Node createdNode = restTemplate.exchange("/nodes/", HttpMethod.POST, request, Node.class).getBody();
		final Node node = restTemplate.getForObject("/nodes/"+createdNode.getId(), Node.class);

		assertEquals(createdNode.getId(),node.getId());
	}

	@Test
	public void create_a_node_with_empty_parent() {
		HttpEntity<Node> request = new HttpEntity<>(new Node("pretty_node","a pretty test node"), new HttpHeaders());

		final Node created = restTemplate.exchange("/nodes/", HttpMethod.POST, request, Node.class).getBody();
		final Node node = restTemplate.getForObject("/nodes/"+created.getId(), Node.class);

		assertEquals(Node.EMPTY,node.getParentId());
	}

	@Test
	void update_a_node() {
		HttpEntity<Node> request = new HttpEntity<>(new Node("pretty_node","a pretty test node"), new HttpHeaders());
		final Node created = restTemplate.exchange("/nodes/", HttpMethod.POST, request, Node.class).getBody();
		final Node node = restTemplate.getForObject("/nodes/"+created.getId(), Node.class);
    node.setDescription("a very pretty node");
		HttpEntity<Node> updateRequest = new HttpEntity<>(node, new HttpHeaders());

		final Node updatedNode = restTemplate.exchange("/nodes/", HttpMethod.PUT, updateRequest, Node.class).getBody();

		assertEquals("a very pretty node",updatedNode.getDescription());
	}

	@Test
	void get_ancestors() {

		 HttpEntity<Node> request = new HttpEntity<>(new Node("father","a pretty test node"), new HttpHeaders());
		 final Node toCreate = restTemplate.exchange("/nodes/", HttpMethod.POST, request, Node.class).getBody();
		 Node father = restTemplate.getForObject("/nodes/"+toCreate.getId(), Node.class);
		 Node son = new Node("son", "another test node", father.getId());
		 request = new HttpEntity<>(son, new HttpHeaders());
		 son = restTemplate.exchange("/nodes/", HttpMethod.POST, request, Node.class).getBody();

		 final FamilyTree ancestors = restTemplate.exchange("/nodes/ancestors/" + son.getId(), HttpMethod.GET, HttpEntity.EMPTY, FamilyTree.class).getBody();

		 assertEquals(2, ancestors.getAncestors().size());
	   assertEquals(father.getId(),ancestors.getAncestors().get(0).getId());
	}

}