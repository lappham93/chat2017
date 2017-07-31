package com.mit.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

	public static BigDecimal roundCurrencyAmount(BigDecimal amount) {
		return round(amount, 2);
	}

	public static BigDecimal round(BigDecimal amount, int decimals) {
		return amount.setScale(decimals, RoundingMode.HALF_EVEN);
	}
	
	public static double round(double amount, int decimals) {
		return round(new BigDecimal(amount), decimals).doubleValue();
	}
	
	public static double distance(double lon1, double lat1, double lon2, double lat2) {
		double R = 6371000;
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		lon1 = Math.toRadians(lon1);
		lon2 = Math.toRadians(lon2);
		double dtLat = lat1 - lat2;
		double dtLon = lon1 - lon2;
		double a = Math.pow(Math.sin(dtLat/2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dtLon/2), 2);
		double c = 2 * Math.asin(Math.pow(a, 0.5));
		double dis = R * c;
		
		return dis;		
	}

	public static long toPrimitive(Long val) {
		return val != null ? val : 0L;
	}

	public static int toPrimitive(Integer val) {
		return val != null ? val : 0;
	}

	public static String ordinalSuffixOf(int i) {
		int j = i % 10,
				k = i % 100;
		if (j == 1 && k != 11) {
			return i + "st";
		}
		if (j == 2 && k != 12) {
			return i + "nd";
		}
		if (j == 3 && k != 13) {
			return i + "rd";
		}
		return i + "th";
	}
	
	public static void main(String[] args) {
		BigDecimal val = new BigDecimal(21.12131);
		
		System.out.println(round(12.123213, 2));
	}
}
