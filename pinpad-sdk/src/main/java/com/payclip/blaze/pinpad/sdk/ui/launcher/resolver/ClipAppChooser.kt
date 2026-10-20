package com.payclip.blaze.pinpad.sdk.ui.launcher.resolver

import android.app.Activity
import android.app.Dialog
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.doOnLayout
import com.payclip.blaze.pinpad.sdk.R

/**
 * Hoja anclada abajo hecha con vistas nativas: parece un bottom sheet pero no depende del tema ni de librerías del host.
 * No es arrastrable; se cierra tocando fuera, con atrás o con "Cancelar".
 */
internal object ClipAppChooser {

    fun show(
        activity: Activity,
        candidates: List<ClipAppCandidate>,
        onChosen: (ClipAppCandidate, remember: Boolean) -> Unit,
        onCancelled: () -> Unit
    ) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val palette = Palette.of(activity.resources.configuration)
        val sheet = buildSheet(activity, palette, candidates, dialog, onChosen)
        dialog.setContentView(sheet)
        dialog.setCanceledOnTouchOutside(true)
        // Sin esto el integrador se queda esperando un resultado que no llega (tocar fuera, atrás, "Cancelar").
        dialog.setOnCancelListener { onCancelled() }

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(MATCH_PARENT, WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }

        dialog.show()
        // Entrada desde abajo: un Dialog no tiene animación propia sin un style de ventana.
        sheet.doOnLayout { sheet.translationY = sheet.height.toFloat(); sheet.animate().translationY(0f).setDuration(ENTER_MS).start() }
    }

    private fun buildSheet(
        activity: Activity,
        palette: Palette,
        candidates: List<ClipAppCandidate>,
        dialog: Dialog,
        onChosen: (ClipAppCandidate, remember: Boolean) -> Unit
    ): View {
        fun dp(value: Int) = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), activity.resources.displayMetrics
        ).toInt()

        val sheet = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(24), dp(12), dp(24), dp(16))
            background = GradientDrawable().apply {
                setColor(palette.surface)
                val r = dp(28).toFloat()
                cornerRadii = floatArrayOf(r, r, r, r, 0f, 0f, 0f, 0f)
            }
        }

        sheet.addView(View(activity).apply {
            background = GradientDrawable().apply { setColor(palette.handle); cornerRadius = dp(2).toFloat() }
        }, LinearLayout.LayoutParams(dp(32), dp(4)).apply { gravity = Gravity.CENTER_HORIZONTAL })

        sheet.addView(TextView(activity).apply {
            text = activity.getString(R.string.pinpad_sdk_app_chooser_title)
            setTextColor(palette.text)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            setPadding(0, dp(20), 0, dp(8))
        })

        val remember = CheckBox(activity).apply {
            text = activity.getString(R.string.pinpad_sdk_app_chooser_remember)
            setTextColor(palette.text)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            buttonTintList = ColorStateList.valueOf(palette.accent)
        }

        candidates.forEach { candidate ->
            sheet.addView(candidateRow(activity, palette, candidate, ::dp) {
                dialog.dismiss()
                onChosen(candidate, remember.isChecked)
            })
        }

        val footer = LinearLayout(activity).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(remember, LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f))
            addView(TextView(activity).apply {
                text = activity.getString(R.string.pinpad_sdk_app_chooser_cancel)
                setTextColor(palette.accent)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                setPadding(dp(16), dp(12), dp(16), dp(12))
                setOnClickListener { dialog.cancel() }
            })
        }
        sheet.addView(footer, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))

        return sheet
    }

    private fun candidateRow(
        activity: Activity,
        palette: Palette,
        candidate: ClipAppCandidate,
        dp: (Int) -> Int,
        onClick: () -> Unit
    ): View = LinearLayout(activity).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(0, dp(12), 0, dp(12))
        setOnClickListener { onClick() }

        val outValue = TypedValue()
        if (activity.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)) {
            setBackgroundResource(outValue.resourceId)
        }

        candidate.icon?.let { icon ->
            addView(ImageView(activity).apply { setImageDrawable(icon) }, LinearLayout.LayoutParams(dp(40), dp(40)).apply {
                marginEnd = dp(16)
            })
        }
        addView(TextView(activity).apply {
            text = candidate.label
            setTextColor(palette.text)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }, LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f))
    }

    private class Palette(val surface: Int, val text: Int, val handle: Int, val accent: Int) {
        companion object {
            fun of(configuration: Configuration): Palette {
                val dark = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
                return if (dark) {
                    Palette(surface = 0xFF2B2B2F.toInt(), text = 0xFFE6E6EA.toInt(), handle = 0xFF6B6B72.toInt(), accent = 0xFF9DB7FF.toInt())
                } else {
                    Palette(surface = 0xFFFFFFFF.toInt(), text = 0xFF1B1B1F.toInt(), handle = 0xFFC4C4CC.toInt(), accent = 0xFF2F55C9.toInt())
                }
            }
        }
    }

    private const val ENTER_MS = 220L
}
