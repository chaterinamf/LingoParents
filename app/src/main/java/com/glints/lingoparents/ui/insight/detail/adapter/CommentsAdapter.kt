package com.glints.lingoparents.ui.insight.detail.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.glints.lingoparents.R
import com.glints.lingoparents.data.model.InsightCommentItem
import com.glints.lingoparents.databinding.ItemInsightCommentBinding
import com.glints.lingoparents.ui.dashboard.hideKeyboard
import com.glints.lingoparents.ui.dashboard.openKeyboard
import com.glints.lingoparents.ui.insight.detail.DetailInsightFragment

class CommentsAdapter(
    private val listener: OnItemClickCallback,
    private val context: Context,
    private val uniqueAdapterId: Double,
) :
    RecyclerView.Adapter<CommentsAdapter.AdapterHolder>() {
    private lateinit var rvChild: RecyclerView

    companion object {
        var parentId: Int = 0
    }

    private val diffUtilCallback = object :
        DiffUtil.ItemCallback<InsightCommentItem>() {
        override fun areItemsTheSame(
            oldItem: InsightCommentItem,
            newItem: InsightCommentItem,
        ): Boolean {
            return oldItem.idComment == newItem.idComment
        }

        override fun areContentsTheSame(
            oldItem: InsightCommentItem,
            newItem: InsightCommentItem,
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    inner class AdapterHolder(private val binding: ItemInsightCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: InsightCommentItem) {
            binding.apply {
                if (parentId == item.idUser)
                    hideTextView(true)
                else hideTextView(false)

                item.photo?.let {
                    ivComment.load(it)
                }

                tvUsernameComment.text = item.name
                tvCommentBody.text = item.comment
                tvCommentLike.text = item.totalLike.toString()
                tvCommentDislike.text = item.totalDislike.toString()

                ivCommentLike.setOnClickListener {
                    listener.onLikeCommentClicked(item)
                }

                ivCommentDislike.setOnClickListener {
                    listener.onDislikeCommentClicked(item)
                }

                tvReportComment.setOnClickListener {
                    showReportDialog(context, item, item.idComment)
                }

                tvReplyComment.setOnClickListener {
                    if (btnReplyComment.text == "Update") {
                        btnReplyComment.text = "Reply"
                        tfReplyComment.editText?.setText("")
                        setPostCommentListener(item)
                    } else {
                        tfReplyComment.isVisible = !tfReplyComment.isVisible
                        btnReplyComment.isVisible = !btnReplyComment.isVisible

                        if (tvShowReplyComment.text.toString().lowercase().contains("show") &&
                            tfReplyComment.isVisible
                        ) {
                            rvCommentReply.visibility = View.VISIBLE
                            tvShowReplyComment.text = "Hide Replies"
                            rvChild = rvCommentReply
                            listener.onShowCommentRepliesClicked(item, uniqueAdapterId)
                        }

                        if (tfReplyComment.isVisible) {
                            tfReplyComment.requestFocus()
                            (context as Activity).openKeyboard()
                            "Reply".also { btnReplyComment.text = it }

                            setPostCommentListener(item)
                        } else {
                            (context as Activity).hideKeyboard()
                            btnReplyComment.text = ""
                            tfReplyComment.editText?.setText("")
                        }
                    }
                }

                rvCommentReply.apply {
                    setHasFixedSize(false)

                    val linearLayoutManager = object : LinearLayoutManager(context) {
                        override fun canScrollVertically(): Boolean {
                            return false
                        }
                    }
                    layoutManager = linearLayoutManager

                }

                if (item.totalReply > 0) {
                    tvShowReplyComment.visibility = View.VISIBLE
                    tvShowReplyComment.text = "Show ${item.totalReply} Replies"
                    val newCommentsAdapter = createNewAdapter()
                    binding.rvCommentReply.adapter = newCommentsAdapter
                }
                tvShowReplyComment.setOnClickListener {
                    if (rvCommentReply.visibility == View.GONE) {
                        rvCommentReply.visibility = View.VISIBLE
                        tvShowReplyComment.text = "Hide Replies"
                        rvChild = rvCommentReply
                        listener.onShowCommentRepliesClicked(item, uniqueAdapterId)
                    } else if (rvCommentReply.visibility == View.VISIBLE) {
                        rvCommentReply.visibility = View.GONE
                        val childRvDiffer = (binding.rvCommentReply.adapter as CommentsAdapter).differ
                        tvShowReplyComment.text = "Show ${childRvDiffer.currentList.size} Replies"
                        println("Differ List Size: ${childRvDiffer.currentList.size}")
                    }
                }

                tvDeleteComment.setOnClickListener {
                    listener.onDeleteCommentClicked(item, item.idComment, uniqueAdapterId)
                }

                tvUpdateComment.setOnClickListener {
                    if (btnReplyComment.text == "Reply") {
                        btnReplyComment.text = "Update"
                        tfReplyComment.editText?.setText(item.comment)
                        tfReplyComment.editText?.selectAll()
                        setUpdateCommentListener(item)
                    } else {
                        tfReplyComment.isVisible = !tfReplyComment.isVisible
                        btnReplyComment.isVisible = !btnReplyComment.isVisible

                        if (tfReplyComment.isVisible) {
                            tfReplyComment.requestFocus()
                            (context as Activity).openKeyboard()
                            "Update".also { btnReplyComment.text = it }

                            setUpdateCommentListener(item)
                        } else {
                            (context as Activity).hideKeyboard()
                            btnReplyComment.text = ""
                            tfReplyComment.editText?.setText("")
                        }
                    }
                }
            }
        }

        private fun hideTextView(b: Boolean) {
            binding.apply {
                tvDeleteComment.isVisible = b
                tvUpdateComment.isVisible = b
            }
        }

        private fun showReportDialog(
            context: Context,
            item: InsightCommentItem,
            id: Int,
        ) {
            val builder = AlertDialog.Builder(context)
            var text = ""
            builder.apply {
                setCancelable(false)
                setTitle(R.string.report)
                setSingleChoiceItems(DetailInsightFragment.report_body, -1) { _, i ->
                    try {
                        text = DetailInsightFragment.report_body[i]
                    } catch (e: IllegalArgumentException) {
                        throw ClassCastException(e.toString())
                    }
                }
                setPositiveButton(R.string.report) { _, _ ->
                    listener.onReportCommentClicked(item, id, text)
                }
                setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
            }
            builder.create().show()
        }

        @SuppressLint("SetTextI18n")
        private fun setPostCommentListener(item: InsightCommentItem) {
            binding.apply {
                btnReplyComment.setOnClickListener {
                    if (TextUtils.isEmpty(tfReplyComment.editText?.text)) {
                        tfReplyComment.requestFocus()
                        tfReplyComment.error = "Please enter your comment"
                    } else {
                        if (rvCommentReply.adapter != null) {
                            listener.onReplyCommentClicked(
                                item,
                                tfReplyComment.editText?.text.toString(),
                                (rvCommentReply.adapter as CommentsAdapter).getUniqueAdapterId()
                            )
                        } else {
                            val newCommentsAdapter = createNewAdapter()
                            rvCommentReply.adapter = newCommentsAdapter
                            rvCommentReply.visibility = View.VISIBLE
                            listener.onReplyCommentClicked(
                                item,
                                tfReplyComment.editText?.text.toString(),
                                newCommentsAdapter.getUniqueAdapterId()
                            )
                        }

                        tfReplyComment.editText?.setText("")
                        tfReplyComment.isVisible = false
                        btnReplyComment.isVisible = false
                        (context as Activity).hideKeyboard()
                    }
                }
            }
        }

        private fun setUpdateCommentListener(item: InsightCommentItem) {
            binding.apply {
                btnReplyComment.setOnClickListener {
                    if (TextUtils.isEmpty(tfReplyComment.editText?.text)) {
                        tfReplyComment.requestFocus()
                        tfReplyComment.error = "Please enter your comment"
                    } else {
                        listener.onUpdateCommentClicked(
                            item,
                            tfReplyComment.editText?.text.toString()
                        )
                        tfReplyComment.editText?.setText("")
                        tfReplyComment.isVisible = false
                        btnReplyComment.isVisible = false
                        (context as Activity).hideKeyboard()
                    }
                }
            }
        }
    }

    fun showCommentReplies(_adapter: CommentsAdapter) {
        rvChild.apply {
            visibility = View.VISIBLE
            adapter = _adapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder {
        return AdapterHolder(ItemInsightCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    interface OnItemClickCallback {
        fun onReportCommentClicked(
            item: InsightCommentItem,
            id: Int,
            report_comment: String,
        )

        fun onLikeCommentClicked(item: InsightCommentItem)
        fun onDislikeCommentClicked(item: InsightCommentItem)
        fun onReplyCommentClicked(
            item: InsightCommentItem,
            comment: String,
            uniqueAdapterId: Double,
        )

        fun onShowCommentRepliesClicked(item: InsightCommentItem, uniqueAdapterId: Double)
        fun onDeleteCommentClicked(item: InsightCommentItem, id: Int, uniqueAdapterId: Double)
        fun onUpdateCommentClicked(item: InsightCommentItem, comment: String)
    }

    fun getUniqueAdapterId() = uniqueAdapterId

    fun submitList(list: List<InsightCommentItem>) {
        differ.submitList(list)
    }

    fun addNewCommentItem(item: InsightCommentItem) {
        val currentList = differ.currentList.map {
            it.copy()
        }
        differ.submitList(listOf(item) + currentList)
    }

    fun deleteCommentItem(item: InsightCommentItem) {
        val newList = differ.currentList.filter {
            it.idComment != item.idComment
        }
        differ.submitList(newList)
    }

    private fun createNewAdapter(): CommentsAdapter {
        val uniqueAdapterId = DetailInsightFragment.randomGenerator.nextDouble()
        val newCommentsAdapter = CommentsAdapter(listener, context, uniqueAdapterId)
        DetailInsightFragment.commentAdapterMap[uniqueAdapterId] = newCommentsAdapter

        return newCommentsAdapter
    }
}