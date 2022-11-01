package com.azuredragon.core.ui

import androidx.fragment.app.DialogFragment

fun DialogFragment.dismissSafely() {
    if (isAdded) {
        if (isStateSaved) {
            dismissAllowingStateLoss()
        } else {
            dismiss()
        }
    }
}