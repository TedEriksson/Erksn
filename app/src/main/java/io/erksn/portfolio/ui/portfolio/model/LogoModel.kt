package io.erksn.portfolio.ui.portfolio.model

import android.widget.TextView
import androidx.core.text.backgroundColor
import androidx.core.text.buildSpannedString
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import io.erksn.portfolio.R
import io.erksn.portfolio.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_logo)
abstract class LogoModel : EpoxyModelWithHolder<LogoHolder>() {

    override fun bind(holder: LogoHolder) {
        holder.logo.setTextColor(holder.logo.context.getColor(android.R.color.white))
        holder.logo.text = buildSpannedString {
            backgroundColor(holder.logo.context.getColor(R.color.colorPrimary)) {
                append(" Ted \n Eriksson ")
            }
        }
    }
}

class LogoHolder : KotlinEpoxyHolder() {
    val logo by bind<TextView>(R.id.logo)
}