package com.example.smarthome;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

public class ClientFragment extends Fragment {

    public ClientFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ClientFragment newInstance() {
        ClientFragment fragment = new ClientFragment();
        return fragment;
    }

    EditText server_phoneNu;
    Button sms_test;

    RadioButton once;
    LinearLayout once_;
    Button once_on;
    Button once_off;

    RadioButton prid;
    RelativeLayout prid_;
    EditText prid_time;
    Spinner prid_spinner;
    Button prid_go;

    RadioButton rept;
    LinearLayout rept_;
    EditText rept_time1;
    Spinner rept_spinner1;
    EditText rept_time2;
    Spinner rept_spinner2;
    Button rept_go;

    RadioButton scdl;
    LinearLayout scdl_;
    CheckBox week1;
    CheckBox week2;
    CheckBox week3;
    CheckBox week4;
    CheckBox week5;
    CheckBox week6;
    CheckBox week7;
    EditText scdl_time1;
    EditText scdl_time2;
    Button scdl_go;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.client_fragment, container, false);

        server_phoneNu = ((EditText) root.findViewById(R.id.server_phoneNu));
        sms_test = ((Button) root.findViewById(R.id.sms_test));
        sms_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSmsg(server_phoneNu.getText().toString(), "TEST!!");
            }
        });

        once = ((RadioButton) root.findViewById(R.id.once));
        once_ = ((LinearLayout) root.findViewById(R.id.once_));
        once_on = ((Button) root.findViewById(R.id.once_on));
        once_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSmsg(server_phoneNu.getText().toString(), "%Khaos%,#A,1");
            }
        });
        once_off = ((Button) root.findViewById(R.id.once_off));
        once_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSmsg(server_phoneNu.getText().toString(), "%Khaos%,#A,1");
            }
        });

        prid = ((RadioButton) root.findViewById(R.id.prid));
        prid_ = ((RelativeLayout) root.findViewById(R.id.prid_));
        prid_time = ((EditText) root.findViewById(R.id.prid_time));
        prid_spinner = ((Spinner) root.findViewById(R.id.prid_spinner));
        prid_go = ((Button) root.findViewById(R.id.prid_go));
        prid_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = Integer.parseInt(prid_time.getText().toString());
                String unit = prid_spinner.getSelectedItem().toString();
                switch (unit) {
                    case "秒":
                        time *= 1;
                        break;
                    case "分":
                        time *= 60;
                        break;
                    case "時":
                        time *= 60 * 60;
                        break;
                }
                sendSMSmsg(server_phoneNu.getText().toString(), "%Khaos%,#B," + String.valueOf(time));
            }
        });

        rept = ((RadioButton) root.findViewById(R.id.rept));
        rept_ = ((LinearLayout) root.findViewById(R.id.rept_));
        rept_time1 = ((EditText) root.findViewById(R.id.rept_time1));
        rept_spinner1 = ((Spinner) root.findViewById(R.id.rept_spinner1));
        rept_time2 = ((EditText) root.findViewById(R.id.rept_time2));
        rept_spinner2 = ((Spinner) root.findViewById(R.id.rept_spinner2));
        rept_go = ((Button) root.findViewById(R.id.rept_go));
        rept_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time1 = Integer.parseInt(rept_time1.getText().toString());
                String unit1 = rept_spinner1.getSelectedItem().toString();
                switch (unit1) {
                    case "秒":
                        time1 *= 1;
                        break;
                    case "分":
                        time1 *= 60;
                        break;
                    case "時":
                        time1 *= 60 * 60;
                        break;
                }

                int time2 = Integer.parseInt(rept_time2.getText().toString());
                String unit2 = rept_spinner2.getSelectedItem().toString();
                switch (unit2) {
                    case "秒":
                        time2 *= 1;
                        break;
                    case "分":
                        time2 *= 60;
                        break;
                    case "時":
                        time2 *= 60 * 60;
                        break;
                }

                sendSMSmsg(server_phoneNu.getText().toString(), "%Khaos%,#C," + String.valueOf(time1) + "," + String.valueOf(time2));
            }
        });

        scdl = ((RadioButton) root.findViewById(R.id.scdl));
        scdl_ = ((LinearLayout) root.findViewById(R.id.scdl_));
        week1 = ((CheckBox) root.findViewById(R.id.week1));
        week2 = ((CheckBox) root.findViewById(R.id.week2));
        week3 = ((CheckBox) root.findViewById(R.id.week3));
        week4 = ((CheckBox) root.findViewById(R.id.week4));
        week5 = ((CheckBox) root.findViewById(R.id.week5));
        week6 = ((CheckBox) root.findViewById(R.id.week6));
        week7 = ((CheckBox) root.findViewById(R.id.week7));
        scdl_time1 = ((EditText) root.findViewById(R.id.scdl_time1));
        scdl_time2 = ((EditText) root.findViewById(R.id.scdl_time2));
        scdl_go = ((Button) root.findViewById(R.id.scdl_go));
        scdl_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scdl_time1.getText().toString() == "" || scdl_time2.getText().toString() == "")
                    return;

                int time1 = (int) scdl_time1.getTag(R.id.HH) * 60 * 60 + (int) scdl_time1.getTag(R.id.MM) * 60;
                int time2 = (int) scdl_time2.getTag(R.id.HH) * 60 * 60 + (int) scdl_time2.getTag(R.id.MM) * 60;

                // Java 中星期 日、一、二、三、四、五、六,分別對應是 1 - 7
                String ww = "";
                ww += (week1.isChecked() ? "1" : "0");
                ww += (week2.isChecked() ? "1" : "0");
                ww += (week3.isChecked() ? "1" : "0");
                ww += (week4.isChecked() ? "1" : "0");
                ww += (week5.isChecked() ? "1" : "0");
                ww += (week6.isChecked() ? "1" : "0");
                ww += (week7.isChecked() ? "1" : "0");

                sendSMSmsg(server_phoneNu.getText().toString(), "%Khaos%,#D," + ww + ","
                        + String.valueOf(time1) + "," + String.valueOf(time2));
            }
        });

        once.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                once_.setVisibility(View.VISIBLE);
                prid_.setVisibility(View.GONE);
                rept_.setVisibility(View.GONE);
                scdl_.setVisibility(View.GONE);
            }
        });
        prid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                once_.setVisibility(View.GONE);
                prid_.setVisibility(View.VISIBLE);
                rept_.setVisibility(View.GONE);
                scdl_.setVisibility(View.GONE);
            }
        });
        rept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                once_.setVisibility(View.GONE);
                prid_.setVisibility(View.GONE);
                rept_.setVisibility(View.VISIBLE);
                scdl_.setVisibility(View.GONE);
            }
        });
        scdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                once_.setVisibility(View.GONE);
                prid_.setVisibility(View.GONE);
                rept_.setVisibility(View.GONE);
                scdl_.setVisibility(View.VISIBLE);
            }
        });
        once.setChecked(true);
        once_.setVisibility(View.VISIBLE);
        prid_.setVisibility(View.GONE);
        rept_.setVisibility(View.GONE);
        scdl_.setVisibility(View.GONE);

        scdl_time1.setFocusable(false);
        scdl_time1.setClickable(true);
        scdl_time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Calendar calendar = Calendar.getInstance();
                // 實作TimePickerDialog的onTimeSet方法，設定時間後將所設定的時間show在textTime上
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ((EditText) v).setText((hourOfDay > 12 ? "PM " : "AM ") +
                                (hourOfDay > 12 ? hourOfDay - 12 : hourOfDay) + ":" + minute);
                        v.setTag(R.id.HH, hourOfDay);
                        v.setTag(R.id.MM, minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        scdl_time2.setFocusable(false);
        scdl_time2.setClickable(true);
        scdl_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Calendar calendar = Calendar.getInstance();
                // 實作TimePickerDialog的onTimeSet方法，設定時間後將所設定的時間show在textTime上
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ((EditText) v).setText((hourOfDay > 12 ? "PM " : "AM ") +
                                (hourOfDay > 12 ? hourOfDay - 12 : hourOfDay) + ":" + minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        return root;
    }

    protected void sendSMSmsg(String phoneNu, String message) {

        Log.i("## Send SMS", phoneNu + " : " + message);

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNu, null, message, null, null);
            Toast.makeText(getContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "SMS faild.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}