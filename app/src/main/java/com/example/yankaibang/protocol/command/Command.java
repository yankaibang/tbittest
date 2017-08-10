package com.example.yankaibang.protocol.command;

/**
 * Created by yankaibang on 2017/8/7.
 */

public abstract class Command {

    public static final int NOT_EXECUTE_YET = 0;
    public static final int PROCESSING = 1;
    public static final int FINISHED = 2;

    public static final int DEFAULT_RETRY_TIMES = 3;
    public static final int DEFAULT_COMMAND_TIMEOUT = 10000;
    protected static final int DEFAULT_ACK_TIMEOUT = 3000;
    protected static final int HANDLE_TIMEOUT = 0;
    protected static final int HANDLE_ACK_TIMEOUT = 1;

    public abstract boolean process();
}
