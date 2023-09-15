package com.github.dongglin.smart.spring.boot.plus.common.process;//package com.redrcd.party.ylxf.common.process;
//
//import cn.hutool.json.JSONUtil;
//import com.redrcd.party.ylxf.common.enums.StatusCode;
//import com.redrcd.party.ylxf.common.exception.BusinessException;
//import com.redrcd.party.ylxf.common.response.ApiResponse;
//import com.redrcd.party.ylxf.common.utils.JsonUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//@RestControllerAdvice(basePackages = "com.redrcd.party.ylxf")
//@Slf4j
//public class ResponseAdvisor implements ResponseBodyAdvice<Object> {
//    @Override
//    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
//        if (methodParameter.getExecutable().getName().contains("error")) return false;
//        return true;
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
//
//        if (o instanceof ApiResponse) {
//            return o;
//        }
//        if (o instanceof Boolean) {
//            boolean result = (boolean) o;
//            return result ? ApiResponse.success() : ApiResponse.failure();
//        }
//        if (o instanceof String) {
//            ApiResponse<Object> result = new ApiResponse<>();
//            try {
//                String response = (String) o;
//                if (JSONUtil.isJson(response)) {
//                    response = JsonUtil.serialize(result);
//                }
//                serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//                serverHttpResponse.getBody().write(response.getBytes());
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                BusinessException.throwBy(true, StatusCode.CODE_999999.getMessage(), StatusCode.CODE_999999.getCode());
//            }
//        }
//        return ApiResponse.success();
//    }
//}
//
