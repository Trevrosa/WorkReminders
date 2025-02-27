package org.trevor.pcup.backend

import kotlinx.serialization.Serializable

@Serializable
enum class DBErrorKind {
    StoreError,
    FetchError,
}
