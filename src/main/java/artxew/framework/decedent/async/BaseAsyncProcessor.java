package artxew.framework.decedent.async;

import java.util.function.Consumer;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

public class BaseAsyncProcessor {

    public void actionWithFinally(
        TransactionTemplate txTemplate
        , Consumer<TransactionStatus> action
        , Consumer<TransactionStatus> finalAction
    ) {
        txTemplate.executeWithoutResult(action);
        txTemplate.executeWithoutResult(finalAction);
    }
}