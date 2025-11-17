package com.pamn.ggmatch.architecture.sharedKernel.id

import java.util.UUID

class UuidGenerator : IdGenerator {
    override fun generate(): String = UUID.randomUUID().toString()
}