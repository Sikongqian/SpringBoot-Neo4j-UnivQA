package com.univqa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.univqa.service.QuestionService;

@RestController
@RequestMapping("/rest/univqa/question")
public class QuestionController {
	@Autowired
	QuestionService questionService;
	
	@RequestMapping("/query")
	public String query(@RequestParam(value = "question")String question)throws Exception {
        return questionService.answer(question);
    }

}
