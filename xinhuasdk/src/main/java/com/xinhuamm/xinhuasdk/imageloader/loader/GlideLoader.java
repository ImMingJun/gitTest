package com.xinhuamm.xinhuasdk.imageloader.loader;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xinhuamm.xinhuasdk.imageloader.config.AnimationMode;
import com.xinhuamm.xinhuasdk.imageloader.config.DiskCacheStrategyMode;
import com.xinhuamm.xinhuasdk.imageloader.config.GlideApp;
import com.xinhuamm.xinhuasdk.imageloader.config.GlideRequest;
import com.xinhuamm.xinhuasdk.imageloader.config.GlideRequests;
import com.xinhuamm.xinhuasdk.imageloader.config.GlobalConfig;
import com.xinhuamm.xinhuasdk.imageloader.config.PriorityMode;
import com.xinhuamm.xinhuasdk.imageloader.config.ScaleMode;
import com.xinhuamm.xinhuasdk.imageloader.config.ShapeMode;
import com.xinhuamm.xinhuasdk.imageloader.config.SingleConfig;
import com.xinhuamm.xinhuasdk.imageloader.utils.DownLoadImageService;
import com.xinhuamm.xinhuasdk.imageloader.utils.GlideCacheUtil;
import com.xinhuamm.xinhuasdk.imageloader.utils.ImageUtil;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


/**
 * 参考:
 * https://mrfu.me/2016/02/28/Glide_Sries_Roundup/
 */

public class GlideLoader implements ILoader {

//    /**
//     * @param context        上下文
//     * @param cacheSizeInM   Glide默认磁盘缓存最大容量250MB
//     * @param memoryCategory 调整内存缓存的大小 LOW(0.5f) ／ NORMAL(1f) ／ HIGH(1.5f);
//     * @param isInternalCD   true 磁盘缓存到应用的内部目录 / false 磁盘缓存到外部存
//     */
//    @Override
//    public void init(Context context, int cacheSizeInM, MemoryCategory memoryCategory, boolean isInternalCD) {
//        Glide.get(context).setMemoryCategory(memoryCategory); //如果在应用当中想要调整内存缓存的大小，开发者可以通过如下方式：
//        GlideBuilder builder = new GlideBuilder(context);
//        if (isInternalCD) {
//            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSizeInM * 1024 * 1024));
//        } else {
//            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheSizeInM * 1024 * 1024));
//        }
//    }

    @Override
    public void request(final SingleConfig config) {

        GlideRequests glideRequests = GlideApp.with(config.getContext());

        //需要补充
//        if (config.isGif()) {
//            GlideRequest<GifDrawable> gif=glideRequests.asGif();
//        }
//
//        if (config.isAsBitmap()) {
//            glideRequests.asBitmap();
//        }

        GlideRequest request = getGlideRequest(config, glideRequests);

        if (request == null) {
            return;
        }

        if (config.getPlaceHolderResId() != 0)//设置占位符
            request.placeholder(config.getPlaceHolderResId());

        if (config.getErrorResId() != 0)//设置错误的图片
            request.error(config.getErrorResId());

        int scaleMode = config.getScaleMode();

        switch (scaleMode) {
            case ScaleMode.CENTER_CROP:
                request.centerCrop();
                break;
            case ScaleMode.FIT_CENTER:
                request.fitCenter();
                break;
            default:
                break;
        }

        setShapeModeAndBlur(config, request);

        //设置缩略图
        if (config.getThumbnail() != 0) {
            request.thumbnail(config.getThumbnail());
        }

        //设置图片加载的分辨 sp
        if (config.getoWidth() != 0 && config.getoHeight() != 0) {
            request.override(config.getoWidth(), config.getoHeight());
        }

        //是否跳过磁盘存储
        setDiskCacheStrategy(config, request);

        //设置图片加载动画
//        setAnimator(config, request);

        //设置图片加载优先级
        setPriority(config, request);

        if (config.getRequestListener() != null) {
            request.listener(new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    config.getRequestListener().onFail();
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    config.getRequestListener().onSuccess();
                    return false;
                }
            });
        }

        if (config.getTarget() instanceof ImageView) {
            request.into((ImageView) config.getTarget());

        }

    }

    /**
     * 设置加载优先级
     *
     * @param config
     * @param request
     */
    private void setPriority(SingleConfig config, GlideRequest request) {
        switch (config.getPriority()) {
            case PriorityMode.PRIORITY_LOW:
                request.priority(Priority.LOW);
                break;
            case PriorityMode.PRIORITY_NORMAL:
                request.priority(Priority.NORMAL);
                break;
            case PriorityMode.PRIORITY_HIGH:
                request.priority(Priority.HIGH);
                break;
            case PriorityMode.PRIORITY_IMMEDIATE:
                request.priority(Priority.IMMEDIATE);
                break;
            default:
                request.priority(Priority.IMMEDIATE);
                break;
        }
    }

    private void setDiskCacheStrategy(SingleConfig config, GlideRequest request) {

        int mode = config.getDiskCacheStrategyMode();

        switch (mode) {
            case DiskCacheStrategyMode.ALL:
                request.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case DiskCacheStrategyMode.NONE:
                request.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case DiskCacheStrategyMode.SOURCE:
                request.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case DiskCacheStrategyMode.RESULT:
                request.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case DiskCacheStrategyMode.AUTOMATIC:
                request.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            default:
                break;
        }
    }

//    /**
//     * 设置加载进入动画
//     *
//     * @param config
//     * @param request
//     */
//    private void setAnimator(SingleConfig config, GlideRequest request) {
//        if (config.getAnimationType() == AnimationMode.ANIMATIONID) {
//            request.animate(config.getAnimationId());
//        } else if (config.getAnimationType() == AnimationMode.ANIMATOR) {
//            request.animate(config.getAnimator());
//        } else if (config.getAnimationType() == AnimationMode.ANIMATION) {
//            request.animate(config.getAnimation());
//        }
//    }

    @Nullable
    private GlideRequest getGlideRequest(SingleConfig config, GlideRequests requestManager) {
        GlideRequest request = null;
        if (!TextUtils.isEmpty(config.getUrl())) {
            request = requestManager.load(ImageUtil.appendUrl(config.getUrl()));
        } else if (!TextUtils.isEmpty(config.getFilePath())) {
            request = requestManager.load(ImageUtil.appendUrl(config.getFilePath()));
        } else if (!TextUtils.isEmpty(config.getContentProvider())) {
            request = requestManager.load(Uri.parse(config.getContentProvider()));
        } else if (config.getResId() > 0) {
            request = requestManager.load(config.getResId());
        } else if (config.getFile() != null) {
            request = requestManager.load(config.getFile());
        } else if (!TextUtils.isEmpty(config.getAssertspath())) {
            request = requestManager.load(config.getAssertspath());
        } else if (!TextUtils.isEmpty(config.getRawPath())) {
            request = requestManager.load(config.getRawPath());
        } else {
            request = requestManager.load("");
        }
        return request;

    }

    /**
     * 设置图片滤镜和形状
     *
     * @param config
     * @param request
     */
    private void setShapeModeAndBlur(SingleConfig config, GlideRequest request) {

        int count = 0;

        Transformation[] transformation = new Transformation[statisticsCount(config)];

        if (config.isNeedBlur()) {
            transformation[count] = new BlurTransformation(config.getBlurRadius());
            count++;
        }

        if (config.isNeedBrightness()) {
            transformation[count] = new BrightnessFilterTransformation(config.getBrightnessLeve()); //亮度
            count++;
        }

        if (config.isNeedGrayscale()) {
            transformation[count] = new GrayscaleTransformation(); //黑白效果
            count++;
        }

        if (config.isNeedFilteColor()) {
            transformation[count] = new ColorFilterTransformation( config.getFilteColor());
            count++;
        }

        if (config.isNeedSwirl()) {
            transformation[count] = new SwirlFilterTransformation( 0.5f, 1.0f, new PointF(0.5f, 0.5f)); //漩涡
            count++;
        }

        if (config.isNeedToon()) {
            transformation[count] = new ToonFilterTransformation(); //油画
            count++;
        }

        if (config.isNeedSepia()) {
            transformation[count] = new SepiaFilterTransformation(); //墨画
            count++;
        }

        if (config.isNeedContrast()) {
            transformation[count] = new ContrastFilterTransformation(config.getContrastLevel()); //锐化
            count++;
        }

        if (config.isNeedInvert()) {
            transformation[count] = new InvertFilterTransformation(); //胶片
            count++;
        }

        if (config.isNeedPixelation()) {
            transformation[count] = new PixelationFilterTransformation( config.getPixelationLevel()); //马赛克
            count++;
        }

        if (config.isNeedSketch()) {
            transformation[count] = new SketchFilterTransformation(); //素描
            count++;
        }

        if (config.isNeedVignette()) {
            transformation[count] = new VignetteFilterTransformation( new PointF(0.5f, 0.5f),
                    new float[]{0.0f, 0.0f, 0.0f}, 0f, 0.75f);//晕映
            count++;
        }

        switch (config.getShapeMode()) {
            case ShapeMode.RECT:

                break;
            case ShapeMode.RECT_ROUND:
                transformation[count] = new RoundedCornersTransformation
                        (config.getRectRoundRadius(), 0, RoundedCornersTransformation.CornerType.ALL);
                count++;
                break;
            case ShapeMode.OVAL:
                transformation[count] = new CropCircleTransformation();
                count++;
                break;

            case ShapeMode.SQUARE:
                transformation[count] = new CropSquareTransformation();
                count++;
                break;
        }

        if (transformation.length != 0) {
            request.transforms(transformation);
        }

    }

    private int statisticsCount(SingleConfig config) {
        int count = 0;

        if (config.getShapeMode() == ShapeMode.OVAL || config.getShapeMode() == ShapeMode.RECT_ROUND || config.getShapeMode() == ShapeMode.SQUARE) {
            count++;
        }

        if (config.isNeedBlur()) {
            count++;
        }

        if (config.isNeedFilteColor()) {
            count++;
        }

        if (config.isNeedBrightness()) {
            count++;
        }

        if (config.isNeedGrayscale()) {
            count++;
        }

        if (config.isNeedSwirl()) {
            count++;
        }

        if (config.isNeedToon()) {
            count++;
        }

        if (config.isNeedSepia()) {
            count++;
        }

        if (config.isNeedContrast()) {
            count++;
        }

        if (config.isNeedInvert()) {
            count++;
        }

        if (config.isNeedPixelation()) {
            count++;
        }

        if (config.isNeedSketch()) {
            count++;
        }

        if (config.isNeedVignette()) {
            count++;
        }

        return count;
    }

    @Override
    public void pause() {
        Glide.with(GlobalConfig.context).pauseRequestsRecursive();

    }

    @Override
    public void resume() {
        Glide.with(GlobalConfig.context).resumeRequestsRecursive();
    }

    @Override
    public void clearDiskCache() {
        Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Glide.get(GlobalConfig.context).clearDiskCache();
                    }
                });

    }

    @Override
    public void clearMemoryCache(View view) {
        Glide.get(GlobalConfig.context)
                .getRequestManagerRetriever()
                .get(GlobalConfig.context)
                .clear(view);
    }

    @Override
    public void clearMemory() {
        Glide.get(GlobalConfig.context).clearMemory();
    }

    @Override
    public void clearAllCache() {
        clearMemory();
        clearDiskCache();
    }

    @Override
    public boolean isCached(String url) {
        return false;
    }

    @Override
    public void trimMemory(int level) {
        Glide.with(GlobalConfig.context).onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        Glide.with(GlobalConfig.context).onLowMemory();
    }

    @Override
    public void saveImageIntoGallery(DownLoadImageService downLoadImageService) {
        new Thread(downLoadImageService).start();
    }

    @Override
    public String getCacheSize() {
        return GlideCacheUtil.getCacheSize(GlobalConfig.context);
    }

}
