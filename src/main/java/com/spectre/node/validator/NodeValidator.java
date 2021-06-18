package com.spectre.node.validator;

import com.spectre.node.model.Node;
import org.springframework.stereotype.Component;

@Component
public interface NodeValidator {
	void validate(Node node);
	void validateUpdate(Node node, Node originalNode);
}
