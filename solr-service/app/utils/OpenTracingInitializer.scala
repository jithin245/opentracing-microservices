package utils

import io.jaegertracing.Configuration
import io.jaegertracing.Configuration.SamplerConfiguration
import io.jaegertracing.internal.JaegerTracer
import io.jaegertracing.internal.samplers.ConstSampler

object OpenTracingInitializer {

  def getJaegerTracer: JaegerTracer = {
    val configuration = new Configuration("solr-service")
      .withSampler(new SamplerConfiguration().withType(ConstSampler.TYPE).withParam(1))
      .withReporter(
        new Configuration.ReporterConfiguration()
          .withLogSpans(true)
          .withSender(
            new Configuration.SenderConfiguration()
              .withEndpoint("http://jaeger:14268/api/traces")
          )
      )
    configuration.getTracer
  }

}
