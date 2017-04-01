package org.ck.webapp.slabCache;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 
 * �����Memcached slab/chunk���Ƶ�Java�ڴ����ʽ��Ϊ����Ķ������һ��chunck����ͬSize��Chunk�ϳ�һ��Slab��
 * 
 * ��ʼslab��Ϊ100B������������С��100B������100B
 * slab���������100B��С�� 100B * Growth Factor = 1.27 = 127B�������127B slab��
 *
 * @param <K>
 * @param <V>
 */
public class LocalMemCache<K, V> {
	// ��cache sizeΪkey����chunks mapΪvalue
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
			// ��������cache size��õ�slab�����ڣ��򴴽�һ��
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
			// �����ڴ�Լcache size��slab����һ����С��slab
			// ����ǰȫ��cache size + ��������size > ���������cache��initSizeʱ������ʹ��LRU����
			boolean isLRU = getCurrentTotalCacheSize() + theSize > initSize;
			entry.getValue().put(key, value, isLRU);
			return true;
		}
	}
}
