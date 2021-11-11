package com.glints.lingoparents.ui.insight.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.glints.lingoparents.data.model.InsightSliderItem
import com.glints.lingoparents.databinding.FragmentLifestyleInsightBinding

class LifestyleInsightFragment : Fragment(), CategoriesAdapter.OnItemClickCallback {

    private lateinit var binding: FragmentLifestyleInsightBinding
    private val viewModel: CategoriesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLifestyleInsightBinding.inflate(inflater, container, false)

        binding.rvLifestyleInsight.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)

            adapter = CategoriesAdapter(
                this@LifestyleInsightFragment,
                mutableListOf(
                    InsightSliderItem("", ""),
                    InsightSliderItem("", ""),
                    InsightSliderItem("", ""),
                    InsightSliderItem("", ""),
                    InsightSliderItem("", ""),
                    InsightSliderItem("", "")
                )
            )
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(LifestyleInsightViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onItemClicked(insightSliderItem: InsightSliderItem) {
        TODO("Not yet implemented")
    }

}