package com.univqa.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.rdd.RDD;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;


public class HandleProcess {
	
	
    /**指定问题question的测试集及词汇表的txt模板所在的根目录*/
    private String vocabularyPath;
    
    /**词汇表，在接下来导入*/
    private Map<String, Integer> vocabulary;

    /**朴素贝叶斯分类器*/
    private NaiveBayesModel nbModel;

    /**问句抽象化模板后分类*/
    private Map<Double, String> questionPattern;

    /**抽象后保留关键字*/
    private Map<String, String> abstractMap;

    /** 对应问题模板号码*/
    int questionIndex = -1;
	
    public HandleProcess(String vocabularyPath) throws Exception{
        this.vocabularyPath = vocabularyPath;
        // 加载词汇表
        vocabulary = loadVocabulary();
        System.out.println("词汇表加载完成"+"词汇表大小为"+":"+vocabulary.size());//后台检测
        // 加载问题模板
        questionPattern = loadQuestionTemplates();
        System.out.println("问题模板加载完成");//后台检测
        // 加载分类模型，初始化贝叶斯分类器对象
        nbModel = loadClassifierModel();
        System.out.println("贝叶斯分类器加载完成");//后台检测
    }
    /**
     * 主方法1：加载词汇表,将vocabulary.txt里的内容加载到哈希表里
     */
    public Map<String,Integer> loadVocabulary() {
        Map<String, Integer> vocabulary = new HashMap<String, Integer>();
        File file = new File(vocabularyPath + "/template/vocabulary.txt");
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        try {
            while ((line = buffer.readLine()) != null) {
            	Object obj = null;
                String[] tokens = line.split(":");
                int index = Integer.parseInt(tokens[0].replace("\uFEFF",""));
                String word = tokens[1];
                obj=vocabulary.put(word, index);//每一行都加入
                if(obj!=null) {
                	System.out.println(word+" "+index);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vocabulary;
    }
    /**
     * 主方法2：加载问题对应模板 
     */
    public Map<Double,String> loadQuestionTemplates() {
        Map<Double, String> questionsPattern = new HashMap<>(13);
        File file = new File(vocabularyPath + "/template/question_classification.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                double index = Double.valueOf(tokens[0].replace("\uFEFF",""));
                String pattern = tokens[1];
                questionsPattern.put(index, pattern);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questionsPattern;
    }
    /**
     * 主方法3：加载贝叶斯分类器，这个方法做了两件事 1. 将问题的训练集导入 2.扔到贝叶斯分类器里去训练得到训练好的分类器
     */
    public NaiveBayesModel loadClassifierModel() throws Exception {
        SparkConf conf = new SparkConf().setAppName("NaiveBayesTest").setMaster("local[*]").set("spark.sql.warehouse.dir", "E:/warehouse");//本地路径，启动多线程
        JavaSparkContext sc = new JavaSparkContext(conf);//通过sparkcontext发布到spark集群
        List<LabeledPoint> trainList = new LinkedList<>();//向量的列表，存储训练集
        String[] sentences;
        
        
        //第一步，导入训练集
        Map<Double, String> seqWithSamples = loadTestSamples("/template");
        if(seqWithSamples == null || seqWithSamples.size() <=0){
            System.out.println("缺少样本");
        }
        System.out.println("问题训练集导入完成"+seqWithSamples.size());
        for (Map.Entry<Double, String> entry : seqWithSamples.entrySet()) {
            Double seq = entry.getKey();
            System.out.println(entry.getKey()+entry.getValue());
            String sampleContent = entry.getValue();
            sentences = sampleContent.split("`");
            for (String sentence : sentences) {
                double[] array = toVectors(sentence);
                LabeledPoint train = new LabeledPoint(seq, Vectors.dense(array));
                trainList.add(train);
            }
        }
        System.out.println(trainList);
        System.out.println("开始训练");
        //第二步 训练样本
        JavaRDD<LabeledPoint> trainingRDD = sc.parallelize(trainList);
        /**开始训练样本*/
        System.out.println("训练完毕1");
        RDD<LabeledPoint> rdd= trainingRDD.rdd();
        System.out.println("训练完毕2");
        NaiveBayesModel nb_model = NaiveBayes.train(rdd);
        System.out.println("训练完毕3");
        /** 关闭资源*/
        sc.close();
        /** 返回训练好的分类器*/
        return nb_model;
    }
    
    /**主方法4：分析问句*/
	public List<String> analysis(String question) throws Exception {

	    System.out.println("后端接收问句："+question);
	
	    /**抽象问题*/
	    System.out.println("开始抽象化句子");
	    String abstractQ = questionAbstract(question);
	    System.out.println("抽象完成："+abstractQ);
	
	    /**将抽象的结果分类*/
	    System.out.println("分类");
	    String pattern = questionClassify(abstractQ);
	    System.out.println("套用模板完成："+pattern);
	
	    /**将关键词还原*/
	    System.out.println("还原关键词");
	    String finalPattern = questionReduction(pattern);
	    System.out.println("关键词还原"+finalPattern);
	
	    List<String> resultList = new ArrayList<>();
	    resultList.add(String.valueOf(questionIndex));
	    String[] finalPatternArr = finalPattern.split(" ");
	    for (String keyWord : finalPatternArr)
	        resultList.add(keyWord);
	    System.out.println("注意问题分类后结果为："+resultList.get(0)+resultList.get(1));
	    return resultList;
	}
	
	/**
     * 3-子方法1：导入测试集样本
     * @param 注意此处传入参数是文件夹路径
     */ 
    public Map<Double,String> loadTestSamples(String path) throws IOException {

        File file = new File(vocabularyPath+path);
        if(!file.exists()){
            throw new IOException("文件不存在！");
        }

        Map<Double,String> seqWithSamples = new HashMap<>(13);
        
        
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.contains("、");
            }
        });//这里选择含有、符号的文件进行读取 用、标记了测试集文件
        System.out.println("此处导入了"+files.length+"个问题训练集");
        if(files!=null && files.length>0){
            for (int i = 0; i <files.length ; i++) {
                File sampleFile = files[i];
                String fileName = sampleFile.getName();
                String subStr = fileName.substring(0, fileName.indexOf("、"));
                String seqStr = subStr.replace("、","");
                BufferedReader br = new BufferedReader(new FileReader(sampleFile));
                String content = "";
                String line;
                while ((line = br.readLine()) != null) {
                    content += line + "`";//这里用`当作换行符
                }
                br.close();
                seqWithSamples.put(Double.parseDouble(seqStr),content);//这里返回的是 标签 问题集 的键值对集
            }
        }
        return seqWithSamples;
    }
    
    /**
     * 3-子方法2：将问题句转换为向量（不仅用于训练集）
     */ 
    public double[] toVectors(String sentence){
    	System.out.println(sentence);
        double[] vector = new double[vocabulary.size()];
        for (int i = 0; i < vocabulary.size(); i++) {
            vector[i] = 0;
        }
        Segment segment = HanLP.newSegment();
        List<Term> terms = segment.seg(sentence);
        for (Term term : terms) {
			System.out.println(term.toString()+" ");
		}
        for (Term term : terms) {
            String word = term.word;
            if (vocabulary.containsKey(word)) {
                int index = vocabulary.get(word);
                vector[index] = 1.0;
            }//检测vocabulary中是否有这个词，并修改对应的下标
        }
        return vector;
    }
    
    /**
     * 4-子方法1：将HanLp分词后的关键词，用抽象词性替换，将问题抽象为训练集的形式
     * 如：介绍一下剑桥大学- 介绍 一下 nu
     */
    public  String questionAbstract(String questionSentence) {

        Segment segment = HanLP.newSegment().enableCustomDictionary(true);
        List<Term> terms = segment.seg(questionSentence);
        String abstractQuestion = "";
        abstractMap = new HashMap<>();
        for (Term term : terms) {
            String word = term.word;
            String termStr = term.toString();
            System.out.println(termStr);
            if (termStr.contains("nu")) {        //nm 电影名
                abstractQuestion += "nu ";
                abstractMap.put("nu", word);
            }else if (termStr.contains("np")) {  //np  地名
                abstractQuestion += "np ";
                abstractMap.put("np", word);
            } else if (termStr.contains("na")) { //na 联盟
            	abstractQuestion += "na ";
                abstractMap.put("na", word);
            }
            else {
                abstractQuestion += word + " ";
            }
        }
        return abstractQuestion;
    }
    
    /**
     * 4-子方法2：将抽象后的句子分类
     */
    public String questionClassify(String sentence) throws Exception {

        double[] testArray = toVectors(sentence);
        Vector questionVector = Vectors.dense(testArray);
        double index = nbModel.predict(questionVector);
        questionIndex = (int)index;
        System.out.println("the model index is " + index);
        Vector patternProbablity = nbModel.predictProbabilities(questionVector);
        double[] probabilities = patternProbablity.toArray();
        System.out.println("============ 问题模板分类概率 =============");
        for (int i = 0; i < probabilities.length; i++) {
            System.out.println("问题模板分类 "+i+"的概率为："+String.format("%.4f", probabilities[i]));
        }
        System.out.println("============ 问题模板分类概率 =============");
        return questionPattern.get(index);
    }
    
    /**
     * 4-子方法3：将抽象后的句子中的关键词还原
     */
    public String questionReduction(String questionPattern) {
        Set<String> set = abstractMap.keySet();
        for (String key : set) {
            if (questionPattern.contains(key)) {
                String value = abstractMap.get(key);
                questionPattern = questionPattern.replace(key, value);
            }
        }
        String extendedQuery = questionPattern;
        abstractMap.clear();
        abstractMap = null;//这里需要清空，不然下一个问题可能会产生冲突
        return extendedQuery;
    }
}
