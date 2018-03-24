package com.omnicrola.justsimpleweather.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class WindConverterTest {

    @Test
    public void testDirections() {
        assertEquals("N", WindConverter.directionFromDegrees(0));
        assertEquals("NE", WindConverter.directionFromDegrees(45));
        assertEquals("E", WindConverter.directionFromDegrees(90));
        assertEquals("SE", WindConverter.directionFromDegrees(135));
        assertEquals("S", WindConverter.directionFromDegrees(180));
        assertEquals("SW", WindConverter.directionFromDegrees(225));
        assertEquals("W", WindConverter.directionFromDegrees(270));
        assertEquals("NW", WindConverter.directionFromDegrees(315));
    }
}