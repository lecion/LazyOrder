package com.cisoft.lazyorder.ui.sureinfo;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.sureinfo.Build;
import com.cisoft.lazyorder.core.sureinfo.BuildService;
import com.cisoft.lazyorder.core.sureinfo.OrderService;
import com.cisoft.lazyorder.finals.SPConstants;
import com.cisoft.lazyorder.widget.ChoiceAddressDialog;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;
import org.kymjs.aframe.utils.PreferenceHelper;
import org.kymjs.aframe.utils.StringUtils;

public class SureInfoActivity extends BaseActivity {


    @BindView(id = R.id.tvPhoneNum)
    private TextView tvPhoneNum;
    @BindView(id = R.id.etInputPhoneNum)
    private EditText etInputPhoneNum;
    @BindView(id = R.id.btnSelectPhoneNum, click = true)
    private Button btnSelectPhoneNum;

    @BindView(id = R.id.tvName)
    private TextView tvName;
    @BindView(id = R.id.etInputName)
    private EditText etInputName;
    @BindView(id = R.id.btnChangeName, click = true)
    private Button btnChangeName;

    @BindView(id = R.id.tvAddress)
    private TextView tvAddress;
    @BindView(id = R.id.btnSelectAddress, click = true)
    private Button btnSelectAddress;

    @BindView(id = R.id.btnSureInfo, click = true)
    private Button btnSureInfo;

    //是否是正在输入电话号码或姓名的状态标记位
    private boolean isInputPhoneNum = false;
    private boolean isInputName = false;

    private int schoolId = 0;
    private BuildService buildService;
    private OrderService orderService;




    public SureInfoActivity(){
        setHiddenActionBar(false);
    }


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_sure_info);
    }


    @Override
    protected void initData() {
        buildService = new BuildService(this);
        orderService = new OrderService(this);
    }


    @Override
    protected void initWidget() {
        initActionBar();
        initDefaultData();
    }


    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setTitle("  确认信息");
    }

    private void initDefaultData(){
        //读取默认的姓名并设置上
        String defName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_NAME, getResources().getString(R.string.input_name_hint));
        tvName.setText(defName);

        //读取默认的电话号码并设置上
        String defPhoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_PHONE_NUM, getResources().getString(R.string.input_phone_num_hint));
        tvPhoneNum.setText(defPhoneNum);

        //读取默认的联系地址并设置上
        String defAddress = getResources().getString(R.string.input_contact_address_hint);
        String recentBuildName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_BUILD_NAME, null);
        int recentRoomNum = PreferenceHelper.readInt(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_ROOM_NUM, 0);
        if(recentBuildName != null && recentRoomNum != 0){
            defAddress = recentBuildName + recentRoomNum;
        }
        tvAddress.setText(defAddress);


        //读取保存的学校id
        schoolId = PreferenceHelper.readInt(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_SCHOOL_ID, 1);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.btnSelectPhoneNum:
                if(isInputPhoneNum)
                    savePhoneNum();
                else
                    inputPhoneNum();
                break;
            case R.id.btnChangeName:
                if(isInputName)
                    saveName();
                else
                    inputName();
                break;
            case R.id.btnSelectAddress:
                selectAddress();
                break;
            case R.id.btnSureInfo:
                selectAddress();
                break;
        }
    }

    /**
     * 输入电话号码
     */
    private void inputPhoneNum(){
        tvPhoneNum.setVisibility(View.GONE);
        etInputPhoneNum.setVisibility(View.VISIBLE);
        btnSelectPhoneNum.setText("确定");
        isInputPhoneNum = true;
    }

    /**
     * 电击确定后保存输入的电话号码
     */
    private void savePhoneNum(){
        tvPhoneNum.setVisibility(View.VISIBLE);
        etInputPhoneNum.setVisibility(View.GONE);
        btnSelectPhoneNum.setText("更改");
        isInputPhoneNum = false;
        String phoneNum = etInputPhoneNum.getText().toString();
        if(StringUtils.isEmpty(phoneNum)){
            phoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                    SPConstants.KEY_RECENT_PHONE_NUM, getResources().getString(R.string.input_phone_num_hint));
        } else {
            PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                    SPConstants.KEY_RECENT_PHONE_NUM, phoneNum);
        }
        tvPhoneNum.setText(phoneNum);
    }

    /**
     * 输入姓名
     */
    private void inputName(){
        tvName.setVisibility(View.GONE);
        etInputName.setVisibility(View.VISIBLE);
        btnChangeName.setText("确定");
        isInputName = true;
    }

    /**
     * 点击确定后保存输入的姓名
     */
    private void saveName(){
        tvName.setVisibility(View.VISIBLE);
        etInputName.setVisibility(View.GONE);
        btnChangeName.setText("更改");
        isInputName = false;
        String name = etInputName.getText().toString();
        if(StringUtils.isEmpty(name)){
            name = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                    SPConstants.KEY_RECENT_NAME, getResources().getString(R.string.input_name_hint));
        } else {
            PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                    SPConstants.KEY_RECENT_NAME, name);
        }
        tvName.setText(name);
    }

    /**
     * 创建一个选择楼栋信息的对话框
     * @return
     */
    private void selectAddress(){
        ChoiceAddressDialog.Builder builder = new ChoiceAddressDialog.Builder(this);
        builder.setBuildService(buildService);
        builder.setSchoolId(schoolId);
        builder.setTitle("请确认联系地址");
        builder.setOnAddressSelectedListener(new ChoiceAddressDialog.OnAddressSelectedListener() {
            @Override
            public void onSelected(Build selectedBuild, int roomNum) {
                String address = selectedBuild.getName() + roomNum;
                tvAddress.setText(address);

                PreferenceHelper.write(SureInfoActivity.this, SPConstants.SP_FILE_NAME,
                        SPConstants.KEY_RECENT_BUILD_NAME, selectedBuild.getName());
                PreferenceHelper.write(SureInfoActivity.this, SPConstants.SP_FILE_NAME,
                        SPConstants.KEY_RECENT_ROOM_NUM, roomNum);
            }
        });
        builder.create().show();
    }


    /**
     * 提交订单
     */
    private void submitOrder(){

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sure_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
