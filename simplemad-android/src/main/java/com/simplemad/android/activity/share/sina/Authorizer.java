package com.simplemad.android.activity.share.sina;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.simplemad.android.activity.RequestAndResultCode;
import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

public class Authorizer{

    private static final String CONSUMER_KEY = "2649418277";
    private static final String CONSUMER_SECRET = "5d262eb00c46bd1c6c1cfca641503191";

    private Activity activity;
    
    public Authorizer(Activity context) {
    	this.activity = context;
    }
    
    public void authorize() {
    	Weibo weibo = Weibo.getInstance();

        // !!Don't forget to set app_key and secret before get
        // token!!!
        weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);

        // Oauth2.0 隐式授权认证方式
        weibo.setRedirectUrl("http://www.sina.com.cn");
        weibo.authorize(activity, new AuthDialogListener());
    }

    class AuthDialogListener implements WeiboDialogListener {

        @Override
        public void onComplete(Bundle values) {
            String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
            accessToken.setExpiresIn(expires_in);
            Weibo.getInstance().setAccessToken(accessToken);
            invokeShare();
        }
        
        private void invokeShare() {
        	 File file = Environment.getExternalStorageDirectory();
             String sdPath = file.getAbsolutePath();
             // 请保证SD卡根目录下有这张图片文件
             String picPath = sdPath + "/" + "abc.jpg";
             File picFile = new File(picPath);
             if (!picFile.exists()) {
                 Toast.makeText(activity, "图片" + picPath + "不存在！", Toast.LENGTH_SHORT)
                         .show();
                 picPath = null;
             }
             try {
                 Intent i = share2weibo("abc", picPath);
                 activity.startActivityForResult(i, RequestAndResultCode.RequestCode.REQUEST_SHARE);

             } catch (WeiboException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             } finally {

             }
        }
        
        private Intent share2weibo(String content, String picPath) throws WeiboException {
            Weibo weibo = Weibo.getInstance();
            return weibo.share2weibo(activity, weibo.getAccessToken().getToken(), weibo.getAccessToken()
                    .getSecret(), content, picPath);
        }

        @Override
        public void onError(DialogError e) {
            Toast.makeText(activity.getApplicationContext(), "Auth error : " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(activity.getApplicationContext(), "Auth cancel", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(activity.getApplicationContext(), "Auth exception : " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

}