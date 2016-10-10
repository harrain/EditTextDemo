package dazd.com.edittextdemo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatePickerDialog mDatePickerDialog;
    TimePickerDialog mTimePickerDialog;
    ProEditText dateEditText;
    ProEditText timeEditText;
    ProEditText companyEditText;

    String[] items = new String[]{"杭州迪安", "上海迪安", "郑州迪安", "合肥迪安", "南京迪安"};

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dateEditText = (ProEditText) findViewById(R.id.date_edit_text);
        timeEditText = (ProEditText) findViewById(R.id.time_edit_text);
        companyEditText = (ProEditText) findViewById(R.id.company_edit_text);

        dateEditText.setRightPicOnclickListener(new ProEditText.RightPicOnclickListener() {
            @Override
            public void rightPicClick() {
                Calendar calendar = Calendar.getInstance();
                mDatePickerDialog = new DatePickerDialog(MainActivity.this, null, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker datePicker = mDatePickerDialog.getDatePicker();
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        String monthStr = month < 10 ? "0" + month : "" + month;
                        String dayStr = day < 10 ? "0" + day : "" + day;
                        dateEditText.setText(year + "-" + monthStr + "-" + dayStr + "");
                    }
                });

                mDatePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                mDatePickerDialog.show();
            }
        });

        timeEditText.setRightPicOnclickListener(new ProEditText.RightPicOnclickListener() {
            @Override
            public void rightPicClick() {
                Calendar calendar = Calendar.getInstance();
                mTimePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int selectHour = view.getHour();
                        int selectMinute = view.getMinute();
                        String selectHourStr = selectHour < 10 ? "0" + selectHour : "" + selectHour;
                        String selectMinuteStr = selectMinute < 10 ? "0" + selectMinute : "" + selectMinute;
                        timeEditText.setText(selectHourStr + ":" + selectMinuteStr);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                mTimePickerDialog.show();
            }
        });

        companyEditText.setRightPicOnclickListener(new ProEditText.RightPicOnclickListener() {
            @Override
            public void rightPicClick() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请选择子公司");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        companyEditText.setText(items[which]);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
