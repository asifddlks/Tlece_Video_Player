package com.asifddlks.tlecevideoplayer.utils

import android.os.Build
import android.text.Html
import android.widget.TextView

fun TextView.htmlText(text: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY))
    } else {
        setText(Html.fromHtml(text))
    }
}