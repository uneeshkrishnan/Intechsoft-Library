package com.intechsoft.baselibrary.baseclasses

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.intechsoft.baselibrary.R
import com.intechsoft.baselibrary.activities.AppContainerActivity
import com.intechsoft.baselibrary.activities.StartActivity
import com.intechsoft.baselibrary.utilities.Globals
import java.util.*


//    protected Button btn_Login;

open class BaseFragmentActivity : AppCompatActivity() {

    internal var actionBar: ActionBar? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.appcontainer)

            try {
                actionBar = getActionBar()
                actionBar!!.hide()
            } catch (e: Exception) {
            }

        } catch (e: Exception) {
        }

    }

    fun finishMe(context: Context) {
        val intent = Intent(context, StartActivity::class.java)
        intent.putExtra("needToCallWS", true)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
        finish()
    }

    fun addFragment(
        fragment: BaseFragment,
        transition: Int,
        FragmentStack: Stack<BaseFragment>,
        isReplace: Boolean = false
    ) {
        val ft = this.supportFragmentManager.beginTransaction()
        if (BaseApplication.prefs?.language == Globals.Arabic_Language) {
            ft.setCustomAnimations(R.anim.anim_revin, R.anim.anim_revout)
        } else {
            ft.setCustomAnimations(R.anim.anim_in, R.anim.anim_out)
        }
        if (isReplace) {
            ft.replace(R.id.content_frame, FragmentStack.push(fragment))
        } else {
            ft.add(R.id.content_frame, FragmentStack.push(fragment))
        }
        ft.setTransition(transition)
        ft.commit()
    }

    protected open fun BackPressed(FragmentStack: Stack<BaseFragment>): Boolean {
//        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START)
//            return true
//        } else {
        var myRetVal = true
        if (FragmentStack.size > 1) {
            val baseFragment = FragmentStack.peek() as BaseFragment
            if (baseFragment.onBackClick()) {
                baseFragment.isBackPressed = true
                val ft = supportFragmentManager.beginTransaction()
                if (BaseApplication.prefs?.language == Globals.Arabic_Language) {
                    ft.setCustomAnimations(R.anim.anim_in, R.anim.anim_out)
                } else {
                    ft.setCustomAnimations(R.anim.anim_revin, R.anim.anim_revout)
                }
                val popFragment = FragmentStack.pop()
                AppContainerActivity.FragmentStack.pop()
//                popFragment.onDestroy()
                ft.remove(popFragment)
                val shown = FragmentStack.peek() as BaseFragment
                if (shown != null) {
                    shown.isBackPressed = false
                    ft.show(shown)
                }
                shown.onFragmentResume()
                ft.commit()
                myRetVal = true
                shown.onResumeAfterBackPressed()
            }
        } else {

            val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            builder.setTitle(getString(R.string.exit_question)).setCancelable(false)
                .setPositiveButton(getString(R.string.Yes))
                { dialog, i ->
                    finish()
                }
            builder.setNegativeButton(getString(R.string.No)) { dialog, i ->
                dialog.cancel()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }
        return myRetVal
//        }
    }

    fun setContext() {

    }

    companion object {

        var isAppWentToBg = false

        var isBackPressed = false
    }

}
