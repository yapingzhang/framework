package cn.bidlink.framework.web;

import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.core.convert.converter.Converter;

import cn.bidlink.framework.core.utils.DateUtils;
 /**
 * @description:	日期转换(目前没有校验日期是否正确，只校验格式)
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-9-7		下午12:32:41
 *
 */
public class BidDateConvert implements Converter<String, Date> {
    static String dateReg = "[\\d]{4}-[\\d]{2}-[\\d]{2}";
    static String timeHHmmReg = "[\\d]{2}:[\\d]{2}";
    static String timeHHmmssReg = "[\\d]{2}:[\\d]{2}:[\\d]{2}";
 
	static Pattern pDate = Pattern.compile(dateReg);
	static Pattern pDateHhMm = Pattern.compile(dateReg+" "+timeHHmmReg);
	static Pattern pDateHhMmSs = Pattern.compile(dateReg+" "+timeHHmmssReg);
	static Pattern pDateHhMmSsZ1 = Pattern.compile(dateReg+"T"+timeHHmmssReg+"Z");
	static Pattern pDateHhMmSsZ2 = Pattern.compile(dateReg+"T"+timeHHmmssReg+"Z");
	static Pattern pDateHhMmSsZ3 = Pattern.compile(dateReg+"T"+timeHHmmssReg+"z");
	static Pattern pDateHhMmSsZ4 = Pattern.compile(dateReg+"T"+timeHHmmssReg);

	@Override
	public Date convert(String source) {
		
		if (pDate.matcher(source).matches()) {
			 return DateUtils.parse(source, DateUtils.DATE);	
		} else if (pDateHhMm.matcher(source).matches()) {
			 return DateUtils.parse(source, DateUtils.DATE_HH_MM);	
		}else if (pDateHhMmSs.matcher(source).matches()) {
			 return DateUtils.parse(source, DateUtils.DATE_HH_MM_SS);	
		}else if (pDateHhMmSsZ1.matcher(source).matches()) {
			 return DateUtils.parse(source, DateUtils.DATE_HH_MM_SS_Z1);	
		}else if (pDateHhMmSsZ2.matcher(source).matches()) {
			 return DateUtils.parse(source, DateUtils.DATE_HH_MM_SS_Z2);	
		}else if (pDateHhMmSsZ3.matcher(source).matches()) {
			return DateUtils.parse(source, DateUtils.DATE_HH_MM_SS_Z3);	
		}else if (pDateHhMmSsZ4.matcher(source).matches()) {
			return DateUtils.parse(source, DateUtils.DATE_HH_MM_SS_Z4);	
		}
		return null;
			
	}
 
 
}

