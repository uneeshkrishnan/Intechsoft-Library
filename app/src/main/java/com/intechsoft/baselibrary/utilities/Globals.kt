package com.intechsoft.baselibrary.utilities

open class Globals {

    class ExceptionInfo {
        companion object {

            var ExMessage = ""
            var ExIcon = android.R.drawable.ic_dialog_alert
            var ExType: Exceptions? = null
        }
    }

    enum class Exceptions {

        ArrayIndexOutOfBoundsException,
        ClassCastException,
        ClassNotFoundException,
        Error,
        Exception,
        IllegalArgumentException,
        IndexOutOfBoundsException,
        NoSuchFieldException,
        NullPointerException,
        NumberFormatException,
        RuntimeException,
        TimeoutException
    }

    companion object {

        val TAG = "KRCS"
        val APPNAME = "KRCS"
        var AppStatus = true
        var AppStatusMSG = ""

        const val English_Language: String = "en"
        const val Arabic_Language: String = "ar"

        const val English_API_Language: String = "en-US"
        const val Arabic_API_Language: String = "ar-KW"
    }

    enum class Widgets {
        Button,
        TextView,
        EditText,
        CheckBox,
        RadioButton,
        View,
        LinearLayout,
        RelativeLayout,
        ImageView,
        ListView,
        Spinner,
        ImageButton,
        ScrollView,
        WebView,
        AutoCompleteTextView,
        FrameLayout,
        TableLayout,
        TableRow,
        GridLayout,
        Switch,
        ToggleButton,
        ProgressBar,
        SeekBar,
        RatingBar,
        RadioGroup,
        GridView,
        ExpandableListView,
        HorizontalScrollView,
        TabHost,
        SlidingDrawer,
        Gallery,
        VideoView,
        TwoLineListItem,
        DialerFilter,
        TextClock,
        AnalogClock,
        DigitalClock,
        Chronometer,
        DatePicker,
        TimePicker,
        CalendarView,
        Space,
        CheckedTextView,
        QuickContactBadge,
        MultiAutoCompleteTextView,
        NumberPicker,
        ZoomButton,
        ZoomControls,
        SurfaceView,
        TextureView,
        MediaController,
        StackView,
        ViewStub,
        ViewAnimator,
        ViewFlipper,
        ViewSwitcher,
        ImageSwitcher,
        TextSwitcher,
        AdapterViewFlipper,
        SearchView
    }

    enum class Button {

        Button
    }

    enum class ImageView {

        ImageView
    }

    enum class TextView {

        TextView
    }

    enum class EditText {

        EditText
    }

    enum class RelativeLayout {

        RelativeLayout
    }

    enum class LinearLayout {

        LinearLayout
    }

    enum class ListView {

        ListView
    }

    enum class Spinner {

        Spinner
    }

    enum class ImageButton {

        ImageButton
    }

    enum class ScrollView {

        ScrollView
    }

    enum class WebView {

        WebView
    }

    enum class AutoCompleteTextView {

        AutoCompleteTextView
    }

    enum class RadioButton {

        RadioButton
    }

    enum class FrameLayout {

        FrameLayout
    }

    enum class TableLayout {

        TableLayout
    }

    enum class TableRow {

        TableRow
    }

    enum class GridLayout {

        GridLayout
    }

    enum class CheckBox {

        CheckBox
    }

    enum class Switch {

        Switch
    }

    enum class ToggleButton {

        ToggleButton
    }

    enum class ProgressBar {

        ProgressBar
    }

    enum class SeekBar {

        SeekBar
    }

    enum class RatingBar {

        RatingBar
    }

    enum class RadioGroup {

        RadioGroup
    }

    enum class GridView {

        GridView
    }

    enum class ExpandableListView {

        ExpandableListView
    }

    enum class HorizontalScrollView {

        HorizontalScrollView
    }

    enum class SearchView {

        SearchView
    }

    enum class TabHost {

        TabHost
    }

    enum class SlidingDrawer {

        SlidingDrawer
    }

    enum class Gallery {

        Gallery
    }

    enum class VideoView {

        VideoView
    }

    enum class TwoLineListItem {

        TwoLineListItem
    }

    enum class DialerFilter {

        DialerFilter
    }

    enum class TextClock {

        TextClock
    }

    enum class AnalogClock {

        AnalogClock
    }

    enum class DigitalClock {

        DigitalClock
    }

    enum class Chronometer {

        Chronometer
    }

    enum class DatePicker {

        DatePicker
    }

    enum class TimePicker {

        TimePicker
    }

    enum class CalendarView {

        CalendarView
    }

    enum class Space {

        Space
    }

    enum class CheckedTextView {

        CheckedTextView
    }

    enum class QuickContactBadge {

        QuickContactBadge
    }

    enum class MultiAutoCompleteTextView {

        MultiAutoCompleteTextView
    }

    enum class NumberPicker {

        NumberPicker
    }

    enum class ZoomButton {

        ZoomButton
    }

    enum class ZoomControls {

        ZoomControls
    }

    enum class MediaController {

        MediaController
    }

    enum class SurfaceView {

        SurfaceView
    }

    enum class TextureView {

        TextureView
    }

    enum class StackView {

        StackView
    }

    enum class ViewStub {

        ViewStub
    }

    enum class ViewAnimator {

        ViewAnimator
    }

    enum class ViewFlipper {

        ViewFlipper
    }

    enum class ViewSwitcher {

        ViewSwitcher
    }

    enum class ImageSwitcher {

        ImageSwitcher
    }

    enum class TextSwitcher {

        TextSwitcher
    }

    enum class AdapterViewFlipper {

        AdapterViewFlipper
    }

    enum class requestFocus {

        requestFocus
    }

    enum class View {

        View
    }

    enum class RecyclerView {

        RecyslerView
    }

    enum class CardView {

        CardView
    }

    enum class ViewPager {

        ViewPager
    }

}
