package com.gionee2.uaam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.gionee.uaam2.util.SearchFields;

public class YamlFileTest {

	@Test
	public void test() throws IOException{
		Yaml yaml = new Yaml();
		File dumpFile = new File( "E:\\project\\myspace\\uaam\\src\\main\\resources\\searchFields.yaml");
		System.out.println(dumpFile.getPath());
		SearchFields searchUserFields = new SearchFields();
		searchUserFields.setCategory("用户");
		searchUserFields.setCategoryValue("user");
		Map<String,String> userFieldMap = new HashMap<String, String>();
		userFieldMap.put("姓名", "username");
		userFieldMap.put("账号", "account");
		userFieldMap.put("邮件", "mail");
		userFieldMap.put("手机号", "mobile");
		searchUserFields.setFields(userFieldMap);
		
		SearchFields searchGroupFields = new SearchFields();
		searchGroupFields.setCategory("分组");
		searchGroupFields.setCategoryValue("group");
		Map<String,String> groupFieldMap = new HashMap<String, String>();
		groupFieldMap.put("组名", "groupName");
		groupFieldMap.put("描述", "description");
		searchGroupFields.setFields(groupFieldMap);
		List<SearchFields> list = new ArrayList<SearchFields>();
		list.add(searchUserFields);
		list.add(searchGroupFields);
		yaml.dump(list,new FileWriter(dumpFile));
//		InputStream is = MenuUtil.class.getClassLoader().getResourceAsStream("menu.yaml");
//		FileInputStream fis = new FileInputStream(dumpFile);
//		List<SearchFields> result= (List<SearchFields>) yaml.load(fis);
//		fis.close();
//		System.out.println(result);
		
	}
	
	
}
