package ru.goryachev.tochka.model.system.flow

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Back

open class GlobalRouter : Router() {

    fun sendResult(resultCode: Int): Boolean {
        return super.sendResult(resultCode, null)
    }

    public override fun sendResult(resultCode: Int, result: Any?): Boolean {
        return super.sendResult(resultCode, result)
    }

    fun backWithResult(resultCode: Int, result: Any?) {
        back()
        sendResult(resultCode, result)
    }

    fun back() {
        executeCommands(Back())
    }
}