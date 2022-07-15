package com.univqa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.univqa.process.HandleProcess;

@Configuration
/**
 * 这个类主要配置下process的路径
 * @author 王骞
 *
 */
public class ConfigBean {
	
	@Value("${vocabularyPath}")
	private String vocabularyPath;
	@Bean
	public HandleProcess modelProcess() throws Exception{
        return new HandleProcess(vocabularyPath);
    }

}
