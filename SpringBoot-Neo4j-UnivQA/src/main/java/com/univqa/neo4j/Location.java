package com.univqa.neo4j;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@Node("location")
public class Location {
	@Id
	private final Long locationId;
	@Property("地点名称")
	private final String locationName;
	@Relationship(type = "位于", direction = Direction.INCOMING)
	private Set<University> universitys = new HashSet<>();
	
	public Location(Long locationId, String locationName) {
		this.locationId = locationId;
		this.locationName = locationName;
	}
	public Long getId() {
		return locationId;
	}
	public String getName() {
		return locationName;
	}
}
