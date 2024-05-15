package com.example.zrprint

import android.content.Context
import android.util.Log
import java.io.File

/**
 * @Author qiwangi
 * @Date 2023/8/11
 * @TIME 14:42
 */

val xmlString =
    """
        <?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/ac_splash_bg_color"
    android:orientation="vertical">

    <com.example.zrtool.ui.uibase.BaseLinearLayoutSetTop
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="2"
        android:orientation="vertical">
        <LinearLayout
            android:orientation="vertical"
            android:background="@drawable/shape_solid_ffffff_corners_10dp"
            android:layout_margin="20dp"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

                <TextView
                    android:id="@+id/mark_"
                    android:gravity="center"
                    android:layout_gravity="center_horizontal"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_margin="15dp"
                    android:text="[ ɪ ]"
                    android:textColor="@color/black"
                    android:textSize="100dp" />
                <ImageView
                    android:layout_alignParentRight="true"
                    android:layout_alignParentBottom="true"
                    android:layout_margin="10dp"
                    android:background="@drawable/shape_solid_sound_mark_bg"
                    android:id="@+id/mark_img_sound"
                    android:layout_width="30dp"
                    android:layout_height="30dp"/>

            <TextView
                android:id="@+id/mark_example"
                android:textStyle="bold"
                android:gravity="left"
                android:layout_gravity="center_horizontal"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginLeft="10dp"
                android:layout_marginTop="10dp"
                android:layout_marginRight="10dp"
                android:text="常发字母及字母组合有：e, ea"
                android:textColor="@color/black" />
           
            <androidx.recyclerview.widget.RecyclerView
                android:visibility="gone"
                android:id="@+id/mark_RecyclerView01"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"/>

            <TextView
                android:textStyle="bold"
                android:gravity="left"
                android:layout_gravity="center_horizontal"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginLeft="10dp"
                android:layout_marginTop="10dp"
                android:layout_marginRight="10dp"
                android:text="发音技巧"
                android:textColor="@color/black" />
            <TextView
                android:id="@+id/mark_details"
                android:gravity="left"
                android:layout_gravity="center_horizontal"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginLeft="10dp"
                android:layout_marginTop="10dp"
                android:layout_marginRight="10dp"
                android:text="发音类似拼音一 ，尾音拉长" />

        </LinearLayout>


    </com.example.zrtool.ui.uibase.BaseLinearLayoutSetTop>

    <RelativeLayout
        android:orientation="horizontal"
        android:layout_weight="1"
        android:layout_width="match_parent"
        android:layout_height="0dp">
        <HorizontalScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:paddingTop="5dp"
            android:scrollbars="none">
            <LinearLayout
                android:padding="3dp"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="1"
                    android:orientation="horizontal">

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="match_parent"
                        android:background="@drawable/shape_solid_sound_mark_bg"
                        android:gravity="center"
                        android:paddingLeft="@dimen/sound_mark_padding_left"
                        android:paddingRight="@dimen/sound_mark_padding_right"
                        android:text="元\n音"
                        android:textColor="@color/color_sound_mark_tv"
                        android:textStyle="bold" />

                    <LinearLayout
                        android:layout_width="wrap_content"
                        android:layout_height="match_parent"
                        android:orientation="vertical">

                        <LinearLayout
                            android:layout_width="wrap_content"
                            android:layout_height="0dp"
                            android:layout_weight="2">

                            <LinearLayout
                                android:layout_width="0dp"
                                android:layout_height="match_parent"
                                android:layout_weight="1">

                                <TextView
                                    android:layout_width="match_parent"
                                    android:layout_height="match_parent"
                                    android:background="@drawable/shape_solid_sound_mark_bg"
                                    android:gravity="center"
                                    android:paddingLeft="10dp"
                                    android:paddingRight="10dp"
                                    android:text="单\n元\n音"
                                    android:textColor="@color/color_sound_mark_tv"
                                    android:textStyle="bold" />
                            </LinearLayout>

                            <LinearLayout
                                android:layout_width="0dp"
                                android:layout_height="match_parent"
                                android:layout_weight="2"
                                android:background="@drawable/shape_solid_sound_mark_bg"
                                android:orientation="vertical">

                                <TextView
                                    android:padding="10dp"
                                    android:layout_width="match_parent"
                                    android:layout_height="0dp"
                                    android:layout_weight="1"
                                    android:gravity="center"
                                    android:text="短元音"
                                    android:textColor="@color/color_sound_mark_tv"
                                    android:textStyle="bold" />

                                <TextView
                                    android:layout_width="match_parent"
                                    android:layout_height="0dp"
                                    android:layout_weight="1"
                                    android:gravity="center"
                                    android:paddingLeft="10dp"
                                    android:paddingRight="10dp"
                                    android:text="长元音"
                                    android:textColor="@color/color_sound_mark_tv"
                                    android:textStyle="bold" />
                            </LinearLayout>
                        </LinearLayout>

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="0dp"
                            android:layout_weight="1">

                            <TextView
                                android:layout_width="match_parent"
                                android:layout_height="match_parent"
                                android:background="@drawable/shape_solid_sound_mark_bg"
                                android:gravity="center"
                                android:paddingLeft="10dp"
                                android:paddingRight="10dp"
                                android:text="双元音"
                                android:textColor="@color/color_sound_mark_tv"
                                android:textStyle="bold" />
                        </LinearLayout>
                    </LinearLayout>

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:orientation="vertical">

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="0dp"
                            android:layout_weight="1"
                            android:background="@drawable/shape_solid_sound_mark_bg"
                            android:orientation="horizontal">

                            <TextView
                                android:id="@+id/s1"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ɪ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s2"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ e ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s3"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ æ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s4"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ʌ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s5"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ɒ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s6"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ʌ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s7"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ʊ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                        </LinearLayout>

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="0dp"
                            android:layout_weight="1"
                            android:background="@drawable/shape_solid_sound_mark_bg">

                            <TextView
                                android:id="@+id/s8"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ i: ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s9"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ɜː ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s10"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ɔː ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s11"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ uː ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s12"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ɑː ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />
                        </LinearLayout>

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="0dp"
                            android:layout_weight="1"
                            android:background="@drawable/shape_solid_sound_mark_bg">

                            <TextView
                                android:id="@+id/s13"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ eɪ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s14"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ aɪ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s15"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ɔɪ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s16"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ aʊ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s17"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ əʊ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s18"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ɪə ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s19"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ eə ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s20"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ʊə ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                        </LinearLayout>

                    </LinearLayout>
                </LinearLayout>


                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="1"
                    android:orientation="horizontal">


                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="match_parent"
                        android:background="@drawable/shape_solid_sound_mark_bg"
                        android:gravity="center"
                        android:paddingLeft="@dimen/sound_mark_padding_left"
                        android:paddingRight="@dimen/sound_mark_padding_right"
                        android:text="辅\n音"
                        android:textColor="@color/color_sound_mark_tv"
                        android:textStyle="bold" />

                    <LinearLayout
                        android:layout_width="wrap_content"
                        android:layout_height="match_parent"
                        android:orientation="vertical">

                        <LinearLayout
                            android:layout_width="wrap_content"
                            android:layout_height="0dp"
                            android:layout_weight="2">

                            <LinearLayout
                                android:layout_width="0dp"
                                android:layout_height="match_parent"
                                android:layout_weight="1">

                                <TextView
                                    android:paddingLeft="10dp"
                                    android:paddingRight="10dp"
                                    android:layout_width="match_parent"
                                    android:layout_height="match_parent"
                                    android:background="@drawable/shape_solid_sound_mark_bg"
                                    android:gravity="center"
                                    android:text="清\n浊\n音"
                                    android:textColor="@color/color_sound_mark_tv"
                                    android:textStyle="bold" />
                            </LinearLayout>

                            <LinearLayout
                                android:layout_width="0dp"
                                android:layout_height="match_parent"
                                android:layout_weight="2"
                                android:background="@drawable/shape_solid_sound_mark_bg"
                                android:orientation="vertical">

                                <TextView
                                    android:layout_width="match_parent"
                                    android:layout_height="0dp"
                                    android:layout_weight="1"
                                    android:gravity="center"
                                    android:paddingLeft="10dp"
                                    android:paddingRight="10dp"
                                    android:text="清辅音"
                                    android:textColor="@color/color_sound_mark_tv"
                                    android:textStyle="bold" />

                                <TextView
                                    android:layout_width="match_parent"
                                    android:layout_height="0dp"
                                    android:layout_weight="1"
                                    android:gravity="center"
                                    android:paddingLeft="10dp"
                                    android:paddingRight="10dp"
                                    android:text="浊辅音"
                                    android:textColor="@color/color_sound_mark_tv"
                                    android:textStyle="bold" />
                            </LinearLayout>
                        </LinearLayout>

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="0dp"
                            android:layout_weight="1">

                            <TextView
                                android:layout_width="match_parent"
                                android:layout_height="match_parent"
                                android:background="@drawable/shape_solid_sound_mark_bg"
                                android:gravity="center"
                                android:paddingLeft="10dp"
                                android:paddingRight="10dp"
                                android:text="其他音"
                                android:textColor="@color/color_sound_mark_tv"
                                android:textStyle="bold" />
                        </LinearLayout>
                    </LinearLayout>

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:orientation="vertical">

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="0dp"
                            android:layout_weight="1"
                            android:background="@drawable/shape_solid_sound_mark_bg"
                            android:orientation="horizontal">

                            <TextView
                                android:id="@+id/s21"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ p ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s22"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ t ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s23"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ k ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s24"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ f ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s25"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ θ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s26"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ s ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s27"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ts ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s28"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ tr ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s29"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ∫ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s30"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ t∫ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s31"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="        "
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />
                        </LinearLayout>

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="0dp"
                            android:layout_weight="1"
                            android:background="@drawable/shape_solid_sound_mark_bg">

                            <TextView
                                android:id="@+id/s32"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ b ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s33"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ d ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s34"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ g ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s35"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ v ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s36"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ð ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s37"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ z ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s38"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ dz ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s39"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ dr ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s40"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ʒ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s41"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ dʒ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                        </LinearLayout>

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="0dp"
                            android:layout_weight="1"
                            android:background="@drawable/shape_solid_sound_mark_bg">

                            <TextView
                                android:id="@+id/s42"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ m ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s43"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ n ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s44"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ ŋ ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s45"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ h ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s46"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ l ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s47"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ r ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:id="@+id/s48"
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ j ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="match_parent"
                                android:gravity="center"
                                android:paddingLeft="@dimen/sound_mark_padding_left"
                                android:paddingRight="@dimen/sound_mark_padding_right"
                                android:text="[ w ]"
                                android:textColor="@color/color_sound_mark_tv2"
                                android:textStyle="bold" />

                        </LinearLayout>

                    </LinearLayout>
                </LinearLayout>

            </LinearLayout>
        </HorizontalScrollView>
    </RelativeLayout>
</LinearLayout>
    """

fun main() {
    //1.打印id
//    FIndViewByIdToool().out(xmlString, "rootView.")
//    FIndViewByIdToool().out(xmlString, "")

    //1.打印id  java版
//    FIndViewByIdTooolJave().out(xmlString, "rootView.")


    //2.打印重复数据 根据需求修改
    print2()



    //3.打印文件夹下所有文件名
//    print3()

    //4.遍历assets文件
//    listFilesInAssets()

}

/**
 * 打印重复数据
 */
fun print2(){
    var stu = StringBuffer()
    (1..48).forEach {
        stu.append(
            """  
                idList!!.add(s${it}!!)
                    """.trimIndent() + "\r\n"
        )
    }
    println(stu.toString())
}

/**
 * 打印文件夹下所有文件名
 */
fun print3(){
//    val directory = File("/Users/qiwangi/Downloads/字体(100)/未命名文件夹") // 指定文件夹路径
    val directory = File("/Users/qiwangi/Downloads") // 指定文件夹路径
    printFileNames(directory)
}

fun printFileNames(directory: File) {
    val files = directory.listFiles()

    if (files != null) {
        for (file in files) {
            if (file.isFile) {
                println(file.name)
            }
//            else if (file.isDirectory) {
//                printFileNames(file)
//            }
        }
    }
}


/**
 * 遍历Assets
 */
fun listFilesInAssets(context: Context) {
    val assetManager = context.assets
    val fileList = assetManager.list("") // 获取 assets 文件夹下的所有文件

    if (fileList != null) {
        for (filename in fileList) {
            println(filename) // 输出文件名
        }
    }
}



