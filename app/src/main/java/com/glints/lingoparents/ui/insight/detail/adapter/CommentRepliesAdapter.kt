package com.glints.lingoparents.ui.insight.detail.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.glints.lingoparents.R
import com.glints.lingoparents.data.model.response.GetCommentRepliesResponse
import com.glints.lingoparents.databinding.ItemInsightCommentBinding
import com.glints.lingoparents.ui.insight.detail.DetailInsightFragment

open class CommentRepliesAdapter(
    private val listener: OnItemClickCallback,
    private val context: Context,
) :
    RecyclerView.Adapter<CommentRepliesAdapter.ChildAdapterHolder>() {
    private val dataList = ArrayList<GetCommentRepliesResponse.Message>()

    inner class ChildAdapterHolder(private val binding: ItemInsightCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        private fun initBinding(
            item: GetCommentRepliesResponse.Message,
            binding: ItemInsightCommentBinding,
        ) {
            binding.apply {
                val firstname: String = item.Master_user.Master_parent.firstname
                val lastname: String = item.Master_user.Master_parent.lastname

                ivComment.load(item.Master_user.Master_parent.photo)
                "$firstname $lastname".also { tvUsernameComment.text = it }
                tvCommentBody.text = item.comment
                tvLikeComment.text = item.total_like.toString()
                tvDislikeComment.text = item.total_dislike.toString()
                tvLikeComment.setOnClickListener {
                    listener.onLikeCommentClicked(item)
                }

                tvDislikeComment.setOnClickListener {
                    listener.onDislikeCommentClicked(item)
                }

                tvReportComment.setOnClickListener {
                    showReportDialog(context, item, item.id)
                }

                tvReplyComment.setOnClickListener {
                    tfReplyComment.visibility = View.VISIBLE
                    btnReplyComment.visibility = View.VISIBLE
                    btnReplyComment.setOnClickListener {
                        listener.onReplyCommentClicked(
                            item,
                            tfReplyComment.editText?.text.toString()
                        )
                        tfReplyComment.editText?.setText("")
                    }
                }

                tvDeleteComment.setOnClickListener {
                    listener.onDeleteCommentClicked(item, item.id)
                }

                tvUpdateComment.setOnClickListener {
                    tfReplyComment.visibility = View.VISIBLE
                    tfReplyComment.requestFocus()
                    btnReplyComment.visibility = View.VISIBLE
                    "Update".also { btnReplyComment.text = it }

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
                        }
                    }
                }

                if (item.replies > 0) createNestedComment(binding, item)
            }
        }

        fun bind(item: GetCommentRepliesResponse.Message) = initBinding(item, binding)

        @SuppressLint("SetTextI18n")
        private fun createNestedComment(
            binding: ItemInsightCommentBinding,
            item: GetCommentRepliesResponse.Message,
        ) {
            binding.apply {
                if (item.replies > 0) tvShowReplyComment.visibility = View.VISIBLE
                tvShowReplyComment.text = "Show ${item.replies} Replies"
                tvShowReplyComment.setOnClickListener {
                    if (llReplies.visibility == View.GONE) {
                        llReplies.isVisible = true
                        tvShowReplyComment.text = "Hide Replies"
                        val newBinding = ItemInsightCommentBinding.inflate(
                            LayoutInflater.from(binding.root.context)
                        )
                        initBinding(item, newBinding)
                        llReplies.addView(newBinding.root)
                        listener.onShowCommentRepliesClicked(item)
                    } else {
                        tvShowReplyComment.text = "Show ${item.replies} Replies"
                        llReplies.removeAllViews()
                        llReplies.isVisible = false
                    }
                }
            }
        }

        private fun showReportDialog(
            context: Context,
            item: GetCommentRepliesResponse.Message,
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
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentRepliesAdapter.ChildAdapterHolder {
        return ChildAdapterHolder(
            ItemInsightCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentRepliesAdapter.ChildAdapterHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    interface OnItemClickCallback {
        fun onReportCommentClicked(
            item: GetCommentRepliesResponse.Message,
            id: Int,
            report_comment: String,
        )

        fun onLikeCommentClicked(item: GetCommentRepliesResponse.Message)
        fun onDislikeCommentClicked(item: GetCommentRepliesResponse.Message)
        fun onReplyCommentClicked(item: GetCommentRepliesResponse.Message, comment: String)
        fun onShowCommentRepliesClicked(item: GetCommentRepliesResponse.Message)
        fun onDeleteCommentClicked(item: GetCommentRepliesResponse.Message, id: Int)
        fun onUpdateCommentClicked(item: GetCommentRepliesResponse.Message, comment: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<GetCommentRepliesResponse.Message>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }
}