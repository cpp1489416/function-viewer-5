package xyz.cxc6922.functionviewer2.config.shiro

import java.io.IOException

import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.core.{JsonGenerator, JsonProcessingException}
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.cache.support.NullValue
import org.springframework.data.redis.serializer.{GenericJackson2JsonRedisSerializer, SerializationException, SerializationUtils}
import org.springframework.util.{Assert, StringUtils}

class NoExceptionJackson2JsonRedisSerializer(val mapper: ObjectMapper)
  extends GenericJackson2JsonRedisSerializer(mapper) {

  def this() {
    this(new ObjectMapper())
    mapper.registerModule(new SimpleModule().addSerializer(new NullValueSerializer(null)))
    mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // this is important
    // mapper.configure(SerializationFeature)
   // mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true)
    mapper.configure(MapperFeature.USE_GETTERS_AS_SETTERS, false)

  }

  @SerialVersionUID(1999052150548658808L)
  private class NullValueSerializer(var classIdentifier: String)
    extends StdSerializer[NullValue](classOf[NullValue]) {

    classIdentifier = {
      if (StringUtils.hasText(classIdentifier)) {
        classIdentifier
      } else {
        "@class"
      }
    }

    @throws[IOException]
    override def serialize(value: NullValue, jgen: JsonGenerator, provider: SerializerProvider): Unit = {
      jgen.writeStartObject()
      jgen.writeStringField(classIdentifier, classOf[NullValue].getName)
      jgen.writeEndObject()
    }
  }

}
