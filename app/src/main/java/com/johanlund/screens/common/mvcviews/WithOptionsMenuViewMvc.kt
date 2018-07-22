package com.johanlund.screens.common.mvcviews

import android.view.Menu
import android.view.MenuInflater

interface WithOptionsMenuViewMvc: ViewMvc {
    fun createOptionsMenu(menu: Menu, inflater: MenuInflater): Boolean
}