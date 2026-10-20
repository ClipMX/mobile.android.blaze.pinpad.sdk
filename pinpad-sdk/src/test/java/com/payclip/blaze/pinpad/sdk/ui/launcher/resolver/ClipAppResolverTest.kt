package com.payclip.blaze.pinpad.sdk.ui.launcher.resolver

import android.content.ComponentName
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ClipAppResolverTest {

    private val fallback = mockComponent(PINPAD, "MainActivity")
    private val resolver = ClipAppResolver(listOf(PINPAD, SMARTPOS), fallback)

    private val pinpad = candidate(PINPAD)
    private val smartpos = candidate(SMARTPOS)

    @Test
    fun `sin candidatas, cae al lanzamiento explicito a PinPad como fallback`() {
        val result = resolver.resolve(emptyList(), rememberedPackage = null)

        result as ClipAppResolution.Launch
        assertEquals(ResolvedBy.FALLBACK, result.resolvedBy)
        assertEquals(fallback, result.candidate.component)
    }

    @Test
    fun `una candidata permitida se lanza directo por action`() {
        val result = resolver.resolve(listOf(smartpos), rememberedPackage = null)

        assertEquals(ClipAppResolution.Launch(smartpos, ResolvedBy.ACTION), result)
    }

    @Test
    fun `una app ajena a Clip que declara la action se descarta y cae al fallback`() {
        val result = resolver.resolve(listOf(candidate("com.otra.app")), rememberedPackage = null)

        result as ClipAppResolution.Launch
        assertEquals(ResolvedBy.FALLBACK, result.resolvedBy)
    }

    @Test
    fun `dos candidatas sin eleccion recordada piden elegir, con PinPad primero`() {
        val result = resolver.resolve(listOf(smartpos, pinpad), rememberedPackage = null)

        assertEquals(ClipAppResolution.Choose(listOf(pinpad, smartpos)), result)
    }

    @Test
    fun `dos candidatas con eleccion recordada se lanza la recordada`() {
        val result = resolver.resolve(listOf(pinpad, smartpos), rememberedPackage = SMARTPOS)

        assertEquals(ClipAppResolution.Launch(smartpos, ResolvedBy.ACTION), result)
    }

    @Test
    fun `una eleccion recordada que ya no esta instalada vuelve a preguntar`() {
        val result = resolver.resolve(listOf(pinpad, smartpos), rememberedPackage = "com.payclip.desinstalada")

        assertEquals(ClipAppResolution.Choose(listOf(pinpad, smartpos)), result)
    }

    private fun candidate(packageName: String) =
        ClipAppCandidate(mockComponent(packageName, "MainActivity"), label = packageName)

    private fun mockComponent(packageName: String, name: String): ComponentName = mockk {
        every { this@mockk.packageName } returns packageName
        every { className } returns name
    }

    private companion object {
        const val PINPAD = "com.payclip.blaze.pinpad"
        const val SMARTPOS = "com.payclip.blaze.smartpos"
    }
}
