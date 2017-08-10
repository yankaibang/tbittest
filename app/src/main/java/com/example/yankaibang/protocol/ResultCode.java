package com.example.yankaibang.protocol;

/**
 * Created by Salmon on 2016/12/5 0005.
 */

public class ResultCode {
    // 操作成功
    public static final int SUCCEED = 0;
    // 操作失败 (未知原因)
    public static final int FAILED = -1;
    // 超时
    public static final int TIMEOUT = -2;
    // 操作被取消
    public static final int CANCELED = -3;
    // 手机蓝牙未开启
    public static final int BLE_NOT_OPENED = -1001;
    // 设备不支持蓝牙BLE或非指定终端
    public static final int BLE_NOT_SUPPORTED = -1002;
    // 权限错误
    public static final int PERMISSION_DENIED = -1003;
    // 未连接或连接已断开
    public static final int DISCONNECTED = -1004;
    // 该指令正在处理中，请稍后发送(部分指令不能短时间重复发送，会收到此条状态)
    public static final int PROCESSING = -1005;
    // 低于API18
    public static final int LOWER_THAN_API_18 = -1006;
    // 设备编号不合法
    public static final int MAC_ADDRESS_ILLEGAL = -2001;
    // 未找到设备
    public static final int DEVICE_NOT_FOUNDED = -2002;
    // 连接错误 - sdk层检测密钥不正确(包括密钥规格不正确和无法通过校验两种可能)
    public static final int KEY_ILLEGAL = -2003;
    // 连接错误 - 广播数据包解析错误
    public static final int BROARCAST_RESOLUTION_FAILED = -2004;
    // 控制指令错误 - 指令非法
    public static final int ILLEGAL_COMMAND = -3001;
    // 控制指令错误 - 运动状态
    public static final int MOTION_STATE = -3002;
    // 控制指令错误 - 非绑定状态
    public static final int NOT_BINDING = -3003;
    // OTA升级文件不合法
    public static final int OTA_FILE_ILLEGAL = -4001;
    // OTA升级失败 - 电量不足
    public static final int OTA_FAILED_LOW_POWER = -4002;
    // OTA升级失败 - 未知原因
    public static final int OTA_FAILED_UNKNOWN = -4003;
    // OTA升级失败 - 写入失败
    public static final int OTA_WRITE_FAILED = -4004;
    // OTA升级失败 - 密钥错误
    public static final int OTA_FAILED_ERR_KEY = -4005;
    // OTA升级失败 - Invalid image bank
    public static final int OTA_FAILED_IMAGE_BANK = -4006;
    // OTA升级失败 - Invalid image header
    public static final int OTA_FAILED_IMAGE_HEADER = -4007;
    // OTA升级失败 - Invalid image size
    public static final int OTA_FAILED_IMAGE_SIZE = -4008;
    // OTA升级失败 - Invalid product header
    public static final int OTA_FAILED_PRODUCT_HEADER = -4009;
    // OTA升级失败 - Same Image Error
    public static final int OTA_FAILED_SAME_IMAGE = -4010;
    // OTA升级失败 - Failed to read from external memory device
    public static final int OTA_FAILED_TO_READ_FROM_EXTERNAL_MEM = -4011;
    // 连接失败，未知原因
    public static final int CONNECT_FAILED_UNKNOWN = -8000;
    // 连接失败，密钥非法
    public static final int CONNECT_FAILED_ILLEGAL_KEY = -8001;
    // 连接失败，数据校验非法
    public static final int CONNECT_DATA_VERIFICATION_FAILED = -8002;
    // 连接失败，指令不支持
    public static final int CONNECT_COMMAND_NOT_SUPPORT= -8003;
    // 连接失败，已有设备连接
    public static final int CONNECT_ALREADY_CONNECTED= -8004;
}
