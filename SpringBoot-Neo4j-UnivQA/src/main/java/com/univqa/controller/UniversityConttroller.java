package com.univqa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.univqa.repository.UniversityRepository;

@RestController
@RequestMapping("/rest/univqa/university")
public class UniversityConttroller {
	@Autowired
	UniversityRepository universityRepository;
	@RequestMapping("/get")
	List<String> byname(@RequestParam(value = "name") String name) {
		return universityRepository.findByName(name);
	}
}
