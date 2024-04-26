package com.example.dalaptrinhapp.API;
import com.example.dalaptrinhapp.Model.bookDto;
import com.example.dalaptrinhapp.Model.bookmodel;
import com.example.dalaptrinhapp.Model.cartDto;
import com.example.dalaptrinhapp.Model.categorymodel;
import com.example.dalaptrinhapp.Model.apiresponse;
import com.example.dalaptrinhapp.Model.orderdetailDto;
import com.example.dalaptrinhapp.Model.ordermodel;
import com.example.dalaptrinhapp.Model.usermodel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface myAPI {
    @GET("getBook.php")
    Call<ArrayList<bookmodel>> callArraylist();

    @GET("bookInformation.php")
    Call<bookDto> getBookwithCatename(@Query("book_id") int book_id);

    @GET("getCategory.php")
    Call<ArrayList<categorymodel>> callCategoryList();

    @GET("getNewBook.php")
    Call<ArrayList<bookmodel>> callNewBooklist();

    //check login
    @FormUrlEncoded
    @POST("auth.php")
    Call<apiresponse> authenticate(@Field("email") String email, @Field("password") String password);

    //check register
    @FormUrlEncoded
    @POST("register.php")
    Call<apiresponse> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("phone_number") String phonenumber,
            @Field("password") String password,
            @Field("confirm_password") String confirm_password
    );

    @GET("getUser.php")
    Call<usermodel> getUserbyUserID(@Query("user_id") int user_id);

    //tra ve cartmodel ?
    @GET("addtoCart.php")
    Call<apiresponse> addtoCart(@Query("user_id") int user_id, @Query("book_id") int book_id);

    @GET("displayCart.php")
    Call<ArrayList<cartDto>> getCartbyUserID(@Query("user_id") int user_id);

    @GET("plusQuantityItem.php")
    Call<apiresponse> plusQuantity(@Query("user_id") int user_id, @Query("book_id") int book_id);

    @GET("diffQuantityItem.php")
    Call<apiresponse> diffQuantity(@Query("user_id") int user_id, @Query("book_id") int book_id);

    @GET("deleteCartItem.php")
    Call<apiresponse> deleteCartItem(@Query("user_id") int user_id, @Query("book_id") int book_id);

    @FormUrlEncoded
    @POST("updateUserInfor.php")
    Call<apiresponse> updateUserInfor(
            @Query("user_id") int user_id,
            @Field("username") String username,
            @Field("phone_number") String phonenumber,
            @Field("address") String password
    );

    @FormUrlEncoded
    @POST("addtoOrder.php")
    Call<apiresponse> addtoOrder(
            @Query("user_id") int user_id,
            @Field("quantity") int quantity,
            @Field("totalprice") double totalprice,
            @Field("email") String email,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("address") String address
    );

    @GET("getOrder.php")
    Call<ArrayList<ordermodel>> getOrderListbyuID(@Query("user_id") int user_id);

    @GET("getOrderbyOrderID.php")
    Call<ordermodel> getOrderbyOrderID( @Query("order_id") int order_id);


    @GET("getOrderdetailbyorderID.php")
    Call<ArrayList<orderdetailDto>> getOrderDetail(@Query("user_id") int user_id, @Query("order_id") int order_id);

    @FormUrlEncoded
    @POST("sendMessage.php")
    Call<apiresponse> sendmessage(
            @Query("user_id") int user_id,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("message") String message
    );
}
