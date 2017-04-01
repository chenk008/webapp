package org.ck.webapp.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandle implements Runnable {

	private String host;
	private int port;

	private Selector selector;
	private SocketChannel socketChannel;

	private volatile boolean stop;

	public TimeClientHandle(String host, int port) {
		this.host = host == null ? "127.0.0.1" : host;
		this.port = port;
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			// ����Ϊ������
			socketChannel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			doConnect();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		while (!stop) {
			try {
				selector.select(1000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				SelectionKey key = null;
				while (it.hasNext()) {
					key = it.next();
					it.remove();
					try {
						handleInput(key);
					} catch (Exception e) {
						if (key != null) {
							key.cancel();
							if (key.channel() != null)
								key.channel().close();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		// ��·�������رպ�����ע���������Channel��Pipe����Դ���ᱻ�Զ�ȥע�Ტ�رգ����Բ���Ҫ�ظ��ͷ���Դ
		if (selector != null)
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	private void handleInput(SelectionKey key) throws IOException {

		if (key.isValid()) {
			// �ж��Ƿ����ӳɹ�
			SocketChannel sc = (SocketChannel) key.channel();
			if (key.isConnectable()) {
				//���ڸ�����״̬���������Ѿ�����ACK��
				if (sc.finishConnect()) {
					sc.register(selector, SelectionKey.OP_READ);
					doWrite(sc);
				} else
					System.exit(1);// ����ʧ�ܣ������˳�
			}
			if (key.isReadable()) {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				//�첽��
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes, "UTF-8");
					System.out.println("Now is : " + body);
					this.stop = true;
				} else if (readBytes < 0) {
					// �Զ���·�ر�
					key.cancel();
					sc.close();
				} else
					; // ����0�ֽڣ�����
			}
		}

	}

	private void doConnect() throws IOException {
		// �첽����
		// ���ֱ�����ӳɹ�����ע�ᵽ��·�������ϣ�����������Ϣ����Ӧ��
		if (socketChannel.connect(new InetSocketAddress(host, port))) {
			//���ӳɹ�
			socketChannel.register(selector, SelectionKey.OP_READ);
			doWrite(socketChannel);
		} else
			//��������û�з���TCP����Ӧ����Ϣ
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
	}

	private void doWrite(SocketChannel sc) throws IOException {
		byte[] req = "QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		sc.write(writeBuffer);
		//д���
		if (!writeBuffer.hasRemaining())
			System.out.println("Send order 2 server succeed.");
	}

}