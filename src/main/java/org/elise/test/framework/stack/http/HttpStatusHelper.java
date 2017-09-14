package org.elise.test.framework.stack.http;

/**
 * Created by Glenn on  2017/9/13 0013 15:10.
 */


public enum HttpStatusHelper {

    INFORMATIONAL(1),
    SUCCESSFUL(2),
    REDIRECTION(3),
    CLIENT_ERROR(4),
    SERVER_ERROR(5);

    private final int value;

    HttpStatusHelper(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static HttpStatusHelper valueOf(int status) {
        int code = status / 100;
        HttpStatusHelper[] types = values();
        for (int i = 0; i < types.length; ++i) {
            HttpStatusHelper helper = types[i];
            if (helper.value == code) {
                return helper;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + status + "]");
    }

    public Boolean isInformational() {
        return this.value() == HttpStatusHelper.INFORMATIONAL.value();
    }

    public Boolean isSuccessful() {
        return this.value() == HttpStatusHelper.SUCCESSFUL.value();
    }

    public Boolean isRedirection() {
        return this.value() == HttpStatusHelper.REDIRECTION.value();
    }

    public Boolean isClientError() {
        return this.value() == HttpStatusHelper.CLIENT_ERROR.value();
    }

    public Boolean isServerError() {
        return this.value() == HttpStatusHelper.SERVER_ERROR.value();
    }
}
