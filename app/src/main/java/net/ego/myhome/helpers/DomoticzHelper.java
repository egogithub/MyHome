package net.ego.myhome.helpers;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import net.ego.myhome.MainActivity;
import net.ego.myhome.MyHomeApp;
import net.ego.myhome.R;
import net.ego.myhome.pojo.DomoticzInfo;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.preference.PreferenceManager;

public class DomoticzHelper {
    public static final String TAG="DomoticzHelper";
    public static DomoticzInfo getDomoticzVersion() {
        try {
            Log.d(TAG, "Getting Domoticz Version");
            final HttpURLConnection connection = getHttpUrlConnection("/json.htm?type=command&param=getversion");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Content-Type", "application/json");

            final int responseCode = connection.getResponseCode();
            Log.d(TAG, "Response Code = " + responseCode);
            if (responseCode == 200) {
                Gson gson = new Gson();
                JsonObject domAnswerJson = gson.fromJson(new JsonReader(new InputStreamReader(connection.getInputStream(), "UTF-8")), JsonObject.class);
                String status = domAnswerJson.get("status").getAsString();
                Log.d(TAG, "Status = "+status);
                if (!status.equals("OK")) {
                    Log.e(TAG, "Got error");
                    return null;
                }
                String title = domAnswerJson.get("title").getAsString();
                Log.d(TAG, "Title = "+title);
                DomoticzInfo info = gson.fromJson(domAnswerJson, DomoticzInfo.class);
                return info;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Could not connect");
            return null;
        }
        Log.d(TAG, "Could not connect2");
        return null;
    }

    //public static void getUsedDevices

    private static HttpURLConnection getHttpUrlConnection(String api) throws Exception {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyHomeApp.getContext());
        String domo_ip = prefs.getString("domo_ip", null);
        if (null == domo_ip) {
            Log.d(TAG, "ip address is not defined");
            return null;
        }
        Log.d(TAG, "ip address = " + domo_ip);

        String domo_port;
        String prefix;
        boolean use_https = prefs.getBoolean("use_https", false);
        if (use_https) {
            prefix="https://";
            domo_port="443"; //TODO later: get from advanced prefs
        } else {
            prefix = "http://";
            domo_port="8080"; //TODO later: get from advanced prefs
        }

        final String URL_DOMOTICZ = prefix+domo_ip+":"+domo_port;
        String url = URL_DOMOTICZ+api;
        Log.d(TAG, url);
        return (HttpURLConnection) new URL(url).openConnection();
    }

}
