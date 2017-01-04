package wbkj.sjapp.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {

	public static String toString(Date date) {
		if(date==null) return "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);

		return str;
	}
	
	public static boolean isToday(Date date) {
		if(date==null) return false;
		Date today = new Date();

		return date.getDate() == today.getDate() && date.getYear() == today.getYear() && date.getMonth() == today.getMonth();
	}
	
	public static String toString(Date date,String format) {
		if(date==null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String str = sdf.format(date);

		return str;
	}

	public static Date toDate(String str) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = (Date) format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}
	

	public static String toSqlDateString(Date date) {
		if(date==null) return "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);

		return str;
	}
	

	public static String toTimeString(Time date) {
		if(date==null) return "";
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		String str = format.format(date);

		return str;
	}
	

	public static Date mergeTime(Date date,Time time){
		//Timestamp timestamp = new Timestamp(date.getTime());
		date.setHours(time.getHours());
		date.setMinutes(time.getMinutes());
		date.setSeconds(time.getSeconds());
		return date;
	}
	
	private static final String[] namesDays = {"今天", "明天", "后天"};
	public static String getNameOfDate(Date dt) {
        Date date = new Date();
        int duration = dt.getDate()-date.getDate();
        if(duration>=0 && duration<=3){
        	return namesDays[duration];
        }
        return toString(dt,"MM-dd");
    }
	
	private static final String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	public static String getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
