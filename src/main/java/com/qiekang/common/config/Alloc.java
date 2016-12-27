package com.qiekang.common.config;

import java.io.File;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public interface Alloc {
	
	Config cfg = load();

    static Config load() {
        Config config = ConfigFactory.load();//扫描加载所有可用的配置文件
        String custom_conf = "application.conf";//加载自定义配置, 值来自jvm启动参数指定-Dmp.conf
        if (config.hasPath(custom_conf)) {
            File file = new File(config.getString(custom_conf));
            if (file.exists()) {
                Config custom = ConfigFactory.parseFile(file);
                config = custom.withFallback(config);
            }
        }
        return config;
    }
    
    interface Alipay {
    	Config cfg = Alloc.cfg.getObject("mp").toConfig();
        String log_dir = cfg.getString("log.dir");
        String log_level = cfg.getString("log.level");
        
    }
    
    
	
}
