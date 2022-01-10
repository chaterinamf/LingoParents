package com.glints.lingoparents.ui.liveevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glints.lingoparents.ui.dashboard.DashboardViewModel.DashboardEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class LiveEventListViewModel : ViewModel() {
    fun sendQueryToLiveEventListFragment(query: String) = viewModelScope.launch {
        EventBus.getDefault()
            .postSticky(LiveEventListEvent.SendQueryToEventListFragment(query))
    }

    fun sendBlackQueryToLiveEventListFragment() = viewModelScope.launch {
        EventBus.getDefault()
            .postSticky(LiveEventListEvent.SendBlankQueryToEventListFragment)
    }

    sealed class LiveEventListEvent {
        data class SendQueryToEventListFragment(val query: String) : LiveEventListEvent()
        object SendBlankQueryToEventListFragment : LiveEventListEvent()
    }
}