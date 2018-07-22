package com.app.handyman.mender.common.rest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Dinesh Prajapati on 13/4/18.
 */
public interface LoadInterface {

    /************************GET_REQUEST******************************/
    @GET("common/notify.php")
    Call<ResponseBody> sendNotify();

}
