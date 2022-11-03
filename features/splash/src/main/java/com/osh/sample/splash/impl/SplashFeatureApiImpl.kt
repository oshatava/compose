package com.osh.sample.splash.impl

import com.osh.sample.splash.SplashFeatureApi
import com.osh.sample.splash.SplashFeatureNavigationGraphCreator
import javax.inject.Inject

internal class SplashFeatureApiImpl @Inject constructor(
    private val splashFeatureNavigationGraphCreator: SplashFeatureNavigationGraphCreator
) : SplashFeatureApi,
    SplashFeatureNavigationGraphCreator by splashFeatureNavigationGraphCreator
