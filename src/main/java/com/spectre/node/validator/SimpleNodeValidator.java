package com.spectre.node.validator;

import com.spectre.node.exception.NodeFormatException;
import com.spectre.node.exception.OperationNotPermitted;
import com.spectre.node.model.Node;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;


@Component
public class SimpleNodeValidator implements NodeValidator {

	public void validate(Node node) {
		if(Strings.isEmpty(node.getDescription()))
			throw new NodeFormatException("Description can't be null");

		if((node.getDescription().length() > 250))
			throw new NodeFormatException("Description exceeds 250 chars");

		if(Strings.isEmpty(node.getName()))
			throw new NodeFormatException("Name can't be null");
		if(node.getName().length() >50)
			throw new NodeFormatException("Name exceeds 50 chars");
	}

	public void validateUpdate(Node node, Node originalNode) {
		if(!originalNode.getName().equals(node.getName()))
			throw new OperationNotPermitted("Can't change node's name");
		if(originalNode.getParentId()!=null && node.getParentId() == originalNode.getId())
			throw new NodeFormatException("A node can't parent of itself");
	}
}
