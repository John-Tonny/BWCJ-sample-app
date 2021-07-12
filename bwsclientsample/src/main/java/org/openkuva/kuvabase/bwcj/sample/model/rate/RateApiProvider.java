/*
 * Copyright (c)  2018 One Kuva LLC, known as OpenKuva.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the One Kuva LLC, known as OpenKuva.org nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.openkuva.kuvabase.bwcj.sample.model.rate;

import com.google.gson.GsonBuilder;

import org.openkuva.kuvabase.bwcj.service.rate.interfaces.IBlackcarrotRateApi;
import org.openkuva.kuvabase.bwcj.service.rate.retrofit2.IRetrofit2RateAPI;
import org.openkuva.kuvabase.bwcj.service.rate.retrofit2.Retrofit2RateApiBridge;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RateApiProvider {
    private static IBlackcarrotRateApi api;

    private RateApiProvider() {
    }

    public static IBlackcarrotRateApi get() {
        if (api == null) {
            synchronized (RateApiProvider.class) {
                if (api == null) {
                    api =
                            new Retrofit2RateApiBridge(
                                    new Retrofit.Builder()
                                            .baseUrl(IBlackcarrotRateApi.URL)
                                            .addConverterFactory(
                                                    GsonConverterFactory.create(
                                                            new GsonBuilder()
                                                                    .setLenient()
                                                                    .create()))
                                            .client(
                                                    new OkHttpClient
                                                            .Builder()
                                                            .addInterceptor(
                                                                    new HttpLoggingInterceptor()
                                                                            .setLevel(HttpLoggingInterceptor.Level.BODY))
                                                            .build())
                                            .build()
                                            .create(IRetrofit2RateAPI.class));
                }
            }
        }
        return api;
    }
}
