package com.yatang.xc.xcr.biz.message.util;

import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.esotericsoftware.minlog.Log;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @描述: 极光推送工具类
 * @作者: huangjianjun
 * @创建时间: 2017年4月7日-上午11:30:30 .
 * @版本: 1.0 .
 */
public class JPushUtil {

    private final static boolean isProduction = Boolean.parseBoolean(PropUtil.use("jpush.properties").get("jpush.isProduction"));
    private final static String PUSH_TITLE = PropUtil.use("jpush.properties").get("jpush.title");

    /**
     * 所有平台所有设备内容为content的通知
     *
     * @param title
     * @param content
     * @return
     */
    public static PushPayload buildPushObjectAllAlert(String tag, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(tag))
                .setOptions(Options.newBuilder().setApnsProduction(isProduction).build())
                .setNotification(Notification.alert(content))
                .build();
    }

    /**
     * 根据设备终端ID推送通知消息
     *
     * @param regesterIds设备终端ID集合
     * @param title               标题
     * @param content             内容
     * @return
     */
    public static PushPayload buildPushObjectByRegesterIds(List<String> regesterIds, String tag, String content) {
        return PushPayload.newBuilder().setPlatform(Platform.all())
                .setAudience(Audience.tag(tag))
                .setAudience(Audience.registrationId(regesterIds))
                .setNotification(
                        Notification.alert(content)).build();
    }

    /**
     * 根据设备终端ID推送通知消息+extras
     *
     * @param regesterIds
     * @param tag
     * @param content
     * @param extras
     * @return
     */
    public static PushPayload buildPushByRegesterIds(List<String> regesterIds, String title, String content, Map<String, String> extras) {
        String orderNo = extras.get("OrderNo");
        String iosSound = getIosSound(extras);
        Log.info("Jpush -> buildPushByRegesterIds -> iosSound:" + iosSound);
        if (StringUtils.isEmpty(orderNo)) {
            return PushPayload.newBuilder().setOptions(Options.newBuilder().setApnsProduction(isProduction).build())
                    .setPlatform(Platform.all())
                    .setAudience(Audience.tag(""))
                    .setAudience(Audience.registrationId(regesterIds))
                    .setNotification(Notification.newBuilder()
                            .addPlatformNotification(AndroidNotification.newBuilder()
                                    .setTitle(title)
                                    .setAlert(content)
                                    .addExtras(extras).build())
                            .addPlatformNotification(IosNotification.newBuilder()
                                    .setSound(iosSound)
                                    .setAlert(IosAlert.newBuilder().setTitleAndBody(title, "", content)
                                            .build())
                                    .addExtras(extras).build())
                            .build()).build();
        }
        if (iosSound.equals("default")) {
            return PushPayload.newBuilder().setOptions(Options.newBuilder().setApnsProduction(isProduction).build())
                    .setPlatform(Platform.all())
                    .setAudience(Audience.tag(""))
                    .setAudience(Audience.registrationId(regesterIds))
                    .setNotification(Notification.newBuilder()
                            .addPlatformNotification(AndroidNotification.newBuilder()
                                    .setTitle(title)
                                    .setAlert(content)
                                    .addExtras(extras).build())
                            .addPlatformNotification(IosNotification.newBuilder()
                                    .setSound(iosSound)
                                    .setMutableContent(true)
                                    .setAlert(IosAlert.newBuilder().setTitleAndBody(title, "", content)
                                            .build())
                                    .addExtras(extras).build())
                            .build()).build();
        }
        return PushPayload.newBuilder().setOptions(Options.newBuilder().setApnsProduction(isProduction).build())
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(""))
                .setAudience(Audience.registrationId(regesterIds))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title)
                                .setAlert(content)
                                .addExtras(extras).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setSound(iosSound)
                                .setAlert(IosAlert.newBuilder().setTitleAndBody(title, "", content)
                                        .build())
                                .addExtras(extras).build())
                        .build()).build();
    }

    /**
     * IOS提示音单独处理
     *
     * @param extras
     * @return
     */
    private static String getIosSound(Map<String, String> extras) {
        String iosSound = "default";
        String type = extras.get("Type");
        Log.info("Jpush -> getIosSound -> Type:" + type);
        if (StringUtils.isNotEmpty(type)) {
            if (type.equals("1")) {
                iosSound = "newOrder.caf";
            }
            if (type.equals("2")) {
                iosSound = "newOrder_fengniao.caf";
            }
            if (type.equals("3")) {
                iosSound = "refuseOrder_fengniao.caf";
            }
            if (type.equals("6")) {
                iosSound = "default";
            }
            if (type.equals("7")) {
                iosSound = "waitingForReceiveOrder.caf";
            }
        }
        return iosSound;
    }

    /**
     * 所有平台,推送目标是别名为 "alias",通知内容为 TEST
     *
     * @param alias
     * @param title
     * @param content
     * @return
     */
    public static PushPayload buildPushObjectAllAliasAlert(String alias, String title, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(content)).build();
    }

    /**
     * @Description: 所有平台推送Message
     * @author huangjianjun
     * @date 2017年4月10日 上午11:37:17
     */
    public static PushPayload buildPushObjectAllMessage(String title, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setMessage(Message.newBuilder()
                        .setTitle(title)
                        .setMsgContent(content).build()
                ).build();
    }


    /**
     * @Description: 根据设备终端ID推送消息
     * @author huangjianjun
     * @date 2017年4月10日 上午11:42:59
     */
    public static PushPayload buildPushObjectMsgByRegesterIds(List<String> regesterIds, String title, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(regesterIds))
                .setMessage(Message.newBuilder().setTitle(title).setMsgContent(content).build())
                .build();
    }

    /**
     * 向IOS推送消息
     *
     * @param title
     * @param content
     * @return
     */
    public static PushPayload buildPushIOSObjectAllAlert(String tag, String content, Map<String, String> extras) {

        String type = extras.get("Type");
        String OrderNo = extras.get("OrderNo");
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(tag))
                .setOptions(Options.newBuilder().setApnsProduction(isProduction).build())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(content)
                                .setSound("happy")
                                .addExtra("from", "JPush").addExtra("Type", type).addExtra("OrderNo", OrderNo)
                                .build())
                        .build())
                .build();
    }

    /**
     * 向android推送消息
     *
     * @param tag
     * @param content
     * @return
     */
    public static PushPayload buildPushAndroidObjectAllAlert(String tag, String content, Map<String, String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(tag))
                .setOptions(Options.newBuilder().setApnsProduction(isProduction).build())
                .setNotification(Notification.android(content, "雅堂小超人", extras))
                .build();
    }


}
