package org.ck.webapp.springReactor1;

import org.ck.webapp.springReactor.MyReactiveLibrary;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class SpringSubContext {

	public static void testSubContext(ApplicationContext rootContext) throws Exception {
		ClassLoader classLoader = new MyURLClassLoader("target/classes", SpringSubContext.class.getClassLoader());
		PathMatchingResourcePatternResolver classResolver = new PathMatchingResourcePatternResolver(classLoader);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.setParent(rootContext);
		context.setResourceLoader(classResolver);
		context.scan("org.ck.webapp.springReactor1");
		context.refresh();
		// 必须用这个classloader
		context.getBean(classLoader.loadClass("org.ck.webapp.springReactor1.MyReactiveLibraryTest"));

		rootContext.getBean(MyReactiveLibrary.class).say();
		context.close();
	}
}
