package org.elise.test.exception;

/**
 * Created by huxuehan on 2016/10/18.
 */
public class InvalidRequestException extends Exception{

    private static final long serialVersionUID = -4121867189740489504L;

    public InvalidRequestException(String message){
           super(message);
    }
}
