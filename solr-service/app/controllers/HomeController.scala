package controllers

import io.jaegertracing.internal.JaegerSpanContext

import javax.inject._
import play.api.mvc._
import utils.OpenTracingInitializer
import utils.SpanContextUtils.getSpanContext


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {


  def helloWorld(): Action[AnyContent] = Action { implicit request =>
    val tracer = OpenTracingInitializer.getJaegerTracer

    val uberTraceId = request.headers.get("uber-trace-id").getOrElse("")
    println("uberTraceId " + uberTraceId)

    if (uberTraceId.nonEmpty) {
      val spanContext: JaegerSpanContext = getSpanContext(uberTraceId)
      val span = tracer.buildSpan("inside scala service 1")
        .asChildOf(spanContext)
        .start()
      span.finish()
    } else {
      val span = tracer.buildSpan("inside scala service 2")
        .start()
      span.finish()
    }

    val result = "Hello, OpenTelemetry!"
    Ok(result)
  }


}
