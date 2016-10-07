package com.dou361.live.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.dou361.live.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ========================================
 * <p>
 * 版 权：dou361.com 版权所有 （C） 2015
 * <p>
 * 作 者：陈冠明
 * <p>
 * 个人网站：http://www.dou361.com
 * <p>
 * 版 本：1.0
 * <p>
 * 创建日期：2016/10/7 13:28
 * <p>
 * 描 述：截图对话框
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class ScreenshotDialog extends Dialog {

    @BindView(R.id.imageview)
    ImageView imageView;

    private Bitmap bitmap;

    public ScreenshotDialog(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_screenshot);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @OnClick({R.id.btn_share, R.id.btn_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                break;
            case R.id.btn_cancel:
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
                dismiss();
                break;
        }
    }
}
