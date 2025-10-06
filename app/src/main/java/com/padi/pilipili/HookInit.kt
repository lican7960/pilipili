package com.padi.pilipili

import android.app.Application

interface HookInit {

    fun init(application: Application)

    fun findDex(application: Application)

}