package com.example.hyperponggruppb.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.adapter.UserSelectionAdapter
import com.example.hyperponggruppb.controller.PlayerManager

class UserSelectScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_user_select_screen, container, false)
        val usersList = view.findViewById<RecyclerView>(R.id.rw_user_list)
        usersList.layoutManager = LinearLayoutManager(super.getContext())
        val userSelectionAdapter = super.getContext()?.let { UserSelectionAdapter(it, PlayerManager.usersArray) }
        usersList.adapter = userSelectionAdapter
        return view
    }


}