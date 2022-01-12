package com.glints.lingoparents.ui.accountsetting

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.glints.lingoparents.R
import com.glints.lingoparents.databinding.FragmentAccountSettingBinding
import com.glints.lingoparents.ui.accountsetting.profile.ProfileViewModel
import com.glints.lingoparents.ui.dashboard.DashboardActivity
import com.glints.lingoparents.utils.CustomViewModelFactory
import com.glints.lingoparents.utils.TokenPreferences
import com.glints.lingoparents.utils.dataStore
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class AccountSettingFragment : Fragment(R.layout.fragment_account_setting) {
    private lateinit var tokenPreferences: TokenPreferences
    private lateinit var viewModel: AccountSettingViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.account_setting_tab_text_1,
            R.string.account_setting_tab_text_2
        )
    }

    private var _binding: FragmentAccountSettingBinding? = null
    private val binding get() = _binding!!
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        tokenPreferences = TokenPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, CustomViewModelFactory(tokenPreferences, this))[
                AccountSettingViewModel::class.java
        ]
        viewModel.loadingState()

        _binding = FragmentAccountSettingBinding.inflate(inflater)

        val sectionsPagerAdapter =
            ProgressSectionsPagerAdapter(requireActivity() as AppCompatActivity)
        val viewPager2 = binding.viewPagerAccountSetting
        viewPager2.adapter = sectionsPagerAdapter
        val tabs = binding.tabAccountSetting
        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val root = tabs.getChildAt(0)
        if (root is LinearLayout) {
            root.apply {
                showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
                val drawable = GradientDrawable()
                drawable.apply {
                    setColor(ContextCompat.getColor(requireContext(), R.color.teal_700))
                    setSize(4, 1)
                }
                dividerPadding = 18
                dividerDrawable = drawable
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                    (activity as DashboardActivity).showBottomNav(true)
                }
            })

        (activity as DashboardActivity).showBottomNav(false)

        binding.apply {
            ivBackButton.setOnClickListener {
                findNavController().popBackStack()
                (activity as DashboardActivity).showBottomNav(true)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.accountSettingEvent.collect { event ->
                when (event) {
                    is AccountSettingViewModel.AccountSetting.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }

        return binding.root
    }

    @Subscribe
    fun getNameAndPhoto(event: ProfileViewModel.EventBusActionToAccountSetting.SendParentData) {
        showLoading(false)
        binding.apply {
            event.parentProfile.apply {
                val name = "$firstname $lastname"
                tvParent.text = name
                if (photo != null) {
                    ivProfilePicture.load(photo)
                } else {
                    ivProfilePicture.load(R.drawable.ic_user_avatar_male_square)
                }

            }
        }
    }

    private fun showLoading(boolean: Boolean) {
        binding.apply {
            ivProfilePicture.isVisible = !boolean
            tvHello.isVisible = !boolean
            tvParent.isVisible = !boolean
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}