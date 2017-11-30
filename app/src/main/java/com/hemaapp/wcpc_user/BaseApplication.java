package com.hemaapp.wcpc_user;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.support.multidex.MultiDex;

import com.hemaapp.hm_FrameWork.HemaApplication;
import com.hemaapp.hm_FrameWork.orm.SqliteUtility;
import com.hemaapp.hm_FrameWork.orm.SqliteUtilityBuilder;
import com.hemaapp.hm_FrameWork.orm.extra.Extra;
import com.hemaapp.wcpc_user.db.SysInfoDBHelper;
import com.hemaapp.wcpc_user.model.SysInitInfo;
import com.hemaapp.wcpc_user.model.User;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;

import xtom.frame.XtomConfig;
import xtom.frame.util.XtomLogger;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 *
 */
public class BaseApplication extends HemaApplication {
    private static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication application;
    private SysInitInfo sysInitInfo;// 系统初始化信息
    private User user;
    private SqliteUtilityBuilder sqliteUtilityBuilder;//orm的对象保存

    @Override
    public void onCreate() {
        application = this;
        XtomConfig.LOG = BaseConfig.DEBUG;
        String iow = XtomSharedPreferencesUtil.get(this, "imageload_onlywifi");
        XtomConfig.IMAGELOAD_ONLYWIFI = "true".equals(iow);
        XtomConfig.DATAKEY = "9qk2hKHaRTysJqCS";
        XtomConfig.DIGITAL_CHECK = true;
        Locale.setDefault(Locale.CHINESE);
        //压缩图片的最大大小
        XtomConfig.MAX_IMAGE_SIZE = 400;
        sqliteUtilityBuilder = new SqliteUtilityBuilder();
        sqliteUtilityBuilder.configVersion(BaseConfig.DATA_BASE_VERSION).build(this);
        super.onCreate();
        initImageLoader();
        MobSDK.init(this,"22208d6b7ef70","3153dc341931b39ed4e521aff81a54a4");
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//突破65535限制
    }

    public static BaseApplication getInstance() {
        return application;
    }

    /**
     * @return 当前用户
     */
    public User getUser() {
        if (user == null) {
            List<User> listUser = null;
            try {
                listUser = SqliteUtility.getInstance().select(new Extra(), User.class);
            } catch (Exception e) {
                listUser=null;
            }
            if (listUser != null && listUser.size() > 0)
                user = listUser.get(0);
        }
        return user;
    }

    /**
     * 设置保存当前用户
     *
     * @param user 当前用户
     */
    public void setUser(User user) {
        this.user = user;
        SqliteUtility.getInstance().deleteAll(new Extra(), User.class);
        if (user != null) {
            SqliteUtility.getInstance().insert(new Extra(), user);
        }
    }
    /**
     * @return 系统初始化信息
     */
    public SysInitInfo getSysInitInfo() {
        if (sysInitInfo == null) {
            SysInfoDBHelper helper = new SysInfoDBHelper(this);
            sysInitInfo = helper.select();
            helper.close();
        }
        return sysInitInfo;
    }

    /**
     * 设置保存系统初始化信息
     *
     * @param sysInitInfo 系统初始化信息
     */
    public void setSysInitInfo(SysInitInfo sysInitInfo) {
        this.sysInitInfo = sysInitInfo;
        if (sysInitInfo != null) {
            SysInfoDBHelper helper = new SysInfoDBHelper(this);
            helper.insertOrUpdate(sysInitInfo);
            helper.close();
        }
    }

    /**
     * 初始化imageLoader
     */
    @SuppressWarnings("deprecation")
    public void initImageLoader() {
        L.writeLogs(false);
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽

                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(1 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(256 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);
    }

    @SuppressWarnings("deprecation")
    public DisplayImageOptions getOptions(int drawableId) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId)
                .showImageForEmptyUri(drawableId)
                .showImageOnFail(drawableId)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Config.RGB_565)
                .build();
    }

}
