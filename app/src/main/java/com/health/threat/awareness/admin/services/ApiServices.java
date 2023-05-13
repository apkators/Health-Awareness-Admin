package com.health.threat.awareness.admin.services;

import com.health.threat.awareness.admin.R;
import com.health.threat.awareness.admin.model.MyResponse;
import com.health.threat.awareness.admin.model.NotificationSender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServices {
    @Headers(
            {"Content-Type:application/json",
                    "Authorization:key=AAAAQVgnE08:APA91bGqWxtWIV6MZchepM3WVQngTEO3heQGwfe8LtQ9_o7x910T8FC_0e1vjBdCh4UzsZPvgZRvmBEki7e29kqIXyjB1RAF2XL0lvgL8ZojR47YZoloPla4g_7-od5O2ifOxMrBDx6K"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);

}
