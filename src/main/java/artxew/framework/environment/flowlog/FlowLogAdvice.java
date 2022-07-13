package artxew.framework.environment.flowlog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public final class FlowLogAdvice {

    @Before("within(artxew.*.layers.*.svc.*Svc)")
    public void before(JoinPoint joinPoint)  {
        log.info(
            "\n{}"
            , FlowLogHolder.push(joinPoint.getSignature().toShortString())
        );
    }

    @After("within(artxew.*.layers.*.svc.*Svc)")
    public void after(JoinPoint joinPoint)  {
        FlowLogHolder.pop();
    }
}