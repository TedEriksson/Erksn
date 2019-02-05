package io.erksn.portfolio.ui.project

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import io.erksn.portfolio.R
import io.erksn.portfolio.ui.base.BaseFragment
import java.lang.IllegalArgumentException

class ProjectDetailFragment : BaseFragment() {

    private val projectId by lazy {
        arguments?.let { ProjectDetailFragmentArgs.fromBundle(it).projectId }
            ?: throw IllegalArgumentException("Must pass a projectId")
    }

    private val viewModel: ProjectDetailViewModel by injectedViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.translationZ = 100f

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        view.findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfiguration)

        val title = view.findViewById<TextView>(R.id.title)
        val appBar = view.findViewById<AppBarLayout>(R.id.app_bar)
        val imageView = view.findViewById<ImageView>(R.id.image)

        viewModel.project.observe(this, Observer {
            title.text = it.title
            title.setTextColor(Color.parseColor(it.textColor))

            appBar.setBackgroundColor(Color.parseColor(it.backgroundColor))

            Glide.with(imageView).load(it.imageUrl).into(imageView)
        })

        viewModel.setProjectId(projectId)
    }
}