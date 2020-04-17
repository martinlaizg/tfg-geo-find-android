package com.martinlaizg.geofind.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SecureTest {

	@Test
	fun hash() {
		val input = "Hola"
		val expected = "c096860be238d7e0a6d3929c7ba06f468d3e6b7b28132ba48d553f845788c513004b1ec78758f24e9e1f006ae9a89651f80023f5505927a7aecd6529fa12c081"
		val actual = Secure.hash("Hola")
		assertEquals(expected, actual)
	}

}