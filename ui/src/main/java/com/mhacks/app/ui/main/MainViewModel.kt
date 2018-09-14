package com.mhacks.app.ui.main

import android.arch.lifecycle.ViewModel
import com.mhacks.app.ui.main.usecase.CheckAuthUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val checkAuthUseCase: CheckAuthUseCase): ViewModel() {


}