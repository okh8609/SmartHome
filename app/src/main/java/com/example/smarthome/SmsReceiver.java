package com.example.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving an Intent broadcast.

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
        Toast.makeText(context, "#號碼：" + sPhoneNo +
                "\n#內容：" + sMsgBody, Toast.LENGTH_LONG).show();
    }
}
