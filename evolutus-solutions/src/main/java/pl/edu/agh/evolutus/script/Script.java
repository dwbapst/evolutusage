package pl.edu.agh.evolutus.script;

import pl.edu.agh.evolutus.environment.Coordinates;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Script {

	public static void main(String[] args) {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

		scriptEngine.put("a", 43235.432);
		scriptEngine.put("b", 1000);
		scriptEngine.put("c", new Coordinates(1, 2, 3));

		try {
			scriptEngine.eval("print(a);print(b); print(c) print(c.x);");
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
