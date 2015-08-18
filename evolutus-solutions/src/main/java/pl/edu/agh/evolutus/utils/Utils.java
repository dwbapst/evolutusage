package pl.edu.agh.evolutus.utils;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> immutableMap(Object... objects) {
		if (objects.length % 2 != 0) {
			throw new IllegalArgumentException("Arguments length has to be even.");
		}

		Map<K, V> map = new LinkedHashMap<>();
		for (int i = 0; i + 1 < objects.length; i += 2) {
			map.put((K) objects[i], (V) objects[i + 1]);
		}
		return Collections.unmodifiableMap(map);
	}

	public static String getTimestampAsString(Timestamp timestamp) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return format.format(new Date(timestamp.getTime()));
	}

	public static InputStream getResourceAsStream(String resourceName) {
		return Utils.class.getClassLoader().getResourceAsStream(resourceName);
	}
}
