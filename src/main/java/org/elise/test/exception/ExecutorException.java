package org.elise.test.exception;

import java.util.concurrent.Executor;

/**
 * Created by Glenn on 2017/5/31 0031.
 */
public class ExecutorException extends RuntimeException{

    private static final long serialVersionUID = -453626210184634193L;

    public ExecutorException(String reason, Executor executor)
    {
        super("ExecutorException<" + reason + ">:" + executor.toString());
    }

    public ExecutorException(String reason)
    {
        super("ExecutorException<" + reason + ">" );
    }
}
