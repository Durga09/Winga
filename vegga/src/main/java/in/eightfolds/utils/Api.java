package in.eightfolds.utils;

import android.annotation.SuppressLint;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import in.eightfolds.winga.BuildConfig;


@SuppressLint("SimpleDateFormat")
public class Api {

	public static String toJson(Object object) throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Writer strWriter = new StringWriter();
		mapper.writeValue(strWriter, object);
		return strWriter.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object fromJson(String json, Class class1)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, class1);
	}

	public static String getCheckSum(File checkSumFIle) {
		String checkSum = "";
		InputStream is = null;
		try {
			if (BuildConfig.DEBUG) {
				System.out.println("in getCheckSum method ");
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			is = new FileInputStream(checkSumFIle);

			int length = 512;
			byte bt[] = new byte[length];
			int read = 0;

			while ((read = is.read(bt, 0, length)) != -1) {
				md.update(bt, 0, read);
			}
			checkSum = new BigInteger(1, md.digest()).toString(16);
			if (checkSum.length() < 32) {
				int l = 32 - checkSum.length();
				for (int i = 0; i < l; i++) {
					checkSum = "0" + checkSum;
				}
			}
			if (BuildConfig.DEBUG) {
				System.out.println("check sum is " + checkSum);
			}
		} catch (Exception e) {
			if (BuildConfig.DEBUG) {
				System.out.println("Error " + e.toString());
			}
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
				if (BuildConfig.DEBUG) {
					System.out.println("Error " + e.toString());
				}
			}
		}
		return checkSum;
	}

	public static String toCSV(String[] strings) {
		if (strings != null) {
			String csv = "";
			for (String string : strings) {
				if (csv.length() > 0) {
					csv += ", ";
				}

				csv += string;
			}

			return csv;
		} else {
			return null;
		}
	}

	public static String toCSV(List<?> list) {
		if (list != null) {
			String csv = "";
			for (Object obj : list) {
				if (csv.length() > 0) {
					csv += ", ";
				}

				csv += obj.toString();
			}

			return csv;
		} else {
			return null;
		}
	}

	public static String toString(String[] strings, String seperator) {
		if (strings != null) {
			String str = "";
			for (String string : strings) {
				if (str.length() > 0) {
					str += seperator;
				}

				str += string;
			}

			return str;
		} else {
			return null;
		}
	}

	public static String getRandomString(int length) {
		String chars = "0123456789abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random r = new Random();

		char[] buf = new char[length];

		for (int i = 0; i < buf.length; i++) {
			buf[i] = chars.charAt(r.nextInt(chars.length()));
		}

		return new String(buf);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDateTimeInUTC(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		return format.format(date);
	}

	public static Date getDateTimeInUTC(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		return format.parse(date);
	}

	// public static long getDateTimeLongInUTC(String date) throws
	// ParseException{
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// format.setTimeZone(TimeZone.getTimeZone("GMT"));
	// return format.parse(date).getTime();
	// }

	public static boolean startsWith(String str, String prefix,
			boolean ignoreCase) {
		if (str == null || prefix == null) {
			return (str == null && prefix == null);
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
	}

	public static String makeNullIfEmpty(String value) {
		if (value != null && value.trim().length() == 0) {
			return null;
		} else {
			return value;
		}
	}

	public static String makeEmptyIfNull(String value) {
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}

	public static boolean isEqualLong(Long long1, Long long2) {
		if (long1 == null) {
			if (long2 == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (long2 == null) {
				return false;
			} else {
				return long1.equals(long2);
			}
		}
	}

	public static boolean isLength(long value, int length) {
		return String.valueOf(value).length() == length ? true : false;
	}

	public static boolean isLong(String value) {
		try {
			Long.parseLong(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isEmptyString(String string1) {
		if (string1 != null && string1.length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isEqualString(String string1, String string2) {
		if (string1 == null) {
			if (string2 == null) {
				return true;
			} else {
				return false;
			}
		} else {
			return string1.equals(string2);
		}
	}

	public static boolean isEqualStringIgnoreCase(String string1, String string2) {
		if (string1 == null) {
			if (string2 == null) {
				return true;
			} else {
				return false;
			}
		} else {
			return string1.equalsIgnoreCase(string2);
		}
	}

	public static String replaceAllNewLineChar(String input) {
		String output = replaceAllSkipNull(input, "\n", " ");
		output = replaceAllSkipNull(output, "\r", " ");
		return output;
	}

	public static String replaceAllSkipNull(String input, String regex,
			String replacement) {
		if (input != null) {
			return input.replaceAll(regex, replacement);
		} else {
			return null;
		}
	}

}
