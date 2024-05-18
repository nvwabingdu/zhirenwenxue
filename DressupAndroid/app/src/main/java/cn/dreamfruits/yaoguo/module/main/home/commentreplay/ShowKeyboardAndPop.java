package cn.dreamfruits.yaoguo.module.main.home.commentreplay;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.airbnb.lottie.L;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.dreamfruits.yaoguo.R;
import cn.dreamfruits.yaoguo.module.main.home.postdetails.PostDetailsActivity;
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean;
import cn.dreamfruits.yaoguo.util.Singleton;
import cn.dreamfruits.yaoguo.util.Tool;
import cn.dreamfruits.yaoguo.util.ToolJava;

/**
 * @Author qiwangi
 * @Date 2023/4/17
 * @TIME 12:54
 */
public class ShowKeyboardAndPop {
    private String currentStr="";
    private String lastStr="";
    private ArrayList<AtTextBean> atUserList=new ArrayList<>();//atUser用户列表
    private Boolean isAtAdd=false;//艾特添加

    private Boolean isAtDel=false;//艾特删除

    private InputMethodManager imm=null;//键盘


    //网络请求下来的 可以@的用户数组
    ArrayList<SearchUserBean.Item> tempList=new ArrayList<>();//临时数据

    PopupWindow popupWindow =null;
    private Boolean isSend=false;//是否发送

    private String searchTempStr="";//临时字符串
    private Boolean isSearchLoadMore=false;

    private TextView send=null;//发送按钮


    /**
     * 弹出键盘 和 pop
     *
     * @param context
     * @param mActivity
     * @param targetId 动态id，一级评论id
     * @param type 0-动态 1-评论
     * @param replyUser 长按时用于显示的 和提示的文本 就是当前item的内容  不是需要发送的内容  比如回复   这种格式 回复 @dadfdad:
     * @param replyId 回复的评论id
     *
     * String atUser,//at的用户字符串,[{"index":0,"len":10,"id":11111,"name":"张三"},{"index":20,"len":5,"id":"222","name":"李四"}]
     * String config//配置字符串
     */
    public void showKeyboard(Context context, Activity mActivity,long targetId,int type,String replyUser,long replyId,Boolean isChild) {
        /**
         * 键盘弹出逻辑
         */
        imm= (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);//pop弹出之后再弹键盘

        /**
         * 获取屏幕高度  用于设置pop位于底部
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        /**
         * 评论回复 pop
         */
        View inflate = View.inflate(context, R.layout.home_dialog_comment_issue, null);
        NoteContentEditText editText=inflate.findViewById(R.id.comment_edittext);//输入框
        if(replyUser.equals("")){
            editText.setHint(" 欢迎您的友善评论……");
        }else {
            editText.setHint(replyUser);//这种格式 回复 @dadfdad:
        }

//        setData();//临时使用

        /**
         * atuser
         */
         TextView atUserDefaultTv=inflate.findViewById(R.id.at_user_default_tv);


        /**
         * 艾特用户 适配器
         */
        RecyclerView mRecyclerView=inflate.findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager =new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        AtUserAdapter mAtUserAdapter = new  AtUserAdapter(context, tempList);
        mRecyclerView.setAdapter(mAtUserAdapter);


        /**
         * 点击用户回调
         */
        mAtUserAdapter.setAtUserAdapterListener(new AtUserAdapter.AtUserAdapterInterface() {
            @NonNull
            @Override
            public List<SearchUserBean.Item> setSelectAtUser(@NonNull List<SearchUserBean.Item> tempList) {

                boolean isSelect=false;
                for (int i =0;i<tempList.size();i++){
                   isSelect=false;
                    for(int a=0;a<atUserList.size();a++){
                        if (tempList.get(i).getId()==atUserList.get(a).getId()){
                            isSelect=true;
                            break;
                        }
                    }
                    tempList.get(i).setSelected(isSelect);
                }
                return tempList;
            }

            @Override
            public void showDefaultView(boolean isShow) {
                if (isShow){//为true 就隐藏列表  显示默认布局
                    mRecyclerView.setVisibility(View.GONE);
                    atUserDefaultTv.setVisibility(View.VISIBLE);
                }else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    atUserDefaultTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadMore(@Nullable String atStr) {
                //加载更多
                if (isSearchLoadMore){//搜索用户
                    mSearchUserInterface.onclick(mAtUserAdapter,false,searchTempStr,false,"","");
                }else {//可能是@用户
                    mSearchUserInterface.onclick(mAtUserAdapter,true,"",false,"","");
                }
            }

            @Override
            public void onclick(long id, int len, @NonNull String userName, boolean isCancel) {
                isAtAdd=true;
                String originName=userName;
                int delIndex=0;
                //添加逻辑
                int tempSelectionStart=editText.getSelectionStart();//获取光标位置
                if (userName.equals("")){//等于空直接返回
                    return;
                }

                userName=" "+"@"+userName+" ";//username用这种格式添加进去

                int indexNow=editText.getSelectionStart();//获取当前的光标位置
                indexNow=indexNow+userName.length();

                if (!isCancel){//一种情况 添加@某用户
                    String originStr=editText.getText().toString();//输入框中的文本
                    int index=editText.getSelectionStart();//光标的位置
                    String indexBeforeStr="";//光标之前的文本
                    ArrayList<DiffTextBean> diffStrList=new ArrayList<>();//差量文本
                    if (!originStr.equals("")){//如果输入框中有文字
                        indexBeforeStr=originStr.substring(0,index);//截取光标之前的文本
                    }
                    if (indexBeforeStr.contains("@")){//如果光标之前的文本有@符号
                        if (indexBeforeStr.lastIndexOf("@")<index){//最后一个@在光标之前  类似  3123131@dadad|    3123131@dad|ad
                            String tempStr=indexBeforeStr.substring(indexBeforeStr.lastIndexOf("@"),index);//截取@到光标的文本   接上   @dadad    @dad
                            if (!tempStr.contains(" ")) {//上诉文本没有空格 就符合条件 删除这一段  并把光标移动到之前

                                for (int i=0;i<tempStr.length();i++){//建立差量集合 用于下面删除
                                    diffStrList.add(new DiffTextBean(indexBeforeStr.lastIndexOf("@")+i,tempStr.split("")[i],false));
                                }

                                indexNow=indexNow-tempStr.length();
                            }
                        }
                    }

                    /**
                     * 添加@用户
                     */
                    for (int i=0;i<userName.length();i++){
                        if(userName.split("")[i].equals(" ")){//空格不加详情
                            atUserList.add(tempSelectionStart+i, new AtTextBean(userName.split("")[i],0,0,0l,""));//空格
                        }else {
                            atUserList.add(tempSelectionStart+i, new AtTextBean(userName.split("")[i],tempSelectionStart+i,len,id,originName));
                        }
                    }

                    /*Log.e("987654", "1==：" + atUserList);*/
                    /**
                     * 删除差量文本
                     */
                    if (diffStrList.size()!=0){
                        diffAtUserList(diffStrList);
                    }

                }else {
                    /**
                     * 取消@用户
                     */
                     ArrayList<AtTextBean> tempAtUserList=new ArrayList<>();
                    for (int i=0;i<atUserList.size();i++){//遍历删除 包含id的角标
                        if (atUserList.get(i).getId()!=id){//包含id
                            tempAtUserList.add(atUserList.get(i));
                        }
                    }

                    Log.e("09872121", delIndex+"");

                    atUserList.clear();
                    atUserList=tempAtUserList;
                }
                /*Log.e("0987",Arrays.toString(atUserList.toArray()));*/
                /**
                 * 更新集合逻辑
                 */
                updateAtUserList(atUserList,editText);

                /**
                 * //添加用户之后 光标位置
                 */
                try {
                    if (!isCancel){
                        editText.setSelection(indexNow);
                    }else {
                        editText.setSelection(editText.getText().length());
                    }
                }catch (Exception e){
                    //报错的情况下 直接把光标移动到最后
                    editText.setSelection(editText.getText().length());
                }
            }
        });


        /**
         * 光标监听  作用 光标不能插入@用户 文本之中
         */
        editText.setOnSelectionChanged(new NoteContentEditText.SelectionChanged() {
            @Override
            public void onclick(int selStart) {
                try {
                    int tempNum=selStart;
                    if (selStart<atUserList.size()){
                        if (atUserList.get(selStart).getId()==0){
                            return;
                        }
                        for(int i=selStart;i<atUserList.size();i++){
                            if (atUserList.get(i).getId()!=0){
                                    tempNum++;
                            }else{
                                break;
                            }
                        }
                    }
                    editText.setSelection(tempNum+1);
                }catch (Exception e){
                    //异常不管
                }
            }
        });
        //2.输入框监听按钮
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {//
                lastStr=s.toString();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//3.通过文本减少判断是否按下删除键  删除逻辑
                    /**
                     * 搜索用户需要的请求的文本
                     */
                    String originStr=s.toString();//输入框中的文本
                if(originStr.equals("")){
                    mRecyclerView.setVisibility(View.GONE);
                    atUserDefaultTv.setVisibility(View.GONE);
                }
                    int index=editText.getSelectionStart();//光标的位置
                    String indexBeforeStr="";//光标之前的文本
                    if (!originStr.equals("")){//如果输入框中有文字
                        indexBeforeStr=originStr.substring(0,index);//截取光标之前的文本
                    }
                    if (indexBeforeStr.contains("@")){//如果光标之前的文本有@符号
                        if (indexBeforeStr.lastIndexOf("@")<index){//最后一个@在光标之前  类似  3123131@dadad|    3123131@dad|ad
                            String tempStr=indexBeforeStr.substring(indexBeforeStr.lastIndexOf("@"),index);//截取@到光标的文本   接上   @dadad    @dad
                            if (!tempStr.contains(" ")) {//上诉文本没有空格 就符合条件 开始请求
                                /**
                                 * 搜索用户回调
                                 */
                                if (!tempStr.equals("@")){//添加判断
                                    Log.e("13413231","key==="+tempStr);
                                    isSearchLoadMore=true;//搜索加载更多 状态设置为搜索更多这边
                                    searchTempStr=tempStr;//把当前搜索字段赋值给搜索字段

                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    atUserDefaultTv.setVisibility(View.GONE);
                                    mSearchUserInterface.onclick(mAtUserAdapter,false,tempStr,true,"","");
                                }
                            }else {
                                Log.e("13413231","key----"+tempStr);
                                mRecyclerView.setVisibility(View.GONE);
                                atUserDefaultTv.setVisibility(View.GONE);
                            }
                        }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")&&editable.toString().endsWith("@")){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    atUserDefaultTv.setVisibility(View.GONE);

//                    String temp=editable.toString();
//                    int tempIndex=editText.getSelectionStart();
//                    temp= new StringBuilder(temp).insert(tempIndex, "@").toString();
//                    editText.setText(temp);
//
//                    updateAtUserList(atUserList,editText);//是否插入
//
//                    editText.setSelection(tempIndex+1);
//                    /**
//                     * 请求@用户列表
//                     */
//                    Log.e("13413231","key为空");
//                    // TODO: 2023/4/18
                    isSearchLoadMore=false;//把加载状态设置到@用户
                    mSearchUserInterface.onclick(mAtUserAdapter,true,"",true,"","");
                }

                if(!editable.toString().equals("")&&editable.toString().length()<=300){
                    isSend=true;
                }else {
                    isSend=false;
                }
                setSendState(send,mActivity);

                /**
                 * 装入当前的文本
                 */
                currentStr=editable.toString();

                if (!isAtAdd&&!isAtDel){
                    if (!currentStr.equals("")){//不等于空才有意义
                        ArrayList<DiffTextBean> diffStrList=new ArrayList<>();
                        if (currentStr.length()>lastStr.length()){//增加了文本
                                diffStrList=diffText(currentStr,lastStr,true);
                                if (diffStrList!=null&&diffStrList.size()!=0){
                                    for (int i = 0; i < diffStrList.size(); i++) {
                                        atUserList.add(
                                                diffStrList.get(i).getIndex(),
                                                new AtTextBean(diffStrList.get(i).getIndexStr(),
                                                        diffStrList.get(i).getIndex(),
                                                        0,
                                                        0l,
                                                        ""));

                                    }
                            }
                           /* Log.e("987654","插入之后的：  "+atUserList);*/
                        }else {//删除了文本
                                diffStrList=diffText(lastStr,currentStr,false);
                                ArrayList<AtTextBean> tempAtUserList=new ArrayList<>();//临时集合
                                if (diffStrList!=null&&diffStrList.size()!=0){

                                    for (int i=0;i<diffStrList.size();i++){
                                        int delIndex=diffStrList.get(i).getIndex();//需要删除的角标位置

                                        if (diffStrList.get(i).getIndexStr().equals(" ")){//如果这个位置上是空格
                                            if(atUserList.size()>delIndex){//如果字符串长度大于这个角标
                                                //判断前一个是否是有id的
                                                if(atUserList.get(delIndex-1).getId()!=0){//说明这个空格就是紧跟的@
                                                    //添加为 艾特删除
                                                    isAtDel=true;
                                                    //删除这个@用户
                                                    for (int a=0;a<atUserList.size();a++){
                                                        Long tempId=atUserList.get(delIndex-1).getId();
                                                        if (!atUserList.get(a).getId().toString().equals(tempId.toString())){//排除所有和这个ID相同的
                                                            tempAtUserList.add(atUserList.get(a));
                                                        }
                                                    }

                                                    if (atUserList!=null){
                                                        atUserList.clear();
                                                        atUserList=tempAtUserList;
                                                    }
                                                    //更新
                                                    updateAtUserList(atUserList,editText);

                                                    //设置的最后
                                                    editText.setSelection(editText.getText().length());
                                                }else {//正常空格
                                                    for (int a=0;a<atUserList.size();a++){
                                                        if (a!=delIndex){//添加除这个空格之外的
                                                            tempAtUserList.add(atUserList.get(a));
                                                        }
                                                    }
                                                    if (atUserList!=null){
                                                        atUserList.clear();
                                                        atUserList=tempAtUserList;
                                                    }
                                                }
                                            }
                                        }else if (atUserList.get(delIndex).getId()!=0){
                                            //添加为 艾特删除
                                            isAtDel=true;
                                            //删除这个@用户
                                            for (int a=0;a<atUserList.size();a++){
                                                Long tempId=atUserList.get(delIndex).getId();
                                                if (!atUserList.get(a).getId().toString().equals(tempId.toString())){//排除所有和这个ID相同的
                                                    tempAtUserList.add(atUserList.get(a));
                                                }
                                            }
                                            if (atUserList!=null){
                                                atUserList.clear();
                                                atUserList=tempAtUserList;
                                            }
                                            //更新
                                            updateAtUserList(atUserList,editText);

                                            //设置的最后
                                            editText.setSelection(editText.getText().length());
                                        } else{//不是空格
                                            for (int a=0;a<atUserList.size();a++){
                                                if (a!=delIndex){//添加除这个角标之外的
                                                    tempAtUserList.add(atUserList.get(a));
                                                }
                                            }
                                            if (atUserList!=null){
                                                atUserList.clear();
                                                atUserList=tempAtUserList;
                                            }
                                        }
                                    }
                                }
                                Log.e("987654","之后的集合："+atUserList);
                            }
                    }else {
                        if (atUserList!=null){
                            atUserList.clear();
                        }
                    }
                }

                isAtAdd=false;

                if (editable.toString().length()>=300){
                    Toast.makeText(context,"评论字数限制300个字",Toast.LENGTH_SHORT).show();
                    return;
                }



            }
        });

        /**
         * 显示与隐藏 @用户列表
         */
        ImageView img1=inflate.findViewById(R.id.comment_img_one);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    atUserDefaultTv.setVisibility(View.GONE);

                    String temp=editText.getText().toString();
                    int tempIndex=editText.getSelectionStart();
                    temp= new StringBuilder(temp).insert(tempIndex, "@").toString();
                    editText.setText(temp);

                    updateAtUserList(atUserList,editText);//是否插入

                    editText.setSelection(tempIndex+1);
                    /**
                     * 请求@用户列表
                     */
                    Log.e("13413231","key为空");
                    // TODO: 2023/4/18
                    isSearchLoadMore=false;//把加载状态设置到@用户
                    mSearchUserInterface.onclick(mAtUserAdapter,true,"",true,"","");
            }
        });

        /**
         * 显示与隐藏 表情包
         */
        // TODO: 2023/5/9  显示与隐藏 表情包
        ImageView img2=inflate.findViewById(R.id.comment_img_two);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示
            }
        });

        /**
         * 发送按钮
         */
         send=inflate.findViewById(R.id.omment_edittext_send);//发送
        setSendState(send,mActivity);//初始化发送按钮状态
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSend){
                    return;
                }
                String contentStr=editText.getText().toString();//评论内容

                //不能全是空格
                if (contentStr.trim().length() == 0) {
                    Toast.makeText(context,"请输入有意义的字符",Toast.LENGTH_SHORT).show();
                    return;
                    /* System.out.println("字符串全是空格");*/
                }
                //发送逻辑
                if (contentStr.equals("")||contentStr.length()>=300){//长度>=300截断
                    Toast.makeText(context,"评论字数限制300个字且至少有1个字",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //        targetId: Long,//动态id，一级评论id
                    //        type: Long,//0-动态 1-评论
                    //        content: String,//评论内容
                    //        replyId: Long,//回复的评论id
                    //        atUser: String,//at的用户字符串,用英文逗号","隔开，例如：id1,id2,id3
                    //        config: String//配置字符串

                    //发送评论回调
                    StringBuffer stu=new StringBuffer();
                    stu.append("[");
                    //[{"index":0,"len":10,"id":11111,"name":"张三"},{"index":20,"len":5,"id":"222","name":"李四"}]
                    for (int i=0;i<atUserList.size();i++){
                        if (i!=0&&atUserList.get(i-1).getSingleStr().equals("@")){
                            if (atUserList.get(i).getId()!=0){
                                stu.append("{\"index\":"+(i-1)+",\"len\":"+(atUserList.get(i).getName().length()+1)+",\"id\":"+atUserList.get(i).getId()+",\"name\":\""+atUserList.get(i).getName()+"\"},");
                            }
                        }
                    }
                  String  atUserStr=stu.toString().substring(0,stu.toString().length()-1);
                    atUserStr=atUserStr+"]";

                    Log.e("zqratuser",atUserStr);
                    Log.e("zqratuser",contentStr);

                    /**
                     * 发布评论 回调
                     */
                    if (atUserStr.length()<5){
                        mSendCommentInterface.onclick(targetId,type,contentStr,replyId,"","",isChild);
                    }else {
                        mSendCommentInterface.onclick(targetId,type,contentStr,replyId,atUserStr,"",isChild);
                    }

                    //置空
                    editText.setText("");
                    atUserList.clear();
                    //如果正在显示，关闭弹窗
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }

                    //如果正在显示  关闭键盘
                    if (imm != null || imm.isActive()) {
                        imm.hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);
                    }

                }
            }
        });

        /**
         * pop弹窗
         */
      popupWindow = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (popupWindow.isShowing()) {//如果正在显示，关闭弹窗。
            popupWindow.dismiss();
        } else {
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
            //表示位置
            popupWindow.showAtLocation(
                    inflate,
                    Gravity.NO_GRAVITY,
                    0, displayMetrics.heightPixels
            );

            /**
             * 弹窗弹出之后，获得焦点
             */
            editText.requestFocus(); // 请求EditText获取焦点

//            //键盘高度  取不到值
//            Rect rect = new Rect();
//            inflate.getLocalVisibleRect(rect);
//            int inflateHeight=inflate.getHeight();
//            int inflateHeightMarginTop=rect.top;
//            int inflateHeightMarginBottom=rect.bottom;
//
//            Log.e("dadadsda","inflateHeight："+inflateHeight);
//            Log.e("dadadsda","inflateHeightMarginTop："+inflateHeightMarginTop);
//            Log.e("dadadsda","inflateHeightMarginBottom："+inflateHeightMarginBottom);

            //透明度设置为0.5
            WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
            lp.alpha = 0.5f;
            mActivity.getWindow().setAttributes(lp);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    atUserList.clear();//消失的时候 清空集合
                    Log.e("da", "pop关闭");
//                        WindowManager.LayoutParams lp= mActivity.getWindow().getAttributes();
                    lp.alpha = 1f;
                    mActivity.getWindow().setAttributes(lp);

                    if (imm!=null||imm.isActive()){//pop正在显示的时候  如果键盘正在显示  也关闭键盘
                        Log.e("da", "键盘关闭");
                        // 方法1：此方法有误
//                            imm.hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);

                        //方法2：获取窗口对象隐藏软键盘
                        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }
                }
            });
        }

        /**
         * 获取app的生命周期，包括所有activity  找到现在使用的 并进行 防止内存泄露的操作
         */
        (mActivity.getApplication()).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }
            @Override
            public void onActivityStarted(Activity activity) {

            }
            @Override
            public void onActivityResumed(Activity activity) {
                if (mActivity != null && mActivity == activity) {
                    /*if(keyboardHeightProvider!=null){
                        keyboardHeightProvider.onResume();
                    }*/
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                //此处判断附着的activity
                if (mActivity != null && mActivity == activity) {

                    //如果正在显示，关闭弹窗
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }

                    //如果正在显示  关闭键盘
                    if (imm != null || imm.isActive()) {
                        imm.hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);
                    }

                    //键盘监听类
                    /*if(keyboardHeightProvider!=null){
                        keyboardHeightProvider.onPause();
                    }*/
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


    /**
     * 设置发送按钮的状态
     * @param send
     * @param mmActivity
     */
    private void setSendState(TextView send,Activity mmActivity){
        if (isSend){
            send.setBackground(mmActivity.getResources().getDrawable(R.drawable.home_shape_solid_ff222222_corners_radiu_dp33));
        }else {
            send.setBackground(mmActivity.getResources().getDrawable(R.drawable.home_shape_solid_ffeeeeee_corners_radiu_dp33));
        }
    }

    /**
     * 删除差量文本并更新edit
     */
    private void diffAtUserList(ArrayList<DiffTextBean> diffStrList) {
        ArrayList<AtTextBean> tempAtUserList2 = new ArrayList<>();//临时集合

        for (int i=0;i<atUserList.size();i++){

            Boolean isDel=false;

            for (int a=0;a<diffStrList.size();a++){
                if (i==diffStrList.get(a).getIndex()){
                    isDel=true;
                }
            }

            if (!isDel){
                tempAtUserList2.add(atUserList.get(i));
            }
        }

        if (atUserList != null) {
            atUserList.clear();
            atUserList = tempAtUserList2;
        }

        Log.e("987654", "2===：" + atUserList);
    }

    /**
     * 字符串比较  当前 和 之前 返回差增量集合
     * @param currentStr
     * @param lastStr
     * @param isAdd
     * @return
     */
    private ArrayList<DiffTextBean> diffText(String currentStr,String lastStr,Boolean isAdd){
        /**
         * diffText("12345678","123",true);
         * diffText("12345678","",true);
         * diffText("12345678","45678",true);
         * diffText("12345678","34",true);//光标输入不会同时在两个位置输入不考虑这种情况
         *
         * 删除
         * 就把当前的currentStr   lastStr  交换位置
         */
        ArrayList<DiffTextBean> diffStrList=new ArrayList<>();
        try {
            if (!currentStr.equals("")){
                if (lastStr.equals("")){//如果上次的为空
                    for (int i=0;i<currentStr.length();i++){
                        diffStrList.add(new DiffTextBean(i,currentStr.split("")[i],isAdd));
                    }
                    /*Log.e("zqr",Arrays.toString(diffStrList.toArray()));*/
                    return diffStrList;
                } else {//之前的不为空
                    if (lastStr.equals(currentStr.substring(0,lastStr.length()))){//12345   123  这种情况
                        for (int i=lastStr.length();i<currentStr.length();i++){
                            diffStrList.add(new DiffTextBean(i,currentStr.split("")[i],isAdd));
                        }
                        /*Log.e("zqr",Arrays.toString(diffStrList.toArray()));*/
                        return diffStrList;
                    }
                    if (currentStr.length()>lastStr.length()){//12534   1234      51234  1234
                        for (int i=0;i<currentStr.length();i++){
                            if (!currentStr.split("")[i].equals(lastStr.split("")[i])){//不同的地方 说明是增加的
                                diffStrList.add(new DiffTextBean(i,currentStr.split("")[i],isAdd));
                                //删除对应角标字符
                                /*currentStr= String.valueOf(new StringBuilder(currentStr).deleteCharAt(i));*/
                                /**
                                 * lastStr对应角标位置 添加字符
                                 */
                                lastStr= new StringBuilder(lastStr).insert(i, currentStr.split("")[i]).toString();
                            }
                        }
                        /*Log.e("zqr",Arrays.toString(diffStrList.toArray()));*/
                        return diffStrList;
                    }
                }
            }
            /*Log.e("zqr",Arrays.toString(diffStrList.toArray()));*/
            return diffStrList;
        }catch (Exception e){
            /*Log.e("zqr","diffText=========>"+e.toString());*/
        }
        return diffStrList;
    }


    /**
     * 更新文本
     */
    private void updateAtUserList(ArrayList<AtTextBean> atUserList,EditText editText){
        //装载集合
        StringBuffer stringBuffer=new StringBuffer();
        for (int i=0;i<atUserList.size();i++){
            stringBuffer.append(atUserList.get(i).getSingleStr());
        }

        //设置为富文本  通过给定文本集合 设置
        SpannableString spanColor = new SpannableString(stringBuffer);
        Boolean hasRichText=false;
        for (int i=0;i<atUserList.size();i++){
            if (atUserList.get(i).getId()!=0){//包含
                spanColor.setSpan(new ForegroundColorSpan(Color.parseColor("#569DE4")), i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                hasRichText=true;
            }
        }

        Log.e("ad1212",atUserList.toString());

        if (hasRichText){
            editText.setText(spanColor);
        }else {
            editText.setText(stringBuffer.toString());
        }
        isAtDel=false;
    }

    /**
     * 发送评论回调
     */
    SendCommentInterface mSendCommentInterface=null;
    public interface SendCommentInterface{
        void onclick(
                long targetId,//动态id，一级评论id
                int type,//0-动态 1-评论
                String content,//评论内容
                long replyId,//回复的评论id
                String atUser,//at的用户字符串,[{"index":0,"len":10,"id":11111,"name":"张三"},{"index":20,"len":5,"id":"222","name":"李四"}]
                String config,//配置字符串
                Boolean isChild
         );
    }

    public void  setSendCommentClickListener(SendCommentInterface mSendCommentInterface) {
        this.mSendCommentInterface = mSendCommentInterface;
    }

    /**
     * 搜索用户回调
     */
    SearchUserInterface mSearchUserInterface=null;
    public interface SearchUserInterface{
        void onclick(
                AtUserAdapter atUserAdapter,
                Boolean isGetAtUserList,//第一次请求at用户接口
                String key,//评论内容
                Boolean isKeyChange,//评论内容是否变化
                String atUser,//at的用户字符串,用英文逗号","隔开，例如：id1,id2,id3
                String config //配置字符串
        );
    }
    public void  setSearchUserClickListener(SearchUserInterface mSearchUserInterface) {
        this.mSearchUserInterface = mSearchUserInterface;
    }

}
