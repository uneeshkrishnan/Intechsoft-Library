package com.intechsoft.baselibrary.baseclasses

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.*
import androidx.annotation.CallSuper
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.intechsoft.baselibrary.R
import com.intechsoft.baselibrary.activities.AppContainerActivity
import com.intechsoft.baselibrary.utilities.Globals
import java.util.*


open class BaseFragment : Fragment() {

    protected lateinit var myBaseFragmentView: View
    protected var act: FragmentActivity? = null

    private var mLastClickTime: Long = 0

    protected var mContext: Context? = null
    var isBackPressed: Boolean = false

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.CALL_PHONE
    )

    var isValidIdeaBackPress = true

    val isReadyToPerformClick: Boolean
        get() {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
                return false
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            return true
        }

    fun newInstance() =
        this.apply {
            arguments = Bundle().apply {
                //                putString(ARG_PARAM1, param1)
//                putString(ARG_PARAM2, param2)
            }
        }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity
        mContext = context
    }

    open fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }

    open fun commitFragment(
        toFragment: BaseFragment,
        toFragmentdata: Bundle,
        FragmentStack: Stack<BaseFragment>,
        KeepPrev: Boolean,
        isReplace: Boolean = false
    ) {
//        (act as AppContainerActivity).llt_Loading?.visibility = View.GONE
        if (context != null) {
            if (Globals.AppStatus) {
                try {
                    val imm =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(myBaseFragmentView.windowToken, 0)
                } catch (e: Exception) {
                }

                toFragment.arguments = toFragmentdata
                var shown: Fragment? = null
                try {
                    shown = FragmentStack.peek() as BaseFragment
//                    shown?.isBackPressed = true
                } catch (e: Exception) {
                }

                val ft = act?.supportFragmentManager?.beginTransaction() as FragmentTransaction
                if (BaseApplication.prefs?.language == Globals.Arabic_Language) {
                    ft.setCustomAnimations(R.anim.anim_revin, R.anim.anim_revout)
                } else {
                    ft.setCustomAnimations(R.anim.anim_in, R.anim.anim_out)
                }
                if (!KeepPrev) {
                    try {
                        if (FragmentStack.size > 0) {
                            val ft1 =
                                act?.supportFragmentManager?.beginTransaction() as FragmentTransaction
                            val popFragment = FragmentStack.pop()
                            ft1.remove(popFragment)
                            ft1.commit()
                        }
                    } catch (e: Exception) {
                    }

                }
                if (isReplace) {
                    ft.replace(R.id.content_frame, FragmentStack.push(toFragment))
                } else {
                    ft.add(R.id.content_frame, FragmentStack.push(toFragment))
                }
                AppContainerActivity.FragmentStack.push(toFragment)
                ft.commit()

                if (shown != null) {
                    ft.hide(shown)
                }
            }
        }
    }

    open fun onBackClick(): Boolean {
        return true
    }

    fun Resume(fr: BaseFragment) {

    }

    protected open fun BackPressed() {

        try {
//            (act as AppContainerActivity).llt_Loading?.visibility = View.GONE
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(myBaseFragmentView.windowToken, 0)
        } catch (e: Exception) {
        }

        (act as AppContainerActivity).onBackPressed()
    }

    open fun onFragmentResume(): Boolean {
        return true
    }

    open fun onFragmentFocused(): Boolean {
        return true
    }

    open fun onResumeAfterBackPressed() {}

    open fun restClickTimer() {
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    open fun onRequestPermissionsResult() {
    }

    fun hideKeyBoard() {
        try {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(myBaseFragmentView.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    fun stripHtml(html: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_OPTION_USE_CSS_COLORS).toString()
        } else {
            Html.fromHtml(html).toString()
        }
    }

    fun verifyStoragePermissions() {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            mContext!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity!!,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    fun getStringValue(stringId: Int): String {
        if (mContext != null) {
            return mContext?.resources?.getString(stringId)!!
        }
        return ""
    }

    fun showToast(msg: String) {
        if (mContext != null) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun showToast(msg: String, editText: EditText) {
        if (mContext != null) {
            editText.error = msg
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            editText.requestFocus()
        }
    }

    fun showToast(msg: String, autoCompleteTextView: AutoCompleteTextView) {
        if (mContext != null) {
            autoCompleteTextView.error = msg
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            autoCompleteTextView.requestFocus()
        }
    }

    fun showToast(msg: Int) {
        if (mContext != null) {
            Toast.makeText(mContext, msg.toString() + "", Toast.LENGTH_SHORT).show()
        }
    }

    protected fun setHeader(fr: BaseFragment, headerSettings: BaseHeader) {}
//    fun BuildUrl( listName:String,fields:String,filter:String) -> String {
//        var url = BaseApplication.BASE_URL
//
//        //if subSite.count > 0 {
//        url.append(contentsOf: AppConstants.tanent + ",")
//        url.append(contentsOf: subSite + "/")
//        //}
//        if listName.count > 0 {
//            url.append(contentsOf:  "lists/\(listName)/items/")
//        }
//        if fields.count > 0 {
//            url.append(contentsOf:  "?expand=fields(select=\(fields))")
//        }
//
//        if filter.count > 0 {
//            url.append(contentsOf:  "&filter=\(filter)")
//        }
//        print(url)
//        return url.replacingOccurrences(of: " ", with: "%20")
//    }

    protected fun findViewByID(id: Int, widget: Globals.Widgets): View {
        when (widget) {
            Globals.Widgets.Button -> {
                return myBaseFragmentView.findViewById(id) as Button
            }

            Globals.Widgets.TextView -> {
                return myBaseFragmentView.findViewById(id) as TextView
            }

            Globals.Widgets.EditText -> {
                return myBaseFragmentView.findViewById(id) as EditText
            }

            Globals.Widgets.LinearLayout -> {
                return myBaseFragmentView.findViewById(id) as LinearLayout
            }

            Globals.Widgets.RelativeLayout -> {
                return myBaseFragmentView.findViewById(id) as RelativeLayout
            }

            Globals.Widgets.ImageView -> {
                return myBaseFragmentView.findViewById(id) as ImageView
            }

            Globals.Widgets.ListView -> {
                return myBaseFragmentView.findViewById(id) as ListView
            }

            Globals.Widgets.ImageButton -> {
                return myBaseFragmentView.findViewById(id) as ImageButton
            }

            Globals.Widgets.ScrollView -> {
                return myBaseFragmentView.findViewById(id) as ScrollView
            }

            Globals.Widgets.WebView -> {
                return myBaseFragmentView.findViewById(id) as WebView
            }

            Globals.Widgets.AutoCompleteTextView -> {
                return myBaseFragmentView.findViewById(id) as AutoCompleteTextView
            }

            Globals.Widgets.RadioButton -> {
                return myBaseFragmentView.findViewById(id) as RadioButton
            }

            Globals.Widgets.FrameLayout -> {
                return myBaseFragmentView.findViewById(id) as FrameLayout
            }

            Globals.Widgets.TableLayout -> {
                return myBaseFragmentView.findViewById(id) as TableLayout
            }

            Globals.Widgets.TableRow -> {
                return myBaseFragmentView.findViewById(id) as TableRow
            }

            Globals.Widgets.GridLayout -> {
                return myBaseFragmentView.findViewById(id) as GridLayout
            }

            Globals.Widgets.CheckBox -> {
                return myBaseFragmentView.findViewById(id) as CheckBox
            }

            Globals.Widgets.Switch -> {
                return myBaseFragmentView.findViewById(id) as Switch
            }

            Globals.Widgets.ToggleButton -> {
                return myBaseFragmentView.findViewById(id) as ToggleButton
            }

            Globals.Widgets.ProgressBar -> {
                return myBaseFragmentView.findViewById(id) as ProgressBar
            }

            Globals.Widgets.SeekBar -> {
                return myBaseFragmentView.findViewById(id) as SeekBar
            }

            Globals.Widgets.RatingBar -> {
                return myBaseFragmentView.findViewById(id) as RatingBar
            }

            Globals.Widgets.RadioGroup -> {
                return myBaseFragmentView.findViewById(id) as RadioGroup
            }

            Globals.Widgets.GridView -> {
                return myBaseFragmentView.findViewById(id) as GridView
            }

            Globals.Widgets.ExpandableListView -> {
                return myBaseFragmentView.findViewById(id) as ExpandableListView
            }

            Globals.Widgets.HorizontalScrollView -> {
                return myBaseFragmentView.findViewById(id) as HorizontalScrollView
            }

            Globals.Widgets.SearchView -> {
                return myBaseFragmentView.findViewById(id) as SearchView
            }

            Globals.Widgets.TabHost -> {
                return myBaseFragmentView.findViewById(id) as TabHost
            }

            Globals.Widgets.SlidingDrawer -> {
                return myBaseFragmentView.findViewById(id) as SlidingDrawer
            }

            Globals.Widgets.Gallery -> {
                return myBaseFragmentView.findViewById(id) as Gallery
            }

            Globals.Widgets.VideoView -> {
                return myBaseFragmentView.findViewById(id) as VideoView
            }

            Globals.Widgets.TwoLineListItem -> {
                return myBaseFragmentView.findViewById(id) as TwoLineListItem
            }

            Globals.Widgets.DialerFilter -> {
                return myBaseFragmentView.findViewById(id) as DialerFilter
            }

            Globals.Widgets.TextClock -> {
                return myBaseFragmentView.findViewById(id) as TextClock
            }

            Globals.Widgets.AnalogClock -> {
                return myBaseFragmentView.findViewById(id) as AnalogClock
            }

            Globals.Widgets.DigitalClock -> {
                return myBaseFragmentView.findViewById(id) as DigitalClock
            }

            Globals.Widgets.Chronometer -> {
                return myBaseFragmentView.findViewById(id) as Chronometer
            }

            Globals.Widgets.DatePicker -> {
                return myBaseFragmentView.findViewById(id) as DatePicker
            }

            Globals.Widgets.TimePicker -> {
                return myBaseFragmentView.findViewById(id) as TimePicker
            }

            Globals.Widgets.CalendarView -> {
                return myBaseFragmentView.findViewById(id) as CalendarView
            }

            Globals.Widgets.Space -> {
                return myBaseFragmentView.findViewById(id) as Space
            }

            Globals.Widgets.CheckedTextView -> {
                return myBaseFragmentView.findViewById(id) as CheckedTextView
            }

            Globals.Widgets.QuickContactBadge -> {
                return myBaseFragmentView.findViewById(id) as QuickContactBadge
            }

            Globals.Widgets.MultiAutoCompleteTextView -> {
                return myBaseFragmentView.findViewById(id) as MultiAutoCompleteTextView
            }

            Globals.Widgets.NumberPicker -> {
                return myBaseFragmentView.findViewById(id) as NumberPicker
            }

            Globals.Widgets.ZoomButton -> {
                return myBaseFragmentView.findViewById(id) as ZoomButton
            }

            Globals.Widgets.ZoomControls -> {
                return myBaseFragmentView.findViewById(id) as ZoomControls
            }

            Globals.Widgets.MediaController -> {
                return myBaseFragmentView.findViewById(id) as MediaController
            }

            Globals.Widgets.SurfaceView -> {
                return myBaseFragmentView.findViewById(id) as SurfaceView
            }

            Globals.Widgets.TextureView -> {
                return myBaseFragmentView.findViewById(id) as TextureView
            }

            Globals.Widgets.StackView -> {
                return myBaseFragmentView.findViewById(id) as StackView
            }

            Globals.Widgets.ViewStub -> {
                return myBaseFragmentView.findViewById(id) as ViewStub
            }

            Globals.Widgets.ViewAnimator -> {
                return myBaseFragmentView.findViewById(id) as ViewAnimator
            }

            Globals.Widgets.ViewFlipper -> {
                return myBaseFragmentView.findViewById(id) as ViewFlipper
            }

            Globals.Widgets.ViewSwitcher -> {
                return myBaseFragmentView.findViewById(id) as ViewSwitcher
            }

            Globals.Widgets.ImageSwitcher -> {
                return myBaseFragmentView.findViewById(id) as ImageSwitcher
            }

            Globals.Widgets.TextSwitcher -> {
                return myBaseFragmentView.findViewById(id) as TextSwitcher
            }

            Globals.Widgets.AdapterViewFlipper -> {
                return myBaseFragmentView.findViewById(id) as AdapterViewFlipper
            }

            Globals.Widgets.View -> {
                return myBaseFragmentView.findViewById(id) as View
            }

            else -> {
                return myBaseFragmentView.findViewById(id) as View
            }
        }
    }

    protected fun findViewByID(id: Int): View {
        return myBaseFragmentView.findViewById(id) as View
    }

    protected fun findViewByID(button: Globals.Button, id: Int): Button {
        return myBaseFragmentView.findViewById(id) as Button
    }

    protected fun findViewByID(textview: Globals.TextView, id: Int): TextView {
        return myBaseFragmentView.findViewById(id) as TextView
    }

    protected fun findViewByID(edittext: Globals.EditText, id: Int): EditText {
        return myBaseFragmentView.findViewById(id) as EditText
    }

    protected fun findViewByID(linearlayout: Globals.LinearLayout, id: Int): LinearLayout {
        return myBaseFragmentView.findViewById(id) as LinearLayout
    }

    protected fun findViewByID(relativelayout: Globals.RelativeLayout, id: Int): RelativeLayout {
        return myBaseFragmentView.findViewById(id) as RelativeLayout
    }

    protected fun findViewByID(ImageView: Globals.ImageView, id: Int): ImageView {
        return myBaseFragmentView.findViewById(id) as ImageView
    }

    protected fun findViewByID(ListView: Globals.ListView, id: Int): ListView {
        return myBaseFragmentView.findViewById(id) as ListView
    }

    protected fun findViewByID(Spinner: Globals.Spinner, id: Int): Spinner {
        return myBaseFragmentView.findViewById(id) as Spinner
    }

    protected fun findViewByID(ImageButton: Globals.ImageButton, id: Int): ImageButton {
        return myBaseFragmentView.findViewById(id) as ImageButton
    }

    protected fun findViewByID(ScrollView: Globals.ScrollView, id: Int): ScrollView {
        return myBaseFragmentView.findViewById(id) as ScrollView
    }

    protected fun findViewByID(WebView: Globals.WebView, id: Int): WebView {
        return myBaseFragmentView.findViewById(id) as WebView
    }

    protected fun findViewByID(
        AutoCompleteTextView: Globals.AutoCompleteTextView,
        id: Int
    ): AutoCompleteTextView {
        return myBaseFragmentView.findViewById(id) as AutoCompleteTextView
    }

    protected fun findViewByID(RadioButton: Globals.RadioButton, id: Int): RadioButton {
        return myBaseFragmentView.findViewById(id) as RadioButton
    }

    protected fun findViewByID(FrameLayout: Globals.FrameLayout, id: Int): FrameLayout {
        return myBaseFragmentView.findViewById(id) as FrameLayout
    }

    protected fun findViewByID(TableLayout: Globals.TableLayout, id: Int): TableLayout {
        return myBaseFragmentView.findViewById(id) as TableLayout
    }

    protected fun findViewByID(TableRow: Globals.TableRow, id: Int): TableRow {
        return myBaseFragmentView.findViewById(id) as TableRow
    }

    protected fun findViewByID(GridLayout: Globals.GridLayout, id: Int): GridLayout {
        return myBaseFragmentView.findViewById(id) as GridLayout
    }

    protected fun findViewByID(CheckBox: Globals.CheckBox, id: Int): CheckBox {
        return myBaseFragmentView.findViewById(id) as CheckBox
    }

    protected fun findViewByID(Switch: Globals.Switch, id: Int): Switch {
        return myBaseFragmentView.findViewById(id) as Switch
    }

    protected fun findViewByID(ToggleButton: Globals.ToggleButton, id: Int): ToggleButton {
        return myBaseFragmentView.findViewById(id) as ToggleButton
    }

    protected fun findViewByID(ProgressBar: Globals.ProgressBar, id: Int): ProgressBar {
        return myBaseFragmentView.findViewById(id) as ProgressBar
    }

    protected fun findViewByID(SeekBar: Globals.SeekBar, id: Int): SeekBar {
        return myBaseFragmentView.findViewById(id) as SeekBar
    }

    protected fun findViewByID(RatingBar: Globals.RatingBar, id: Int): RatingBar {
        return myBaseFragmentView.findViewById(id) as RatingBar
    }

    protected fun findViewByID(RadioGroup: Globals.RadioGroup, id: Int): RadioGroup {
        return myBaseFragmentView.findViewById(id) as RadioGroup
    }

    protected fun findViewByID(GridView: Globals.GridView, id: Int): GridView {
        return myBaseFragmentView.findViewById(id) as GridView
    }

    protected fun findViewByID(
        ExpandableListView: Globals.ExpandableListView,
        id: Int
    ): ExpandableListView {
        return myBaseFragmentView.findViewById(id) as ExpandableListView
    }

    protected fun findViewByID(
        HorizontalScrollView: Globals.HorizontalScrollView,
        id: Int
    ): HorizontalScrollView {
        return myBaseFragmentView.findViewById(id) as HorizontalScrollView
    }

    protected fun findViewByID(SearchView: Globals.SearchView, id: Int): SearchView {
        return myBaseFragmentView.findViewById(id) as SearchView
    }

    protected fun findViewByID(TabHost: Globals.TabHost, id: Int): TabHost {
        return myBaseFragmentView.findViewById(id) as TabHost
    }

    protected fun findViewByID(SlidingDrawer: Globals.SlidingDrawer, id: Int): SlidingDrawer {
        return myBaseFragmentView.findViewById(id) as SlidingDrawer
    }

    protected fun findViewByID(Gallery: Globals.Gallery, id: Int): Gallery {
        return myBaseFragmentView.findViewById(id) as Gallery
    }

    protected fun findViewByID(VideoView: Globals.VideoView, id: Int): VideoView {
        return myBaseFragmentView.findViewById(id) as VideoView
    }

    protected fun findViewByID(TwoLineListItem: Globals.TwoLineListItem, id: Int): TwoLineListItem {
        return myBaseFragmentView.findViewById(id) as TwoLineListItem
    }

    protected fun findViewByID(DialerFilter: Globals.DialerFilter, id: Int): DialerFilter {
        return myBaseFragmentView.findViewById(id) as DialerFilter
    }

    protected fun findViewByID(TextClock: Globals.TextClock, id: Int): TextClock {
        return myBaseFragmentView.findViewById(id) as TextClock
    }

    protected fun findViewByID(AnalogClock: Globals.AnalogClock, id: Int): AnalogClock {
        return myBaseFragmentView.findViewById(id) as AnalogClock
    }

    protected fun findViewByID(DigitalClock: Globals.DigitalClock, id: Int): DigitalClock {
        return myBaseFragmentView.findViewById(id) as DigitalClock
    }

    protected fun findViewByID(Chronometer: Globals.Chronometer, id: Int): Chronometer {
        return myBaseFragmentView.findViewById(id) as Chronometer
    }

    protected fun findViewByID(DatePicker: Globals.DatePicker, id: Int): DatePicker {
        return myBaseFragmentView.findViewById(id) as DatePicker
    }

    protected fun findViewByID(TimePicker: Globals.TimePicker, id: Int): TimePicker {
        return myBaseFragmentView.findViewById(id) as TimePicker
    }

    protected fun findViewByID(CalendarView: Globals.CalendarView, id: Int): CalendarView {
        return myBaseFragmentView.findViewById(id) as CalendarView
    }

    protected fun findViewByID(Space: Globals.Space, id: Int): Space {
        return myBaseFragmentView.findViewById(id) as Space
    }

    protected fun findViewByID(CheckedTextView: Globals.CheckedTextView, id: Int): CheckedTextView {
        return myBaseFragmentView.findViewById(id) as CheckedTextView
    }

    protected fun findViewByID(
        QuickContactBadge: Globals.QuickContactBadge,
        id: Int
    ): QuickContactBadge {
        return myBaseFragmentView.findViewById(id) as QuickContactBadge
    }

    protected fun findViewByID(
        MultiAutoCompleteTextView: Globals.MultiAutoCompleteTextView,
        id: Int
    ): MultiAutoCompleteTextView {
        return myBaseFragmentView.findViewById(id) as MultiAutoCompleteTextView
    }

    protected fun findViewByID(NumberPicker: Globals.NumberPicker, id: Int): NumberPicker {
        return myBaseFragmentView.findViewById(id) as NumberPicker
    }

    protected fun findViewByID(ZoomButton: Globals.ZoomButton, id: Int): ZoomButton {
        return myBaseFragmentView.findViewById(id) as ZoomButton
    }

    protected fun findViewByID(ZoomControls: Globals.ZoomControls, id: Int): ZoomControls {
        return myBaseFragmentView.findViewById(id) as ZoomControls
    }

    protected fun findViewByID(MediaController: Globals.MediaController, id: Int): MediaController {
        return myBaseFragmentView.findViewById(id) as MediaController
    }

    protected fun findViewByID(SurfaceView: Globals.SurfaceView, id: Int): SurfaceView {
        return myBaseFragmentView.findViewById(id) as SurfaceView
    }

    protected fun findViewByID(TextureView: Globals.TextureView, id: Int): TextureView {
        return myBaseFragmentView.findViewById(id) as TextureView
    }

    protected fun findViewByID(StackView: Globals.StackView, id: Int): StackView {
        return myBaseFragmentView.findViewById(id) as StackView
    }

    protected fun findViewByID(ViewStub: Globals.ViewStub, id: Int): ViewStub {
        return myBaseFragmentView.findViewById(id) as ViewStub
    }

    protected fun findViewByID(ViewAnimator: Globals.ViewAnimator, id: Int): ViewAnimator {
        return myBaseFragmentView.findViewById(id) as ViewAnimator
    }

    protected fun findViewByID(ViewFlipper: Globals.ViewFlipper, id: Int): ViewFlipper {
        return myBaseFragmentView.findViewById(id) as ViewFlipper
    }

    protected fun findViewByID(ViewSwitcher: Globals.ViewSwitcher, id: Int): ViewSwitcher {
        return myBaseFragmentView.findViewById(id) as ViewSwitcher
    }

    protected fun findViewByID(ImageSwitcher: Globals.ImageSwitcher, id: Int): ImageSwitcher {
        return myBaseFragmentView.findViewById(id) as ImageSwitcher
    }

    protected fun findViewByID(TextSwitcher: Globals.TextSwitcher, id: Int): TextSwitcher {
        return myBaseFragmentView.findViewById(id) as TextSwitcher
    }

    protected fun findViewByID(
        AdapterViewFlipper: Globals.AdapterViewFlipper,
        id: Int
    ): AdapterViewFlipper {
        return myBaseFragmentView.findViewById(id) as AdapterViewFlipper
    }

    protected fun findViewByID(View: Globals.View, id: Int): View {
        return myBaseFragmentView.findViewById(id) as View
    }

    protected fun findViewByID(AdapterViewFlipper: Globals.RecyclerView, id: Int): RecyclerView {
        return myBaseFragmentView.findViewById(id) as RecyclerView
    }

    protected fun findViewByID(View: Globals.CardView, id: Int): CardView {
        return myBaseFragmentView.findViewById(id) as CardView
    }

    protected fun findViewByID(View: Globals.ViewPager, id: Int): ViewPager {
        return myBaseFragmentView.findViewById(id) as ViewPager
    }

}
