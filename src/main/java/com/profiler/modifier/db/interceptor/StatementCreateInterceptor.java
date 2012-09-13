package com.profiler.modifier.db.interceptor;

import com.profiler.context.Trace;
import com.profiler.interceptor.StaticAfterInterceptor;
import com.profiler.util.InterceptorUtils;
import com.profiler.util.MetaObject;
import com.profiler.util.StringUtils;

import java.sql.Connection;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatementCreateInterceptor implements StaticAfterInterceptor {

    private final Logger logger = Logger.getLogger(StatementCreateInterceptor.class.getName());

    // connection 용.
    private final MetaObject<String> getUrl = new MetaObject<String>("__getUrl", String.class);

    private final MetaObject setUrl = new MetaObject("__setUrl", String.class);

    @Override
    public void after(Object target, String className, String methodName, String parameterDescription, Object[] args, Object result) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info("after " + StringUtils.toString(target) + " " + className + "." + methodName + parameterDescription + " args:" + Arrays.toString(args) + " result:" + result);
        }
        if (!InterceptorUtils.isSuccess(result)) {
            return;
        }
        if (Trace.getCurrentTraceId() == null) {
            return;
        }
        if (target instanceof Connection) {
            String connectionUrl = getUrl.invoke(target);
            setUrl.invoke(result, connectionUrl);
        }
    }

}
