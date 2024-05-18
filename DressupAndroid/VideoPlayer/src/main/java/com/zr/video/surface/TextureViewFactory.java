package com.zr.video.surface;

import android.content.Context;

/**
 * 实现类
 */
public class TextureViewFactory extends SurfaceFactory {

    public static TextureViewFactory create() {
        return new TextureViewFactory();
    }

    @Override
    public InterSurfaceView createRenderView(Context context) {
        //创建TextureView
        return new RenderTextureView(context);
    }
}
