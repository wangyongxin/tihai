package com.tihai.common;

import java.net.URLEncoder;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.web.util.HtmlUtils;

public class CodeTransferTest {

	public static void main(String[] args) throws URIException {
		// TODO Auto-generated method stub
		System.out.println(URLEncoder.encode("&"));//%26
		System.out.println(URIUtil.encodePath("&"));//&
		System.out.println(HtmlUtils.htmlEscape("&"));//&amp;
	}

}
