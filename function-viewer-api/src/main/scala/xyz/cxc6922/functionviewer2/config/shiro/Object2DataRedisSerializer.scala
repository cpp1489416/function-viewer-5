package xyz.cxc6922.functionviewer2.config.shiro

import org.springframework.data.redis.serializer.{RedisSerializer, SerializationException}
import org.springframework.core.serializer.support.DeserializingConverter
import org.springframework.core.serializer.support.SerializingConverter

class Object2DataRedisSerializer extends RedisSerializer[Object] {
  private val serializer = new SerializingConverter
  private val deserializer = new DeserializingConverter
  private val EMPTY_ARRAY = new Array[Byte](0)

  private def isEmpty(data: Array[Byte]): Boolean = {
    data == null || data.length == 0
  }

  override def serialize(t: Object): Array[Byte] = {
    if (t == null) return EMPTY_ARRAY
    try
      serializer.convert(t)
    catch {
      case _: Exception =>
        EMPTY_ARRAY
    }
  }

  override def deserialize(bytes: Array[Byte]): AnyRef = {
    if (isEmpty(bytes)) return null
    try
      deserializer.convert(bytes)
    catch {
      case e: Exception =>
        throw new SerializationException("Cannot deserialize", e)
    }
  }


}
