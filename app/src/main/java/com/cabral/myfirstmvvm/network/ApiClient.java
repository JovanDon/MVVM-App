package com.cabral.myfirstmvvm.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static String BaseUrl="https://jsonplaceholder.typicode.com/";
    private static  ApiRequests apiRequests;

    public static ApiRequests getInstance(){
        if(apiRequests==null){

            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
                                    //.addHeader(header, authToken)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            Retrofit retrofit= new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            apiRequests=retrofit.create(ApiRequests.class);

            return  apiRequests;
        }else {
            return  apiRequests;
        }
    }

}
