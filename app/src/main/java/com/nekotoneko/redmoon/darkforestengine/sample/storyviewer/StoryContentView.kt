package com.nekotoneko.redmoon.darkforestengine.sample.storyviewer

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.nekotoneko.redmoon.darkforestengine.sample.models.SceneContent
import kotlinx.android.synthetic.main.view_story.view.*

/**
 * @author Silwèk on 2019-08-17
 */
class StoryContentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    fun display(content: SceneContent?) {
        text.text = content?.textKey ?: ""
    }
}