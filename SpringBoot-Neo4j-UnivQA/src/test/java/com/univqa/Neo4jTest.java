package com.univqa;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.univqa.neo4j.University;
import com.univqa.repository.UniversityRepository;
/**
 * 测试neo4j是否链接
 */
@SpringBootTest
public class Neo4jTest {
	@Autowired
	UniversityRepository universityRepository;
	
	@Test
	public void testfind() {
		Optional<University> byId = universityRepository.findById(79L);
		
		University university = new University(1000L, "0", "家里蹲大学","2022", "成功人士的选择");//成功创建，连接映射没有问题
		universityRepository.save(university);//测试neo4j是否连接
	}

}
