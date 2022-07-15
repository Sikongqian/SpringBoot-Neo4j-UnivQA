package com.univqa.repository;

import com.univqa.neo4j.University;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
/**
 * 测试接口
 * @author Sikong Qian
 */
public interface UniversityRepository extends Neo4jRepository<University, Long> {
	
	/*问题8测试 @Query("match(n:university)-[:申请条件]-(m:qualification) where n.学校名称=$name "
			+"match(m)-[:申请条件]-(p:university) where NOT(p.学校名称=$name) return  p.学校名称")//测试返回列表*/
	
	@Query("match(n:university)-[:申请条件]-(m:qualification) where n.学校名称 contains $universityName "
			+"match(m)-[:申请条件]-(p:university) where NOT(p.学校名称=n.学校名称) return  p.学校名称")
	List<String> findByName(@Param("universityName") String universityName);
}
