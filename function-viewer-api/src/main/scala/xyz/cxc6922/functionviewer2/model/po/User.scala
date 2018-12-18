package xyz.cxc6922.functionviewer2.model.po

import com.fasterxml.jackson.annotation.JsonIgnore

import scala.beans.BeanProperty


class User {
  @BeanProperty var name: String = "admin"

  @JsonIgnore
  @BeanProperty var password: String = ""

  @BeanProperty var roles: List[Role] = List(new Role())
}
