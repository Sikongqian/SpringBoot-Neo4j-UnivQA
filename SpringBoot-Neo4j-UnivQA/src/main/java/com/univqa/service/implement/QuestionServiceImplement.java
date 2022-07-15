package com.univqa.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.univqa.process.HandleProcess;
import com.univqa.repository.QuestionRepository;
import com.univqa.service.QuestionService;

@Service
@Primary
public class QuestionServiceImplement implements QuestionService{
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private HandleProcess handleProcess;
	@Override
	public String answer(String question) throws Exception{

        List<String> reOfProcess = handleProcess.analysis(question);
        int modelIndex = Integer.valueOf(reOfProcess.get(0));
        String answer =null;//答案

        switch (modelIndex) {
            case 0:
                answer = getUnivIntro(reOfProcess);
                break;
            case 1:
                answer = getUnivLoc(reOfProcess);
                break;
            case 2:
                answer = getUnivQuali(reOfProcess);
                break;
            case 3:
                answer = getUnivQs(reOfProcess);
                break;
            case 4:
                answer = getQualiDate(reOfProcess);
                break;
            case 5:
                answer = getUnivEstDate(reOfProcess);
                break;
            case 6:
                answer = getUnivFee(reOfProcess);
                break;
            case 7:
                answer = getSameQualiUniv(reOfProcess);
                break;
            case 8:
                answer = getLocUniv(reOfProcess);
                break;
            case 9:
                answer = getAliUniv(reOfProcess);
                break;
            case 10:
                answer = getAliIntro(reOfProcess);
                break;
            default:
                break;
        }
        System.out.println(answer);
        if (answer != null && !"".equals(answer)) {
            return "找到啦OVO\n"+answer;
        } else {
            return "sorry,没有找到你要的答案QAQ";
        }
	}
	/** nu 高校介绍*/
	private String  getUnivIntro(List<String> reOfProcess) {
		String univName = reOfProcess.get(1);
		String univIntro = questionRepository.getUniversityIntroduction(univName);
		return univName+"的简介："+univIntro;
	}
	/** nu 高校地点介绍*/
	private String  getUnivLoc(List<String> reOfProcess) {
		String univName = reOfProcess.get(1);
		String univLoc = questionRepository.getUniversityLocation(univName);
		return univName+"的地点介绍："+univLoc;
	}
	/** nu 高校申请条件*/
	private String  getUnivQuali(List<String> reOfProcess) {
		String univName = reOfProcess.get(1);
		String univQuali = questionRepository.getUniversityQualification(univName);
		return univName+"的申请条件为："+univQuali;
		
	}
	/** nu 高校QS排名*/
	private String  getUnivQs(List<String> reOfProcess) {
		String univName = reOfProcess.get(1);
		String univQs = questionRepository.getUniversityQsRanking(univName);
		return univName+"的QS排名为："+univQs;
	}
	/** nu 高校提交申请条件时间*/
	private String  getQualiDate(List<String> reOfProcess) {
		String univName = reOfProcess.get(1);
		String univQualiDate = questionRepository.getQualificationDate(univName);
		return univName+"的申请截止时间为："+univQualiDate;
	}
	/** nu 高校成立时间*/
	private String  getUnivEstDate(List<String> reOfProcess) {
		String univName = reOfProcess.get(1);
		String univEstabDate = questionRepository.getUniversityEstablishDate(univName);
		return univName+"的创立时间为："+univEstabDate;
	}
	/** nu 高校参考学费*/
	private String  getUnivFee(List<String> reOfProcess) {
		String univName = reOfProcess.get(1);
		String univFee = questionRepository.getUniversityFee(univName);
		return univName+"的参考学费为："+univFee;
	}
	/** nu 相似申请条件的高校*/
	private String  getSameQualiUniv(List<String> reOfProcess) {
		String univName = reOfProcess.get(1);
		String univQualiDate = questionRepository.getQualificationDate(univName);
		return univName+"的申请截止时间为："+univQualiDate;
	}
	/** np 某地的高校*/
	private String  getLocUniv(List<String> reOfProcess) {
		String placeName = reOfProcess.get(1);
		List<String> locUnivs = questionRepository.getLocationUniversity(placeName);
		String locUniv;
		if (locUnivs.size() == 0) {
	        locUniv = null;
	    } else {
	    	locUniv= locUnivs.toString().replace("[", "").replace("]", "");
	    }
		return placeName+"的大学有："+locUniv;
	}
	/** na 某联盟的高校*/
	private String  getAliUniv(List<String> reOfProcess) {
		String allianceName = reOfProcess.get(1);
		List<String> aliUnivs = questionRepository.getAlianceUniversity(allianceName);
		String allianceUniv;
		if (aliUnivs.size() == 0) {
			allianceUniv = null;
	    } else {
	    	allianceUniv= aliUnivs.toString().replace("[", "").replace("]", "");
	    }
		return allianceName+"中的大学有："+allianceUniv;
	}
	/** na 某联盟的介绍*/
	private String  getAliIntro(List<String> reOfProcess) {
		String allianceName = reOfProcess.get(1);
		String allianceIntro = questionRepository.getAlianceIntroduction(allianceName);
		return allianceName+"的介绍："+allianceIntro;
	}
}
