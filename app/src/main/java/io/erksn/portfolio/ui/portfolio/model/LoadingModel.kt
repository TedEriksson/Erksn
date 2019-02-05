package io.erksn.portfolio.ui.portfolio.model

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import io.erksn.portfolio.R
import io.erksn.portfolio.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_loading)
abstract class LoadingModel : EpoxyModelWithHolder<LoadingHolder>() {

    override fun bind(holder: LoadingHolder) {
    }
}

class LoadingHolder : KotlinEpoxyHolder()