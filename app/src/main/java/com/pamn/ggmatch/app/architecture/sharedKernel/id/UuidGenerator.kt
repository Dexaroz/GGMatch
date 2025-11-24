package com.pamn.ggmatch.app.architecture.sharedKernel.id

import java.util.UUID

class UuidGenerator : com.pamn.ggmatch.app.architecture.sharedKernel.id.IdGenerator {
    override fun generate(): String = UUID.randomUUID().toString()
}