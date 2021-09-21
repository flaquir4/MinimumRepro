package com.example.minimumrepro

import platform.UIKit.UIDevice
import Mixpanel.Mixpanel
actual class Platform actual constructor() {
    actual val platform: String = getPlatformString()

    private fun getPlatformString(): String {
        Mixpanel.sharedInstance()
        return  UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    }
}