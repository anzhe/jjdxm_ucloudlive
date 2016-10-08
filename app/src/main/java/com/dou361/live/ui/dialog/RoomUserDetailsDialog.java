package com.dou361.live.ui.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.baseutils.utils.UIUtils;
import com.dou361.live.R;
import com.dou361.live.bean.AnchorBean;
import com.dou361.live.ui.config.StatusConfig;
import com.dou361.live.ui.fragment.ChatFragment;
import com.dou361.live.ui.listener.OnATUserListener;
import com.hyphenate.easeui.widget.EaseImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
 * 创建日期：2016/10/7 13:02
 * <p>
 * 描 述：聊天室成员详情
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class RoomUserDetailsDialog extends DialogFragment {

    Unbinder unbinder;
    @BindView(R.id.eiv_avatar)
    EaseImageView eiv_avatar;
    @BindView(R.id.tv_username)
    TextView usernameView;
    @BindView(R.id.btn_mentions)
    Button mentionBtn;
    private AnchorBean anchorBean;

    public static RoomUserDetailsDialog newInstance(AnchorBean anchorBean) {
        RoomUserDetailsDialog dialog = new RoomUserDetailsDialog();
        Bundle args = new Bundle();
        args.putSerializable(StatusConfig.ARG_ANCHOR, anchorBean);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_room_user_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            anchorBean = (AnchorBean) getArguments().getSerializable(StatusConfig.ARG_ANCHOR);
        }
        if (anchorBean != null) {
            usernameView.setText(anchorBean.getName());
            Glide.with(UIUtils.getContext())
                    .load(anchorBean.getCover())
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(eiv_avatar);
        }
        mentionBtn.setText("@TA");
    }

    @OnClick({R.id.btn_message, R.id.btn_mentions, R.id.btn_follow})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_message:
                ChatFragment fragment = ChatFragment.newInstance(anchorBean, false);
                dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.message_container, fragment).commit();
                break;
            case R.id.btn_mentions:
                if (dialogListener != null) {
                    dialogListener.onATClick(anchorBean.getName());
                }
                break;
            case R.id.btn_follow:
                break;
        }
    }

    private OnATUserListener dialogListener;

    public void setOnATUserListener(OnATUserListener dialogListener) {
        this.dialogListener = dialogListener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        Dialog dialog = new Dialog(getActivity(), R.style.room_user_details_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.dialog_room_user_details);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
