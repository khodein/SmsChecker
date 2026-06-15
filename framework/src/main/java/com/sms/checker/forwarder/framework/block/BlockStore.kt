package com.sms.checker.forwarder.framework.block

import com.sms.checker.forwarder.framework.UiEvent
import kotlinx.coroutines.CoroutineScope

class BlockStore(
    private val scope: CoroutineScope,
    private val onEvent: (UiEvent) -> Unit,
) {
    private val blocks = mutableSetOf<Block<*, *, *>>()
    internal var isRegister: Boolean = false
        private set

    fun <P> add(block: Block<*, *, P>, provider: P) {
        if (isRegister) return
        block.attach(
            scope = scope,
            provider = provider,
            onEvent = onEvent,
        )
        blocks.add(block)
    }

    fun add(block: Block<*, *, Unit>) {
        if (isRegister) return
        block.attach(
            scope = scope,
            provider = Unit,
            onEvent = onEvent,
        )
        blocks.add(block)
    }

    internal fun build(): List<Block<*, *, *>> {
        if (blocks.isNotEmpty()) isRegister = true
        return blocks.toList()
    }

    internal fun startBlock() {
        blocks.forEach { it.startBlock() }
    }

    internal fun onUiStart() {
        if (!isRegister) return
        blocks.forEach { it.onUiStart() }
    }

    internal fun onUiStop() {
        if (!isRegister) return
        blocks.forEach { it.onUiStop() }
    }
}
