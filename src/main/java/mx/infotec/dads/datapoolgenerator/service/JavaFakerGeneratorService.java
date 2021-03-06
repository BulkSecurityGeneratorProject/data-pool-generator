package mx.infotec.dads.datapoolgenerator.service;

import java.util.List;

import mx.infotec.dads.datapoolgenerator.domain.DataType;

/**
 * The interface for the JavaFakerGeneratorService
 * @author Roberto Villarejo Martínez
 *
 */
public interface JavaFakerGeneratorService {

	public String generate(DataType dataType);
	
	public List<String> generate(DataType dataType, int n);
	
	public List<List<String>> generate(List<DataType> dataTypes, int n);
	
}
