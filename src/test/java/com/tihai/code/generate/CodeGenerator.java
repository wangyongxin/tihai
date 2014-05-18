package com.tihai.code.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tihai.entity.Article;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CodeGenerator {
	private final static Log logger = LogFactory.getLog(CodeGenerator.class);
	private static Configuration freemarker_cfg = null;
	
	private final static String projectPath = System.getProperty("user.dir") + "/src/main/java";
	private final static String entityPath = projectPath + "/com/tihai/entity";
	private final static String daoPath = projectPath + "/com/tihai/dao";
	private final static String servicePath = projectPath + "/com/tihai/service";
	private final static String serviceImplPath = projectPath + "/com/tihai/service/impl";
	static List<String> entityClassNameList = new ArrayList<String>();

	/**
	 * 鍔犺浇瀹炰綋绫诲悕绉伴泦鍚�
	 */
	static{
		File file = new File(entityPath);
		for(File file2 : file.listFiles()){
			String file2Name = file2.getName().substring(0, file2.getName().lastIndexOf('.')).trim();
			entityClassNameList.add(file2Name);
		}
	}
	

	/**
	 * 
	 * @param baseDir
	 * @param templateFileName
	 * @param propMap
	 * @param xmlFileName
	 * @return
	 */
	public static boolean generateBaseDaoMappingXml(String baseDir, String templateFileName, Map propMap,
			String xmlFileName) {
		try {
			Template t = getFreeMarkerCFG().getTemplate(templateFileName);

			File afile = new File(baseDir + "/" + xmlFileName);

			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(afile)));

			t.process(propMap, out);
		} catch (TemplateException e) {
			logger.error("Error while processing FreeMarker template "
					+ templateFileName, e);
			return false;
		} catch (IOException e) {
			logger.error("Error while generate Static Html File "
					+ xmlFileName, e);
			return false;
		}

		return true;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		gerenate();
	}
	private static void gerenate() {
		Field[] fields = Article.class.getDeclaredFields();
		Map<String, Object> propMap = new HashMap<String, Object>();
		List<String> properties = new ArrayList<String>();
		for(Field field : fields){
			if(isNeedProperty(field)){
				System.out.println(field.getName());
				properties.add(field.getName());
			}
		}
		propMap.put("properties", properties);
		propMap.put("entity", Article.class.getSimpleName());
		
		generateBaseDaoMappingXml(daoPath, "DaoXml.ftl", propMap, Article.class.getSimpleName() + ".xml");
	}
	
	private static void checkCount(){
//		System.out.println(CodeGenerator.class.getResource("/").getPath());
//		System.out.println(System.getProperty("user.dir"));
		checkFilesCount(entityPath);
		checkFilesCount(daoPath);
		checkFilesCount(servicePath);
		checkFilesCount(serviceImplPath);
	}
	private static void checkFilesCount(String path){
		File file = new File(path);
		System.out.println(path + " 鐩綍涓嬫枃浠舵暟锛� " + file.listFiles().length);
	}

	
	/**
	 * 鍒涘缓澶氱骇鐩綍
	 * 
	 * @param aParentDir
	 *            String
	 * @param aSubDir
	 *            浠� / 寮�澶�
	 * @return boolean 鏄惁鎴愬姛
	 */
	private static boolean creatDirs(String aParentDir, String aSubDir) {
		File aFile = new File(aParentDir);
		if (aFile.exists()) {
			File aSubFile = new File(aParentDir + aSubDir);
			if (!aSubFile.exists()) {
				return aSubFile.mkdirs();
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * 鑾峰彇freemarker鐨勯厤缃�. freemarker鏈韩鏀寔classpath,鐩綍鍜屼粠ServletContext鑾峰彇.
	 */
	private static Configuration getFreeMarkerCFG() {
		if (null == freemarker_cfg) {
			// Initialize the FreeMarker configuration;
			// - Create a configuration instance
			freemarker_cfg = new Configuration();

			// - FreeMarker鏀寔澶氱妯℃澘瑁呰浇鏂瑰紡,鍙互鏌ョ湅API鏂囨。,閮藉緢绠�鍗�:璺緞,鏍规嵁Servlet涓婁笅鏂�,classpath绛夌瓑

			// htmlskin鏄斁鍦╟lasspath涓嬬殑涓�涓洰褰�
			freemarker_cfg.setClassForTemplateLoading(CodeGenerator.class,
					"/template");
		}

		return freemarker_cfg;
	}
	
	/**
	 * 鐢熸垚闈欐�佹枃浠�.
	 * 
	 * @param templateFileName
	 *            妯℃澘鏂囦欢鍚�,鐩稿htmlskin璺緞,渚嬪"/tpxw/view.ftl"
	 * @param propMap
	 *            鐢ㄤ簬澶勭悊妯℃澘鐨勫睘鎬bject鏄犲皠
	 * @param htmlFilePath
	 *            瑕佺敓鎴愮殑闈欐�佹枃浠剁殑璺緞,鐩稿璁剧疆涓殑鏍硅矾寰�,渚嬪 "/tpxw/1/2005/4/"
	 * @param htmlFileName
	 *            瑕佺敓鎴愮殑鏂囦欢鍚�,渚嬪 "1.htm"
	 */
	public boolean geneHtmlFile(String sRootDir, String templateFileName, Map propMap,
			String htmlFilePath, String htmlFileName) {
		// @todo 浠庨厤缃腑鍙栧緱瑕侀潤鎬佹枃浠跺瓨鏀剧殑鏍硅矾寰�:闇�瑕佹敼涓鸿嚜宸辩殑灞炴�х被璋冪敤
//		String sRootDir = "e:/webtest/htmlfile";

		try {
			Template t = getFreeMarkerCFG().getTemplate(templateFileName);

			// 濡傛灉鏍硅矾寰勫瓨鍦�,鍒欓�掑綊鍒涘缓瀛愮洰褰�
			creatDirs(sRootDir, htmlFilePath);

			File afile = new File(sRootDir + "/" + htmlFilePath + "/"
					+ htmlFileName);

			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(afile)));

			t.process(propMap, out);
		} catch (TemplateException e) {
			logger.error("Error while processing FreeMarker template "
					+ templateFileName, e);
			return false;
		} catch (IOException e) {
			logger.error("Error while generate Static Html File "
					+ htmlFileName, e);
			return false;
		}

		return true;
	}
	
	private static boolean isNeedProperty(Field property){
		if(Modifier.isFinal(property.getModifiers())){
			return false;
		}
		if(Modifier.isStatic(property.getModifiers())){
			return false;
		}
		if(StringUtils.equals("java.util.List", property.getType().getName())){
			return false;
		}
		if(entityClassNameList.contains(property.getType().getName())){
			return false;
		}
		return true;
	}
}
