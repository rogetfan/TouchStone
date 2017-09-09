//package org.elise.test.framework.transaction.http.methods;
//
//import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
//import java.net.URI;
//
///**
// * Created by huxuehan on 2016/10/19.
// * Rewrite to support http content
// */
//public class HttpDelete extends HttpEntityEnclosingRequestBase {
//    public static final String METHOD_NAME = "DELETE";
//
//    public HttpDelete() {
//    }
//
//    public HttpDelete(URI uri) {
//        this.setURI(uri);
//    }
//
//    public HttpDelete(String uri) {
//        this.setURI(URI.create(uri));
//    }
//
//    public String getMethod() {
//        return "DELETE";
//    }
//}
