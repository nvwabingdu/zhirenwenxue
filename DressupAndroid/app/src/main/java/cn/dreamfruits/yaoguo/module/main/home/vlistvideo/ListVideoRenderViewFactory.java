package cn.dreamfruits.yaoguo.module.main.home.vlistvideo;

import android.content.Context;

import com.zr.video.surface.InterSurfaceView;
import com.zr.video.surface.RenderTextureView;
import com.zr.video.surface.SurfaceFactory;


public class ListVideoRenderViewFactory extends SurfaceFactory {

    public static ListVideoRenderViewFactory create() {
        return new ListVideoRenderViewFactory();
    }

    @Override
    public InterSurfaceView createRenderView(Context context) {
        return new ListVideoRenderView(new RenderTextureView(context));
    }
}
