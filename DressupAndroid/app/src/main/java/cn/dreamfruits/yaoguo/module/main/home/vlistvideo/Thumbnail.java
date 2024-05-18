package cn.dreamfruits.yaoguo.module.main.home.vlistvideo;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;


import java.util.HashMap;

import cn.dreamfruits.yaoguo.R;

/**
 * 缩略图 demo
 */
public class Thumbnail extends AppCompatActivity {
    private ImageView mThumbnailImageView;
    private SeekBar mVideoSeekBar;
    private MediaMetadataRetriever mMediaMetadataRetriever;
    private long mVideoDuration;
    private static final int VIDEO_THUMBNAIL_COUNT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_listvideo_thumbnail);


        mThumbnailImageView = findViewById(R.id.thumbnail_image_view);
        mVideoSeekBar = findViewById(R.id.video_seek_bar) ;

        mMediaMetadataRetriever = new MediaMetadataRetriever();

//        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.ttt;
//        Log.e("zqr", "videoPath==" + videoPath);
//        String videoPath2 = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/" + "ttt";
//        Log.e("zqr", "videoPath2==" + videoPath2);
//        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/" + "ttt.mp4");

        try {
            mMediaMetadataRetriever.setDataSource("https://txmov2.a.kwimgs.com/upic/2021/08/20/01/BMjAyMTA4MjAwMTEyMTNfMjE1OTk1OTQ2XzU1NTk0ODcyMTk0XzFfMw==_b_B8774ec30bb49cf46e3549623ed2ea543.mp4?clientCacheKey=3xz92ewvjxdntsg_b.mp4", new HashMap<String, String>());
            mVideoDuration = Long.parseLong(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        generateThumbnails();
        setSeekBarListener();
    }

    /**
     * 首先获取缩略图的宽度和 `SeekBar` 的宽度，
     * 并计算出缩略图应该显示的位置。然后，将缩略图移动到指定的位置。
     * 注意，在计算缩略图位置时需要考虑 `SeekBar` 的 padding。
     * 需要注意的是，如果你的缩略图太大，可能会遮挡住 `SeekBar`
     * 上方的进度条，导致用户无法看到进度条。
     * 在这种情况下，可以考虑将缩略图放置在 `SeekBar` 旁边或下方，或者将缩略图的大小调整为合适的尺寸。
     */
    private void setSeekBarListener() {
        mVideoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateThumbnail(progress);
                    // 获取缩略图的宽度
                    int thumbnailWidth = mThumbnailImageView.getWidth();
                    // 获取SeekBar的宽度
                    int seekBarWidth = mVideoSeekBar.getWidth() -
                            mVideoSeekBar.getPaddingLeft() -
                            mVideoSeekBar.getPaddingRight();
                    // 计算缩略图的距离
                    int thumbPos = seekBarWidth * progress / mVideoSeekBar.getMax() + mVideoSeekBar.getPaddingLeft() - thumbnailWidth / 2;
                    // 将缩略图移动到指定位置
                    mThumbnailImageView.setX(thumbPos);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mThumbnailImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mThumbnailImageView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void generateThumbnails() {
        Bitmap bitmap = mMediaMetadataRetriever.getFrameAtTime(0);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        mVideoSeekBar.setBackground(bitmapDrawable);
        int interval = mVideoSeekBar.getMax() / VIDEO_THUMBNAIL_COUNT;
        for (int i = 1; i < VIDEO_THUMBNAIL_COUNT; i++) {
            long timeUs = i * mVideoDuration * 1000L / VIDEO_THUMBNAIL_COUNT;
            bitmap = mMediaMetadataRetriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{bitmapDrawable});
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layerDrawable.setLayerSize(0, interval, mVideoSeekBar.getHeight());
            }
            layerDrawable.setBounds(new Rect(0, 0, interval, mVideoSeekBar.getHeight()));
            mVideoSeekBar.setThumb(layerDrawable);
        }
    }

    private void updateThumbnail(int progress) {
        long timeUs = progress * 1000L * mVideoDuration / mVideoSeekBar.getMax();
        Bitmap bitmap = mMediaMetadataRetriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        mThumbnailImageView.setImageBitmap(bitmap);
    }
}