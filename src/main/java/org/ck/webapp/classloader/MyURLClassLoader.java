package org.ck.webapp.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class MyURLClassLoader extends URLClassLoader {

	private static File dir = new File("testClasses");

	public MyURLClassLoader() throws MalformedURLException {
//		super(new URL[] {}, MyURLClassLoader.class.getClassLoader());
		super(new URL[] {});
	}

	public static void main(String[] args) {
		System.out.println(new File("testClasses").getAbsolutePath());
	}

	/**
	 * 失去的双亲委托会导致 被加载类所依赖的类 找不到
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		ByteBuffer buffer;
		try {
			String fileName = name.replace('.', File.separatorChar) + ".class";
			File classFile = new File(dir, fileName);
			FileChannel channel = new FileInputStream(classFile).getChannel();
			buffer = ByteBuffer.allocate(new Long(channel.size()).intValue());
			int result = channel.read(buffer);
			if (result != channel.size()) {
				throw new ClassNotFoundException("file read error");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new ClassNotFoundException("file read error");
		} catch (IOException e) {
			e.printStackTrace();
			throw new ClassNotFoundException("file read error");
		}
		return this.defineClass(name, buffer.array(), 0, buffer.limit());
	
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		ByteBuffer buffer;
		try {
			String fileName = name.replace('.', File.separatorChar) + ".class";
			File classFile = new File(dir, fileName);
			FileChannel channel = new FileInputStream(classFile).getChannel();
			buffer = ByteBuffer.allocate(new Long(channel.size()).intValue());
			int result = channel.read(buffer);
			if (result != channel.size()) {
				throw new ClassNotFoundException("file read error");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new ClassNotFoundException("file read error");
		} catch (IOException e) {
			e.printStackTrace();
			throw new ClassNotFoundException("file read error");
		}
		return this.defineClass(name, buffer.array(), 0, buffer.limit());
	}

}
