package com.example.lsl017.starterapplications;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by lsl017 on 6/5/2015.
 */
public class LoadJSON extends AsyncTask<Void, Void, String>  {

    public AsyncResponse mCallback;

    public LoadJSON(AsyncResponse asyncResponse){
        mCallback = asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public interface AsyncResponse {
        void processFinish(String JSONString);
    }

    public HttpsURLConnection load() {

        HttpsURLConnection urlConnection = null;

        try {
            URL url = new URL("https://devssg.amfam.com:8443/afipresents/v1/config");
            urlConnection = (HttpsURLConnection) url.openConnection();
        } catch (MalformedURLException badURLException) {
            badURLException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return urlConnection;
    }

    public String loadJSONData(HttpsURLConnection urlConnection) {

        StringBuilder builtJSONString = new StringBuilder();

        if (urlConnection != null) {
            BufferedReader br = null;

            try{
                InputStream is = urlConnection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                while(br.ready()){
                    String streamLine = br.readLine();
                    builtJSONString.append(streamLine);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
        return builtJSONString.toString();
    }

    @Override
    protected String doInBackground(Void... params) {
        return loadJSONData(load());
    }

    @Override
    protected void onPostExecute(String result){
        mCallback.processFinish(result);
    }
}
