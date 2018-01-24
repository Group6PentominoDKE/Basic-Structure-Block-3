/**
 * Creates an object that is a representation of a cargo space
 * @author Christie Courtnage
 *
 */
public class Filled {

	private String[][][] filled;
	
	/**
	 * Constructor
	 * @param x width 
	 * @param y length
	 * @param z height
	 */
	public Filled(int x, int y, int z){
		filled = new String[z+1][y+1][x+1];
		for (int z1 = 0 ; z1 < z+1; z1++)
			for (int y1 = 0 ; y1 < y+1; y1++)
				for (int x1 = 0 ; x1 < x+1; x1++) {
					filled[z1][y1][x1] = "0";
				}
	}
	
	/**
	 * Constructor
	 * @param fill an already filled String array 
	 */
	public Filled(String[][][] fill) {
		filled = fill;
	}
	
	/**
	 * Accessor method
	 * @return string representation of the cargo space
	 */
	public String[][][] getFilled(){
		return filled;
	}
}
