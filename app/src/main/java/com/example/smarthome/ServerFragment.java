package com.example.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //filter.setPriority(18);
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

                String msg = "## 號碼：" + sPhoneNo + "\n## 內容：" + sMsgBody;
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
}