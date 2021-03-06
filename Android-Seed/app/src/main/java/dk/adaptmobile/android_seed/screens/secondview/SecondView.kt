package dk.adaptmobile.android_seed.screens.secondview

import android.app.Activity
import android.view.View
import com.jakewharton.rxbinding4.view.clicks
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.navigation.BaseView
import dk.adaptmobile.android_seed.screens.secondview.SecondViewModel.Input
import dk.adaptmobile.android_seed.screens.secondview.SecondViewModel.Output
import kotlinx.android.synthetic.main.view_second.*


class SecondView : BaseView<SecondViewModel, Output>() {
    override fun setViewModel() = SecondViewModel()

    override fun inflateView() = R.layout.view_second

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun onViewBound(view: View, activity: Activity) {
        viewModel.input.onNext(
                Input.Events(getNewsClicked = second_getNews.clicks())
        )
        setupAdapter()
    }

    private fun setupAdapter() {
        adapter = GroupAdapter()
        second_newsList.adapter = this.adapter

    }

    override fun handleOutput(output: Output) {
        when (output) {
            is Output.ShowNewsFeed -> {
                second_getNews.visibility = View.GONE
                second_newsList.visibility = View.VISIBLE
                adapter.updateAsync(output.data.map { NewsItem(it) })
            }
            Output.NoNews -> {
                second_nonews.visibility = View.VISIBLE
            }
        }
    }
}