package com.glints.lingoparents.ui.course

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glints.lingoparents.R
import com.glints.lingoparents.data.model.CourseItem
import com.glints.lingoparents.data.model.response.AllCoursesResponse
import com.glints.lingoparents.databinding.FragmentAllCoursesBinding
import com.glints.lingoparents.databinding.FragmentTodayEventBinding
import com.glints.lingoparents.ui.course.adapter.CourseAdapter
import com.glints.lingoparents.utils.CustomViewModelFactory
import com.glints.lingoparents.utils.TokenPreferences
import com.glints.lingoparents.utils.dataStore
import kotlinx.coroutines.flow.collect

class AllCoursesFragment : Fragment(R.layout.fragment_all_courses),
    CourseAdapter.OnItemClickCallback {
    private var _binding: FragmentAllCoursesBinding? = null
    private val binding get() = _binding!!

    private lateinit var courseAdapter: CourseAdapter
    private lateinit var tokenPreferences: TokenPreferences
    private lateinit var viewModel: AllCoursesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllCoursesBinding.inflate(inflater)

        tokenPreferences = TokenPreferences.getInstance(requireContext().dataStore)
        viewModel =
            ViewModelProvider(this, CustomViewModelFactory(tokenPreferences, this, arguments))[
                    AllCoursesViewModel::class.java
            ]
        //amin
//        viewModel = ViewModelProvider(this, CustomViewModelFactory(tokenPreferences))[
//                AllCoursesViewModel::class.java
//
//        ]

        binding.apply {
            rvCourse.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                courseAdapter = CourseAdapter(this@AllCoursesFragment)
                adapter = courseAdapter
            }
        }

        viewModel.getAccessToken().observe(viewLifecycleOwner) { accessToken ->
            viewModel.getAllCourses(accessToken)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.allCoursesEvent.collect { event ->
                when (event) {
                    is AllCoursesViewModel.AllCoursesEvent.Loading -> {
                        showLoading(true)
                        showEmptyWarning(false)
                    }
                    is AllCoursesViewModel.AllCoursesEvent.Success -> {
                        courseAdapter.submitList(event.list)
                        showLoading(false)
                    }
                    is AllCoursesViewModel.AllCoursesEvent.Error -> {
                        showLoading(false)
                        showEmptyWarning(true)
                    }
//                    is AllCoursesViewModel.AllCoursesEvent.NavigateToDetailLiveEventFragment -> {
//                        val action =
//                            LiveEventListFragmentDirections.actionLiveEventListFragmentToLiveEventDetailFragment(event.id)
//                        Log.d("IDEvent", event.id.toString())
//                        findNavController().navigate(action)
//                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClicked(item: AllCoursesResponse.CourseItemResponse) {
        //navigation
        Toast.makeText(context, "id: ${item.id}, course: ${item.title}", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(bool: Boolean) {
        binding.apply {
            if (bool) {
                rvCourse.visibility = View.GONE
                shimmerLayout.visibility = View.VISIBLE
            } else {
                rvCourse.visibility = View.VISIBLE
                shimmerLayout.visibility = View.GONE
            }
        }
    }

    private fun showEmptyWarning(bool: Boolean) {
        binding.apply {
            if (bool) {
                ivNoCourse.visibility = View.VISIBLE
                tvNoCourse.visibility = View.VISIBLE
            } else {
                ivNoCourse.visibility = View.GONE
                tvNoCourse.visibility = View.GONE
            }
        }
    }
}