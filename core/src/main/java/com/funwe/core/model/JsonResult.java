package com.funwe.core.model;

import java.util.HashMap;

/**
 * @Author: Corey
 * @Description: 返回给前端的统一JSON格式
 * @Date: 2020/2/11 23:35
 */
public class JsonResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "code";

    public static final String MSG_TAG = "msg";

    public static final String DATA_TAG = "data";

    /**
     * 状态类型
     */
    public enum Type
    {
        /** 成功 */
        SUCCESS(1),
        /**用户没有授权 */
        FAIL(0),
        /** 权限不足 */
        WARN(403),
        /** 错误 */
        ERROR(500);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    /** 状态类型 */
    private Type type;

    /** 状态码 */
    private int code;

    /** 返回内容 */
    private String msg;

    /** 数据对象 */
    private Object data;

    /**
     * 初始化一个新创建的 JsonResult 对象，使其表示一个空消息。
     */
    public JsonResult()
    {
    }

    /**
     * 初始化一个新创建的 JsonResult 对象
     *
     * @param type 状态类型
     * @param msg 返回内容
     */
    public JsonResult(Type type, String msg)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 JsonResult 对象
     *
     * @param type 状态类型
     * @param msg 返回内容
     * @param data 数据对象
     */
    public JsonResult(Type type, String msg, Object data)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        super.put(DATA_TAG, data);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static JsonResult success()
    {
        return JsonResult.success("操作成功");
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static JsonResult success(String msg)
    {
        return JsonResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static JsonResult success(String msg, Object data)
    {
        return new JsonResult(Type.SUCCESS, msg, data);
    }

    /**
     * 返回失败消息
     *
     * @return 成功消息
     */
    public static JsonResult fail()
    {
        return JsonResult.fail("没有授权访问");
    }

    /**
     * 返回失败消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static JsonResult fail(String msg)
    {
        return JsonResult.fail(msg, null);
    }

    /**
     * 返回失败消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static JsonResult fail(String msg, Object data)
    {
        return new JsonResult(Type.FAIL, msg, data);
    }


    public static JsonResult warn()
    {
        return JsonResult.warn("权限不足");
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static JsonResult warn(String msg)
    {
        return JsonResult.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static JsonResult warn(String msg, Object data)
    {
        return new JsonResult(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static JsonResult error()
    {
        return JsonResult.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 错误消息
     */
    public static JsonResult error(String msg)
    {
        return JsonResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 错误消息
     */
    public static JsonResult error(String msg, Object data)
    {
        return new JsonResult(Type.ERROR, msg, data);
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

}
