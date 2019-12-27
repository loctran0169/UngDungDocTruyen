package com.example.truyenqq.ui.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.truyenqq.R
import com.example.truyenqq.databinding.FragmentHomeBinding
import com.example.truyenqq.module.adapters.AdapterSchedule
import com.example.truyenqq.module.adapters.AdapterSliderView
import com.example.truyenqq.module.adapters.AdapterVerticalRestFull
import com.example.truyenqq.module.adapters.SpaceItem
import com.example.truyenqq.ui.activities.main.ViewModelHome
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class FragmentHome : Fragment() {
    val cal = Calendar.getInstance()
    private val adapterNewRestFull: AdapterVerticalRestFull by lazy {
        AdapterVerticalRestFull(activity!!, mutableListOf())
    }
    private val adapterStoryMale: AdapterVerticalRestFull by lazy {
        AdapterVerticalRestFull(activity!!, mutableListOf())
    }
    private val adapterStoryFemale: AdapterVerticalRestFull by lazy {
        AdapterVerticalRestFull(activity!!, mutableListOf())
    }
    private val adapterSchedule: AdapterSchedule by lazy {
        AdapterSchedule(activity!!, mutableListOf())
    }
    val viewModel: ViewModelHome by lazy {
        ViewModelProviders
            .of(activity!!)
            .get(ViewModelHome::class.java)
    }
    var _new = false
    var _male = false
    var _female = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@FragmentHome
        return binding.root
    }

    @SuppressLint("CheckResult", "WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val snapHelperNew: SnapHelper = LinearSnapHelper()
        val snapHelperMale: SnapHelper = LinearSnapHelper()
        val snapHelperFemale: SnapHelper = LinearSnapHelper()

        snapHelperNew.attachToRecyclerView(rcvNewUpdate)
        snapHelperMale.attachToRecyclerView(rcvStoryMale)
        snapHelperFemale.attachToRecyclerView(rcvStoryFemale)
        rcvNewUpdate.run {
            layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false)
            adapter = adapterNewRestFull
            addItemDecoration(SpaceItem(4))
        }
        rcvStoryMale.run {
            layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false)
            adapter = adapterStoryMale
            addItemDecoration(SpaceItem(4))

        }
        rcvStoryFemale.run {
            layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false)
            adapter = adapterStoryFemale
            addItemDecoration(SpaceItem(4))
        }
        //New
        viewModel.storyNew.observe(this@FragmentHome, Observer {
            adapterNewRestFull.updateData(it)
            progressNewStory.visibility = View.INVISIBLE
            _new = true
            if (_male && _female)
                refresh.isRefreshing = false
        })
        //Male
        viewModel.storyStoryMale.observe(this@FragmentHome, Observer {
            adapterStoryMale.updateData(it)
            progressStoryMale.visibility = View.INVISIBLE
            _male = true
            if (_new && _female)
                refresh.isRefreshing = false
        })
        //Female
        viewModel.storyStoryFemale.observe(this@FragmentHome, Observer {
            adapterStoryFemale.updateData(it)
            progressStoryFeMale.visibility = View.INVISIBLE
            _female = true
            if (_male && _new)
                refresh.isRefreshing = false
        })
        //slider
        viewModel.slider.observe(this@FragmentHome, Observer {
            viewPagerSlider.sliderAdapter = AdapterSliderView(activity!!, it)
            viewPagerSlider.scrollTimeInSec = 4
            viewPagerSlider.startAutoCycle()
        })
        //Schedule
        imgCalendar.setOnClickListener {
            viewModel.sLoadingSchedulers.observe(this@FragmentHome, Observer {
                val dialog = AlertDialog.Builder(activity!!)
                val view: View =
                    LayoutInflater.from(activity!!).inflate(R.layout.fragment_schedule, null)
                val schedule = view.findViewById<RecyclerView>(R.id.rcvSchedule)
                val date = view.findViewById<TextView>(R.id.tvScheduleDate)
                date.text =
                    "Ngày cập nhật : ${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(
                        Calendar.YEAR
                    )}"
                schedule.run {
                    adapter = adapterSchedule
                    layoutManager = LinearLayoutManager(activity)
                }
                adapterSchedule.updateData(it.list!!)
                dialog.setView(view)
                    .setIcon(R.drawable.ic_calendar)
                val dislay = dialog.create()
                dislay.setTitle("Lịch ra truyện")
                dislay.show()
            })
        }
        lbCalendar.setOnClickListener {
            viewModel.sLoadingSchedulers.observe(this@FragmentHome, Observer {
                val dialog = AlertDialog.Builder(activity!!)
                val view: View =
                    LayoutInflater.from(activity!!).inflate(R.layout.fragment_schedule, null)
                val schedule = view.findViewById<RecyclerView>(R.id.rcvSchedule)
                val date = view.findViewById<TextView>(R.id.tvScheduleDate)
                date.text =
                    "Ngày cập nhật : ${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(
                        Calendar.YEAR
                    )}"
                schedule.run {
                    adapter = adapterSchedule
                    layoutManager = LinearLayoutManager(activity)
                }
                adapterSchedule.updateData(it.list!!)
                dialog.setView(view)
                    .setIcon(R.drawable.ic_calendar)
                val dislay = dialog.create()
                dislay.setTitle("Lịch ra truyện")
                dislay.show()
            })
        }
        refresh.setOnRefreshListener {
            progressNewStory.visibility = View.VISIBLE
            progressStoryMale.visibility = View.VISIBLE
            progressStoryFeMale.visibility = View.VISIBLE
            viewModel.refresh()
            val handle = Handler()
            handle.postDelayed(
                {
                    refresh.isRefreshing = false
                }, 200
            )
        }

    }
}