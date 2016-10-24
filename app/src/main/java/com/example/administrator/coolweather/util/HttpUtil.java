package com.example.administrator.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/10/23.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address, final HttpCallbackListener httpCallbackListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;
        try {
            URL url=new URL(address);
            httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(8000);
            httpURLConnection.setConnectTimeout(8000);
            inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder builder=new StringBuilder();
            while((line=bufferedReader.readLine())!=null){
                builder.append(line);
            }
            String response=builder.toString();
            if(httpCallbackListener!=null){
                httpCallbackListener.onFinished(response);
            }

        } catch (IOException e) {
            if(httpCallbackListener!=null){
                httpCallbackListener.onError(e);
            }
        } finally {
            assert httpURLConnection != null;
            httpURLConnection.disconnect();
        }

            }
        }).start();


    }


}
