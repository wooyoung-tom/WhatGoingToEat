package tom.dev.whatgoingtoeat.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import tom.dev.whatgoingtoeat.R

class LoadingDialog(context: Context): Dialog(context) {
    init {
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setContentView(R.layout.dialog_loading)
    }
}