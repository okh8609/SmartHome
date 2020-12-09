package com.example.smarthome;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
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

    EditText server_ip;

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

        // region Find Views
        once_ = ((LinearLayout) root.findViewById(R.id.once_));
        once_on = ((Button) root.findViewById(R.id.once_on));
        once_off = ((Button) root.findViewById(R.id.once_off));

        prid = ((RadioButton) root.findViewById(R.id.prid));
        prid_ = ((RelativeLayout) root.findViewById(R.id.prid_));
        prid_time = ((EditText) root.findViewById(R.id.prid_time));
        prid_spinner = ((Spinner) root.findViewById(R.id.prid_spinner));
        prid_go = ((Button) root.findViewById(R.id.prid_go));

        rept = ((RadioButton) root.findViewById(R.id.rept));
        rept_ = ((LinearLayout) root.findViewById(R.id.rept_));
        rept_time1 = ((EditText) root.findViewById(R.id.rept_time1));
        rept_spinner1 = ((Spinner) root.findViewById(R.id.rept_spinner1));
        rept_time2 = ((EditText) root.findViewById(R.id.rept_time2));
        rept_spinner2 = ((Spinner) root.findViewById(R.id.rept_spinner2));
        rept_go = ((Button) root.findViewById(R.id.rept_go));

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
        // endregion

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
}