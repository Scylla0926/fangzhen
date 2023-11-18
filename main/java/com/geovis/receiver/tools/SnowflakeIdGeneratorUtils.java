package com.geovis.receiver.tools;

public class SnowflakeIdGeneratorUtils {
    // 起始的时间戳，这里选择的是 2021-01-01 00:00:00
    private static final long START_TIMESTAMP = 1609430400000L;
    // 机器 id 所占的位数，这里留了 10 位 Bits 给机器 ID
    private static final long MACHINE_ID_BITS = 10L;
    // 序列号所占的位数
    private static final long SEQUENCE_BITS = 12L;
    // 机器 id 向左移 12 位
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    // 时间戳向左移 22 位（10+12）
    private static final long TIMESTAMP_LEFT_SHIFT = MACHINE_ID_BITS + SEQUENCE_BITS;
    // 可以表示的最大的机器 id，即 2^10-1
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
    // 可以表示的最大的序列号，即 2^12-1
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);;
    // 上一次的时间戳
    private static long lastTimestamp = -1L;
    // 序列号
    private static long sequence = 0L;
    // 机器 ID，建议使用一个静态常量或者配置文件来设置 ID
    private final long machineId;

    public SnowflakeIdGeneratorUtils(long machineId) {
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException("machine id illegal: " + machineId);
        }
        this.machineId = machineId;
    }

    public synchronized long getNextId() {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards! Refusing to generate id");
        }
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // 序列号用尽，只能等待下一毫秒
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = currentTimestamp;
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT) | (machineId << MACHINE_ID_SHIFT) | sequence;
    }

    private long waitNextMillis(long currentTimestamp) {
        long waitTimestamp = currentTimestamp;
        while (waitTimestamp <= lastTimestamp) {
            waitTimestamp = System.currentTimeMillis();
        }
        return waitTimestamp;
    }
}
