package io.erksn.portfolio.ui.project

import android.content.Intent
import android.graphics.Color
import android.net.Uri
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
import com.google.android.material.button.MaterialButton
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

        view.translationZ = 100f // For transition to be above list screen

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        view.findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfiguration)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val title = requireView().findViewById<TextView>(R.id.title)
        val appBar = requireView().findViewById<AppBarLayout>(R.id.app_bar)
        val imageView = requireView().findViewById<ImageView>(R.id.image)
        val tagline = requireView().findViewById<TextView>(R.id.tagline)
        val playStoreButton = requireView().findViewById<MaterialButton>(R.id.playStoreLink)

        viewModel.project.observe(viewLifecycleOwner, Observer { project ->
            title.text = project.title
            title.setTextColor(Color.parseColor(project.textColor))

            appBar.setBackgroundColor(Color.parseColor(project.backgroundColor))

            Glide.with(imageView).load(project.imageUrl).into(imageView)

            tagline.visibility = if (project.tagline != null) View.VISIBLE else View.GONE
            tagline.text = project.tagline

            playStoreButton.visibility = if (project.appUrl != null) View.VISIBLE else View.GONE
            playStoreButton.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(project.appUrl)
                })
            }
        })

        viewModel.setProjectId(projectId)
    }
}