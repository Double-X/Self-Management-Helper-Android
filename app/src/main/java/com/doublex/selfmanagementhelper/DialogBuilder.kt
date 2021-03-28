package com.doublex.selfmanagementhelper

import android.app.AlertDialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.UiThread

@UiThread
internal fun showConfirmDialog(context: Context, msg: String, onConfirm: () -> Unit) {
    val builder = baseDialogBuilder(context, onConfirm) {}
    builder.setMessage(msg)
    builder.show()
}

@UiThread
internal fun showEditTextDialog(
    context: Context,
    titleR: Int,
    hintR: Int,
    onConfirm: (text: String) -> Unit
) {
    val editText = EditText(context)
    editText.setHint(hintR)
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    val builder = baseDialogBuilder(context, {
        val text = editText.text.toString()
        if (text.isEmpty()) onEmptyEditText(context) else onConfirm(text)
    }) { imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0) }
    builder.setTitle(titleR)
    builder.setView(editText)
    builder.show()
}

@UiThread
private fun baseDialogBuilder(
    context: Context,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
): AlertDialog.Builder {
    val builder = AlertDialog.Builder(context)
    builder.setNegativeButton(R.string.cancel) { dialog, _ ->
        onDismiss()
        dialog.dismiss()
    }
    builder.setOnCancelListener { onDismiss() }
    builder.setPositiveButton(R.string.confirm) { dialog, _ ->
        onConfirm()
        dialog.dismiss()
    }
    return builder
}

// It's just accidentally the same as onEmptyNewActivityName in MainActivity
@UiThread
private fun onEmptyEditText(context: Context) {
    val res = context.resources
    val format = res.getString(R.string.on_empty_edit_text)
    val msg = String.format(format, res.getString(R.string.activity_name))
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}