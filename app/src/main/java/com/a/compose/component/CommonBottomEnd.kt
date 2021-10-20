package com.a.compose.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.a.compose.databinding.CommonBottomEndBinding

open class SampleAndroidView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

    var binding: CommonBottomEndBinding
    private var onClick: () -> Unit = {}

    init {
        CommonBottomEndBinding.inflate(LayoutInflater.from(context), this, true).apply {
            binding = this
        }

    }

    fun setTitle(title: String?) {
        binding.tvLoadMore.text = title
    }

    override fun onClick(v: View?) {
        onClick.invoke()
        isFocusableInTouchMode = true
        requestFocus()
    }

}
        