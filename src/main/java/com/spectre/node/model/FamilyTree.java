package com.spectre.node.model;

import lombok.*;


import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyTree {
	List<Node> ancestors;
}
