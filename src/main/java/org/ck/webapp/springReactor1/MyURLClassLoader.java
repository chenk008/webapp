package org.ck.webapp.springReactor1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 优先本classloader记载
 * 
 * @author viruser
 *
 */
public class MyURLClassLoader extends URLClassLoader {

	private File classesDir;

	public MyURLClassLoader(String classesDir, ClassLoader parent) throws MalformedURLException {
		super(new URL[] {}, parent);
		this.classesDir = new File(classesDir);
	}

	public static void main(String[] args) {
		System.out.println(new File("target/classes").getAbsolutePath());
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if(!name.startsWith("org.ck.webapp.springReactor1.")){
			return this.getParent().loadClass(name);
		}
		synchronized (getClassLoadingLock(name)) {
			Class<?> clasz = findLoadedClass(name);
			if (clasz != null) {
				return clasz;
			}
			try {
				String fileName = name.replace('.', File.separatorChar) + ".class";
				File classFile = new File(classesDir, fileName);
				FileChannel channel = new FileInputStream(classFile).getChannel();
				ByteBuffer buffer = ByteBuffer.allocate(new Long(channel.size()).intValue());
				int result = channel.read(buffer);
				if (result != channel.size()) {
					throw new ClassNotFoundException("file read error");
				}
				return this.defineClass(name, buffer.array(), 0, buffer.limit());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return this.getParent().loadClass(name);
		}
	}

}
