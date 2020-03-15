package com.radityalabs.android.corona.features.widget

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SystemUiManager(
    private val context: Context
) {
    private val _systemUiVisibility = MutableLiveData(0)
    val systemUiVisibility: LiveData<Int> = _systemUiVisibility

    private val _statusBarColor =
        MutableLiveData(COLOR_STATUS_BAR_INVISIBLE)
    val statusBarColor: LiveData<Int> = _statusBarColor

    private val _navigationBarColor =
        MutableLiveData(COLOR_NAVIGATION_BAR_INVISIBLE)
    val navigationBarColor: LiveData<Int> = _navigationBarColor

    var initial: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                updateColors()
            }
        }

    var drawerSlideOffset: Float = 0f
        set(value) {
            if (field != value) {
                field = value
                updateColors()
            }
        }
    private val drawerIsOpened: Boolean
        get() = drawerSlideOffset > 0f

    var isIndigoBackground: Boolean? = null
        set(value) {
            if (field != value) {
                field = value
                updateColors()
            }
        }

    private fun updateColors() {
        if (23 <= Build.VERSION.SDK_INT) {
            updateColorsM()
        } else {
            updateColorsPreM()
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = (context as Activity).window
        val winParams: WindowManager.LayoutParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    @TargetApi(23)
    private fun updateColorsM() {
        _systemUiVisibility.value = if (isIndigoBackground == true || context.isNightMode()) {
            0
        } else {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        // Status bar color
        _statusBarColor.value =
            if ((isIndigoBackground == true || context.isNightMode()) && drawerIsOpened) {
                COLOR_STATUS_BAR_VISIBLE
            } else {
                COLOR_STATUS_BAR_INVISIBLE
            }

        // Navigation bar color
        _navigationBarColor.value =
            if (context.isNightMode()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    COLOR_NAVIGATION_BAR_INVISIBLE
                } else {
                    COLOR_NAVIGATION_BAR_VISIBLE
                }
            } else {
                COLOR_NAVIGATION_BAR_INVISIBLE
            }
    }

    private fun updateColorsPreM() {
        _systemUiVisibility.value = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        // Status bar color
        _statusBarColor.value =
            if (!(isIndigoBackground == true || context.isNightMode()) || drawerIsOpened) {
                COLOR_STATUS_BAR_VISIBLE
            } else {
                COLOR_STATUS_BAR_INVISIBLE
            }

        // Navigation bar color
        _navigationBarColor.value =
            if (context.isNightMode()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    COLOR_NAVIGATION_BAR_INVISIBLE
                } else {
                    COLOR_NAVIGATION_BAR_VISIBLE
                }
            } else {
                COLOR_NAVIGATION_BAR_INVISIBLE
            }
    }

    companion object {
        private const val COLOR_STATUS_BAR_INVISIBLE = Color.TRANSPARENT
        private const val COLOR_STATUS_BAR_VISIBLE = 0x8a000000.toInt()
        private const val COLOR_NAVIGATION_BAR_INVISIBLE = Color.TRANSPARENT
        private const val COLOR_NAVIGATION_BAR_VISIBLE = 0x4DFFFFFF
    }
}

fun Context.isNightMode(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}
