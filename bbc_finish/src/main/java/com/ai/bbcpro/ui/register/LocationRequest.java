package com.ai.bbcpro.ui.register;

import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.BaseJSONRequest;

public class LocationRequest extends BaseJSONRequest {

    public LocationRequest(String latitude, String longitude) {
        setAbsoluteURI("http://maps.google.cn/maps/api/geocode/json?latlng="
                + latitude + "," + longitude + "&sensor=true&language=zh-CN");
    }


    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new LocationResponse();
    }

}

