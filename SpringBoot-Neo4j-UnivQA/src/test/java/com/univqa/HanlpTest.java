package com.univqa;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.hankcs.hanlp.HanLP;
//import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
@SpringBootTest
/**
 * 测试hanlp分词器是否运作
 */
public class HanlpTest {
	@Test
	public void TestA(){
		String lineStr = "剑桥镇的剑桥大学是G5院校";
		try{
			Segment segment = HanLP.newSegment();
		    segment.enableCustomDictionary(true);
		    //CustomDictionary.add("剑桥大学","ng");//可以自定义名词词性
		    //CustomDictionary.add("剑桥","np");
			List<Term> seg = segment.seg(lineStr);
			for (Term term : seg) {
				System.out.print(term.toString()+" ");
			}
		}catch(Exception ex){
			System.out.println(ex.getClass()+","+ex.getMessage());
		}		
	}
}