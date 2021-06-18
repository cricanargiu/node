package com.spectre.node.validator;

import com.spectre.node.exception.NodeFormatException;
import com.spectre.node.exception.OperationNotPermitted;
import com.spectre.node.model.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleNodeValidatorTest {
	public static final String A_VERY_LONG_NAME = "oMa0DOuYaUHCHLzpsoHY7T0i5hgocwSoGAKlzBRGQNUJi2HUHaZ";
	public static final String A_VERY_LONG_DESCRIPTION = "Haqv4kf30FDiQH6xVkHQ2cEyKYXCOOec3Dcawnnb6b3o9lCWdWZk2tCT9F4SkUDPsnKY12MF9P3KN4SvmRKlgcX77gr1ZawlUdXQbqGHpIa6d7vJZTzo5tsuJc0cnuuJnaXMOg26WYr02aPHFp67SPaED7I30QqNHNtlG5tEdwMN6FYu2jpLimocVY7T1vkHyeuASyF11qcboUCWRkajdTuUx3RtF5lv9lKn0pUdW5Z6Dox1nnmNb8Vonoz";
	private SimpleNodeValidator validator = new SimpleNodeValidator();

	@Test
	void should_throw_an_exception_when_name_is_empty() {
		Node node =new Node("", "a valid description", 8);

		Exception exception = assertThrows(NodeFormatException.class, () -> {
			validator.validate(node);
		});
	}

	@Test
	void should_throw_an_exception_if_name_too_long() {
		Node node =new Node(A_VERY_LONG_NAME, "description", 8);

		Exception exception = assertThrows(NodeFormatException.class, () -> {
			validator.validate(node);
		});
	}

	@Test
	void should_throw_an_exception_when_descritpion_is_empty() {
		Node node =new Node("name", "", 8);

		Exception exception = assertThrows(NodeFormatException.class, () -> {
			validator.validate(node);
		});
	}

	@Test
	void should_throw_an_exception_when_descritpion_is_too_long() {
		Node node =new Node("name", A_VERY_LONG_DESCRIPTION, 8);

		Exception exception = assertThrows(NodeFormatException.class, () -> {
			validator.validate(node);
		});
	}

	@Test
	void should_throw_an_exception_when_name_of_node_change() {
		Node originalNode =new Node("name", "description", 8);
		Node node =new Node("another name", "description", 8);

		Exception exception = assertThrows(OperationNotPermitted.class, () -> {
			validator.validateUpdate(node, originalNode);
		});
	}

	@Test
	void should_throw_an_exception_when_node_parents_itself() {
		Node originalNode =new Node(1,"name", "description", 8);
		Node node =new Node("name", "description", 1);

		Exception exception = assertThrows(NodeFormatException.class, () -> {
			validator.validateUpdate(node, originalNode);
		});
	}

}