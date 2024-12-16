package com.example.project1

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LogoutInterface {
    @FormUrlEncoded
    @POST("realms/realm-ebank/protocol/openid-connect/logout")
    suspend fun logout(
        @Field("client_id") clientId: String,
        @Field("refresh_token") refreshToken: String
    ): retrofit2.Response<Void>
}
