package ru.goryachev.tochka.feature.global.presentation.view

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface ErrorView {
    fun showErrorMessage(message: String, needCallback: Boolean = false)
    fun hideErrorMessage() {}
}