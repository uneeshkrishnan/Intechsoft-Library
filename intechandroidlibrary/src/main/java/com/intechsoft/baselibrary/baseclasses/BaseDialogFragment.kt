package com.intechsoft.baselibrary.baseclasses

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.intechsoft.baselibrary.R
import com.intechsoft.baselibrary.activities.AppContainerActivity
import com.intechsoft.baselibrary.utilities.Globals
import java.util.*


open class BaseDialogFragment : DialogFragment() {

    protected var myBaseFragmentView: View? = null
    protected var act: FragmentActivity? = null
    private var mLastClickTime: Long = 0

    val isReadyToPerformClick: Boolean
        get() {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
                return false
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            return true
        }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            myBaseFragmentView = inflater.inflate(R.layout.appcontainer, container, false);
//            context = getActivity();
//            return myBaseFragmentView;
//
//        }

    fun commitFragment(
        context: Context,
        fragment: Fragment,
        data: Bundle,
        FragmentStack: Stack<Fragment>,
        KeepPrev: Boolean
    ) {
        //        Globals.drawerEnabled = false;
        if (Globals.AppStatus) {
            try {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(myBaseFragmentView!!.windowToken, 0)
            } catch (e: Exception) {
            }

            fragment.arguments = data
            var shown: Fragment? = null
            try {
                shown = FragmentStack.peek()
            } catch (e: Exception) {
            }

            val ft = fragmentManager?.beginTransaction() as FragmentTransaction
            if (BaseApplication.prefs?.language == Globals.Arabic_Language) {
                ft.setCustomAnimations(R.anim.anim_revin, R.anim.anim_revout)
            } else {
                ft.setCustomAnimations(R.anim.anim_in, R.anim.anim_out)
            }
            if (!KeepPrev) {
                try {
                    if (FragmentStack.size > 0) {
                        val ft1 = act!!.supportFragmentManager.beginTransaction()
                        val popFragment = FragmentStack.pop()
                        ft1.remove(popFragment)
                        ft1.commit()
                    }
                } catch (e: Exception) {
                }

            }
            ft.add(R.id.content_frame, FragmentStack.push(fragment))
            ft.commit()

            if (shown != null) {
                ft.hide(shown)
            }
        }
    }

    fun Resume(fr: BaseFragment) {

    }

    protected fun BackPressed() {

        try {
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(myBaseFragmentView!!.windowToken, 0)
        } catch (e: Exception) {
        }

        (act as AppContainerActivity).onBackPressed()
    }

}
