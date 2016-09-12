package com.lanma.lostandfound.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lanma.lostandfound.R;

public class LoadingDialog extends BaseDialog {

	private Context mContext;
	public LoadingDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading_layout, null);
		view.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#77000000"), dp2px(5)));
		view.setScaleX(0.4f);
		view.setScaleY(0.4f);
		return view;
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void setUiBeforShow() {
		setCanceledOnTouchOutside(false);
	}

}
