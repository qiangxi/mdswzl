package com.lanma.lostandfound.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lanma.lostandfound.R;
import com.lanma.lostandfound.utils.NetWorkUtils;

/**
 * 网络情况提醒Dialog
 *
 * @author Administrator
 */
public class NetworkSettingDialog extends BaseDialog {

    private TextView dialogTitle;
    private TextView dialogContent;
    private Button dialogLeftButton;
    private Button dialogRightButton;
    private Context mContext;

    public NetworkSettingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_setting_network_layout, null);
        dialogTitle = (TextView) view.findViewById(R.id.dialogTitle);
        dialogContent = (TextView) view.findViewById(R.id.dialogContent);
        dialogLeftButton = (Button) view.findViewById(R.id.dialogLeftButton);
        dialogRightButton = (Button) view.findViewById(R.id.dialogRightButton);
        view.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));
        view.setScaleX(0.8f);
        view.setScaleY(0.8f);
        dialogTitle.setText("未连接到网络!");
        dialogContent.setText("请检查WiFi或数据是否开启");
        return view;
    }

    @Override
    public void onBackPressed() {
        setIsFirstReceiveToTrue();
        super.onBackPressed();
    }

    private void setIsFirstReceiveToTrue() {
        SharedPreferences preferences = mContext.getSharedPreferences("ReceiveNetChange", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("isFirstReceive", true).apply();
    }

    @Override
    public void setUiBeforShow() {
        setCanceledOnTouchOutside(false);
        dialogLeftButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setIsFirstReceiveToTrue();
                dismiss();
            }
        });
        dialogRightButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setIsFirstReceiveToTrue();
                dismiss();
                NetWorkUtils.openSettingNetActivity(mContext);
            }
        });

    }

}
