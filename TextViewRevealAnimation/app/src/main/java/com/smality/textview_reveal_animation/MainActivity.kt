package com.smality.textview_reveal_animation

import android.animation.ValueAnimator
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private lateinit var tvSubtitle: TextView
    private lateinit var clContainer: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ivExpand: View = findViewById(R.id.iv_expand)
        tvSubtitle = findViewById(R.id.tv_subtitle)
        clContainer = findViewById(R.id.cl_container)
        //dropdown icon tıkladığında...
        ivExpand.setOnClickListener {
            if (!tvSubtitle.isVisible) {
                showSubtitle()
                //dropdown icon 180 derece döndürüyoruz
                ivExpand.rotation = 180f
            } else {
                hideSubtitle()
                //dropdown iconu ilk haline döndürüyoruz
                ivExpand.rotation = 0f
            }
        }
    }

    private fun hideSubtitle() {
        //yazı alanının kullandığı yükselik değerini alalım
        val subtitleHeight = tvSubtitle.height
        //makale de bu bölümün açıklamasını bulabilirsiniz
        val heightAnimator = ValueAnimator.ofInt(subtitleHeight, 0)

        heightAnimator.addUpdateListener {
            tvSubtitle.updateHeight(it.animatedValue as Int)
        }
        heightAnimator.doOnEnd {
            tvSubtitle.isVisible = false
        }
        heightAnimator.start()
    }

    private fun showSubtitle() {
        tvSubtitle.updateHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT)
        //View measure metodu sayesinde tvSubtitle adlı Textview'in yükseklik değeri buluyoruz
        val totalMarginForSubtitle = 2 * 16.toPx()
        tvSubtitle.measure(
            View.MeasureSpec.makeMeasureSpec(
                clContainer.width - totalMarginForSubtitle,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.UNSPECIFIED
        )

        val subtitleHeight = tvSubtitle.measuredHeight

        tvSubtitle.height = 0
        tvSubtitle.isVisible = true
        //Textviewdeki içerik animasyonlu görünür hale getiriyoruz
        val heightAnimator = ValueAnimator.ofInt(0, subtitleHeight)
        heightAnimator.addUpdateListener {
            tvSubtitle.height = it.animatedValue as Int
        }
        heightAnimator.start()
    }


    fun View.updateHeight(newHeight: Int) {
        layoutParams = layoutParams.apply {
            height = newHeight
        }
    }
    fun Int.toPx() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
}