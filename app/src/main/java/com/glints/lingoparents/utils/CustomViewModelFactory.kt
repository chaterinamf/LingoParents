package com.glints.lingoparents.utils

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.glints.lingoparents.ui.accountsetting.AccountSettingViewModel
import com.glints.lingoparents.ui.accountsetting.changepassword.PasswordSettingViewModel
import com.glints.lingoparents.ui.accountsetting.linkedaccount.LinkedAccountViewModel
import com.glints.lingoparents.ui.accountsetting.linkedaccount.category.LinkedAccountListViewModel
import com.glints.lingoparents.ui.accountsetting.linkedaccount.codeinvitation.ChildrenCodeInvitationFragment
import com.glints.lingoparents.ui.accountsetting.linkedaccount.codeinvitation.ChildrenCodeInvitationViewModel
import com.glints.lingoparents.ui.accountsetting.profile.ProfileViewModel
import com.glints.lingoparents.ui.course.AllCoursesViewModel
import com.glints.lingoparents.ui.home.HomeViewModel
import com.glints.lingoparents.ui.course.DetailCourseViewModel
import com.glints.lingoparents.ui.dashboard.DashboardViewModel
import com.glints.lingoparents.ui.insight.InsightListViewModel
import com.glints.lingoparents.ui.insight.detail.DetailInsightViewModel
import com.glints.lingoparents.ui.liveevent.LiveEventListViewModel
import com.glints.lingoparents.ui.liveevent.category.CompletedLiveEventViewModel
import com.glints.lingoparents.ui.liveevent.category.TodayLiveEventViewModel
import com.glints.lingoparents.ui.liveevent.category.UpcomingLiveEventViewModel
import com.glints.lingoparents.ui.liveevent.detail.LiveEventDetailViewModel
import com.glints.lingoparents.ui.login.LoginViewModel
import com.glints.lingoparents.ui.progress.ProgressViewModel
import com.glints.lingoparents.ui.progress.learning.ProgressLearningCourseViewModel
import com.glints.lingoparents.ui.progress.learning.ProgressLearningViewModel
import com.glints.lingoparents.ui.progress.learning.assignment.AssignmentViewModel
import com.glints.lingoparents.ui.progress.profile.ProgressProfileViewModel
import com.glints.lingoparents.ui.register.RegisterViewModel
import com.glints.lingoparents.ui.splash.SplashViewModel

class CustomViewModelFactory(
    private val tokenPref: TokenPreferences,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
    private val eventId: Int? = null,
    private val insightId: Int? = null,
    private val studentId: Int? = null,
    private val courseId: Int? = null,
    private val sessionId: Int? = null,
    private val googleIdToken: String? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(tokenPref, googleIdToken = googleIdToken) as T
            }
            modelClass.isAssignableFrom(LiveEventListViewModel::class.java) -> {
                LiveEventListViewModel() as T
            }
            modelClass.isAssignableFrom(TodayLiveEventViewModel::class.java) -> {
                TodayLiveEventViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(UpcomingLiveEventViewModel::class.java) -> {
                UpcomingLiveEventViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(CompletedLiveEventViewModel::class.java) -> {
                CompletedLiveEventViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(LiveEventDetailViewModel::class.java) -> {
                LiveEventDetailViewModel(tokenPref, eventId!!) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(PasswordSettingViewModel::class.java) -> {
                PasswordSettingViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(InsightListViewModel::class.java) -> {
                InsightListViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(AllCoursesViewModel::class.java) -> {
                AllCoursesViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(DetailCourseViewModel::class.java) -> {
                DetailCourseViewModel(tokenPref, eventId!!) as T
            }
            modelClass.isAssignableFrom(DetailInsightViewModel::class.java) -> {
                DetailInsightViewModel(tokenPref, insightId!!) as T
            }
            modelClass.isAssignableFrom(ProgressViewModel::class.java) -> {
                ProgressViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(ProgressProfileViewModel::class.java) -> {
                ProgressProfileViewModel() as T
            }
            modelClass.isAssignableFrom(ProgressLearningViewModel::class.java) -> {
                ProgressLearningViewModel() as T
            }
            modelClass.isAssignableFrom(ProgressLearningCourseViewModel::class.java) -> {
                ProgressLearningCourseViewModel(studentId = studentId!!, courseId = courseId!!) as T
            }
            modelClass.isAssignableFrom(AssignmentViewModel::class.java) -> {
                AssignmentViewModel(studentId!!, sessionId!!) as T
            }
            modelClass.isAssignableFrom(AccountSettingViewModel::class.java) -> {
                AccountSettingViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(LinkedAccountViewModel::class.java) -> {
                LinkedAccountViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(LinkedAccountListViewModel::class.java) -> {
                LinkedAccountListViewModel(tokenPref) as T
            }
            modelClass.isAssignableFrom(ChildrenCodeInvitationViewModel::class.java) -> {
                ChildrenCodeInvitationViewModel(tokenPref) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

}