package com.glints.lingoparents.data.api

import com.glints.lingoparents.data.model.response.*
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @FormUrlEncoded
    @POST("api/v1/login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginUserResponse>

    @POST("api/v1/logout")
    fun logoutUser(): Call<LogoutUserResponse>

    @FormUrlEncoded
    @POST("api/v1/register")
    fun registerUser(
        @Field("email") email: String,
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("address") address: String = "_",
        @Field("gender") gender: String = "Male",
        @Field("role") role: String = "parent"
    ): Call<RegisterUserResponse>

    @FormUrlEncoded
    @POST("api/v1/forgot-password")
    fun sendForgotPasswordRequest(
        @Field("email") email: String,
        @Field("frontend_url") url: String = "http://fe-main.ipe-glintsacademy.com/api/v1/reset-password"
    ): Call<ForgotPasswordResponse>

    @FormUrlEncoded
    @POST("api/v1/password-reset")
    fun resetPassword(
        @QueryMap options: Map<String, String>,
        @Field("password") newPassword: String,
        @Field("confirmpassword") confirmNewPassword: String,
    ): Call<ResetPasswordResponse>

    @GET("api/v1/events/participants/pages")
    fun getLiveEventsByStatus(
        @QueryMap options: Map<String, String>,
    ): Call<LiveEventListResponse>

    @GET("api/v1/events/parent/live/{title}")
    fun getTodayLiveEventByStatusAndTitle(
        @Path("title") title: String
    ): Call<LiveEventSearchListResponse>

    @GET("api/v1/events/parent/upcoming/{title}")
    fun getUpcomingLiveEventByStatusAndTitle(
        @Path("title") title: String
    ): Call<LiveEventSearchListResponse>

    @GET("api/v1/events/parent/completed/{title}")
    fun getCompletedLiveEventByStatusAndTitle(
        @Path("title") title: String
    ): Call<LiveEventSearchListResponse>

    @GET("api/v1/events/participants/{id}")
    fun getLiveEventById(
        @Path("id") id: Int,
    ): Call<LiveEventDetailResponse>


    @GET("api/v1/insights?status=Publish")
    fun getAllInsightList(
        @QueryMap options: Map<String, String>,
    ): Call<AllInsightsListResponse>

    @GET("api/v1/insights?status=Publish")
    fun getInsightSearchList(
        @QueryMap options: Map<String, String>
    ): Call<AllInsightsListResponse>

    @GET("api/v1/insights/{id}")
    fun getInsightDetail(
        @Path("id") id: Int,
    ): Call<InsightDetailResponse>

    @POST("api/v1/insights/like/{id}/{type}")
    fun likeInsightDetail(
        @Path("id") id: Int,
        @Path("type") type: String,
    ): Call<InsightLikeDislikeResponse>

    @POST("api/v1/insights/dislike/{id}/{type}")
    fun dislikeInsightDetail(
        @Path("id") id: Int,
        @Path("type") type: String,
    ): Call<InsightLikeDislikeResponse>

    @FormUrlEncoded
    @POST("api/v1/insights/comment/{id}/{type}")
    fun createComment(
        @Path("id") id: Int,
        @Path("type") type: String,
        @Field("comment") comment: String
    ): Call<CreateCommentResponse>

    @GET("api/v1/insights/comment/{id}")
    fun getCommentReplies(
        @Path("id") id: Int
    ): Call<GetCommentRepliesResponse>

    @DELETE("api/v1/insights/comment/{id}")
    fun deleteComment(
        @Path("id") id: Int
    ): Call<DeleteCommentResponse>

    //amin
    @GET("api/v1/courses")
    fun getCourseList(
        @Header("authorization") authorization: String
    ): Call<AllCoursesResponse>

    @GET("api/v1/parents/profile")
    fun getParentProfile(
        @Header("authorization") authorization: String
    ): Call<ParentProfileResponse>

    @FormUrlEncoded
    @PUT("api/v1/parents/profile")
    fun editParentProfile(
        @Header("authorization") authorization: String,
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("address") address: String,
        @Field("phone") phone: String
    ): Call<EditParentProfileResponse>

    @FormUrlEncoded
    @PUT("api/v1/parents/profile/change-password")
    fun changePassword(
        @Header("authorization") authorization: String,
        @Field("password") currentPassword: String,
        @Field("new_password") password: String,
        @Field("confirmpassword") confirmPassword: String,
    ): Call<ChangePasswordResponse>

    @GET("api/v1/students/list/{id}")
    fun getStudentList(
        @Path("id") id: Int,
    ): Call<StudentListResponse>

    @GET("api/v1/insights/recent")
    fun getRecentInsight(
    ): Call<RecentInsightResponse>

    @GET("api/v1/events/parent")
    fun getAllEvent(
    ): Call<AllEventResponse>

    @GET("api/v1/courses/{id}")
    fun getCourseDetail(
        @Path("id") id: Int,
        @Header("authorization") authorization: String
    ): Call<DetailCourseResponse>

    @GET("api/v1/students/list/{id}")
    fun getStudentListByParentId(
        @Path("id") id: String,
    ): Call<StudentListResponse>

    @GET("api/v1/students/{id}")
    fun getStudentProfileById(
        @Path("id") id: Int,
    ): Call<StudentProfileResponse>

    @GET("api/v1/students/{id}/courses")
    fun getCourseListByStudentId(
        @Path("id") studentId: Int
    ): Call<CourseListByStudentIdResponse>

    @GET("api/v1/students/{studentId}/courses/{courseId}")
    fun getCourseDetailByStudentId(
        @Path("studentId") studentId: Int,
        @Path("courseId") courseId: Int
    ): Call<CourseDetailByStudentIdResponse>

    @GET("api/v1/students/{studentId}/session-detail/{sessionId}")
    fun getSessionDetailBySessionId(
        @Path("studentId") studentId: Int,
        @Path("sessionId") sessionId: Int
    ): Call<SessionDetailBySessionIdResponse>

    @FormUrlEncoded
    @POST("api/v1/events/participants")
    fun registerLiveEvent(
        @Field("id_user") id_user: Int,
        @Field("id_event") id_event: Int,
        @Field("fullname") fullname: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("attendance") attendance: String,
        @Field("attendance_time") attendance_time: String,
        @Field("idUser_create") idUser_create: Int,
        @Field("total_prize") total_prize: Int,
        @Field("voucher_code") voucher_code: String,
        @Field("payment_method") payment_method: String,
        @Field("status") status: String
    ): Call<LiveEventRegisterResponse>

}