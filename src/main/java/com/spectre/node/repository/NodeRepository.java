package com.spectre.node.repository;

import com.spectre.node.model.Node;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends CrudRepository<Node, Integer> {
}
