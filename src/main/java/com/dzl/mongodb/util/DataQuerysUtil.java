package com.dzl.mongodb.util;



import org.springframework.util.StringUtils;

import java.util.List;


public class DataQuerysUtil {


	public static void wipeVirus(String var) {
		if (var != null) {
			// 防止sql注入的字符转义
			var = escapeSql(var);
			if (sql_inj(var)) {
				throw new RuntimeException(var + "违反SQL注入风险约束！");
			}
		}
	}

	public static String escapeSql(String str) {
		if (str == null) {
			return null;
		}
		return StringUtils.replace(str, "'", "''");
	}

	private static boolean sql_inj(String str) {
		String inj_str = "'| and | exec | insert | select | delete | update | count |*|%| chr | mid | master | truncate | char | declare |;| or |+|,";
		// 这里的东西还可以自己添加
		String[] inj_stra = inj_str.split("\\|");
		for (int i = 0; i < inj_stra.length; i++) {
			String charstr = inj_stra[i];
			if (str.indexOf(charstr) >= 0) {
			/*	log.error(str + "违反SQL注入风险约束:" + inj_stra[i]);*/
				return true;
			}
		}
		return false;
	}



	public static String getWhereInSubVals(List<String> ids) {
		String where = null;
		for (String id : ids) {
			if (where == null) {
				where = "'" + id + "'";
			} else {
				where = where + ",'" + id + "'";
			}
		}
		return where;
	}
}
