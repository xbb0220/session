package com.xbb.session.config.shiro.redis;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.redis.Redis;

public class RedisSessionDAO extends EnterpriseCacheSessionDAO {

	private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
	/**
	 * The Redis key prefix for the sessions
	 */
	private String keyPrefix = "shiro_redis_session:";

	public void setSessionAttr(Serializable sessionId, String sessionKey, Object value) {
		Session doReadSession = doReadSession(sessionId);
		if (null != doReadSession) {
			doReadSession.setAttribute(sessionKey, value);
			update(doReadSession);
		}
	}

	public void deleteById(Serializable id) {
		Redis.use().del(getByteKey(id));
	}

	@Override
	public void update(Session session) throws UnknownSessionException {
		this.saveSession(session);
	}

	/**
	 * save session
	 * 
	 * @param session
	 * @throws UnknownSessionException
	 */
	private void saveSession(Session session) throws UnknownSessionException {
		if (session == null || session.getId() == null) {
			logger.error("session or session id is null");
			return;
		}
		Redis.use().setex(getByteKey(session.getId()), (int) session.getTimeout(), session);
	}

	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			logger.error("session or session id is null");
			return;
		}
		Redis.use().del(getByteKey(session.getId()));
	}

	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet<Session>();
		Set<String> keys = Redis.use().keys(this.keyPrefix + "*");
		if (keys != null && keys.size() > 0) {
			for (String key : keys) {
				Session s = Redis.use().get(key);
				;
				sessions.add(s);
			}
		}
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.saveSession(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			logger.error("session id is null");
			return null;
		}
		Session s = Redis.use().get(getByteKey(sessionId));
		return s;

	}

	private String getByteKey(Serializable sessionId) {
		String preKey = this.keyPrefix + sessionId;
		return preKey;
	}

	@Override
	protected Serializable generateSessionId(Session session) {
		Serializable sessionId = super.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.saveSession(session);
		return sessionId;
	}

	@Override
	public Session readSession(Serializable sessionId) throws UnknownSessionException {
		return doReadSession(sessionId);
	}

}