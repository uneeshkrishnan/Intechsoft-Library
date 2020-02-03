package com.intechsoft.baselibrary.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.intechsoft.baselibrary.R
import com.intechsoft.baselibrary.baseclasses.BaseFragment
import com.intechsoft.baselibrary.baseclasses.BaseFragmentActivity
import com.intechsoft.baselibrary.utilities.AlertMessageModel
import com.intechsoft.baselibrary.utilities.Globals
import com.intechsoft.baselibrary.utilities.Utility
import java.util.*


open class AppContainerActivity : BaseFragmentActivity() {

//    var llt_Loading: LinearLayout? = null

    protected var FragmentVal = 0
    protected var fragmentLoaded = false

    fun hideKeyBoard(view: View) {
        try {
            val imm =
                baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
        }

    }

    fun hideKeyBoard() {
        try {
            val imm =
                baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
        }

    }

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        try {
            if (Globals.AppStatus) {
                Utility.loadSettings(this)

            } else {
                Globals.AppStatusMSG =
                    if (!Globals.AppStatusMSG.isNullOrEmpty()) Globals.AppStatusMSG else getString(R.string.tryagain)
                Utility.showAlertDialog(
                    AlertMessageModel(
                        context = this,
                        title = "Message",
                        message = Globals.AppStatusMSG,
                        NeedToCloseApp = true,
                        PositiveButtonText = getString(R.string.OK)
                    )
                )
            }
        } catch (e: Exception) {
//            Toast.makeText(baseContext, "from master " + e.toString(), Toast.LENGTH_LONG).show()
        }

    }

//    override fun onBackPressed() {}

    fun finishMe() {
        finishMe(baseContext)
    }

    companion object {

        var FragmentStack: Stack<BaseFragment> = Stack<BaseFragment>()
    }

}
