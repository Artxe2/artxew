package artxew.project.layers.example.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import artxew.framework.decedent.async.BaseAsyncProcessor;
import artxew.framework.layers.cmminq.svc.CmmInqSvc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Async
@RequiredArgsConstructor
@Service
@Slf4j
public class ExampleAsync extends BaseAsyncProcessor {

    private final TransactionTemplate transactionTemplate;

    private final CmmInqSvc cmmInqSvc;
    
    public void async(String start) {
        final String[] end = new String[1];

        actionWithFinally(
            transactionTemplate
            , status -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        Thread.sleep(1000);

                        String time = cmmInqSvc.currentTime();

                        end[0] = time;
                        log.info("{}: {}", i, time);
                    }
                } catch (Exception e) {
                    status.setRollbackOnly();
                }
            }
            , status -> {
                log.info("async end {} -> {}", start, end[0]);
            }
        );
    }
}
