package com.example.zrwenxue.moudel.main.home.lottery;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.example.newzr.R;
import com.example.zrwenxue.app.TitleBarView;
import com.example.zrwenxue.moudel.BaseActivity;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-06-02
 * Time: 17:10
 */
public class LotteryActivity extends BaseActivity {

    View view;
    TextView s1,s2,s3,s4,s5,s6,s7,d1,d2,d3,d4,d5,d6,d7,p31,p32,p33,p51,p52,p53,p54,p55,
    q1,q2,q3,q4,q5,q6,q7;
    TextView text_12;
    TitleBarView topView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initDate(1);
        initDate(2);
        initDate(3);
        initDate(4);
        initDate(5);
    }





    //初始化控件
    public void initView() {

        topView=findViewById(R.id.lottery_topView);
        topView.setTitle("今日幸运号");
        topView.setOnclickLeft(View.VISIBLE, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        s1=findViewById(R.id.s1);
        s2=findViewById(R.id.s2);
        s3=findViewById(R.id.s3);
        s4=findViewById(R.id.s4);
        s5=findViewById(R.id.s5);
        s6=findViewById(R.id.s6);
        s7=findViewById(R.id.s7);

        d1=findViewById(R.id.d1);
        d2=findViewById(R.id.d2);
        d3=findViewById(R.id.d3);
        d4=findViewById(R.id.d4);
        d5=findViewById(R.id.d5);
        d6=findViewById(R.id.d6);
        d7=findViewById(R.id.d7);

        p31=findViewById(R.id.p31);
        p32=findViewById(R.id.p32);
        p33=findViewById(R.id.p33);

        p51=findViewById(R.id.p51);
        p52=findViewById(R.id.p52);
        p53=findViewById(R.id.p53);
        p54=findViewById(R.id.p54);
        p55=findViewById(R.id.p55);

        q1=findViewById(R.id.q1);
        q2=findViewById(R.id.q2);
        q3=findViewById(R.id.q3);
        q4=findViewById(R.id.q4);
        q5=findViewById(R.id.q5);
        q6=findViewById(R.id.q6);
        q7=findViewById(R.id.q7);

        text_12=findViewById(R.id.text_12);
        text_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDate(1);
                initDate(2);
                initDate(3);
                initDate(4);
                initDate(5);
            }
        });
    }

    //初始化数据
    private void initDate(int num) {

        LotteryChoice lotteryChoice=new LotteryChoice();
        switch (num){
            case 1:
                ArrayList<Integer> tempList=new ArrayList<>();
                tempList=lotteryChoice.dlt_ssq(35,12);
                d1.setText(method(tempList.get(0)));
                d2.setText(method(tempList.get(1)));
                d3.setText(method(tempList.get(2)));
                d4.setText(method(tempList.get(3)));
                d5.setText(method(tempList.get(4)));
                d6.setText(method(tempList.get(5)));
                d7.setText(method(tempList.get(6)));
                break;
            case 2:
                ArrayList<Integer> tempList2=new ArrayList<>();
                tempList2=lotteryChoice.dlt_ssq(33,16);
                s1.setText(method(tempList2.get(0)));
                s2.setText(method(tempList2.get(1)));
                s3.setText(method(tempList2.get(2)));
                s4.setText(method(tempList2.get(3)));
                s5.setText(method(tempList2.get(4)));
                s6.setText(method(tempList2.get(5)));
                s7.setText(method(tempList2.get(6)));
                break;
            case 3:
                ArrayList<Integer> tempList3=new ArrayList<>();
                tempList3=lotteryChoice.pl3_pl5(3);
                p31.setText(tempList3.get(0)+"");
                p32.setText(tempList3.get(1)+"");
                p33.setText(tempList3.get(2)+"");
                break;
            case 4:
                ArrayList<Integer> tempList4=new ArrayList<>();
                tempList4=lotteryChoice.pl3_pl5(5);
                p51.setText(tempList4.get(0)+"");
                p52.setText(tempList4.get(1)+"");
                p53.setText(tempList4.get(2)+"");
                p54.setText(tempList4.get(3)+"");
                p55.setText(tempList4.get(4)+"");
                break;
            case 5:
                ArrayList<Integer> tempList5=new ArrayList<>();
                tempList5=lotteryChoice.pl3_pl5(7);
                q1.setText(tempList5.get(0)+"");
                q2.setText(tempList5.get(1)+"");
                q3.setText(tempList5.get(2)+"");
                q4.setText(tempList5.get(3)+"");
                q5.setText(tempList5.get(4)+"");
                q6.setText(tempList5.get(5)+"");
                q7.setText(tempList5.get(6)+"");
                break;
        }
    }

    //换成字符串型更好看
    String method(int num){
        if(num<10){
            return "0"+num;
        }else {
            return num+"";
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.fragment_lottery;
    }

    @Override
    protected void init() {

    }
}
