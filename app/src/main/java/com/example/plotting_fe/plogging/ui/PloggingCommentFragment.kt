package com.example.plotting_fe.plogging.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.plogging.dto.Comment
import com.example.plotting_fe.plogging.dto.Reply

class PloggingCommentFragment : Fragment() {

    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private val comments = mutableListOf<Comment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plogging_comment, container, false)

        commentsRecyclerView = view.findViewById(R.id.rv_comment)
        commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 예시 댓글 데이터 추가
        val replyList = listOf(
            Reply("1", "YU", "2024.10.08 12:30 PM", "답변 내용"),
            Reply("2", "PARK", "2024.10.08 12:31 PM", "또 다른 답변 내용")
        )

        comments.add(Comment("1", "LEE", "2024.10.08 12:00 PM", "같이 플로깅 할래?", replyList))
        comments.add(Comment("2", "OH", "2024.10.08 12:00 PM", "또 다른 댓글", emptyList()))

        commentAdapter = CommentAdapter(comments)
        commentsRecyclerView.adapter = commentAdapter

        return view
    }
}