package com.pamn.ggmatch.architecture.sharedKernel.paging

data class PageRequest(
    val page: Int = 0,
    val size: Int = 20
) {
    init {
        require(page >= 0) { "page must be >= 0."}
        require(size >= 0) { "size must be >= 0."}
    }

    val offset: Int get() = page * size
}
