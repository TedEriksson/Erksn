package io.erksn.portfolio.ui.portfolio

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.android.material.snackbar.Snackbar
import io.erksn.portfolio.R
import io.erksn.portfolio.data.api.model.Project
import io.erksn.portfolio.ui.base.BaseFragment
import io.erksn.portfolio.ui.portfolio.model.loading
import io.erksn.portfolio.ui.portfolio.model.logo
import io.erksn.portfolio.ui.portfolio.model.project


class PortfolioFragment : BaseFragment() {

    private val viewModel: PortfolioViewModel by injectedViewModels()

    private lateinit var recyclerView: EpoxyRecyclerView

    var projects = emptyList<Project>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView = requireView().findViewById(R.id.recycler)
        recyclerView.setController(ProjectController())

        viewModel.data.observe(viewLifecycleOwner, Observer {
            projects = when (it) {
                is PortfolioState.Data.Loading -> emptyList()
                is PortfolioState.Data.Done -> it.projects
            }
            recyclerView.requestModelBuild()
        })

        viewModel.errors.observe(viewLifecycleOwner, Observer {
            if (!it.isHandled) {
                it.handle()
                when (it) {
                    is PortfolioState.Error.Retry -> showErrorSnackbar(R.string.error_message_retry, canRetry = true)
                    is PortfolioState.Error.Bail -> showErrorSnackbar(R.string.error_message_bail, canRetry = false)
                }
            }
        })
    }

    private fun showErrorSnackbar(@StringRes message: Int, canRetry: Boolean) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_INDEFINITE).apply {
            if (canRetry) {
                setAction(getString(R.string.retry)) {
                    viewModel.retryProjects()
                }
            }
        }.show()
    }

    inner class ProjectController : EpoxyController() {
        override fun buildModels() {
            logo {
                id("erksn_logo")
            }

            if (projects.isEmpty()) {
                loading {
                    id("loading")
                }
            } else {
                projects.onEach { project ->
                    addProject(project)
                }
            }
        }

        private fun addProject(project: Project) = project {
            id(project.id)
            title(project.title)
            imageUrl(project.imageUrl)

            textColor(Color.parseColor(project.textColor))
            backgroundColor(Color.parseColor(project.backgroundColor))

            listener {
                findNavController().navigate(
                    PortfolioFragmentDirections.actionPortfolioFragmentToProjectDetailFragment(
                        project.id
                    )
                )
            }
        }
    }
}