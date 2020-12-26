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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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

    Thread service_thread = null;

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

                try {
                    // 測試資料
                    //             [0]     [1] [2]     [3]    [4]
//                    sMsgBody = "%Khaos%, #A, 1";                  // 單開
//                    sMsgBody = "%Khaos%, #A, 0";                  // 單關
//                    sMsgBody = "%Khaos%, #B, 3";                  // 開三秒 (單位為秒!!!)
//                    sMsgBody = "%Khaos%, #C, 2,       2";         // 開2秒後關2秒(循環)
//                    sMsgBody = "%Khaos%, #D, 0010000, 2,     2";  // 排程(日123456), 某天中的第幾秒開啟, 某天中的第幾秒關閉
                    //

                    String[] msg = sMsgBody.split(",");
                    if (msg[0].equals("%Khaos%")) {
                        if (service_thread != null) {
                            service_thread.interrupt();
                            service_thread = null;
                        }

                        switch (msg[1]) {
                            case "#A":
                                Log.i("%Khaos%", "Class #A");
                                if (Integer.parseInt(msg[2].trim()) == 1)
                                    deviceON(device_ip.getText().toString());
                                else if (Integer.parseInt(msg[2].trim()) == 0)
                                    deviceOFF(device_ip.getText().toString());
                                break;
                            case "#B":
                                Log.i("%Khaos%", "Class #B");
                                final int time = Integer.parseInt(msg[2].trim()); // 秒
                                service_thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            deviceON(device_ip.getText().toString());
                                            Thread.sleep(time * 1000);
                                            deviceOFF(device_ip.getText().toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                service_thread.start();
                                break;
                            case "#C":
                                Log.i("%Khaos%", "Class #C");
                                final int time1 = Integer.parseInt(msg[2].trim()); // 秒
                                final int time2 = Integer.parseInt(msg[3].trim()); // 秒
                                service_thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            while (true) {
                                                deviceON(device_ip.getText().toString());
                                                Thread.sleep(time1 * 1000);
                                                deviceOFF(device_ip.getText().toString());
                                                Thread.sleep(time2 * 1000);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                service_thread.start();
                                break;
                            case "#D":
                                Log.i("%Khaos%", "Class #D");

                                final String ww = msg[2].trim(); // 日123456
                                final int t1 = Integer.parseInt(msg[3].trim()); // 秒
                                final int t2 = Integer.parseInt(msg[4].trim()); // 秒

                                if (t1 < t2) {
                                    service_thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                while (true) {
                                                    if (ww.charAt(getDayOfWeek() - 1) == '1') // Java 中星期 日、一、二、三、四、五、六,分別對應是 1 - 7
                                                    {
                                                        int now = (int) getSecondsPassedFromMidnight();
                                                        if (now > t1 && now < t2)
                                                            deviceON(device_ip.getText().toString());
                                                        else
                                                            deviceOFF(device_ip.getText().toString());
                                                    } else {
                                                        deviceOFF(device_ip.getText().toString());
                                                    }
                                                    Thread.sleep(60 * 1000);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    service_thread.start();
                                } else if (t1 > t2) {
                                    service_thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                while (true) {
                                                    if (ww.charAt(getDayOfWeek() - 1) == '1') // Java 中星期 日、一、二、三、四、五、六,分別對應是 1 - 7
                                                    {
                                                        int now = (int) getSecondsPassedFromMidnight();
                                                        if (now > t1 || now < t2)
                                                            deviceON(device_ip.getText().toString());
                                                        else
                                                            deviceOFF(device_ip.getText().toString());
                                                    } else {
                                                        deviceOFF(device_ip.getText().toString());
                                                    }
                                                    Thread.sleep(60 * 1000);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    service_thread.start();
                                }
                                break;
                        }

                        TextView server_log = (TextView) root.findViewById(R.id.server_log);
                        server_log.setText("## 號碼：\n" + sPhoneNo + '\n' +
                                "## 內容：\n" + sMsgBody + '\n' +
                                "\n\n" + server_log.getText());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Log.i("SmsReceiver", "sPhoneNo:" + sPhoneNo);
                Log.i("SmsReceiver", "sMsgBody:" + sMsgBody);
            }
        }, filter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(broadcastReceiver);

        if (service_thread != null) {
            service_thread.interrupt();
            service_thread = null;
        }
    }

    long getSecondsPassedFromMidnight() {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        long secondsPassed = passed / 1000;

        return secondsPassed;
    }

    int getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK);
    }

    void deviceON(final String deviceIP) {
        Log.i("## ", "deviceON: ");
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

//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(getActivity(), "Request Error!", Toast.LENGTH_LONG).show();
//                        }
//                    });
                }
            }
        }).start();
    }

    void deviceOFF(final String deviceIP) {
        Log.i("## ", "deviceOFF: ");
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

//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(getActivity(), "Request Error!", Toast.LENGTH_LONG).show();
//                        }
//                    });
                }
            }
        }).start();
    }
}