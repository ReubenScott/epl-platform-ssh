package com.soak.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PathUtil {
	static String classRootPath = "";
	static protected final Log log = LogFactory.getLog(PathUtil.class);

	public static String getClassRootPath(Object obj) {
		try {
			return URLDecoder.decode(obj.getClass().getResource("/").getPath(),
					"GBK");
		} catch (UnsupportedEncodingException e) {
			log.error("PathUtil getClassRootPath Object=" + obj.toString()
					+ "ERROR:" + e.getMessage());
		}
		return classRootPath;
	}

	public static String getClassRootPath(Class clasz) {
		try {
			return URLDecoder.decode(clasz.getResource("/").getPath(), "GBK");
		} catch (UnsupportedEncodingException e) {
			log.error("PathUtil getClassRootPath Class=" + clasz.toString()
					+ "ERROR:" + e.getMessage());
		}
		return classRootPath;
	}

	public static void initDefaultClassRootPath(String rootPath) {
		classRootPath = rootPath;
	}

	/**
	 * Return a context-relative path, beginning with a "/", that represents the
	 * canonical version of the specified path after ".." and "." elements are
	 * resolved out. If the specified path attempts to go outside the boundaries
	 * of the current context (i.e. too many ".." path elements are present),
	 * return <code>null</code> instead.
	 * 
	 * @param path
	 *            Path to be normalized
	 * @return String normalized path
	 */
	public static final String normalizePath(String path) {
		// Normalize the slashes and add leading slash if necessary
		String normalized = path;
		if (normalized.indexOf('\\') >= 0) {
			normalized = normalized.replace('\\', '/');
		}

		if (!normalized.startsWith("/")) {
			normalized = "/" + normalized;
		}

		// Resolve occurrences of "//" in the normalized path
		while (true) {
			int index = normalized.indexOf("//");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index)
					+ normalized.substring(index + 1);
		}

		// Resolve occurrences of "%20" in the normalized path
		while (true) {
			int index = normalized.indexOf("%20");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + " "
					+ normalized.substring(index + 3);
		}

		// Resolve occurrences of "/./" in the normalized path
		while (true) {
			int index = normalized.indexOf("/./");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index)
					+ normalized.substring(index + 2);
		}

		// Resolve occurrences of "/../" in the normalized path
		while (true) {
			int index = normalized.indexOf("/../");
			if (index < 0)
				break;
			if (index == 0)
				return (null); // Trying to go outside our context
			int index2 = normalized.lastIndexOf('/', index - 1);
			normalized = normalized.substring(0, index2)
					+ normalized.substring(index + 3);
		}

		// Return the normalized path that we have completed
		return (normalized);
	}

	public static void main(String[] args) {
		System.out.println(normalizePath("\\d//aa.xml\\aaaa.xml"));
	}
}
