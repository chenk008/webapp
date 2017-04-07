package org.ck.webapp.chronicle;

import net.openhft.chronicle.map.ChronicleMap;

public class ChronicleMapTest {

	public static void main(String[] args) {
		ChronicleMap<CharSequence, String> cityPostalCodes = ChronicleMap.of(CharSequence.class, String.class)
				.name("city-postal-codes-map").averageKey("Amsterdam").entries(50_000).create();
	}
}