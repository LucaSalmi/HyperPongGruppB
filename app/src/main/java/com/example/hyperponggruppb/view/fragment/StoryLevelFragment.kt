package com.example.hyperponggruppb.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hyperponggruppb.view.StoryView

class StoryLevelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return activity?.applicationContext?.let { StoryView(super.getContext(), activity!!)

        }
    }

}