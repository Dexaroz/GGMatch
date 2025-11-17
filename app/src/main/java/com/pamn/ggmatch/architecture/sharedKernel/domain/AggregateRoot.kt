package com.pamn.ggmatch.architecture.sharedKernel.domain

abstract class AggregateRoot<ID>(
    id: ID
) : Entity<ID>(id) {

    @Transient
    private val _domainEvents: MutableList<DomainEvent> = mutableListOf()

    val domainEvents: List<DomainEvent>
        get() = _domainEvents.toList()

    protected fun registerEvent(event: DomainEvent) {
        _domainEvents.add(event)
    }

    fun pullDomainEvents(): List<DomainEvent> {
        val events = _domainEvents.toList()
        _domainEvents.clear()
        return events
    }
}