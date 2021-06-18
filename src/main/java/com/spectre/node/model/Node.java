package com.spectre.node.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.StringJoiner;

@Entity
public class Node {
	public static final Integer EMPTY = null;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	private String name;
	private String description;
	private Integer parentId;

	public Node( String name, String description, Integer parentId) {

		this.name = name;
		this.description = description;
		this.parentId = parentId;
	}

	private Node() {
	}

	public Node( String name, String description) {

		this.name = name;
		this.description = description;
		this.parentId = EMPTY;
	}

	public Node(int id, String name, String description, int parentId) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.parentId = parentId;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Integer getParentId() {
		return parentId;
	}


	public void setDescription(String description) {

		this.description = description;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Node.class.getSimpleName() + "[", "]")
						.add("id=" + id)
						.add("name='" + name + "'")
						.add("description='" + description + "'")
						.add("parentId=" + parentId)
						.toString();
	}
}
