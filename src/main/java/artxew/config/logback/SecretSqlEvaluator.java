package artxew.config.logback;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluatorBase;
public class SecretSqlEvaluator extends EventEvaluatorBase<ILoggingEvent> {

	@Override
	public boolean evaluate(ILoggingEvent event) throws NullPointerException, EvaluationException {
		return "jdbc.sqltiming".equals(event.getLoggerName()) && event.getMessage().contains(":SECRET");
	}
}