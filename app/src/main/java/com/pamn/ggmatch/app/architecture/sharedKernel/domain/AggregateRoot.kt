package com.pamn.ggmatch.app.architecture.sharedKernel.domain

abstract class AggregateRoot<ID>(
    id: ID
) : com.pamn.ggmatch.app.architecture.sharedKernel.domain.Entity<ID>(id) {

    @Transient
    private val _domainEvents: MutableList<com.pamn.ggmatch.app.architecture.sharedKernel.domain.DomainEvent> = mutableListOf()

    val domainEvents: List<com.pamn.ggmatch.app.architecture.sharedKernel.domain.DomainEvent>
        get() = _domainEvents.toList()

    protected fun registerEvent(event: com.pamn.ggmatch.app.architecture.sharedKernel.domain.DomainEvent) {
        _domainEvents.add(event)
    }

    fun pullDomainEvents(): List<com.pamn.ggmatch.app.architecture.sharedKernel.domain.DomainEvent> {
        val events = _domainEvents.toList()
        _domainEvents.clear()
        return events
    }
}