package com.gjb.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
  private final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Pointcut("execution(* com.gjb.demo.web.*.*(..))")
    public void log(){}

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =attributes.getRequest();
        String classMethon=joinPoint.getSignature().getDeclaringTypeName()+"."+
                joinPoint.getSignature().getName();
        RequestLog requestLog=new RequestLog(
                request.getRequestURL().toString(),
                request.getRemoteAddr(),
                classMethon,
                joinPoint.getArgs()
                );
        logger.info("REquest------{}",requestLog);
    }

    @After("log()")
    public void doAfter(){
        logger.info("-------------doAfter------------------");
    }

    @AfterReturning(returning = "result" ,pointcut = "log()")
    public void doAfterRuturn(Object result){
    logger.info("RESULT:{}",result);
    }

    private  class RequestLog{
        private String url;
        private String ip;
        private  String classMethon;
        private  Object[] args;

        public RequestLog(String url, String ip, String classMethon, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethon = classMethon;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethon='" + classMethon + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }


}
