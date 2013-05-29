package com.github.fge.msgsimple.source;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public final class MapMessageSourceTest
{
    @Test
    public void mapContentsAreCopiedCorrectly()
    {
        final String key = "foo";
        final String value = "bar";

        final Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);

        final MessageSource source = new MapMessageSource(map);

        assertEquals(source.getMessage(key), value);
    }

    @Test
    public void nullKeysAreNotAllowedInMap()
    {
        final Map<String, String> map = new HashMap<String, String>();
        map.put(null, null);

        try {
            new MapMessageSource(map);
            fail("No exception thrown!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "null keys not allowed in map");
        }
    }

    @Test
    public void nullValuesAreNotAllowedInMap()
    {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("a", null);

        try {
            new MapMessageSource(map);
            fail("No exception thrown!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "null values not allowed in map");
        }
    }
}