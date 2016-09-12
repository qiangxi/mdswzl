package com.lanma.lostandfound.receiver;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.lanma.lostandfound.activities.NotificationDetailActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者 任强强 on 2016/8/31 11:53.
 */
public class ServerMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "ServerMessageReceiver";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        //SDK 向 JPush Server 注册所得到的注册 ID
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        }
        //收到了自定义消息 Push,SDK 对自定义消息，只是传递，不会有任何界面上的展示。
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            Intent dataIntent = new Intent(context, NotificationDetailActivity.class);
//            dataIntent.putExtra("notificationTitle", bundle.getString(JPushInterface.EXTRA_TITLE));
//            dataIntent.putExtra("notificationTime", "2016-11-12 12:45:21");
//            dataIntent.putExtra("notificationContent", bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, dataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            Notification.Builder builder = new Notification.Builder(context);
//            builder.setContentTitle(bundle.getString(JPushInterface.EXTRA_TITLE))// 通知标题
//                    .setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE))// 通知内容
//                    .setSmallIcon(R.mipmap.ic_launcher)// 设置状态栏里面的图标（小图标）
//                    .setContentIntent(pendingIntent)// 通知点击时的意图
//                    .setTicker(bundle.getString(JPushInterface.EXTRA_MESSAGE))// 状态栏上显示
//                    .setAutoCancel(true)// 设置可以清除
//                    .setShowWhen(true);
//            Notification notification = builder.build();// 获取一个Notification
//            notification.defaults = Notification.DEFAULT_SOUND;// 设置为默认的声音
//            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;// 发起Notification后，铃声和震动均只执行一次
//            manager.notify(0, notification);// 显示通知
            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        }
        //收到了通知 Push。
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        }
        //用户点击了通知。
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            //打开自定义的Activity
            String notificationTitle = bundle.getString(JPushInterface.EXTRA_ALERT);
            String notificationContent = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent dataIntent = new Intent(context, NotificationDetailActivity.class);
            dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dataIntent.putExtra("notificationTitle", notificationTitle);
            dataIntent.putExtra("notificationContent", notificationContent);
            context.startActivity(dataIntent);
        }
        //收到了富文本push
        else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        }
        //与JPush服务端连接状态改变的push
        else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            // boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        }
        //其他类型的push
        else {
            //...
        }
    }
}
