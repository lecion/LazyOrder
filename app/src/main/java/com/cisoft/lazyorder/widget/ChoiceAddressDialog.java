package com.cisoft.lazyorder.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.sureinfo.Build;
import com.cisoft.lazyorder.bean.sureinfo.BuildChoiceCounter;
import com.cisoft.lazyorder.core.sureinfo.BuildService;
import com.cisoft.lazyorder.ui.sureinfo.BuildListAdapter;
import org.kymjs.aframe.utils.StringUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2014/11/7.
 */
public class ChoiceAddressDialog extends Dialog {

    public ChoiceAddressDialog(Context context) {
        super(context);
    }

    public ChoiceAddressDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ChoiceAddressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder{

        private Context context;
        private String title;
        private String message;
        private EditText etInputRoomNum;
        private TextView tvTipInfo;
        private ListView lvSelectBuild;

        private int schoolId = 0;
        private OnAddressSelectedListener addressSelectedListener;
        private BuildService buildService;
        private BuildListAdapter buildListAdapter;
        private BuildChoiceCounter BuildChoiceCounter = new BuildChoiceCounter();
        private List<Build> buildListData = new ArrayList<Build>();
        private Build selectedBuildObj;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setBuildService(BuildService buildService){
            this.buildService = buildService;
            return this;
        }

        public Builder setSchoolId(int schoolId){
            this.schoolId = schoolId;
            return this;
        }


        /**
         * 设置地址选择完成后的回调监听器
         */
        public void setOnAddressSelectedListener(OnAddressSelectedListener addressSelectedListener) {
            this.addressSelectedListener = addressSelectedListener;
        }


        private View createLayout(){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_sure_info_select_address_dialog_layout, null);


            final LinearLayout llLoadingBuildListTip = (LinearLayout) layout.findViewById(R.id.llLoadingBuildListTip);
            etInputRoomNum = (EditText) layout.findViewById(R.id.etInputRoomNum);
            tvTipInfo = (TextView) layout.findViewById(R.id.tvTipInfo);
            lvSelectBuild = (ListView) layout.findViewById(R.id.lvSelectBuild);
            buildListAdapter = new BuildListAdapter(context, buildListData, BuildChoiceCounter);
            lvSelectBuild.setAdapter(buildListAdapter);
            lvSelectBuild.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    selectedBuildObj = (Build) adapterView.getItemAtPosition(position);

                    BuildChoiceCounter.setBuildChoicePos(position);
                    buildListAdapter.refresh();
                }
            });
            if(schoolId != 0 && buildService != null){
                buildService.loadBuildListFromNet(schoolId, new ChoiceAddressDialog.BuildDataLoadFinish() {
                    @Override
                    public void onLoadFinish(List<Build> builds) {
                        llLoadingBuildListTip.setVisibility(View.GONE);
                        lvSelectBuild.setVisibility(View.VISIBLE);

                        buildListAdapter.clearAll();
                        buildListAdapter.addData(builds);
                        buildListAdapter.refresh();
                    }
                });
            }
            return layout;
        }



        public AlertDialog create() {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            if (title != null) {
                builder.setTitle(title);
            }

            if (message != null) {
                builder.setMessage(message);
            } else {
                builder.setView(createLayout());
            }

            builder.setPositiveButton("确定", new OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    cheatSytemForDialogClosed(dialogInterface);
                    if(onPositiveButtonClick()){
                        closeDialog(dialogInterface);
                    }
                }
            });
            builder.setNegativeButton("取消", new OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   closeDialog(dialogInterface);
                }
            });

            return builder.create();
        }

        /**
         *
         * @return 是否往下继续执行
         */
        public boolean onPositiveButtonClick() {
            if(message != null){
                return true;
            }

            String enteredRoomNum = etInputRoomNum.getText().toString();
            if (StringUtils.isEmpty(enteredRoomNum)) {
                tvTipInfo.setVisibility(View.VISIBLE);
                tvTipInfo.setTextColor(Color.RED);
                tvTipInfo.setText("请输入所在寝室号");
                return false;
            }
            if (selectedBuildObj == null) {
                tvTipInfo.setVisibility(View.VISIBLE);
                tvTipInfo.setTextColor(Color.RED);
                tvTipInfo.setText("请选择所在楼栋");
                return false;
            }

            tvTipInfo.setVisibility(View.GONE);
            tvTipInfo.setTextColor(0xff33b5e5);

            if(addressSelectedListener != null){
                addressSelectedListener.onSelected(selectedBuildObj, Integer.parseInt(enteredRoomNum));
            }

            return true;
        }



        /**
         * 欺骗系统对话框已经关闭
         * @param mDialogInterface
         */
        private void cheatSytemForDialogClosed(DialogInterface mDialogInterface) {
            if (mDialogInterface != null) {
                try {
                    Field field = mDialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);
                    field.set(mDialogInterface, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 唯一使对话框消息的方法
         * @param mDialogInterface
         */
        private void closeDialog(DialogInterface mDialogInterface) {
            if (mDialogInterface != null) {
                try {
                    Field field = mDialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);
                    field.set(mDialogInterface, true);
                    mDialogInterface.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }


    /**
     * 地址选择完成后的回调监听器
     */
    public interface OnAddressSelectedListener {
        public void onSelected(Build selectedBuild, int roomNum);
    }

    /**
     * 加载楼栋数据完成后的回调接口
     */
    public interface BuildDataLoadFinish {
        public void onLoadFinish(List<Build> buildList);
    }
}
