package org.jeecg.modules.flowable.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * 在线表单应用类
 * @author nbacheng
 * @date 2023-05-16
 */

public class onlineUtils {
	
	public static Map<String, Object> objectHashMap(HttpServletRequest request) {
		Map map = request.getParameterMap();
		HashMap hashmap = new HashMap();
		Iterator iterator = map.entrySet().iterator();
		String key = "";
		String sObject = "";

		for (Object object = null; iterator.hasNext(); hashmap.put(key, sObject)) {
			Entry entry = (Entry) iterator.next();
			key = (String) entry.getKey();
			object = entry.getValue();
			if (!"_t".equals(key) && null != object) {
				if (!(object instanceof String[])) {
					sObject = object.toString();
				} else {
					String[] ssObject = (String[]) ((String[]) object);

					for (int i = 0; i < ssObject.length; ++i) {
						sObject = ssObject[i] + ",";
					}

					sObject = sObject.substring(0, sObject.length() - 1);
				}
			} else {
				sObject = "";
			}
		}

		return hashmap;
	}
	
	public static boolean isJoinQuery(OnlCgformHead onlCgformHead) {
		if (onlCgformHead.getTableType() == 2) {
			String themeTemplate = onlCgformHead.getThemeTemplate();
			if ("erp".equals(themeTemplate) || "innerTable".equals(themeTemplate) || "Y".equals(onlCgformHead.getIsTree())) {
				return false;
			}

			String extConfigJson = onlCgformHead.getExtConfigJson();
			if (extConfigJson != null && !"".equals(extConfigJson)) {
				JSONObject jsonObject = JSON.parseObject(extConfigJson);
				if (jsonObject.containsKey("joinQuery") && 1 == jsonObject.getInteger("joinQuery")) {
					return true;
				}
			}
		}

		return false;
	}
	
	public static JSONObject resultOnlineFormItem(JSONObject joFormItem) {
		JSONObject joproperties;
		if (joFormItem.containsKey("properties")) {
			joproperties = joFormItem.getJSONObject("properties");
		} else {
			JSONObject joschema = joFormItem.getJSONObject("schema");
			joproperties = joschema.getJSONObject("properties");
		}

		ISysBaseAPI iSysBaseApi = (ISysBaseAPI) SpringContextUtils.getBean(ISysBaseAPI.class);
		Iterator iproperties = joproperties.keySet().iterator();

		while (true) {
			while (iproperties.hasNext()) {
				String sproperties = (String) iproperties.next();
				JSONObject jonextproperties = joproperties.getJSONObject(sproperties);
				String view = jonextproperties.getString("view");
				String relation;
				if (checkCompType(view)) {
					relation = jonextproperties.getString("dictCode");
					String dictText = jonextproperties.getString("dictText");
					String dictTable = jonextproperties.getString("dictTable");
					Object arraylist = new ArrayList();
					if (oConvertUtils.isNotEmpty(dictTable)) {
						arraylist = iSysBaseApi.queryTableDictItemsByCode(dictTable, dictText, relation);
					} else if (oConvertUtils.isNotEmpty(relation)) {
						arraylist = iSysBaseApi.queryEnableDictItemsByCode(relation);
					}

					if (arraylist != null && ((List) arraylist).size() > 0) {
						jonextproperties.put("enum", arraylist);
					}
				} else if ("tab".equals(view)) {
					relation = jonextproperties.getString("relationType");
					if ("1".equals(relation)) {
						resultOnlineFormItem(jonextproperties);
					} else {
						JSONArray jocolumns = jonextproperties.getJSONArray("columns");

						for (int i = 0; i < jocolumns.size(); ++i) {
							JSONObject joview = jocolumns.getJSONObject(i);
							if (checkView(joview)) {
								String dictCode = joview.getString("dictCode");
								String dictText = joview.getString("dictText");
								String dictTable = joview.getString("dictTable");
								Object arraylist = new ArrayList();
								if (oConvertUtils.isNotEmpty(dictTable)) {
									arraylist = iSysBaseApi.queryTableDictItemsByCode(dictTable, dictText, dictCode);
								} else if (oConvertUtils.isNotEmpty(dictCode)) {
									arraylist = iSysBaseApi.queryEnableDictItemsByCode(dictCode);
								}

								if (arraylist != null && ((List) arraylist).size() > 0) {
									joview.put("options", arraylist);
								}
							}
						}
					}
				}
			}

			return joFormItem;
		}
	}
	
	public static boolean checkCompType(String type) {
		if ("list".equals(type)) {
			return true;
		} else if ("radio".equals(type)) {
			return true;
		} else if ("checkbox".equals(type)) {
			return true;
		} else {
			return "list_multi".equals(type);
		}
	}
	
	private static boolean checkView(JSONObject jsonObject) {
		Object view = jsonObject.get("view");
		if (view != null) {
			String sView = view.toString();
			if ("list".equals(sView) || "radio".equals(sView) || "checkbox_meta".equals(sView) || "list_multi".equals(sView)
					|| "sel_search".equals(sView)) {
				return true;
			}
		}

		return false;
	}
	
	
}
