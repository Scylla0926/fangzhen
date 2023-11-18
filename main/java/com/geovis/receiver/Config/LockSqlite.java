package com.geovis.receiver.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class LockSqlite {

    public volatile static Map<String,Lock> fileNameLock = new HashMap<>();
}
