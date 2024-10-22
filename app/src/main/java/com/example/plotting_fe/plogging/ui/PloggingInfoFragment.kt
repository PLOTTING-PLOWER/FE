package com.example.plotting_fe.plogging.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.plogging.dto.Participant


class PloggingInfoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var participantsRecyclerView: RecyclerView
    private lateinit var adapter: PloggingUserAdapter
    private lateinit var participantList: ArrayList<Participant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plogging_info, container, false)

        // R.id.ll_people 클릭 리스너 설정
        val llPeople: LinearLayout = view.findViewById(R.id.ll_people)
        llPeople.setOnClickListener {
            showParticipantsDialog()
        }

        return view
    }

    private fun showParticipantsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_participants, null)

        participantsRecyclerView = dialogView.findViewById(R.id.participantsRecyclerView)
        participantList = ArrayList()

        // 예시 데이터 추가
        participantList.add(Participant("가가가", "신상 정보 뭐 넘겨야 하냐?"))
        participantList.add(Participant("거거거", "뭐가 필요하니?"))
        participantList.add(Participant("APPLE", "BCA 2468 3545 ****"))
        participantList.add(Participant("ANT", "BCA 2468 3545 ****"))
        participantList.add(Participant("ARIANA GRANDE", "BCA 2468 3545 ****"))
        participantList.add(Participant("A+", "BCA 2468 3545 ****"))
        participantList.add(Participant("B급인생", "BCA 2468 3545 ****"))

        adapter = PloggingUserAdapter(participantList)
        participantsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        participantsRecyclerView.adapter = adapter

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = dialogBuilder.create()

        dialog.show()
        dialog.window?.setLayout(
            800,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setGravity(Gravity.END)
    }
}
