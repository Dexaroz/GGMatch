package com.pamn.ggmatch.app.architecture.control.swipe

interface ProfilePresenterProvider {
    fun get(): ProfilePresenterImplementation
}