package com.a.compose

import android.content.Context

interface AppContainer {
    val menu:PepoMenu
}


class AppContainerImpl(val applicationContext: Context):AppContainer {
    override val menu: PepoMenu
        get() = RepoMenuImpl()
}