## 最低安卓版本号
18
## 需要用到的权限
```
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

## 状态码
| 状态码     | 说明           |
| ---------- | -----------|
| 0             | 操作成功     |
| -1            | 操作失败 (未知原因)     |
| -2            | 超时         |
| -3            | 操作被取消         |
| -1001      | 手机蓝牙未开启         |
| -1002      | 设备不支持蓝牙BLE或非指定终端         |
| -1003      | 权限错误         |
| -1004      | 未连接或连接已断开         |
| -1005      | 该指令正在处理中，请稍后发送         |
| -1006      | 低于API18         |
| -2001      | 设备编号不合法         |
| -2002      | 未找到设备         |
| -2003      | 连接错误 - 密钥规格不正确         |
| -2004      | 连接错误 - 广播数据包解析错误         |
| -3001      | 控制指令错误 - 指令非法         |
| -3002      | 控制指令错误 - 运动状态         |
| -3003      | 控制指令错误 - 非绑定状态         |
| -4001      | OTA升级文件不合法         |
| -4002      | OTA升级失败 - 电量不足        |
| -4003      | OTA升级失败 - 未知原因         |
| -4004      | OTA升级失败 - 写入失败         |
| -4005      | OTA升级失败 - 密钥错误         |
| -4006      | OTA升级失败 - Invalid image bank         |
| -4007      | OTA升级失败 - Invalid image header         |
| -4008      | OTA升级失败 - Invalid image size         |
| -4009      | OTA升级失败 - Invalid product header        |
| -4010      | OTA升级失败 - Same Image Error         |
| -4011      | OTA升级失败 - Failed to read from external memory device         |
| -8000      | 连接失败，未知原因         |
| -8001      | 连接失败，密钥非法         |
| -8002      | 连接失败，数据校验非法        |
| -8003      | 连接失败，指令不支持         |
| -8004      | 连接失败，已有设备连接         |

其中 **-1，-2，-1002，-3001，-3002，-3003**可以重试，其它指令的失败不需要重试

### 使用
#### 关于权限
在Android 6.0 及以上系统，除了需要**动态**申请文中说明的权限，最好也将该开关打开
```Java
LocationManager locationManager =
        (LocationManager) getSystemService(Context.LOCATION_SERVICE);
if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
    Intent intent = new Intent();
    intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    MainActivity.this.startActivity(intent);
    Toast.makeText(MainActivity.this, "请开启", Toast.LENGTH_LONG).show();
}
```
#### 初始化

在程序**入口**的Activity中初始化
```Java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TbitBle.initialize(MainActivity.this, new MyProtocolAdapter());
    }
}
```
**或者**

在Application中进行初始化，与在入口Activity中初始化类似，但需要确保只在主进程被初始化
```Java
private boolean isMainProcess() {
    int pid = Process.myPid();
    String processNameString = "";
    ActivityManager m = (ActivityManager) getSystemService(
            Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningAppProcessInfo appProcess :
            m.getRunningAppProcesses()) {
        if (appProcess.pid == pid) {
            processNameString = appProcess.processName;
        }
    }
    return TextUtils.equals(BuildConfig.APPLICATION_ID, processNameString);
}
```
#### RxJava依赖冲突
本SDK依赖Rxjava2, 如果出现和你当前项目的依赖冲突，按如下操作排除冲突
```gradle

android {
	...
	packagingOptions {
    		exclude 'META-INF/rxjava.properties'
	}
}

dependencies {
	compile ('com.github.billy96322:tbitble:x.y.z') {
	    exclude module: 'rxjava', group: 'io.reactivex'
	    exclude module: 'rxandroid', group: 'io.reactivex'
	}
}
```
#### 操作
```Java
// 如果是Android 6.0以上需要在权限被同意之后再做初始化工作以下操作

// 连接命令，参数为设备编号，密钥
TbitBle.connect(deviceId, key, new ResultCallback() {
            @Override
            public void onResult(int resultCode) {
              // 连接回应
            }
        }, new StateCallback() {
            @Override
            public void onStateUpdated(BikeState bikeState) {
              // 连接成功状态更新
            }
        });

// 解锁
TbitBle.unlock(new ResultCallback() {
            @Override
            public void onResult(int resultCode) {
               // 解锁回应
            }
        });

// 上锁
TbitBle.lock(new ResultCallback() {
            @Override
            public void onResult(int resultCode) {
               // 上锁回应
            }
        });

// 更新状态
TbitBle.update(new ResultCallback() {
            @Override
            public void onResult(int resultCode) {
              // 更新状态回复
            }
        }, new StateCallback() {
            @Override
            public void onStateUpdated(BikeState bikeState) {
              // 最新状态
            }
        });

// 断开连接
TbitBle.disConnect();

// 读取rssi(在已经连接的前提下)
TbitBle.readRssi(new ResultCallback() {
            @Override
            public void onResult(int resultCode) {
              // 状态
            }
        }, new RssiCallback() {
            @Override
            public void onRssiReceived(int rssi) {
              // rssi
            }
        });

// 销毁（在退出程序的Activity中的onDestroy中调用）
// 销毁后无法再进行操作，如需要请重新初始化
TbitBle.destroy();
```
#### 通用指令
通用指令的回应仅仅代表**回应是否成功**，不代表指令的结果是否成功，结果需要自行解析
```Java
// 自行解析packet内容
TbitBle.commonCommand((byte)0x03, (byte)0x02, new Byte[]{0x01},
                new ResultCallback() {
                    @Override
                    public void onResult(int resultCode) {
                      // 发送状态回复
                    }
                }, new PacketCallback() {
                    @Override
                    public void onPacketReceived(Packet packet) {
                      // 收到packet回复
                    }
                });

```

#### 清空命令队列
该功能用于，当发出一个或多个请求尚未得到回应的时候需要解释当前的Activity，为防止泄漏可以在该Activity的onDestroy方法中调用取消所有任务的方法。
```Java
// 取消队列中的所有任务
TbitBle.cancelAllCommand();
```

#### OTA升级
升级文件的后缀必须是**.img**
```Java
// OTA升级
TbitBle.ota(new File(imagePath), new ResultCallback() {
            @Override
            public void onResult(int resultCode) {
              // 升级状态回应
            }
        }, new ProgressCallback() {
            @Override
            public void onProgress(int progress) {
              // 升级进度
            }
        });
```

#### 日志记录
为了更好的排查问题，当出现非预期的错误的时候，将日志拼接并记录。
```Java
// 注册日志监听器
TbitBle.setDebugListener(new TbitDebugListener() {
        @Override
        public void onLogStrReceived(String logStr) {
            detailLogBuilder.insert(0, "\n\n")
                    .insert(0, logStr)
                    .insert(0, "\n")
                    .insert(0, getTime());  // 最好一并记录下时间
        }
    };);

// 反注册日志监听器
TbitBle.setDebugListener(null);
```

以上操作均需要在**主线程**执行

#### 直接可以获得的参数
```Java
// 获得当前蓝牙连接状态
TbitBle.getBleConnectionState();
// 获得最后一次更新的车辆状态信息(需要更新请执行更新操作并等待相应回调)
TbitBle.getState()
```

#### 蓝牙状态信息
```Java
public static final int STATE_DISCONNECTED = 0;
public static final int STATE_SCANNING = 1;
public static final int STATE_CONNECTING = 2;
public static final int STATE_CONNECTED = 3;
public static final int STATE_SERVICES_DISCOVERED = 4;
```

#### 车辆状态字段
```Java
// 电量
private float battery;

// 位置,location[0]是经度，location[1]是纬度
private double[] location = new double[]{0, 0};

// 信号量，数组的三位分别为如下
// GSM：用于标识 GSM 信号强度，范围 0~9
// GPS：用于标识 GPS 信号强度，范围 0~9
// BAT：用于标识定位器后备电池电量，范围 0~9
private int[] signal = new int[]{0, 0, 0};

// 校验失败原因
// 0：未知原因 1：连接密钥非法 2：数据校验失败 3：指令不支持
private int verifyFailedCode;

// 车辆故障 电机类故障 中控类故障 通信类故障 其它故障 详见常用数据类型说明
private int deviceFaultCode;

// 数组每一位的解析如下
// 0 | 0：车辆撤防模式开启 1：车辆设防模式开启
// 1 | 0：车辆处于静止状态 1：车辆处于运动状态
// 2 | 0：车辆锁电机为关闭状态 1：车辆锁电机为打开状态
// 3 | 0：车辆 ACC 为关闭状态 1：车辆 ACC 为打开状态
// 4 | 0：车辆不处于休眠模式 1：车辆处于休眠模式
// 5 | 0：车辆蓝牙处于非连接状态 1：车辆蓝牙处于连接状态
// 6~7 | 11：断电告警  10：震动告警  01：低电告警  00：无告警信息
private int[] systemState = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

// 0x00：成功  0x01：指令非法 0x02：运动状态 0x03：非绑定状态
private int operateFaultCode;
// 0:MCC 1:MNC 2:LAC 3.Cell ID
private int[] baseStation = new int[]{0, 0, 0, 0};
// 版本号 0：硬件版本 1：软件版本
private int[] version = new int[]{0, 0};
// 控制器信息
private ControllerState controllerState;
```

#### 控制器状态字段
```Java
// 总里程，单位是 KM
private int totalMillage;
// 单次里程，单位是 0.1KM
private int singleMillage;
// 速度，单位是 0.1KM/H
private int speed;
// 电压，单位是 0.1V
private int voltage;
// 电流，单位是 MA
private int electricCurrent;
// 电量，单位是 MAH
private int battery;
// 故障码
// BIT7 电机缺相      0: 电机不故障，1: 电机缺相故障
// BIT6 霍尔故障状态  0: 霍尔无故障，1: 霍尔故障
// BIT5 转把故障状态  0: 转把无故障，1: 转把故障
// BIT4 MOS 故障状态   0：MOS 无故障，1：MOS 故障
// BIT3 欠压状态      0: 不在欠压保护，1: 正在欠压保护
// BIT2 巡航状态      0: 巡航无效，1: 巡航有效
// BIT1 刹车状态      0：刹车无效，1：刹车有效
// BIT0 WALK 状态      0：WALK 无效，1：WALK 有效
private int[] errCode = new int[]{0,0,0,0,0,0,0,0};
```


#### 扫描设备

```Java
// 最终得到的结果的回调
ScannerCallback scannerCallback = new ScannerCallback() {
    @Override
    public void onScanStart() {
        Log.d(TAG, "onScanStart: ");
    }

    @Override
    public void onScanStop() {
        Log.d(TAG, "onScanStop: ");
    }

    @Override
    public void onScanCanceled() {
        Log.d(TAG, "onScanCanceled: ");
    }

    @Override
    public void onDeviceFounded(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        String machineId = BikeUtil.resolveMachineIdByAdData(bytes);
        if (!TextUtils.isEmpty(machineId)) {
            showLog("扫描到设备: " + bluetoothDevice.getAddress()+ " | " + machineId);
        }
    }
};

// 添加装饰器

// 方式一：
// 过滤设备名字的装饰器
FilterNameCallback filterNameCallback = new FilterNameCallback(DEVICE_NAME, scannerCallback);
// 确保结果非重复的装饰器
NoneRepeatCallback noneRepeatCallback = new NoneRepeatCallback(filterNameCallback);
// 收集日志的装饰器，这个最好放在最外层包裹
LogCallback logCallback = new LogCallback(noneRepeatCallback);

// 方式二：(与上述效果相同)
ScanBuilder builder = new ScanBuilder(scannerCallback);
ScannerCallback decoratedCallback = builder
        .setFilter(DEVICE_NAME)
        .setRepeatable(false)
        .setLogMode(true)
        .build();

// 开始扫描(目前同一时间仅支持启动一个扫描),返回状态码
int code = TbitBle.startScan(logCallback, 10000);

// 结束当前扫描
TbitBle.stopScan();
```
