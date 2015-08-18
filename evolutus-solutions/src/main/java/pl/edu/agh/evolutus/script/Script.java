package pl.edu.agh.evolutus.script;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import pl.edu.agh.evolutus.utils.VectorL;

public class Script {

	public interface Interface {
		double foramsInitialCount(double x, double y, double z);
	}

	static class InterfaceImpl implements Interface {

		@Override
		public double foramsInitialCount(double x, double y, double z) {
			return 101 - z;
		}
	}

	public static void main(String[] args) {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

		scriptEngine.put("a", 43235.432);
		scriptEngine.put("b", 1000);
		scriptEngine.put("c", new VectorL(1, 2, 3));

		try {
			String script = "" +
					"function foramsInitialCount(x,y,z){" +
					"  return 101 - z" +
					"}";
			CompiledScript compiledScript = ((Compilable) scriptEngine).compile(script);
			ScriptObjectMirror function = (ScriptObjectMirror) scriptEngine.eval(script);
			ScriptObjectMirror compiledFunction = (ScriptObjectMirror) compiledScript.eval();
			Interface interfaceInstance = ((Invocable) scriptEngine).getInterface(Interface.class);
			Interface interfaceImplInstance = new InterfaceImpl();

			System.out.println("\n\n");

			double d = 0.0;
			long beg = System.currentTimeMillis();
			for (int i = 0; i < 10 * 1000 * 1000; i++) {
				d += (double) function.call(null, 1, 2, i);
			}
			System.out.println(d);
			System.out.println(System.currentTimeMillis() - beg);

			d = 0.0;
			beg = System.currentTimeMillis();
			for (int i = 0; i < 10 * 1000 * 1000; i++) {
				d += (double) compiledFunction.call(null, 1, 2, i);
			}
			System.out.println(d);
			System.out.println(System.currentTimeMillis() - beg);

			d = 0.0;
			beg = System.currentTimeMillis();
			for (int i = 0; i < 10 * 1000 * 1000; i++) {
				d += interfaceInstance.foramsInitialCount(1, 2, i);
			}
			System.out.println(d);
			System.out.println(System.currentTimeMillis() - beg);

			d = 0.0;
			beg = System.currentTimeMillis();
			for (int i = 0; i < 10 * 1000 * 1000; i++) {
				d += interfaceImplInstance.foramsInitialCount(1, 2, i);
			}
			System.out.println(d);
			System.out.println(System.currentTimeMillis() - beg);

			System.out.println("\n\n");
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		for (ScriptEngineFactory f : scriptEngineManager.getEngineFactories()) {
			System.out.println(f.getEngineName());
		}
	}

}
