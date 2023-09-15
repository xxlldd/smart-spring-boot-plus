package com.github.dongglin.smart.spring.boot.plus.core.framework.util;

import com.alibaba.fastjson2.JSON;
import com.github.dongglin.smart.spring.boot.plus.core.framework.response.ApiResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author DongGL
 * @date 2018-11-08
 */
public final class HttpResponseUtil {

    private static String UTF8 = "UTF-8";
    private static String CONTENT_TYPE = "application/json";

    private HttpResponseUtil() {
        throw new AssertionError();
    }


    public static void printFailJsonMessage(HttpServletResponse response, String errorMessage) {
        ApiResult apiResult = ApiResult.fail(errorMessage);
        printJson(response, apiResult);
    }

    public static void printJson(HttpServletResponse response, Object object) {
        response.setCharacterEncoding(UTF8);
        response.setContentType(CONTENT_TYPE);
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.write(JSON.toJSONString(object));
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public static void print(HttpServletResponse response, String string) {
        response.setCharacterEncoding(UTF8);
        response.setContentType(CONTENT_TYPE);
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.write(string);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

}
