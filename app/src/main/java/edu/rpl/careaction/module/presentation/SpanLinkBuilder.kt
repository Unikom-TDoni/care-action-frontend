package edu.rpl.careaction.module.presentation

import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_INCLUSIVE
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View

class SpanLinkBuilder {
    private var colorId: Int = 0
    private var endLinkIndex: Int = 0
    private var startLinkIndex: Int = 0
    private var text: String = String()
    private var linkCallback: () -> Unit = {}

    fun setText(text: String) = apply { this.text = text }
    fun setColorId(colorId: Int) = apply { this.colorId = colorId }
    fun setEndLinkIndex(lastLinkIndex: Int) = apply { this.endLinkIndex = lastLinkIndex }
    fun setStartLinkIndex(startLinkIndex: Int) = apply { this.startLinkIndex = startLinkIndex }
    fun setLinkCallback(linkCallback: () -> Unit) = apply { this.linkCallback = linkCallback }
    fun build(): SpannableString {
        val spannable = SpannableString(text)

        spannable.setSpan(
            UnderlineSpan(),
            startLinkIndex,
            endLinkIndex,
            SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(colorId),
            startLinkIndex,
            endLinkIndex,
            SPAN_EXCLUSIVE_INCLUSIVE
        )

        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    linkCallback()
                }
            }, startLinkIndex, endLinkIndex, SPAN_EXCLUSIVE_INCLUSIVE
        )

        return spannable
    }
}