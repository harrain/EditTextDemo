# Android实现EditText响应drawableRight点击事件 #

这里自定义了一个ProEditText，继承AppCompatEditText，并且在其三个构造函数中调用父组件的相应构造函数，是为了适配5.0以下应用MD风格，代码如下：

    public ProEditText(Context context) {
        super(context);
        init(context);
    }

    public ProEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

我们或许还需要定义一个右侧图标变量的引用，你可以对这个右侧图标干些自定义的事，代码如下：

    /**
     * EditText右侧的图标
     */
    protected Drawable mRightDrawable;

    private void init(final Context context) {
        mRightDrawable = getCompoundDrawables()[2];

        if (mRightDrawable == null) {
			//这里当没有设置右侧图标时你可以给它设置个默认的右侧图标，当然根据你的项目需求来
            return;
        }

		//这里当设置了右侧图标时，我们对图标做了一些自定义设置，你也可以做其他设置
        mRightDrawable.setBounds(0, 0, mRightDrawable.getIntrinsicWidth(), mRightDrawable.getIntrinsicHeight());
    }

最关键的来了，我们需要重写自定义EditText的触摸事件onTouchEvent，这里我们通过判断触摸的坐标位置是否在右侧图标范围内来模拟右侧图标的点击事件。如果在这个范围内，则证明点击了右侧图标，为了方便外部调用处理，我们定义了一个接口在这里回调，一旦点击了右侧图标，则触发接口回调。代码如下：

    public void setRightPicOnclickListener(RightPicOnclickListener rightPicOnclickListener) {
        this.rightPicOnclickListener = rightPicOnclickListener;
    }

    public interface RightPicOnclickListener {
        void rightPicClick();
    }

    private RightPicOnclickListener rightPicOnclickListener;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {

                    //设置点击EditText右侧图标EditText失去焦点，
                    // 防止点击EditText右侧图标EditText获得焦点，软键盘弹出
                    setFocusableInTouchMode(false);
                    setFocusable(false);

                    //点击EditText右侧图标事件接口回调
                    if (rightPicOnclickListener != null) {
                        rightPicOnclickListener.rightPicClick();
                    }
                } else {
                    //设置点击EditText输入区域，EditText请求焦点，软键盘弹出，EditText可编辑
                    setFocusableInTouchMode(true);
                    setFocusable(true);
                }
            }
        }
        return super.onTouchEvent(event);
    }

外部应用就简单了，示例代码如下：

    <dazd.com.edittextdemo.ProEditText
        android:id="@+id/date_edit_text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:drawableRight="@drawable/pic_date"
        android:drawableEnd="@drawable/pic_date"
        android:inputType="date"
        android:hint="@string/please_select_date"/>


	DatePickerDialog mDatePickerDialog;
    ProEditText dateEditText;
    dateEditText = (ProEditText) findViewById(R.id.date_edit_text);
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

实现这样一个功能，我们除了像上面这样干之外，还可以直接使用布局包裹一个EditText和一个ImageView，然后给ImageView设置点击事件即可，这种方法稍微简单点。