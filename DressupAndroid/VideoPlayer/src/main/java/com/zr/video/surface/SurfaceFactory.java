package com.zr.video.surface;

import android.content.Context;

/**
 * 扩展自己的渲染View 可以使用TextureView，可参考{@link RenderTextureView}和{@link TextureViewFactory}的实现。
 */
public abstract class SurfaceFactory {

    public abstract InterSurfaceView createRenderView(Context context);

}
