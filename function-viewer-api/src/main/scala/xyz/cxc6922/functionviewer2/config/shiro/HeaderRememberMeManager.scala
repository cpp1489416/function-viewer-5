package xyz.cxc6922.functionviewer2.config.shiro

import org.apache.shiro.codec.Base64
import org.apache.shiro.mgt.AbstractRememberMeManager
import org.apache.shiro.subject.{Subject, SubjectContext}
import org.apache.shiro.web.subject.WebSubjectContext
import org.apache.shiro.web.util.WebUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.cxc6922.functionviewer2.config.shiro.HeaderRememberMeManager

class HeaderRememberMeManager extends AbstractRememberMeManager {
  private val log = LoggerFactory.getLogger(classOf[HeaderRememberMeManager])

  private val REMEMBER_ME_HEADER_NAME: String = "Remember-Me"

  override def forgetIdentity(subject: Subject): Unit = ???

  override def rememberSerializedIdentity(subject: Subject, serialized: Array[Byte]): Unit = {
    if (!WebUtils.isHttp(subject)) {
      log.info("Subject is not HTTP when remember me")
    } else {
      val response = WebUtils.getHttpResponse(subject)
      val base64 = Base64.encodeToString(serialized)
      response.setHeader(REMEMBER_ME_HEADER_NAME, base64)
    }
  }

  override def getRememberedSerializedIdentity(subjectContext: SubjectContext): Array[Byte] = {
    null
  }

  override def forgetIdentity(subjectContext: SubjectContext): Unit = ???
}
