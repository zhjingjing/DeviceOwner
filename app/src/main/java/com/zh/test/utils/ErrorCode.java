package com.zh.test.utils;

/**
 * @Author: Administrator
 * @Time 2020/11/27 002711:03
 * @Email: zhaohang@zhizhangyi.com
 * @Describe:
 */
public interface ErrorCode {
    //unknown error
    int RESULT_UNKNOWN_ERROR = 1 << (16 + 1); // 1 << 16  == 65536
    //net work is not available
    int RESULT_NETWORK_ERROR = 1 << (16 + 2);
    //json is not parsable
    int RESULT_JSON_FORMAT_ERROR = 1 << (16 + 3);
    //server's response can not be parsed, or server returns 500(internal error)
    int RESULT_SERVER_ERROR = 1 << (16 + 5);
    //background work is cancelled by user or us.
    int RESULT_CANCEL_BY_USER = 1 << (16 + 6);
}
