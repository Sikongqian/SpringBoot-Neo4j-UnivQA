package com.univqa.neo4j;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@Node("university")
public class University {
	@Id
	private final Long universityId;
	@Property("QS排名")
	private final String qsRank;
	@Property("学校名称")
	private final String universityName;
	@Property("学校创立时间")
	private final String universityEstablishDate;
	@Property("学校介绍")
	private final String universityIntroduction;
	@Relationship(type = "位于", direction = Direction.OUTGOING)
	private Set<Location> universityLocation = new HashSet<>();
	@Relationship(type = "申请条件", direction = Direction.INCOMING)
	private Set<Qualification> universityQualifications = new HashSet<>();
	@Relationship(type = "计算机学费", direction = Direction.INCOMING)
	private Set<Fee> universityComputerFees = new HashSet<>();
	@Relationship(type = "通信学费", direction = Direction.INCOMING)
	private Set<Fee> universityCommunicationFees = new HashSet<>();
	@Relationship(type = "工商管理和会计学费", direction = Direction.INCOMING)
	private Set<Fee> universityBusinessFees = new HashSet<>();
	@Relationship(type = "环境工程学费", direction = Direction.INCOMING)
	private Set<Fee> universityEnvironmentFees = new HashSet<>();
	public University(Long universityId, String qsRank,String universityName,String universityEstablishDate,String universityIntroduction) {
		this.universityId = universityId;
		this.qsRank = qsRank;
		this.universityName = universityName;
		this.universityEstablishDate = universityEstablishDate;
		this.universityIntroduction = universityIntroduction;
	}
	public Long getId() {
		return universityId;
	}
	public String getqsRank() {
		return qsRank;
	}
	public String getName() {
		return universityName;
	}
	public String getEstablishDate() {
		return universityEstablishDate;
	}
}
