package com.cyc.app.tool;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *  通过 zip comment 方式打多渠道包时
 *    ChannelUtil 用于获取 渠道信息
 * Created by ys on 2016/9/30 0030.
 */
public class ChannelUtil {

    private static final String CHANNEL_KEY = "uuchannel";
    private static final String DEFAULT_CHANNEL = "cyc_default";
    private static String mChannel;

    public static String getChannel(Context context) {
        return getChannel(context, DEFAULT_CHANNEL);
    }

    public static String getChannel(Context context, String defaultChannel) {
        if (!TextUtils.isEmpty(mChannel)) {
            return mChannel;
        }
        //从apk中获取
        mChannel = getChannelFromApk(context, CHANNEL_KEY);
        if (!TextUtils.isEmpty(mChannel)) {
            return mChannel;
        }
        //全部获取失败
        return defaultChannel;
    }

    /**
     * 从apk中获取版本信息
     *
     * @param context
     * @param channelKey
     * @return
     */
    private static String getChannelFromApk(Context context, String channelKey) {
        long startTime = System.currentTimeMillis();
        //从apk包中获取
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String channel = "";
        if (!TextUtils.isEmpty(ret)) {
            String[] split = ret.split("_");
            if (split != null && split.length >= 2) {
                channel = ret.substring(split[0].length() + 1);
            }
            System.out.println("-----------------------------");
            System.out.println("渠道号：" + channel + "，解压获取渠道号耗时:" + (System.currentTimeMillis() - startTime) + "ms");
            System.out.println("-----------------------------");
        } else {
            System.out.println("未解析到相应的渠道号，使用默认内部渠道号");
            channel = DEFAULT_CHANNEL;
        }
        return channel;
    }
}

