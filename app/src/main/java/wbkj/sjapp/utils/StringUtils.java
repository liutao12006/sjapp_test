package wbkj.sjapp.utils;

public class StringUtils {

	public static boolean isNotBlank(String... strings) {
		if (strings != null && strings.length > 0) {
			for (String string : strings) {
				if (string == null || string.trim().equals("")) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public static String toString(Object obj) {

		String str = "";
		try {
			str = (String) obj;
			if (str == null) {
				str = "";
			}
		} catch (ClassCastException ce) {
			try {
				str = String.valueOf(obj);
			} catch (Exception e) {
				str = "";
			}

		}
		return str.trim();
	}

}
