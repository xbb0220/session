
package com.xbb.session.config.shiro;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;

public class MySessionManager extends DefaultWebSessionManager {

	public MySessionManager() {
		super();
		setGlobalSessionTimeout(PropKit.use("config.properties").getLong("session.timeout") * 1000);
		System.out.println(getSessionValidationInterval());
	}

	@Override
	public Serializable getSessionId(SessionKey key) {
		ServletRequest request = WebUtils.getRequest(key);
		HttpServletRequest req = (HttpServletRequest) request;
		String sessionId = req.getHeader("session");
		if (StrKit.isBlank(sessionId)) {
			sessionId = req.getParameter("session");
		}
		if (StrKit.isBlank(sessionId)) {
			sessionId = (String) super.getSessionId(key);
		}
		return sessionId;
	}

}
