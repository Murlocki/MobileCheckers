import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.ui.layout.LayoutCoordinates
import com.example.mobilecheckers.R
import com.example.mobilecheckers.controllers.CheckerViewController
import com.example.mobilecheckers.models.Checker
import com.example.mobilecheckers.ui.theme.BlackCellText
import com.example.mobilecheckers.ui.theme.WhiteCellText

@RequiresApi(Build.VERSION_CODES.O)
class CheckerView(context: Context, val checker: Checker) : FrameLayout(context) {
    private val checkerViewController: CheckerViewController = CheckerViewController(this)
    private var button:Button = Button(context).apply {
        val buttonLayoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParams = buttonLayoutParams

        background = ShapeDrawable(OvalShape()).apply {
            paint.color = if (checker.isWhite) Color.WHITE else Color.BLACK
        }
        if(checker.isQueen){
            text = if (checker.isWhite) WhiteCellText else BlackCellText
            setTextColor(if (checker.isWhite) Color.BLACK else Color.WHITE)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setAutoSizeTextTypeWithDefaults(Button.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        }
    }
    init {
        this.button.setOnClickListener {
            println("${checker.row} ${checker.col} ${checker.isWhite}")
        }
        addView(button)
    }
    fun setButtonListener(clickListener: OnClickListener){
        this.button.setOnClickListener(clickListener)
    }

    fun animateMoveSequence(coordinates: Pair<Float,Float>, onAnimationEnd: ()->Unit) {
        this.checkerViewController.checkerViewMoveAnimation(
            coordinates.first,
            coordinates.second,
            onAnimationEnd
        )
    }
    fun animateDeathSequence(onAnimationEnd: ()->Unit) {
        this.checkerViewController.checkerDeathAnimation(onAnimationEnd)
    }

}
