package com.univqa.neo4j;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("fee")
public class Fee {
	@Id
	private final Long feeId;
	@Property("学费")
	private final String fee;
	public Fee(Long feeId,String fee) {
		this.feeId = feeId;
		this.fee = fee;
	}
	public Long getId() {
		return feeId;
	}
	public String geeFee() {
		return fee;
	}
}
