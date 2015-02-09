package pl.edu.agh.evolutus.script;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import pl.edu.agh.evolutus.utils.Vector;

public class Script {

	public static void main(String[] args) {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

		scriptEngine.put("a", 43235.432);
		scriptEngine.put("b", 1000);
		scriptEngine.put("c", new Vector(1, 2, 3));

		try {
			String script = "" +
					"function foramsInitialCount_(x,y,z){" +
					"  return 101 - z" +
					"}" +
					"" +
					"function config(x,y,z){\n" +
					"  return { foramsInitialCount: foramsInitialCount_(x,y,z) }\n" +
					"}";
			CompiledScript compiledScript = ((Compilable) scriptEngine).compile(script);
			ScriptObjectMirror function = (ScriptObjectMirror) compiledScript.eval();
			ScriptObjectMirror function2 = (ScriptObjectMirror) scriptEngine.eval(script);

			System.out.println(function2.hasMember("config"));
			System.out.println(function2.hasMember("foramsInitialCount"));
			try {
				System.out.println(((Invocable) scriptEngine).invokeFunction("foramsInitialCount_", 1, 2, 3));
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			double d = 0.0;
			long beg = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
				d += (double) ((ScriptObjectMirror) function2.call(null, 1, 2, i)).get("foramsInitialCount");
			}
			System.out.println(d);
			System.out.println(System.currentTimeMillis() - beg);

			d = 0.0;
			beg = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
				d += (double) ((ScriptObjectMirror) function.call(null, 1, 2, i)).get("foramsInitialCount");
			}
			System.out.println(d);
			System.out.println(System.currentTimeMillis() - beg);

			scriptEngine.eval("print(a);print(b); print(c); print(c.x);");
			Object c = scriptEngine.get("c");
			System.out.println(c);
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		for (ScriptEngineFactory f : scriptEngineManager.getEngineFactories()) {
			System.out.println(f.getEngineName());
		}
	}

}
