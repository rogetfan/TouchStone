package org.elise.test.exception;

/**
 * Created by Glenn on 2016/10/18.
 */
public class NullRequestException extends Exception{

    /**
     *
     * */
    private static final long serialVersionUID = 4548581576162402148L;

    public NullRequestException(String message){
        super(message);
    }

}
