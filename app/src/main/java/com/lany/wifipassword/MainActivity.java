package com.lany.wifipassword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    WebView w;
    String pwd="";
    String ssid="";
    String type="";
    String open="";
    String error="";
    String ret;
    int id=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        w = (WebView) this.findViewById(R.id.w1);
        pwd=getResources().getString(R.string.pwd);
        ssid=getResources().getString(R.string.ssid);

        type=getResources().getString(R.string.type);
        open=getResources().getString(R.string.open);
        error=getResources().getString(R.string.error);
        //RunAsRoot("busybox cp /data/misc/wifi/wpa_supplicant.conf /sdcard/wpa_supplicant.conf");
        String aa;
        aa=RunAsRoot("su -c cat /data/misc/wifi/wpa_supplicant.conf | busybox grep -E \"key_mgmt|ssid|psk\"");
        aa=aa.replace("\t", "<br>");
        aa=aa.replace("key_mgmt=NONE", "<font color=00ff00>"+type+": "+open+"</font><hr>");
        aa=aa.replace("key_mgmt=WPA-PSK", "<font color=ff0000>"+type+": "+"WPA-PSK</font><hr>");
        aa=aa.replace("ssid=", ssid+": ");
        aa=aa.replace("psk=", pwd+": ");
        aa=aa.replace("\"", "");
        w.loadData(aa, "text/html", null);
    }

    private String RunAsRoot(String cmd) {
        try {
            // Executes the command.
            Process process = Runtime.getRuntime().exec(cmd);

            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            String aa="";
            id=0;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
                aa += output.toString();
                ++id;
            }
            reader.close();

            // Waits for the command to finish.
            process.waitFor();

            ret = id == 0 ? error+"<hr>" : aa;
        } catch (IOException e) {
            return e.toString();
        } catch (InterruptedException e) {
            return e.toString();
        }
        return ret;
    }

}
