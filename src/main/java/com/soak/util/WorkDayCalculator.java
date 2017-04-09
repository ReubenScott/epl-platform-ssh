package com.soak.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 工作日计算器 类加载的时候字段把所有节假日和非节假日按月（YYYYMM类型）存入Map中,计算的时候找到相关的月份的节假日，然后用自然日减去节假日
 * 如果直接用数学方法运算，混合法定节假日，实现起来反而不如此算法单一可靠。
 * 
 * @author 王石
 * 
 */
public class WorkDayCalculator {
	//protected static final Logger log = Logger.getLogger(WorkDayCalculator.class);

	public static java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static java.text.SimpleDateFormat formatter2 = new java.text.SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * 此处添加节假日日期
	 */
	public static  String[] HOLIDAY = new String[] { };

	/**
	 * 此处添加非节假日日期
	 */
	public static  String[] NONE_HOLIDAY = new String[] { };

	private final long[] HOLIDAY_NUMBER;
	private final long[] NONE_HOLIDAY_NUMBER;
	private long maxH = 0; // 最大数等于零，便于后面求最大最小值
	private long minH = Long.MAX_VALUE;
	private long maxNH = 0;
	private long minNH = Long.MAX_VALUE;

	private static final long DAY_MILLSECOND = 24L * 60L * 60L * 1000L;

	private Map<Integer, Long[]> days = new TreeMap<Integer, Long[]>();

	private static WorkDayCalculator wdc = new WorkDayCalculator();

	public static WorkDayCalculator newInstance() {
		return wdc;
	}

	/**
	 * 获取两个日期之间的工作日
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getDays(Date startDate, Date endDate) {
		long start = startDate.getTime() / DAY_MILLSECOND * DAY_MILLSECOND;
		long end = endDate.getTime() / DAY_MILLSECOND * DAY_MILLSECOND;
		assert start < end;

		Calendar startDay = GregorianCalendar.getInstance();
		startDay.setTime(startDate);
		Calendar endDay = GregorianCalendar.getInstance();
		endDay.setTime(endDate);
		endDay.add(Calendar.MONTH, 1);
		int count = 0;
		while (startDay.before(endDay)) {
			Long[] hdays = getHolidaysInMonth(startDay);
			for (long hd : hdays) {
				if (hd >= start && hd <= end) {
					count++;
				}
			}

			startDay.add(Calendar.MONTH, 1);
		}

		return (int) ((end - start) / DAY_MILLSECOND - count);
	}

	void print(Calendar c) {
		System.out.println(formatter.format(c.getTime()));
	}
	
	void print(Date c) {
		System.out.println(formatter.format(c.getTime()));
	}
	
	/**
	 * 获取一个日期之后的多个工作日的日期，如果得到的是节假日，那么顺延到后面的第一个工作日
	 * 
	 * @param startDate
	 * @param days
	 * @return
	 */
	public Date getDate(Date startDate, int __days) {
		long start = startDate.getTime();
		//从次日开始计算。
		int count = 0;
		Calendar startDay = GregorianCalendar.getInstance();
		startDay.setTimeInMillis(start);
		startDay.clear(Calendar.HOUR_OF_DAY);
		
		while (count < __days) {
			startDay.add(Calendar.DATE, 1);
			// 如果不是节假日
			if (!isHoliday(startDay)) {
				count++;
			}
		}

		// 如果当前算出的日期是节假日，那么需要一直顺延到第一个非节假日
		while (isHoliday(startDay)) {
			startDay.add(Calendar.DAY_OF_MONTH, 1);
		}

		 return startDay.getTime();
	}
	/**
	 * 获取一个日期之后的自然日
	 * 
	 * @param startDate
	 * @param days
	 * @return
	 */
	public Date getDate2(Date startDate, int __days) {
		long start = startDate.getTime();
		 
		Calendar startDay = GregorianCalendar.getInstance();
		startDay.setTimeInMillis(start);
		startDay.clear(Calendar.HOUR_OF_DAY);
		startDay.add(Calendar.DATE, __days);
		
		 return startDay.getTime();
	}
	
	private WorkDayCalculator() {
		HOLIDAY_NUMBER = new long[HOLIDAY.length];
		NONE_HOLIDAY_NUMBER = new long[NONE_HOLIDAY.length];
		// 初始化假日和非假日常量
		init();

		// 建立从2006年到2015年的假日
		for (int y = 2005; y < 2015; y++) {
			for (int m = 0; m < 12; m++) {
				setup(y, m, days);
			}
		}

		//log.info("工作日计算器初始化完毕");
	}
	
	void init() {
		// 转换节假日到LONG数组中
		for (int i = 0; i < HOLIDAY.length; i++) {
			try {
				HOLIDAY_NUMBER[i] = formatter2.parse(HOLIDAY[i]).getTime();
				if (HOLIDAY_NUMBER[i] < minH)
					minH = HOLIDAY_NUMBER[i];
				if (HOLIDAY_NUMBER[i] > maxH)
					maxH = HOLIDAY_NUMBER[i];

			} catch (Exception e) {
				//log.warn("日期格式错误：" + HOLIDAY[i]);
			}
		}
		Arrays.sort(HOLIDAY_NUMBER);

		// 转换非节假日到LONG数组中
		for (int i = 0; i < NONE_HOLIDAY.length; i++) {
			try {
				NONE_HOLIDAY_NUMBER[i] = formatter2.parse(NONE_HOLIDAY[i])
						.getTime();
				if (NONE_HOLIDAY_NUMBER[i] < minNH)
					minNH = NONE_HOLIDAY_NUMBER[i];
				if (NONE_HOLIDAY_NUMBER[i] > maxNH)
					maxNH = NONE_HOLIDAY_NUMBER[i];
			} catch (Exception e) {
				//log.warn("日期格式错误：" + NONE_HOLIDAY[i]);
			}
		}
		Arrays.sort(NONE_HOLIDAY_NUMBER);
	}

	/**
	 * 返回指定日期所在月份的所有假日
	 * 
	 * @param day
	 * @return
	 */
	private Long[] getHolidaysInMonth(Calendar day) {
		int key = day.get(Calendar.YEAR) * 100 + day.get(Calendar.MONTH) + 1;

		Long[] hdays = days.get(key);

		return hdays;
	}

	/**
	 * 是否是假日
	 * 
	 * @param day
	 * @return
	 */
	private boolean isHoliday(Calendar day) {
		Long[] hds = getHolidaysInMonth(day);

		return Arrays.binarySearch(hds, day.getTimeInMillis()) >= 0;
	}

	/**
	 * 一次性建立一个月的所有休息日，将周日周六和法定休息日与非休息日混合
	 * 
	 * @param year
	 * @param month
	 * @param days
	 */
	private void setup(int year, int month, Map<Integer, Long[]> days) {
		Calendar c = GregorianCalendar.getInstance();
		c.clear();
		c.set(year, month, 1);
		c.set(Calendar.AM_PM, 0);
		Set<Long> ts = new TreeSet<Long>();

		for (int i = 0; i < 31; i++) {
			if (c.get(Calendar.MONTH) > month)
				break;

			int dow = c.get(Calendar.DAY_OF_WEEK);
			long s = c.getTimeInMillis();

			// 如果是周日或周六
			if (dow == Calendar.SATURDAY || dow == Calendar.SUNDAY) {
				if (s < minNH || s > maxNH)
					ts.add(s);
				// 不在NON_HOLIDAY中
				else if (Arrays.binarySearch(NONE_HOLIDAY_NUMBER, s) < 0)
					ts.add(s);
			} else {
				if (s < minH || s > maxH)
					;
				// 如果在节假日列表中
				else if (Arrays.binarySearch(HOLIDAY_NUMBER, s) >= 0)
					ts.add(s);
			}

			c.add(Calendar.DATE, 1);
		}

		int key = year * 100 + month + 1;
		Long[] tmp = ts.toArray(new Long[0]);
		Arrays.sort(tmp);
		days.put(key, tmp);
	}

	public static void main(String[] argv) {
		WorkDayCalculator wdc = WorkDayCalculator.newInstance();
		// wdc.setup(2009, 9, wdc.days);

//		 for (Long[] ts : wdc.days.values()) {
//		 for (long t : ts) {
//		 System.out.println(formatter.format(new Date(t)));
//		 }
//		 }

		String sd = "2009-09-11";
		String ed = "2009-10-10";

		try {
			Date d1 = new Date(formatter2.parse(sd).getTime());
			Date d2 = new Date(formatter2.parse(ed).getTime());
			int d = wdc.getDays(d1, d2);

			System.out.println(sd);
			System.out.println(ed);
			System.out.println(d);
			System.out.println(wdc.getDate(d1, d));
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * for (Long[] ts : wdc.days.values()) { for (long t : ts) {
		 * System.out.println(formatter.format(new Date(t))); } }
		 */
	}
}
