package com.beardytop.beatzaddik.data

actual fun createAppRepository(platformContext: Any?): AppRepository = JsonFileAppRepository()
