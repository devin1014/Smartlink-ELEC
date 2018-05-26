package com.android.smartlink.application.manager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntDef;

import com.android.smartlink.Constants;
import com.android.smartlink.bean.Modbus;
import com.android.smartlink.bean.Modbus.Equipment;
import com.android.smartlink.bean.Modbus.EquipmentToggle;
import com.android.smartlink.bean.Weather;
import com.android.smartlink.ui.widget.adapter.SuggestPagerAdapter;

import java.util.Date;
import java.util.List;

/**
 * User: NeuLion(wei.liu@neulion.com.com)
 * Date: 2017-10-18
 * Time: 15:36
 */
public class AppManager
{
    @SuppressLint("StaticFieldLeak")
    private static AppManager sAppManager;

    public static AppManager getInstance()
    {
        return sAppManager;
    }

    public static void initialize(Application application)
    {
        if (sAppManager == null)
        {
            sAppManager = new AppManager(application);
        }
    }

    @IntDef({RequestMode.MODE_LOCAL, RequestMode.MODE_HTTP, RequestMode.MODE_REMOTE})
    public @interface RequestMode
    {
        int MODE_LOCAL = 1;
        int MODE_HTTP = 2;
        int MODE_REMOTE = 3;
    }

    private final Application mApplication;

    private SharedPreferences mSharedPreferences;

    private EquipmentManager mEquipmentManager;

    private List<EquipmentToggle> mToggles;

    private Weather mWeather;

    private boolean mPhoneType;

    @RequestMode
    private int mRequestMode = RequestMode.MODE_REMOTE;

    private AppManager(Application application)
    {
        mApplication = application;

        mSharedPreferences = application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);

        mEquipmentManager = new EquipmentManager(application);

        mPhoneType = application.getResources().getConfiguration().smallestScreenWidthDp < 600;
    }

    // -----------------------------------
    // ------------ 初始化 ----------------
    public boolean isInitialized()
    {
        return mApplication != null && mEquipmentManager.isInitialized();
    }

    public Application getApplication()
    {
        return mApplication;
    }

    public String getString(int resId)
    {
        return mApplication.getResources().getString(resId);
    }

    public String[] getStringArray(int resId)
    {
        return mApplication.getResources().getStringArray(resId);
    }

    public boolean isPhone()
    {
        return mPhoneType;
    }

    // -----------------------------------
    // ------------ Weather&Location -----
    public Weather getWeather()
    {
        if (mWeather != null)
        {
            try
            {
                Date date = mWeather.getBasic().getUpdateTime();

                long deltaTime = Math.abs(System.currentTimeMillis() - date.getTime());

                if (deltaTime <= Constants.ONE_DAY && deltaTime > 0)
                {
                    return mWeather;
                }
            }
            catch (Exception ignored)
            {
            }
        }

        return null;
    }

    public void setWeather(Weather weather)
    {
        mWeather = weather;
    }

    public void setLocation(String location)
    {
        mSharedPreferences.edit().putString(Constants.KEY_SHARE_PREFERENCE_LOCATION, location).apply();

        mSharedPreferences.edit().putLong(Constants.KEY_SHARE_PREFERENCE_LOCATION_TIME, System.currentTimeMillis()).apply();
    }

    public String getLocation()
    {
        long time = mSharedPreferences.getLong(Constants.KEY_SHARE_PREFERENCE_LOCATION_TIME, -1L);

        if (time < 0 || (System.currentTimeMillis() - time) >= Constants.ONE_DAY)
        {
            return null;
        }

        return mSharedPreferences.getString(Constants.KEY_SHARE_PREFERENCE_LOCATION, null);
    }

    // -----------------------------------
    // ------------ Equipment ------------
    public void setEquipmentName(int id, String name)
    {
        mEquipmentManager.update(id, name);
    }

    public String getEquipmentName(int id)
    {
        return mEquipmentManager.getName(id);
    }

    public void setEquipments(Modbus modbus)
    {
        mEquipmentManager.setEquipments(modbus);

        mToggles = modbus.getToggles();
    }

    public Equipment getEquipment(int id)
    {
        return mEquipmentManager.getEquipment(id);
    }

    public List<Equipment> getEquipments()
    {
        return mEquipmentManager.getEquipments();
    }

    public EquipmentToggle getToggle(int channelId)
    {
        for (EquipmentToggle t : mToggles)
        {
            if (t.getChannel() == channelId)
            {
                return t;
            }
        }

        return null;
    }

    // -----------------------------------
    // ------------ Demo status ----------
    public void setDemoMode(boolean demoMode)
    {
        mSharedPreferences.edit().putBoolean(Constants.KEY_SHARE_PREFERENCE_DEMO_MODE, demoMode).apply();
    }

    public boolean isDemoMode()
    {
        return mSharedPreferences.getBoolean(Constants.KEY_SHARE_PREFERENCE_DEMO_MODE, isPhone());
    }

    public void setDemoModeStatus(int status)
    {
        mSharedPreferences.edit().putInt(Constants.KEY_SHARE_PREFERENCE_DEMO_STATUS, status).apply();
    }

    public int getDemoModeStatus()
    {
        return mSharedPreferences.getInt(Constants.KEY_SHARE_PREFERENCE_DEMO_STATUS, Constants.STATUS_NORMAL);
    }

    public void setRequestMode(@RequestMode int mode)
    {
        mRequestMode = mode;
    }

    public int getRequestMode()
    {
        return mRequestMode;
    }

    public boolean isLocalMode()
    {
        return mRequestMode == RequestMode.MODE_LOCAL;
    }

    public boolean isHttpMode()
    {
        return mRequestMode == RequestMode.MODE_HTTP;
    }

    public boolean isRemoteMode()
    {
        return mRequestMode == RequestMode.MODE_REMOTE;
    }

    // -----------------------------------
    // ------------ Suggest --------------
    public int getEnergySuggestIndex()
    {
        final int index = mSharedPreferences.getInt(Constants.KEY_SHARE_PREFERENCE_SUGGEST, 0);

        int nextIndex = index + 1;

        if (nextIndex >= SuggestPagerAdapter.SUGGESTIONS.length)
        {
            nextIndex = 0;
        }

        mSharedPreferences.edit().putInt(Constants.KEY_SHARE_PREFERENCE_SUGGEST, nextIndex).apply();

        return index;
    }

}
