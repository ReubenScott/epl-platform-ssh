package com.soak.util;

import java.util.List;

import org.dom4j.Element;

@SuppressWarnings("unchecked")
public final class TreeUtil {

	public static void resetRoot(Element root) {
		List<Element> elements = root.elements();

		for (Element child : elements) {
			String parentStr = child.attributeValue("parent_id");

			if (parentStr != null) {
				String[] parent = parentStr.split(",");

				for (String parentId : parent) {
					if (parentId != null && !parentId.equals("")) {
						Element parentEle = root.elementByID(parentId);
						// if(parentEle!=null){
						parentEle.add(child.createCopy());
						// }
					}
				}
				root.remove(child);
			}
		}
	}

	public static void createHtml(Element root, StringBuffer html, String id) {
		List<Element> elements = root.elements();
		if (elements.size() > 0) {
			if (root.getName() == "root") {
				html.append("<ul id=");
				html.append(id);
				html.append(" class=filetree>");
			} else
				html.append("<ul>");
			for (Element child : elements) {

				if (child.elements().size() > 0) {
					html.append("<li><span class=folder>");
				} else {
					html.append("<li><span class=file>");
				}
				html.append("<a id=");
				html.append(child.attributeValue("ID"));
				String valid = child.attributeValue("VALID");
				if (valid != null && !valid.equals("")) {
					html.append(" valid=");
					html.append(valid);
				}
				html.append(">");
				html.append(child.getText());
				html.append("</a>");
				html.append("</span>");
				createHtml(child, html, id);
				html.append("</li>");
			}
			html.append("</ul>");
		}
	}

	public static void createOrgHtml(Element root, StringBuffer html, int level) {
		level++;
		List<Element> elements = root.elements();
		if (elements.size() > 0) {

			for (Element child : elements) {
				String oldId = child.attributeValue("ID");

				html.append("<tr id=\"");
				html.append(oldId);
				html.append("\" parentId=\"");
				html.append(child.getParent().attributeValue("ID"));
				html.append("\"");
				html.append("><td style=\"text-align:left\">");
				for (int i = 0; i < level; i++)
					html.append("&nbsp;&nbsp;&nbsp;&nbsp;");
				if (child.elements().isEmpty()) {
					html.append(child.attributeValue("name"));
					html.append("</td><td>");
				} else {
					html.append("<a href=\"javascript:controllChilds(\'");
					html.append(child.attributeValue("ID"));
					html.append("\')\">");
					html.append(child.attributeValue("name"));
					html.append("</a></td><td>");
				}
				String type = child.attributeValue("type");
				if (type.equals("org"))
					html.append("组织机构</td><td>");
				else if (type.equals("user"))
					html.append("人员</td><td>");
				String leader = child.attributeValue("leader");
				html.append(leader == null ? "" : leader);
				html.append("</td><td>");
				String remark = child.attributeValue("remark");
				html.append(remark == null ? "" : remark);
				String id = child.attributeValue("ID");
				id = id.substring(3, id.length());

				if (type.equals("org")) {
					html.append("</td><td><a href=\"../../System/OrgManage/edit?id=");
					html.append(id);
					html.append("\"><img src=\"../../img/edit.gif\"/></a><a href=\"#\" onClick=\"submitSingleDelete(");
				} else if (type.equals("user")) {
					html.append("</td><td><a href=\"../../System/UserManage/edit?id=");
					html.append(id);
					html.append("\"><img src=\"../../img/edit.gif\"/></a><a href=\"#\" onClick=\"submitSingleDelete2(");
				}
				html.append(id);
				html.append(", this)\"><img src=\"../../img/del.gif\"/></a></td></tr>");

				createOrgHtml(child, html, level);
			}

		}
	}

	public static void createDocTypeHtml(Element root, StringBuffer html,
			int level) {
		level++;
		List<Element> elements = root.elements();
		if (elements.size() > 0) {

			for (Element child : elements) {
				String oldId = child.attributeValue("ID");
				String id = oldId.substring(3, oldId.length());

				html.append("<tr id=\"");
				html.append(oldId);
				html.append("\" parentId=\"");
				html.append(child.getParent().attributeValue("ID"));
				html.append("\"");
				html.append("><td align=\"center\"><input type=\"checkbox\" name=\"idList\" value=\"");
				html.append(id);
				html.append("\" attach=\"selectAll\" /></td><td style=\"text-align:left\">");
				for (int i = 0; i < level; i++)
					html.append("&nbsp;&nbsp;&nbsp;&nbsp;");
				if (child.elements().isEmpty()) {
					html.append(child.attributeValue("name"));
					html.append("</td><td>");
				} else {
					html.append("<a href=\"javascript:controllChilds(\'");
					html.append(child.attributeValue("ID"));
					html.append("\')\">");
					html.append(child.attributeValue("name"));
					html.append("</a></td><td>");
				}
				String type = child.attributeValue("type");
				html.append(type == null ? "" : type);
				html.append("</td><td>");
				String templateFile = child.attributeValue("templateFile");
				html.append(templateFile == null ? "" : templateFile);

				html.append("</td><td><a href=\"../../Flow/DocTypeManage/edit?id=");
				html.append(id);
				html.append("\"><img src=\"../../img/edit.gif\"/></a><a href=\"#\" onClick=\"submitSingleDelete(");

				html.append(id);
				html.append(", this)\"><img src=\"../../img/del.gif\"/></a></td></tr>");

				createDocTypeHtml(child, html, level);
			}

		}

	}

	public static String userHomeHtml(Element element) {
		org.dom4j.Document doc = element.getDocument();
		StringBuffer html = new StringBuffer("");
		try {
			Element rootElement = doc.getRootElement();
			List list = rootElement.elements();
			for (int i = 0; i < list.size(); i++) {
				Element childElement = (Element) list.get(i);
				html.append("<tr><td align=\"middle\">"
						+ "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"Menu\">"
						+ "<tr>" + "<td>" + "<div id=\"");
				html.append("pNode" + i + "\"");
				html.append("onclick=\"showList(this.id);\"  style=\"cursor:pointer;\">");

				html.append("<img src='../images/2.png'  width=\"24\" height=\"24\" border=\"0\" />");

				html.append("<span>");
				if (childElement.elements().size() == 0) {
					/*
					 * 添加 一级超链接
					 */
					html.append("<a href=");
					html.append(childElement.attributeValue("link")); // 超链接 URL
																		// 地址
					html.append(" target=\"_top\" class=\"Menu\">");
					html.append(childElement.getText());
					html.append("</a>");
				} else {
					html.append(childElement.getText());
				}
				html.append("</span>");
				html.append("</div></td></tr></table></td></tr>");

				html.append("<tr><td align=\"middle\"><div style=\"display:none;\"");
				html.append(" id=\"pNode" + i + "_ljf\">");
				List childListOne = childElement.elements();
				if (childListOne.size() != 0) {

				}
				for (int j = 0; j < childListOne.size(); j++) {
					Element child = (Element) childListOne.get(j);

					html.append("<div  onclick=\"showList(this.id);\" class=\"MenuMenu\"");
					html.append(" id=\"pNode" + i + "_sub" + j + "\">");

					/**
					 * 添加二级菜单 超链接
					 */
					if (child.elements().size() == 0) {
						/*
						 * 添加 二级超链接
						 */
						html.append("<a href=");
						html.append(child.attributeValue("link")); // 超链接 URL 地址
						html.append(" target=\"mainFrame\" class=\"MenuMenu\">");
						html.append(child.getText());
						html.append("</a>");
						html.append("</div>");
					} else {
						/**
						 * 如果 有子菜单 不添加 超链
						 */
						html.append(child.getText());
						html.append("</div>");
					}

					List childListTwo = child.elements();
					if (childListOne.size() != 0) {
						html.append("<div style=\"display:none;\"");
						html.append("id=\"pNode" + i + "_sub" + j + "_ljf\">");
					}
					for (int k = 0; k < childListTwo.size(); k++) {
						Element childOne = (Element) childListTwo.get(k);
						html.append("<div id=\"pNode" + i + "_sub" + j + "_s"
								+ k + "\">");
						/**
						 * 只做到三级菜单没有判断 是否有 第四级菜单
						 */

						html.append("<a href=");
						html.append(childOne.attributeValue("link")); // 超链接 URL
																		// 地址
						html.append(" target=\"mainFrame\" class=\"MenuMenu_list\">·");
						html.append(childOne.getText());
						html.append("</a></div>");
					}
					html.append("</div>");
				}
				html.append("</td></tr>");
			}
			html.append("</table></td>");
		} catch (Exception ex) {

		}

		return html.toString();
	}

	public static String shortcuts(Element element) {
		org.dom4j.Document doc = element.getDocument();
		StringBuffer html = new StringBuffer("");
		try {
			Element rootElement = doc.getRootElement();
			List list = rootElement.elements();
			for (int i = 0; i < list.size(); i++) {
				Element childElement = (Element) list.get(i);
				List childListOne = childElement.elements();

				if (childListOne.size() != 0) {
				}

				for (int j = 0; j < childListOne.size(); j++) {
					Element child = (Element) childListOne.get(j);
					// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
					html.append("<tr><td align=\"middle\">"
							+ "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"Menu\">"
							+ "<tr>" + "<td>" + "<div id=\"");
					html.append("pNode" + j + "\"");
					html.append("onclick=\"showList(this.id);\"  style=\"cursor:pointer;\">");
					html.append("<img src='../images/2.png'  width=\"24\" height=\"24\" border=\"0\" />");
					html.append("<span>");

					if (child.elements().size() == 0) {
						/*
						 * 添加 一级超链接
						 */
						html.append("<a href=");
						html.append(child.attributeValue("link")); // 超链接	
						html.append(" target=\"mainFrame\" class=\"Menu\">");
						html.append(child.getText());
						html.append("</a>");
					} else {
						html.append(child.getText());
					}

					html.append("</span>");
					html.append("</div></td></tr></table></td></tr>");

					html.append("<tr><td align=\"middle\"><div style=\"display:none;\"");
					html.append(" id=\"pNode" + j + "_ljf\">");

					List childListnew = child.elements();
					if (childListnew.size() != 0) {

					}

					// +++++++++++++++++++++++++++++++++++++++++++++
					for (int k = 0; k < childListnew.size(); k++) {
						Element childK = (Element) childListnew.get(k);

						html.append("<div  onclick=\"showList(this.id);\" class=\"MenuMenu\"");
						html.append(" id=\"pNode" + j + "_sub" + k + "\">");

						/**
						 * 添加二级菜单 超链接
						 */
						if (childK.elements().size() == 0) {
							/*
							 * 添加 二级超链接
							 */
							html.append("<a href=");
							html.append(childK.attributeValue("link")); // 超链接
																		// URL
																		// 地址
							html.append(" target=\"mainFrame\" class=\"MenuMenu\">");
							html.append(childK.getText());
							html.append("</a>");
							html.append("</div>");
						} else {
							/**
							 * 如果 有子菜单 不添加 超链
							 */
							html.append(childK.getText());
							html.append("</div>");
						}

						List childListTwo = childK.elements();
						if (childListOne.size() != 0) {
							html.append("<div style=\"display:none;\"");
							html.append("id=\"pNode" + j + "_sub" + k
									+ "_ljf\">");
						}

						html.append("</div>");
					}
					html.append("</td></tr>");
				}
			}
			html.append("</table></td>");
		} catch (Exception ex) {

		}

		return html.toString();
	}
}
