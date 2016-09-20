package com.lanma.lostandfound.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lanma.lostandfound.R;

/**
 * 作者 任强强 on 2016/9/11 16:27.
 */
public class DeleteInfoDialog extends BaseDialog {
    private Context mContext;
    private TextView dialogMessage;
    private Button dialogLeftButton;
    private Button dialogRightButton;
    private View.OnClickListener mListener;
    private String messsage;

    public DeleteInfoDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_delete_info_layout, null);
        dialogMessage = (TextView) view.findViewById(R.id.dialogMessage);
        dialogLeftButton = (Button) view.findViewById(R.id.dialogDismiss);
        dialogRightButton = (Button) view.findViewById(R.id.dialogDelete);
        view.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));
        view.setScaleX(0.8f);
        view.setScaleY(0.8f);
        return view;
    }

    public void setDeleteMessage(String message) {
        this.messsage = message;
    }

    public void setOnDeleteClickListener(View.OnClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void setUiBeforShow() {
        setCanceledOnTouchOutside(false);
        dialogMessage.setText(messsage);
        dialogLeftButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialogRightButton.setOnClickListener(mListener);
    }
}
