package pl.edu.agh.evolutus.utils;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public interface FromScriptObjectInstantiable<T> {

	T fromScriptObject(ScriptObjectMirror scriptObjectMirror);
}
