package xyz.cxc6922.functionviewer2.config.shiro

import java.io.Serializable

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.servlet.{ServletRequest, ServletResponse}
import org.apache.shiro.session.mgt.{DefaultSessionManager, DelegatingSession, SessionContext, SessionKey}
import org.apache.shiro.session.{ExpiredSessionException, InvalidSessionException, Session}
import org.apache.shiro.web.servlet.ShiroHttpServletRequest
import org.apache.shiro.web.session.mgt.{WebSessionKey, WebSessionManager}
import org.apache.shiro.web.util.WebUtils
import org.slf4j.LoggerFactory
import org.springframework.util.StringUtils

class HeaderSessionManager extends DefaultSessionManager with WebSessionManager {
  private val log = LoggerFactory.getLogger(classOf[HeaderSessionManager])

  private val AUTH_TOKEN: String = "X-Auth-Token"

  private def getSessionIdOfHeader(request: ServletRequest, response: ServletResponse): String = {
    val request1 = request.asInstanceOf[HttpServletRequest]
    val response1 = response.asInstanceOf[HttpServletResponse]
    val id = request1.getHeader(AUTH_TOKEN)
    if (!StringUtils.isEmpty(id)) {
      response1.setHeader(AUTH_TOKEN, id)
      id
    } else {
      null
    }
  }

  private def storeSessionId(sessionId: Serializable, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    response.setHeader(AUTH_TOKEN, sessionId.toString)
  }

  private def getReferencedSessionId(request: ServletRequest, response: ServletResponse): Serializable = {
    val id = getSessionIdOfHeader(request, response)

    if (id != null) {
      request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header")
      request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id)
      request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, true)
    }
    //不会把sessionId放在URL后
    request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, false)
    id
  }

  private def removeSessionIdHeader(request: HttpServletRequest, response: HttpServletResponse): Unit = {
    response.setHeader(AUTH_TOKEN, "deleteMe")
  }

  private def onInvalidation(key: SessionKey): Unit = {
    val request = WebUtils.getRequest(key)
    if (request != null) {
      request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID)
    }

    if (WebUtils.isHttp(key)) {
      log.debug("Referenced session was invalid.  Removing session ID header.")
      removeSessionIdHeader(WebUtils.getHttpRequest(key), WebUtils.getHttpResponse(key))
    } else {
      log.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response pair. " +
        "Session ID cookie will not be removed due to invalidated session.")
    }
  }

  private def getSessionId(request: ServletRequest, response: ServletResponse): Serializable = {
    getReferencedSessionId(request, response)
  }

  override def createExposedSession(session: Session, context: SessionContext): Session = {
    if (!WebUtils.isWeb(context)) {
      super.createExposedSession(session, context) // 无法操作
    } else {
      val request = WebUtils.getRequest(context)
      val response = WebUtils.getResponse(context)
      val sessionKey = new WebSessionKey(session.getId, request, response)
      new DelegatingSession(this, sessionKey)
    }
  }

  override def createExposedSession(session: Session, key: SessionKey): Session = {
    if (!WebUtils.isWeb(key)) {
      super.createExposedSession(session, key) // 无法操作
    } else {
      val request = WebUtils.getRequest(key)
      val response = WebUtils.getResponse(key)
      val sessionKey = new WebSessionKey(session.getId, request, response)
      new DelegatingSession(this, sessionKey)
    }
  }

  override def onStart(session: Session, context: SessionContext): Unit = {
    super.onStart(session, context)

    if (!WebUtils.isHttp(context)) {
      log.debug("SessionContext argument is not HTTP compatible or does not have an HTTP request/response " +
        "pair. No session ID cookie will be set.")
    } else {
      val request = WebUtils.getHttpRequest(context)
      val response = WebUtils.getHttpResponse(context)
      val sessionId = session.getId
      storeSessionId(sessionId, request, response)
      request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE)
      request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, true)
    }
  }

  override def getSessionId(key: SessionKey): Serializable = {
    var id = super.getSessionId(key)
    if (id == null && WebUtils.isWeb(key)) {
      val request = WebUtils.getRequest(key)
      val response = WebUtils.getResponse(key)
      id = getSessionId(request, response)
    }
    id
  }

  override def onExpiration(s: Session, ese: ExpiredSessionException, key: SessionKey): Unit = {
    super.onExpiration(s, ese, key)
    onInvalidation(key)
  }

  override def onInvalidation(session: Session, ise: InvalidSessionException, key: SessionKey): Unit = {
    super.onInvalidation(session, ise, key)
    onInvalidation(key)
  }

  override def onStop(session: Session, key: SessionKey): Unit = {
    super.onStop(session, key)
    if (WebUtils.isHttp(key)) {
      val request = WebUtils.getHttpRequest(key)
      val response = WebUtils.getHttpResponse(key)
      log.debug("Session has been stopped (subject logout or explicit stop).  Removing session ID cookie.")
      removeSessionIdHeader(request, response)
    } else {
      log.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response " +
        "pair. Session ID cookie will not be removed due to stopped session.")
    }
  }

  override def isServletContainerSessions: Boolean = false
}
