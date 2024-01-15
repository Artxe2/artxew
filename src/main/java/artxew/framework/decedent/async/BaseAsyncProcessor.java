package artxew.framework.decedent.async;
import java.util.function.Consumer;

import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Artxe2
 */
public class BaseAsyncProcessor {

	/**
	 * @author Artxe2
	 */
	public void actionWithFinally(
		TransactionTemplate txTemplate
		, @NonNull Consumer<TransactionStatus> action
		, Consumer<TransactionStatus> finalAction
	) {
		txTemplate.executeWithoutResult(action);
		if (finalAction != null) {
			txTemplate.executeWithoutResult(finalAction);
		}
	}
}