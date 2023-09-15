package com.github.dongglin.smart.spring.boot.plus.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.dongglin.smart.spring.boot.plus.common.enums.IResponseEnum;
import com.github.dongglin.smart.spring.boot.plus.common.enums.StatusCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.Serializable;

/**
 * 统一响应封装
 *
 * @param <T>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class ApiResponse<T> implements Serializable {
    private int code = StatusCode.CODE_UNDEFINED.getCode();
    private String messages = "";
    private String detailMsg = "";
    private T data;

    public static <T> ApiResponse<T> success() {
        return success(StatusCode.CODE_000000);
    }

    public static <T> ApiResponse<T> success(StatusCode statusCode) {
        return success(statusCode.getCode(), statusCode.getMessage());
    }

    public static <T> ApiResponse<T> success(StatusCode statusCode, T data) {
        return build(StatusCode.CODE_000000.getCode(), statusCode.getMessage(), "", data);
    }

    public static <T> ApiResponse<T> success(int code, String msg) {
        return build(code, msg, "", null);
    }

    public static <T> ApiResponse<T> success(String msg, T data) {
        return build(StatusCode.CODE_000000.getCode(), msg, "", data);
    }

    public static <T> ApiResponse<T> success(String msg) {
        return build(StatusCode.CODE_000000.getCode(), msg == null ? StatusCode.CODE_000000.getMessage() : msg, "", null);
    }

    public static <T> ApiResponse<T> failure(IResponseEnum statusCode) {
        return failure(statusCode, "");
    }

    public static <T> ApiResponse<T> failure(IResponseEnum statusCode, String detailMsg) {
        return failure(statusCode.getCode(), statusCode.getMessage(), detailMsg);
    }

    public static <T> ApiResponse<T> failure(IResponseEnum statusCode, T data) {
        return build(statusCode.getCode(), statusCode.getMessage(), "", data);
    }

    public static <T> ApiResponse<T> failure(String msg) {
        return failure(StatusCode.CODE_UNDEFINED.getCode(), msg, "");
    }

    public static <T> ApiResponse<T> failure() {
        return failure(StatusCode.CODE_999999.getCode(), StatusCode.CODE_999999.getMessage(), "");
    }

    public static <T> ApiResponse<T> failure(String stateInfo, String detailMsg) {
        return failure(StatusCode.CODE_UNDEFINED.getCode(), stateInfo, detailMsg);
    }

    public static <T> ApiResponse<T> failure(int stateCode, String stateInfo, String detailMsg) {
        return build(stateCode, stateInfo, detailMsg, null);
    }

    public static <T> ApiResponse<T> failure(int stateCode, String stateInfo, Throwable ex) {
        return build(stateCode, stateInfo, ExceptionUtils.getStackTrace(ex), null);
    }

    public static <T> ApiResponse<T> failure(int stateCode, String stateInfo, Throwable ex, T data) {
        return build(stateCode, stateInfo, ExceptionUtils.getStackTrace(ex), data);
    }

    public static <T> ApiResponse<T> build(int stateCode, String stateInfo, String detailMsg, T data) {
        return new ApiResponse(stateCode, stateInfo, detailMsg, data);
    }

    public Boolean isSuccess() {
        return StatusCode.CODE_000000.getCode() == this.code;
    }

    public ApiResponse() {
    }

    public ApiResponse(int stateCode, String stateInfo) {
        this.code = stateCode;
        this.messages = stateInfo;
    }

    public ApiResponse(int stateCode, String stateInfo, String detailMsg, T data) {
        this.code = stateCode;
        this.messages = stateInfo;
        this.detailMsg = detailMsg;
        this.data = data;
    }


    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ApiResponse)) {
            return false;
        } else {
            ApiResponse<?> other = (ApiResponse) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59:
                {
                    Object this$stateCode = this.getCode();
                    Object other$stateCode = other.getCode();
                    if (this$stateCode == null) {
                        if (other$stateCode == null) {
                            break label59;
                        }
                    } else if (this$stateCode.equals(other$stateCode)) {
                        break label59;
                    }

                    return false;
                }

                Object this$stateInfo = this.getMessages();
                Object other$stateInfo = other.getMessages();
                if (this$stateInfo == null) {
                    if (other$stateInfo != null) {
                        return false;
                    }
                } else if (!this$stateInfo.equals(other$stateInfo)) {
                    return false;
                }

                Object this$log = this.getDetailMsg();
                Object other$log = other.getDetailMsg();
                if (this$log == null) {
                    if (other$log != null) {
                        return false;
                    }
                } else if (!this$log.equals(other$log)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ApiResponse;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $stateCode = this.getCode();
        result = result * 59 + $stateCode.hashCode();
        Object $stateInfo = this.getMessages();
        result = result * 59 + ($stateInfo == null ? 43 : $stateInfo.hashCode());
        Object $log = this.getDetailMsg();
        result = result * 59 + ($log == null ? 43 : $log.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", messages='" + messages + '\'' +
                ", detailMsg='" + detailMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
