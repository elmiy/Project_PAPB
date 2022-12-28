package com.example.tugasakhir.api;

import com.example.tugasakhir.model.BaseResponse;
import com.example.tugasakhir.model.transaction.TransactionBody;
import com.example.tugasakhir.model.transaction.TransactionResponse;
import com.example.tugasakhir.model.user.AvatarBody;
import com.example.tugasakhir.model.user.UserBody;
import com.example.tugasakhir.model.user.UserResponse;
import com.example.tugasakhir.model.wallet.WalletResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIRequestData {
    @POST("/user")
    Call<BaseResponse<String>> postUser(@Body UserBody body);

    @GET("/user/{uid}")
    Call<BaseResponse<UserResponse>> fetchUser(@Path("uid") String uid);

    @PUT("/user/{uid}/avatar")
    Call<BaseResponse<String>> putAvatar(@Path("uid") String uid, @Body AvatarBody body);

    @GET("/user/{uid}/wallet")
    Call<BaseResponse<WalletResponse>> fetchWallet(@Path("uid") String uid);

    @POST("/transaction")
    Call<BaseResponse<String>> postTransaction(@Body TransactionBody body);

    @GET("/user/{uid}/transaction")
    Call<BaseResponse<List<TransactionResponse>>> fetchTransactions(@Path("uid") String uid);

    @GET("/transaction/{transactionId}")
    Call<BaseResponse<TransactionResponse>> fetchTransaction(@Path("transactionId") String transactionId);

    @PUT("/transaction/{transactionId}")
    Call<BaseResponse<String>> putTransaction(@Path("transactionId") String transactionId, @Body TransactionBody body);

    @DELETE("/transaction/{transactionId}")
    Call<BaseResponse<String>> deleteTransaction(@Path("transactionId") String transactionId);
}
