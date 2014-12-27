package pl.edu.agh.evolutus.environment;

import pl.edu.agh.evolutus.foram.IForam;

public interface IOceanFragment {

	IEnvironmentInfo getIEnvironmentInfo();

	void createForam(double energy);

	void addForam(IForam foram);

	void removeForam(IForam foram);

	int takeAlgae(int demand);

}
