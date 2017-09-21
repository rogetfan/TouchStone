package org.elise.test.framework.stack.http;

/**
 * Created by Glenn on  2017/9/13 0013 15:10.
 */


public enum EliseHttpStatusHelper {

    INFORMATIONAL(1),
    SUCCESSFUL(2),
    REDIRECTION(3),
    CLIENT_ERROR(4),
    SERVER_ERROR(5);

    private final int value;

    EliseHttpStatusHelper(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static EliseHttpStatusHelper valueOf(int status) {
        int code = status / 100;
        EliseHttpStatusHelper[] types = values();
        for (int i = 0; i < types.length; ++i) {
            EliseHttpStatusHelper helper = types[i];
            if (helper.value == code) {
                return helper;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + status + "]");
    }

    public Boolean isInformational() {
        return this.value() == EliseHttpStatusHelper.INFORMATIONAL.value();
    }

    public Boolean isSuccessful() {
        return this.value() == EliseHttpStatusHelper.SUCCESSFUL.value();
    }

    public Boolean isRedirection() {
        return this.value() == EliseHttpStatusHelper.REDIRECTION.value();
    }

    public Boolean isClientError() {
        return this.value() == EliseHttpStatusHelper.CLIENT_ERROR.value();
    }

    public Boolean isServerError() {
        return this.value() == EliseHttpStatusHelper.SERVER_ERROR.value();
    }

    public Boolean isError(){
        return this.value() == EliseHttpStatusHelper.SERVER_ERROR.value()
                || this.value() == EliseHttpStatusHelper.CLIENT_ERROR.value();
    }
}
