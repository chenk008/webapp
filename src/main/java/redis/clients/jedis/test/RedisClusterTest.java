package redis.clients.jedis.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.MyBinaryJedisCluster;
import redis.clients.util.JedisClusterCRC16;

public class RedisClusterTest {

	public static void main(String[] args) throws IOException {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		// 只需要添加一个实例，jedis会自动发现集群中其它节点
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7000));
		MyBinaryJedisCluster jc = new MyBinaryJedisCluster(jedisClusterNodes);

		String key = "1417";
		jc.setnx(key, "bar");
		String value = jc.get(key);
		System.out.println("key-" + key + " slot-" + JedisClusterCRC16.getSlot(key) + " value-" + value);

		String key2 = "288";
		jc.setnx(key2, "bar2");
		String value2 = jc.get(key2);
		System.out.println("key-" + key2 + " slot-" + JedisClusterCRC16.getSlot(key2) + " value-" + value2);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (null != jc) {
				jc.close();
			}
		}
	}
}
