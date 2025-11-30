package com.pamn.ggmatch.app.architecture.model.user

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.BaseDomainEvent

data class UserRegisteredEvent(
    val userId: UserId,
    val email: Email,
) : BaseDomainEvent()
