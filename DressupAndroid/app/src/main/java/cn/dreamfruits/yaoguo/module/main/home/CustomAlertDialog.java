package cn.dreamfruits.yaoguo.module.main.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.dreamfruits.yaoguo.R;

/**
 * @Author qiwangi
 * @Date 2023/7/5
 * @TIME 20:51
 */
public class CustomAlertDialog {
    private Context context;
    private String title;
    private String positiveButtonText;
    private String negativeButtonText;
    private DialogInterface.OnClickListener positiveButtonClickListener;
    private DialogInterface.OnClickListener negativeButtonClickListener;

    public CustomAlertDialog(Context context) {
        this.context = context;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPositiveButton(String text, DialogInterface.OnClickListener listener) {
        this.positiveButtonText = text;
        this.positiveButtonClickListener = listener;
    }

    public void setNegativeButton(String text, DialogInterface.OnClickListener listener) {
        this.negativeButtonText = text;
        this.negativeButtonClickListener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.home_dialog_ok_cancel, null);
        builder.setView(dialogView);

        TextView titleTextView = dialogView.findViewById(R.id.content);
        titleTextView.setText(title);

        builder.setPositiveButton(positiveButtonText, positiveButtonClickListener);
        builder.setNegativeButton(negativeButtonText, negativeButtonClickListener);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

//使用方法：
//CustomAlertDialog alertDialog = new CustomAlertDialog(context);
//alertDialog.setTitle("对话框标题");
//alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//        // 处理确认按钮点击事件
//    }
//});
//alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//        // 处理取消按钮点击事件
//    }
//});
//alertDialog.show();
