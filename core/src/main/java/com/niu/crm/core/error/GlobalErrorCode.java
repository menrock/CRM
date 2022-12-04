package com.niu.crm.core.error;


import java.util.HashMap;
import java.util.Map;

public enum GlobalErrorCode {

    // 成功
    SUCESS(200, "Success"),

    CREATED(201, "Created"),

    // 未知异常
    UNKNOWN(-1, "Unknown error"),

    //  =========系统级别的异常 start===========
    // 没有权限
    UNAUTHORIZED(401, "Unauthorized"),

    // 系统异常，请稍后再试
	INTERNAL_ERROR(500, "Server internal error"),

    // 无效参数
    INVALID_ARGUMENT(11001, "Invalid argument"),

    //  =========系统级别的异常 end===========

    // 资源为空
    RESOURCE_NOT_FOUND(404, "Resource not found"),

    //password invalid
    PASSWORD_INVALID(2001, "Password invalid"),

    //用户不存在
    USER_NOT_EXIST(2003, "User not exist"),
    //unit不存在
    UNIT_NOT_EXIST(2004, "Unit not exist"),
    //数据不存在
    DATA_NOT_EXIST(2005, "Data not exist");

    private static final Map<Integer, GlobalErrorCode> values = new HashMap<Integer, GlobalErrorCode>();
    static {
        for (GlobalErrorCode ec : GlobalErrorCode.values()) {
            values.put(ec.code, ec);
        }
    }

    private int        code;
    private String     error;

    private GlobalErrorCode(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public static GlobalErrorCode valueOf(int code) {
        GlobalErrorCode ec = values.get(code);
        if (ec != null)
            return ec;
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }
}

