package utils

import io.jaegertracing.internal.JaegerSpanContext

import java.math.BigInteger

object SpanContextUtils {
  def getSpanContext(uberTraceId: String) = {
    val parts: Array[String] = uberTraceId.split(":")
    val traceIdString: String = parts.headOption.getOrElse("")
    val spanIdString: String = parts.lift(1).getOrElse("")

    val traceId = new BigInteger(traceIdString, 16).longValue()
    val spanId = new BigInteger(spanIdString, 16).longValue()

    val spanContext = new JaegerSpanContext(0, traceId, spanId, traceId, 1);
    spanContext
  }
}
