package org.ck.webapp.slabCache;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 
 * 构造和Memcached slab/chunk类似的Java内存管理方式，为缓存的对象分配一组chunck，相同Size的Chunk合成一组Slab。
 * 
 * 初始slab设为100B，如果缓存对象小于100B，放入100B
 * slab，如果大于100B，小于 100B * Growth Factor = 1.27 = 127B，则放入127B slab。
 *
 * @param <K>
 * @param <V>
 */
public class LocalMemCache<K, V> {
	// 以cache size为key，以chunks map为value
	ConcurrentSkipListMap<Float, LocalMCSlab> slabs;
	long initSize;
	float scale = 1.5f;

	public LocalMemCache(long initSize, Float initChunkSize) {
		this.initSize = initSize;
		slabs = new ConcurrentSkipListMap<Float, LocalMCSlab>();
		slabs.put(initChunkSize, new LocalMCSlab(initChunkSize.intValue()));
	}

	public float getCurrentTotalCacheSize() {
		float size = 0f;

		for (java.util.Iterator<Float> iterator = slabs.keySet().iterator(); iterator.hasNext();) {
			Float slabsize = iterator.next();
			size += slabsize * slabs.get(slabsize).size();
			Stat.set("Chunk[" + slabsize + "] count=", slabs.get(slabsize).size() + "");
		}

		return size;
	}

	public boolean put(K key, byte[] value) {
		Map.Entry<Float, LocalMCSlab> entry = null;
		Float theSize = Float.valueOf(value.length);
		Stat.set("CacheSize=", ((getCurrentTotalCacheSize() / 1024f)) + "KB");

		
		
		if ((entry = slabs.tailMap(theSize).firstEntry()) == null) {
			// 如果比这个cache size大得的slab不存在，则创建一个
			Float floorKey = slabs.floorKey(theSize);
			float needSize = floorKey == null ? theSize : floorKey * scale;

			while (needSize < theSize) {
				needSize = needSize * scale;
			}

			LocalMCSlab<K, byte[]> slab = new LocalMCSlab<K, byte[]>((int) needSize);
			slab.put(key, value, false);
			slabs.put(needSize, slab);
			return true;
		} else {
			// 否则，在大约cache size的slab中找一个最小的slab
			// 当当前全部cache size + 这个缓存的size > 分配给整个cache的initSize时，则需使用LRU策略
			boolean isLRU = getCurrentTotalCacheSize() + theSize > initSize;
			entry.getValue().put(key, value, isLRU);
			return true;
		}
	}
}
