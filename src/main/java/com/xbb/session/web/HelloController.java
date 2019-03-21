package com.xbb.session.web;

import com.jfinal.core.Controller;

public class HelloController extends Controller {

	public void index() {
		renderText("hello jfinal");
	}
	
	public void addSession() {
		setSessionAttr("a", "a");
		getSession().setMaxInactiveInterval(120 * 1000);
		System.out.println(getSession().getClass());
		renderText("a session value is set");
	}
	
	public void sessionValue() {
		String sessionValue = getSessionAttr("a");
		renderText("session value is :" + sessionValue);
	}
	
}
