package com.android.smartlink.assist;

import com.android.smartlink.bean.WeatherLocation;
import com.lzy.okgo.OkGo;

/**
 * User: LIUWEI
 * Date: 2017-12-25
 * Time: 11:52
 */
public abstract class LocationProvider extends BaseRequestProvider<WeatherLocation>
{
    public static LocationProvider newInstance(RequestCallback<WeatherLocation> callback)
    {
        return new LocalProvider(callback);
    }

    LocationProvider(RequestCallback<WeatherLocation> callback)
    {
        super(callback);
    }

    @Override
    Class<WeatherLocation> getConvertObjectClass()
    {
        return WeatherLocation.class;
    }

    static class LocalProvider extends LocationProvider
    {
        LocalProvider(RequestCallback<WeatherLocation> callback)
        {
            super(callback);
        }

        @Override
        public void request(String url)
        {
            notifyResponse(new WeatherLocation());
        }
    }

    static class HttpProvider extends LocationProvider
    {
        HttpProvider(RequestCallback<WeatherLocation> callback)
        {
            super(callback);
        }

        @Override
        public void request(String url)
        {
            OkGo.getInstance().cancelTag(this);

            OkGo.<WeatherLocation>get(url).tag(this).execute(new ResponseCallback());
        }
    }
}
