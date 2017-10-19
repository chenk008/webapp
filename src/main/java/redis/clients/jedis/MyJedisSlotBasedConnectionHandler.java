package redis.clients.jedis;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.exceptions.JedisNoReachableClusterNodeException;

public class MyJedisSlotBasedConnectionHandler implements Closeable {

	protected final MyJedisClusterInfoCache cache;

	public MyJedisSlotBasedConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
			int connectionTimeout, int soTimeout, String password) {
		this.cache = new MyJedisClusterInfoCache(poolConfig, connectionTimeout, soTimeout, password);
		initializeSlotsCache(nodes, poolConfig, password);
	}

	public MyJedisSlotBasedConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
			int timeout) {
		this(nodes, poolConfig, timeout, timeout);
	}

	public MyJedisSlotBasedConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
			int connectionTimeout, int soTimeout) {
		this(nodes, poolConfig, connectionTimeout, soTimeout, null);
	}

	public Jedis getConnectionFromNode(HostAndPort node) {
		return cache.setupNodeIfNotExist(node).getResource();
	}

	public Map<String, JedisPool> getNodes() {
		return cache.getNodes();
	}

	private void initializeSlotsCache(Set<HostAndPort> startNodes, GenericObjectPoolConfig poolConfig,
			String password) {
		for (HostAndPort hostAndPort : startNodes) {
			Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort());
			if (password != null) {
				jedis.auth(password);
			}
			try {
				cache.discoverClusterNodesAndSlots(jedis);
				break;
			} catch (JedisConnectionException e) {
				// try next nodes
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
	}

	/**
	 * 重新拉取slot信息（因为连接失败，或者获取不到slot的连接）
	 */
	public void renewSlotCache() {
		cache.renewClusterSlots(null);
	}

	/**
	 * 当发生JedisMovedDataException的时候
	 * 
	 * @param jedis
	 */
	public void renewSlotCache(Jedis jedis) {
		cache.renewClusterSlots(jedis);
	}

	@Override
	public void close() {
		cache.reset();
	}

	public Jedis getConnection() {
		// In antirez's redis-rb-cluster implementation,
		// getRandomConnection always return valid connection (able to
		// ping-pong)
		// or exception if all connections are invalid

		List<JedisPool> pools = cache.getShuffledNodesPool();

		for (JedisPool pool : pools) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();

				if (jedis == null) {
					continue;
				}

				String result = jedis.ping();

				if (result.equalsIgnoreCase("pong"))
					return jedis;

				jedis.close();
			} catch (JedisException ex) {
				if (jedis != null) {
					jedis.close();
				}
			}
		}

		throw new JedisNoReachableClusterNodeException("No reachable node in cluster");
	}

	public Jedis getConnectionFromSlot(int slot) {

		JedisPool connectionPool = cache.getSlotPool(slot);
		if (connectionPool != null) {
			// It can't guaranteed to get valid connection because of node
			// assignment
			return connectionPool.getResource();
		} else {
			renewSlotCache(); // It's abnormal situation for cluster mode, that
								// we have just nothing for slot, try to
								// rediscover state
			connectionPool = cache.getSlotPool(slot);
			if (connectionPool != null) {
				return connectionPool.getResource();
			} else {
				// no choice, fallback to new connection to random node
				return getConnection();
			}
		}
	}

	/**
	 * 从slave节点取数据
	 * @param slot
	 * @return
	 */
	public Jedis getConnectionFromSlaveSlot(int slot) {
		JedisPool connectionPool = cache.getSlaveSlotPool(slot);
		if (connectionPool != null) {
			// It can't guaranteed to get valid connection because of node
			// assignment
			return connectionPool.getResource();
		} else {
			return getConnectionFromSlot(slot);
		}
	}

}
