package org.elise.test.framework.stack.http;

/**
 * Created by Glenn on  2017/9/20 0020 17:02.
 */

public enum EliseHttpMethod
{
    POST("POST"), GET("GET"), PUT("PUT"), DELETE("DELETE");

    private String method;

    EliseHttpMethod(String method)
    {
        this.method = method;
    }

    public String getMethod()
    {
        return method;
    }

    @Override
    public String toString()
    {
        return method;
    }
}