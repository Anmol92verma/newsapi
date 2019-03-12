package com.tfexample.anmolgrabassignment.injection

import javax.inject.Scope
import javax.inject.Qualifier

@Scope annotation class ActivityScope

@Retention
@Qualifier annotation class ActivityContext

@Retention
@Qualifier annotation class ApplicationContext