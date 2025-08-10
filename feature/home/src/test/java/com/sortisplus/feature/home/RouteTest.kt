package com.sortisplus.feature.home

import com.sortisplus.core.common.Route
import org.junit.Assert.assertEquals
import org.junit.Test

class RouteTest {
    @Test
    fun home_and_details_route_are_stable() {
        assertEquals("home", Route.Home)
        assertEquals("details", Route.Details)
    }
}
