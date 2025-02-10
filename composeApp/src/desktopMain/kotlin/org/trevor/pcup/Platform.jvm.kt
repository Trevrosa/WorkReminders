package org.trevor.pcup

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val battery: Float? = null
}

actual fun getPlatform(): Platform = JVMPlatform()