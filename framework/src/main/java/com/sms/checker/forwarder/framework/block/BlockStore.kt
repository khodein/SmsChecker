package com.sms.checker.forwarder.framework.block

import androidx.annotation.CallSuper
import com.sms.checker.forwarder.framework.UiEvent
import kotlinx.coroutines.CoroutineScope

class BlockStore(
    private val scope: CoroutineScope,
    private val onEvent: (UiEvent) -> Unit,
) {
    private val blocks = mutableListOf<BaseBlock<*, *, *>>()
    var isRegister: Boolean = false
        private set

    fun <P> add(block: BaseBlock<*, *, P>, provider: P) {
        if (isRegister) return
        block.attach(
            scope = scope,
            provider = provider,
            onEvent = onEvent,
        )
        blocks.add(block)
        block.start()
    }

    fun add(block: BaseBlock<*, *, Unit>) {
        if (isRegister) return
        block.attach(
            scope = scope,
            provider = Unit,
            onEvent = onEvent,
        )
        blocks.add(block)
        block.start()
    }
    internal fun build(): List<BaseBlock<*, *, *>> {
        if (blocks.isNotEmpty()) isRegister = true
        return blocks.toList()
    }

    internal fun onUiStart() {
        blocks.forEach { it.onUiStart() }
    }

    internal fun onUiStop() {
        blocks.forEach { it.onUiStop() }
    }
}
