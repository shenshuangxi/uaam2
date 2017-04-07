package com.gionee.uaam2.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.gionee.uaam2.dao.ResponseFields;

public class SearchFieldsUtil {

	private static Map<String,SearchFields> searchFiledMap = new HashMap<String, SearchFields>();
	
	static{
		Yaml yaml = new Yaml();
		InputStream is = null;
		try {
			is = SearchFieldsUtil.class.getClassLoader().getResourceAsStream("searchFields.yaml");
			List<SearchFields> list = (List<SearchFields>) yaml.load(is);
			for(SearchFields searchFileds : list){
				searchFiledMap.put(searchFileds.getCategoryValue(), searchFileds);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	}
	
	public static List<ResponseFields> getCategorys(){
		List<ResponseFields> responseFieldList = new ArrayList<ResponseFields>();
		for(SearchFields searchFields : searchFiledMap.values()){
			ResponseFields responseFields = new ResponseFields();
			responseFields.setText(searchFields.getCategory());
			responseFields.setId(searchFields.getCategoryValue());
			responseFieldList.add(responseFields);
		}
		return responseFieldList;
	}
	
	public static List<ResponseFields> getFieldsByCategory(String categoryValue){
		List<ResponseFields> responseFieldList = new ArrayList<ResponseFields>();
		Map<String,String> fieldMaps = searchFiledMap.get(categoryValue).getFields();
		for(String key : fieldMaps.keySet()){
			ResponseFields responseFields = new ResponseFields();
			responseFields.setText(key);
			responseFields.setId(fieldMaps.get(key));
			responseFieldList.add(responseFields);
		}
		return responseFieldList;
	}
	
	
	
	public static void main(String[] args){
		System.out.println("ok");
	}
	
}
