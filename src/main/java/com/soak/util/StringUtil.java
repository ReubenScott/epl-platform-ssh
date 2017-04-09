package com.soak.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This class provides some methods for dynamically invoking methods in objects,
 * and some string manipulation methods used by torque. The string methods will
 * soon be moved into the turbine string utilities class.
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id: StringUtil.java,v 1.1 2008/12/19 03:08:24 cyg Exp $
 */
public class StringUtil {

	private static final String EOL = System.getProperty("line.separator");

	@SuppressWarnings("unchecked")
	public String concat(List list) {
		StringBuffer sb = new StringBuffer();
		int size = list.size();

		for (int i = 0; i < size; i++) {
			sb.append(list.get(i).toString());
		}
		return sb.toString();
	}

	static public String getPackageAsPath(String pckge) {
		return pckge.replace('.', File.separator.charAt(0)) + File.separator;
	}

	static public String removeAndHump(String data) {
		return removeAndHump(data, "_");
	}

	static public String removeAndHump(String data, String replaceThis) {
		String temp = null;
		StringBuffer out = new StringBuffer();
		temp = data;

		StringTokenizer st = new StringTokenizer(temp, replaceThis);

		while (st.hasMoreTokens()) {
			String element = (String) st.nextElement();
			out.append(capitalizeFirstLetter(element));
		}

		return out.toString();
	}


	static public String firstLetterCaps(String data) {
		String firstLetter = data.substring(0, 1).toUpperCase();
		String restLetters = data.substring(1).toLowerCase();
		return firstLetter + restLetters;
	}

	static public String capitalizeFirstLetter(String data) {
		String firstLetter = data.substring(0, 1).toUpperCase();
		String restLetters = data.substring(1);
		return firstLetter + restLetters;
	}


	@SuppressWarnings("unchecked")
	public static String[] split(String line, String delim) {
		List list = new ArrayList();
		StringTokenizer t = new StringTokenizer(line, delim);
		while (t.hasMoreTokens()) {
			list.add(t.nextToken());
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String chop(String s, int i) {
		return chop(s, i, EOL);
	}

	/**
	 * Chop i characters off the end of a string. A 2 character EOL will count
	 * as 1 character.
	 * 
	 * @param string
	 *            String to chop.
	 * @param i
	 *            Number of characters to chop.
	 * @param eol
	 *            A String representing the EOL (end of line).
	 * @return String with processed answer.
	 */
	public static String chop(String s, int i, String eol) {
		if (i == 0 || s == null || eol == null) {
			return s;
		}
		int length = s.length();
		/*
		 * if it is a 2 char EOL and the string ends with it, nip it off. The
		 * EOL in this case is treated like 1 character
		 */
		if (eol.length() == 2 && s.endsWith(eol)) {
			length -= 2;
			i -= 1;
		}

		if (i > 0) {
			length -= i;
		}

		if (length < 0) {
			length = 0;
		}

		return s.substring(0, length);
	}

	@SuppressWarnings("unchecked")
	public static StringBuffer stringSubstitution(String argStr, Hashtable vars) {
		return stringSubstitution(argStr, (Map) vars);
	}

	@SuppressWarnings("unchecked")
	public static StringBuffer stringSubstitution(String argStr, Map vars) {
		StringBuffer argBuf = new StringBuffer();

		for (int cIdx = 0; cIdx < argStr.length();) {
			char ch = argStr.charAt(cIdx);

			switch (ch) {
			case '$':
				StringBuffer nameBuf = new StringBuffer();
				for (++cIdx; cIdx < argStr.length(); ++cIdx) {
					ch = argStr.charAt(cIdx);
					if (ch == '_' || Character.isLetterOrDigit(ch))
						nameBuf.append(ch);
					else
						break;
				}

				if (nameBuf.length() > 0) {
					String value = (String) vars.get(nameBuf.toString());

					if (value != null) {
						argBuf.append(value);
					}
				}
				break;

			default:
				argBuf.append(ch);
				++cIdx;
				break;
			}
		}

		return argBuf;
	}

	public static String fileContentsToString(String file) {
		String contents = "";

		File f = new File(file);

		if (f.exists()) {
			try {
				FileReader fr = new FileReader(f);
				char[] template = new char[(int) f.length()];
				fr.read(template);
				contents = new String(template);
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}

		return contents;
	}

	public static String collapseNewlines(String argStr) {
		char last = argStr.charAt(0);
		StringBuffer argBuf = new StringBuffer();

		for (int cIdx = 0; cIdx < argStr.length(); cIdx++) {
			char ch = argStr.charAt(cIdx);
			if (ch != '\n' || last != '\n') {
				argBuf.append(ch);
				last = ch;
			}
		}

		return argBuf.toString();
	}

	public static String collapseSpaces(String argStr) {
		char last = argStr.charAt(0);
		StringBuffer argBuf = new StringBuffer();

		for (int cIdx = 0; cIdx < argStr.length(); cIdx++) {
			char ch = argStr.charAt(cIdx);
			if (ch != ' ' || last != ' ') {
				argBuf.append(ch);
				last = ch;
			}
		}

		return argBuf.toString();
	}

	public static final String sub(String line, String oldString,
			String newString) {
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	public static final String stackTrace(Throwable e) {
		String foo = null;
		try {
			
			ByteArrayOutputStream ostr = new ByteArrayOutputStream();
			e.printStackTrace(new PrintWriter(ostr, true));
			foo = ostr.toString();
		} catch (Exception f) {
			f.printStackTrace();
		}
		return foo;
	}

	public String select(boolean state, String trueString, String falseString) {
		if (state) {
			return trueString;
		} else {
			return falseString;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean allEmpty(List list) {
		int size = list.size();

		for (int i = 0; i < size; i++) {
			if (list.get(i) != null && list.get(i).toString().length() > 0) {
				return false;
			}
		}
		return true;
	}

	static public String getTextBetweenTags(String src, String bTag, String eTag) {
		try {
			if ((src == null) || (src.trim().length() == 0) || bTag == null	|| eTag.trim().length() == 0)
				return "";
			int b = src.indexOf(bTag);
			int e = src.indexOf(eTag,b+bTag.length());
			if (b == -1){
				return "";
			}
			if (e==-1){
				e=src.length();
			}
			return src.substring(b + bTag.length(), e);
		} catch (Exception e) {
			return "";
		}
	}
 
	public static String replaceNull(String source) {
		return source == null ? "" : source;
	}

	public static int getFirstLetterIndex(String data) throws Exception {
		char[] chars = data.toCharArray();
		int size = chars.length;
		for (int i = 0; i < size; i++) {
			if (((chars[i] >= 'a') && (chars[i] <= 'z'))
					|| ((chars[i] >= 'A') && (chars[i] <= 'Z'))) {
				return i;
			}
		}
		throw new Exception("no letter in string:" + data);
	}

	public static String getXMLText(String data) {
		return data.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	public static boolean isStartWithFromLetter(String source, String data)	throws Exception {
		int i = getFirstLetterIndex(source);
		String s = source.substring(i, data.length());
		return s.equals(data);

	}

	public static boolean isStartWithFromLetterIgnoreCase(String source, String data) throws Exception {
		int i = getFirstLetterIndex(source);
		String s = source.substring(i, data.length());
		return s.equalsIgnoreCase(data);
	}
	
	public static  String getMessShort(String mess,int messSize){
		if (mess==null||mess.trim().length()==0){
			return "";
		}
		StringBuffer strBuf=new StringBuffer();
		if (mess.length()>messSize){
			strBuf.append(mess.substring(0, messSize)).append("...");
		}else{
			strBuf.append(mess);
		}
		return strBuf.toString();
	}
	
	public static String getMessWithBr(String mess,int splitSize){
		if (mess==null||mess.trim().length()==0){
			return "";
		}
		StringBuffer strBuf=new StringBuffer();
		if (mess.length()>splitSize){
			int length=mess.length();
			int tmp=0;
			while(length>splitSize){
				strBuf.append(mess.substring(tmp, tmp+splitSize)).append("<br/>");
				length=length-splitSize;
				tmp=tmp+splitSize;
			}
			strBuf.append(mess.substring(tmp));
		}else{
			strBuf.append(mess);
		}
		return strBuf.toString();
	}
}
