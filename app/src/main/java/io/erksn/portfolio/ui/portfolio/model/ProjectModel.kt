package io.erksn.portfolio.ui.portfolio.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import io.erksn.portfolio.R
import io.erksn.portfolio.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_project)
abstract class ProjectModel : EpoxyModelWithHolder<ProjectHolder>() {

    @EpoxyAttribute lateinit var listener: (holder: ProjectHolder) -> Unit
    @EpoxyAttribute lateinit var title: String
    @EpoxyAttribute lateinit var imageUrl: String
    @EpoxyAttribute var textColor: Int = 0
    @EpoxyAttribute var backgroundColor: Int = 0

    override fun bind(holder: ProjectHolder) {
        holder.title.text = title
        holder.title.setTextColor(textColor)
        holder.card.setCardBackgroundColor(backgroundColor)

        ViewCompat.setTransitionName(holder.card, null)
        ViewCompat.setTransitionName(holder.overlay, null)

        holder.card.setOnClickListener {
            ViewCompat.setTransitionName(holder.card, "project_card")
            ViewCompat.setTransitionName(holder.overlay, "project_overlay")
            listener(holder)
        }

        Glide.with(holder.image).load(imageUrl).into(holder.image)
    }
}

class ProjectHolder : KotlinEpoxyHolder() {
    val title by bind<TextView>(R.id.textView)
    val image by bind<ImageView>(R.id.image)
    val card by bind<CardView>(R.id.card)
    val overlay by bind<View>(R.id.overlay)
}