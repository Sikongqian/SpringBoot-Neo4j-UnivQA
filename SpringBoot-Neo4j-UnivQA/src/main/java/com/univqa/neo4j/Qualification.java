package com.univqa.neo4j;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@Node("qualification")
public class Qualification {
	@Id
	private final Long qualificationId;
	@Property("申请条件")
	private final String qualification;
	@Relationship(type = "申请条件", direction = Direction.INCOMING)
	private Set<University> qualificationUniversitys = new HashSet<>();
	public Qualification(Long qualificationId,String qualification) {
		this.qualificationId = qualificationId;
		this.qualification = qualification;
	}
	public Long getId() {
		return qualificationId;
	}
	public String getQualification(){
		return qualification;
	}
}
