package com.theopendle.aem.wknd.core;

public class TestUtil {

    private TestUtil() {
        // noop
    }

    public static  String getJsonTestResourcePath(Class<?> testClass) {
        StringBuilder sb = new StringBuilder();
        return sb.append("/")
                .append(testClass.getPackage().toString()
                        .replace("package ", "")
                        .replace(".", "/"))
                .append("/")
                .append(testClass.getSimpleName())
                .append(".json")
                .toString();
    }
}
