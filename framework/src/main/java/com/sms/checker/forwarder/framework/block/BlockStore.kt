package com.sms.checker.forwarder.framework.block

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope

class BlockStore(
    private val scope: CoroutineScope,
    private val savedStateHandle: SavedStateHandle,
) {
    private val blocks = mutableListOf<BaseBlock<*, *>>()
    var isRegister: Boolean = false
        private set

    fun <P> add(block: BaseBlock<*, P>, provider: P) {
        if (isRegister) return
        block.attach(
            scope = scope,
            savedStateHandle = savedStateHandle,
            provider = provider,
        )
        blocks.add(block)
        block.start()
    }

    fun add(block: BaseBlock<*, Unit>) {
        if (isRegister) return
        block.attach(
            scope = scope,
            savedStateHandle = savedStateHandle,
            provider = Unit,
        )
        blocks.add(block)
        block.start()
    }
    internal fun build(): List<BaseBlock<*, *>> {
        if (blocks.isNotEmpty()) isRegister = true
        return blocks.toList()
    }
}