package com.doublex.selfmanagementhelper

import android.app.AlertDialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.UiThread

@UiThread
internal fun showActivityNameDialog(
    context: Context,
    titleR: Int,
    hintR: Int,
    onConfirm: (text: String) -> Unit
) {
    val inputMethodManager = shownInputMethodManager(context)
    activityNameDialogBuilder(context, titleR, hintR, onConfirm, inputMethodManager).show()
}
@UiThread
internal fun showEditTextDialog(
    context: Context,
    titleR: Int,
    hintR: Int,
    onConfirm: (text: String) -> Unit
) {
    val inputMethodManager = shownInputMethodManager(context)
    editTextDialogBuilder(context, titleR, hintR, onConfirm, inputMethodManager).show()
}

@UiThread
internal fun showConfirmDialog(context: Context, msgR: Int, onConfirm: () -> Unit) {
    showConfirmDialog(context, context.resources.getString(msgR), onConfirm)
}
@UiThread
internal fun showConfirmDialog(context: Context, msg: String, onConfirm: () -> Unit) {
    confirmDialogBuilder(context, msg, onConfirm).show()
}

private fun activityNameDialogBuilder(
    context: Context,
    titleR: Int,
    hintR: Int,
    onConfirm: (text: String) -> Unit,
    inputMethodManager: InputMethodManager
): AlertDialog.Builder {
    val editText = editText(context, hintR)
    val builder = baseDialogBuilder(context, {
        val text = editText.text.toString()
        if (text.isEmpty()) onEmptyEditText(context) else onConfirm(text)
    }) { hideSoftInput(inputMethodManager) }
    builder.setTitle(titleR)
    builder.setView(editText)
    return builder
}
private fun editTextDialogBuilder(
    context: Context,
    titleR: Int,
    hintR: Int,
    onConfirm: (text: String) -> Unit,
    inputMethodManager: InputMethodManager
): AlertDialog.Builder {
    val editText = editText(context, hintR)
    val builder = baseDialogBuilder(context, { onConfirm(editText.text.toString()) }) {
        hideSoftInput(inputMethodManager)
    }
    builder.setTitle(titleR)
    builder.setView(editText)
    return builder
}
private fun editText(context: Context, hintR: Int): EditText {
    val editText = EditText(context)
    editText.setHint(hintR)
    return editText
}
private fun confirmDialogBuilder(
    context: Context,
    msg: String,
    onConfirm: () -> Unit
): AlertDialog.Builder {
    val builder = baseDialogBuilder(context, onConfirm) {}
    builder.setMessage(msg)
    return builder
}
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

@UiThread
private fun shownInputMethodManager(context: Context): InputMethodManager {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    return imm
}
@UiThread
private fun hideSoftInput(inputMethodManager: InputMethodManager) {
    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}
// It's just accidentally the same as onEmptyNewActivityName in MainActivity
@UiThread
private fun onEmptyEditText(context: Context) {
    val res = context.resources
    val format = res.getString(R.string.on_empty_edit_text)
    val msg = String.format(format, res.getString(R.string.activity_name))
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}