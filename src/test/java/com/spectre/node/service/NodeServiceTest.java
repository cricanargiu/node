package com.spectre.node.service;

import com.spectre.node.exception.NodeFormatException;
import com.spectre.node.exception.OperationNotPermitted;
import com.spectre.node.model.Node;
import com.spectre.node.repository.NodeRepository;


import com.spectre.node.validator.NodeValidator;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NodeServiceTest {

	public static final int UNKNOWN_PARENT_ID = 4;
	private static final int KNOWN_PARENT_ID = 1;
	Mockery context = new Mockery();
	private NodeRepository nodeRepository = context.mock(NodeRepository.class);
	private NodeValidator simpleNodeValidator = context.mock(NodeValidator.class);
	private NodeService nodeService = new NodeService(nodeRepository, simpleNodeValidator);

	@Test
	void should_throw_an_exception_when_name_is_empty() {
		Node node =new Node("", "a valid description");
		context.checking( new Expectations(){{
			oneOf(simpleNodeValidator).validate(node);
			will(throwException(new NodeFormatException("any string")));
		}});
	}

	@Test
	void should_throw_an_exception_when_description_is_empty() {
		Node node =new Node("a valid name", "");
		context.checking( new Expectations(){{
			oneOf(simpleNodeValidator).validate(node);
			will(throwException(new NodeFormatException("any string")));
		}});
	}

	@Test
	void should_throw_an_exception_when_parent_does_not_exists() {
		Node node =new Node("a valid name", "a valid description", UNKNOWN_PARENT_ID);

		context.checking( new Expectations(){{
			oneOf(simpleNodeValidator).validate(node);
			oneOf(nodeRepository).findById(UNKNOWN_PARENT_ID);
			will(returnValue(Optional.empty()));
		}});

		Exception exception = assertThrows(NodeFormatException.class, () -> {
			nodeService.create(node);
		});
	}

	@Test
	void should_throw_an_exception_when_update_name() {
		Node node =new Node("a valid name", "a valid description", KNOWN_PARENT_ID);
		Node updatedNode =new Node( "a still valid name", "a valid description", KNOWN_PARENT_ID);

		context.checking( new Expectations(){{
			oneOf(simpleNodeValidator).validate(updatedNode);
			oneOf(nodeRepository).findById(updatedNode.getId());
				will(returnValue(Optional.of(node)));
		  oneOf(simpleNodeValidator).validateUpdate(updatedNode,node );
		     will(throwException(new OperationNotPermitted("any string")));
		}});

		Exception exception = assertThrows(OperationNotPermitted.class, () -> {
			nodeService.update(updatedNode);
		});

	}
	@Test
	void should_throw_an_exception_when_parent_of_updated_does_not_exists() {
		Node node =new Node(1,"a valid name", "a valid description", KNOWN_PARENT_ID);
		Node updatedNode =new Node( 1,"a valid name", "a valid description updated", UNKNOWN_PARENT_ID);

		context.checking( new Expectations(){{
			oneOf(simpleNodeValidator).validate(updatedNode);
			oneOf(nodeRepository).findById(1);
				will(returnValue(Optional.of(node)));
			oneOf(simpleNodeValidator).validateUpdate(updatedNode,node );
			oneOf(nodeRepository).findById(UNKNOWN_PARENT_ID);
				will(returnValue(Optional.empty()));
		}});

		Exception exception = assertThrows(NodeFormatException.class, () -> {
			nodeService.update(updatedNode);
		});
	}
	@Test
	void retrive_ancestors() {
		Node father = new Node("father", "a father with a son", null);
		Node son = new Node("son", "the son", father.getId());

		context.checking( new Expectations(){{
			oneOf(nodeRepository).findById(son.getParentId());
			will(returnValue(Optional.of(father)));
		}});
		nodeService.getAncestors(son.getId());
	}
}