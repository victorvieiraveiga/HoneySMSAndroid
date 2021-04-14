package victor.veiga.honeysmsapp.view

import android.app.Activity
import android.app.AlertDialog
import victor.veiga.honeysmsapp.R


class LoadingDialog {
    lateinit var activity: Activity
    lateinit var dialog: AlertDialog

    constructor(myActivity: Activity) {
        activity = myActivity
    }

    fun startLoadingDialog() {
        var builder = AlertDialog.Builder(activity)
        var inflater = activity.getLayoutInflater()
        builder.setView(inflater.inflate(R.layout.custom_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}