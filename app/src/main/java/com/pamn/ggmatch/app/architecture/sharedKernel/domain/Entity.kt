package com.pamn.ggmatch.app.architecture.sharedKernel.domain

abstract class Entity<ID>(
    override val id: ID,
) : Identifiable<ID> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false

        other as Entity<*>
        return this.id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0

    override fun toString(): String = "${this::class.simpleName}(id=$id)"
}
