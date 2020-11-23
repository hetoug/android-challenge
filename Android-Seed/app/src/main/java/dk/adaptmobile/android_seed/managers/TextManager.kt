package dk.adaptmobile.android_seed.managers

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.Annotation

import androidx.annotation.StringRes

/**
 * Textmanager is for handling the String resources from ViewData classes
 */

class TextManager(val context: Context) {

    fun getString(@StringRes resourceId: Int?, vararg formatArgs: Any): String {
        resourceId ?: return ""
        return context.resources.getString(resourceId, *formatArgs)
    }

    /**
     * Formats a [resourceId] and a dynamic [formatArgs] with Spannable
     * @return a formatted text
     * Can be used in String.xml forexample <string name="mypass_expires_at">Expires <annotation arg="0" typeface="bold" color="#FF0000">placeholder</annotation></string>
     */
    fun getFormattedString(@StringRes resourceId: Int, vararg formatArgs: Any): SpannableString {
        val text = context.resources.getText(resourceId)
        val spannableText = SpannableStringBuilder(text)
        val annotations = spannableText.getSpans(0, text.length, Annotation::class.java)

        annotations.forEach { annotation ->
            when (annotation.key) {
                ARGUMENT_ANNOTATION_KEY -> {
                    val argumentIndex = Integer.parseInt(annotation.value)
                    when (val argument = formatArgs[argumentIndex]) {
                        is String -> argument(annotation, spannableText, argument)
                    }
                }
                TEXT_TYPEFACE_ANNOTATION_KEY -> {
                    when (annotation.value) {
                        ANNOTATION_VALUE_BOLD -> bold(spannableText, annotation)
                        ANNOTATION_VALUE_ITALIC -> italic(spannableText, annotation)
                        ANNOTATION_VALUE_BOLD_ITALIC -> boldItalic(spannableText, annotation)
                    }
                }
                COLOR_ANNOTATION_KEY -> color(spannableText, annotation)
            }
        }
        return SpannableString.valueOf(spannableText)
    }

    private fun argument(annotation: Annotation, spannableText: SpannableStringBuilder, argument: CharSequence) {
        spannableText.replace(
            spannableText.getSpanStart(annotation),
            spannableText.getSpanEnd(annotation),
            argument)
    }

    private fun boldItalic(spannableText: SpannableStringBuilder, annotation: Annotation) {
        spannableText.setSpan(
            StyleSpan(Typeface.BOLD_ITALIC),
            spannableText.getSpanStart(annotation),
            spannableText.getSpanEnd(annotation),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun italic(spannableText: SpannableStringBuilder, annotation: Annotation) {
        spannableText.setSpan(
            StyleSpan(Typeface.ITALIC),
            spannableText.getSpanStart(annotation),
            spannableText.getSpanEnd(annotation),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun bold(spannableText: SpannableStringBuilder, annotation: Annotation) {
        spannableText.setSpan(
            StyleSpan(Typeface.BOLD),
            spannableText.getSpanStart(annotation),
            spannableText.getSpanEnd(annotation),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun color(spannableText: SpannableStringBuilder, annotation: Annotation) {
        spannableText.setSpan(
            ForegroundColorSpan(Color.parseColor(annotation.value)),
            spannableText.getSpanStart(annotation),
            spannableText.getSpanEnd(annotation),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    companion object {
        const val ARGUMENT_ANNOTATION_KEY = "arg"
        const val TEXT_TYPEFACE_ANNOTATION_KEY = "typeface"
        const val COLOR_ANNOTATION_KEY = "color"

        const val ANNOTATION_VALUE_BOLD = "bold"
        const val ANNOTATION_VALUE_ITALIC = "italic"
        const val ANNOTATION_VALUE_BOLD_ITALIC = "boldItalic"
    }
}