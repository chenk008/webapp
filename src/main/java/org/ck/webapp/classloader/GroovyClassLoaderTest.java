package org.ck.webapp.classloader;

import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * -verbose 输出类的load和unload信息
 * 
 * @author wuhua.ck
 *
 */
public class GroovyClassLoaderTest {
	public static void main(String[] args) {
		System.out.println("Script57".matches("Script(\\d)+"));

		while (true) {
			jsr223();
		}

	}

	public static void groovyLoaderTest() {
		String scriptText = "def mul(x, y) { x * y }\nprintln mul(5, 7)";
		GroovyClassLoader loader = new GroovyClassLoader();
		Class<?> newClazz = loader.parseClass(scriptText);
		try {
			//利用GroovyClassLoader$InnerLoader加载
			Object obj = newClazz.newInstance();
			Script script = (Script) obj;
			script.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void jsr223() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("groovy");

		engine.put("output", "");
		String tmp = "def f() { 1+1 } " + "\n" + " output = f() + \"\"; ";
		try {
			engine.eval(tmp);
		} catch (javax.script.ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String output = (String) engine.get("output");
		System.out.println(output);

		// System.out.println(Class.forName("Script2").getClassLoader());
	}
}
