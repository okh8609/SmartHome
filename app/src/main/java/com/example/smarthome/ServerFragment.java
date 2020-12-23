package com.example.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ServerFragment extends Fragment {

    public ServerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ServerFragment newInstance() {
        ServerFragment fragment = new ServerFragment();
        return fragment;
    }

    BroadcastReceiver broadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.server_fragment, container, false);

        final EditText device_ip = ((EditText) root.findViewById(R.id.device_ip));
        Button test_on = ((Button) root.findViewById(R.id.test_on));
        Button test_off = ((Button) root.findViewById(R.id.test_off));

        test_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceON(device_ip.getText().toString());
            }
        });

        test_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceOFF(device_ip.getText().toString());
            }
        });

        // 收到SMS
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        getContext().registerReceiver(broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();

                String sPhoneNo = "";
                String sMsgBody = "";

                if (bundle != null) {
                    //The messages are stored in an Object array in the PDU format.
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] smsMsgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++)
                        smsMsgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    //取得簡訊發送人的電話號碼
                    sPhoneNo = smsMsgs[0].getDisplayOriginatingAddress();

                    //取得簡訊內容
                    for (SmsMessage msg : smsMsgs)
                        sMsgBody += msg.getDisplayMessageBody();
                }

                Log.i("SmsReceiver", "sPhoneNo:" + sPhoneNo);
                Log.i("SmsReceiver", "sMsgBody:" + sMsgBody);

                String msg = "## 號碼：\n" + sPhoneNo + "\n## 內容：\n" + sMsgBody;
                // Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                TextView server_log = (TextView) root.findViewById(R.id.server_log);
                server_log.setText(msg + "\n\n\n" + server_log.getText());
            }
        }, filter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(broadcastReceiver);
    }

    void deviceON(final String deviceIP) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // http://192.168.50.152/io/1
                    URL url = new URL("http://" + deviceIP + "/io/1");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000); //set timeout to 5 seconds
                    conn.setDoInput(true); //允許輸入流，即允許下載
                    conn.setUseCaches(false); //設置是否使用緩存

                    // 讀資料
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String response = "";
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response += line + '\n';
                    }
                    reader.close();

                    conn.disconnect();
                    Log.d("## Response ", response);
                } catch (Exception e) {
                    e.printStackTrace();

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "Request Error!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    void deviceOFF(final String deviceIP) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // http://192.168.50.152/io/0
                    URL url = new URL("http://" + deviceIP + "/io/0");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000); //set timeout to 5 seconds
                    conn.setDoInput(true); //允許輸入流，即允許下載
                    conn.setUseCaches(false); //設置是否使用緩存

                    // 讀資料
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String response = "";
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response += line + '\n';
                    }
                    reader.close();

                    conn.disconnect();
                    Log.d("## Response ", response);
                } catch (Exception e) {
                    e.printStackTrace();

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "Request Error!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }
}