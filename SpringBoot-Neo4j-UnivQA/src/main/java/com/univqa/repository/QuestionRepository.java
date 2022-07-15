package com.univqa.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.univqa.neo4j.University;

/**
 * 基于高校知识图谱的问答的查询接口
 * @author Sikong Qian
 * @date   2022/4/2
 * @version 1.1
 */
public interface QuestionRepository extends Neo4jRepository<University, Long>{
	
	/**
	 * 对应问题1 介绍某学校
	 * @param 高校名称
	 * @return 返回介绍
	 */
	@Query ("match(n:university) where n.学校名称=$universityName return n.学校介绍")
	String getUniversityIntroduction(@Param("universityName") String universityName );
	/**
	 * 对应问题2 某学校的地点介绍
	 * @param 高校名称
	 * @return 返回地点及地点介绍
	 */
	@Query ("match(n:university)-[r:位于]->(m:location) where n.学校名称=$universityName return m.地点名称")
	String getUniversityLocation(@Param("universityName") String universityName);
	/**
	 * 对应问题3 某学校的申请条件
	 * @param 高校名称
	 * @return 返回申请条件
	 */
	@Query ("match(n:university)<-[r:申请条件]-(m:qualification) where n.学校名称=$universityName return m.申请条件")
	String getUniversityQualification(@Param("universityName") String universityName );
	/**
	 * 对应问题4 某学校QS排名情况
	 * @param 高校名称
	 * @return QS排名
	 */
	@Query ("match(n:university) where n.学校名称=$universityName return n.QS排名")
	String getUniversityQsRanking(@Param("universityName") String universityName );
	/**
	 * 对应问题5 提交申请时间
	 * @param 高校名称
	 * @return 返回申请时间
	 */
	@Query ("match(n:university) where n.学校名称=$universityName return n.提交申请时间")
	String getQualificationDate(@Param("universityName") String universityName );
	/**
	 * 对应问题6 学校成立时间
	 * @param 高校名称
	 * @return 返回成立时间
	 */
	@Query ("match(n:university) where n.学校名称=$universityName return n.学校创立时间")
	String getUniversityEstablishDate(@Param("universityName") String universityName );
	/**
	 * 对应问题7 某学校参考学费情况
	 * @param 高校名称，专业名称
	 * @return 返回参考学费
	 */
	@Query ("match(n:university) where n.学校名称=$universityName return n.学费情况")
	String getUniversityFee(@Param("universityName") String universityName );
	/**
	 * 对应问题8 与某学校申请条件相同的学校
	 * @param 高校名称
	 * @return 返回申请条件及相应学校
	 */
	@Query("match(n:university)-[:申请条件]-(m:qualification) where n.学校名称=$universityName "
			+"match(m)-[:申请条件]-(p:university) where NOT(p.学校名称=$universityName) return  p.学校名称")
	List<String> getSameQualificationUniversity(@Param("universityName") String universityName );
	/**
	 * 对应问题9 某地的学校
	 * @param 地点名称
	 * @return 返回学校列表
	 */
	@Query("match(n:university)-[:位于]-(m:location) where m.地点名称=$locationname return n.学校名称")
	List<String> getLocationUniversity(@Param("locationName") String locationName);
	/**
	 * 对应问题10 某联盟有哪几所学校
	 * @param 高校校联盟名称
	 * @return 返回学校列表
	 */
	@Query("match(n:university)-[:属于]-(m:aliance) where m.联盟名称=$alianceName return n.学校名称")
	List<String> getAlianceUniversity(@Param("alianceName") String alianceName);
	/**
	 * 对应问题11 介绍某联盟
	 * @param 高校校联盟名称
	 * @return 返回联盟介绍
	 */
	@Query("match(n:aliance) where n.联盟名称=$alianceName return n.联盟介绍")
	String getAlianceIntroduction(@Param("alianceName") String alianceName);
}

