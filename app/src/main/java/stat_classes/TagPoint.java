package stat_classes;

public class TagPoint {
	private String name;
	private double quantity;

	//this one is avg Point hours after tag occurred.
	private double orig_tot_points;

	/**
	 * adjusted_avg_points is changed once in end of loop
	 */
	private double tot_points = 0;
	
	private int sumCompletes;
	
	
	private double sumBristol;
	private int nrOfBMs;
	private double blueZonesQuant;

	public TagPoint(String name, double quantity) {
		super();
		this.name = name;
		this.quantity = quantity;
	}
	public TagPoint(String name, double quantity, double orig_tot_points) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.orig_tot_points = orig_tot_points;

		//initiate with this value
		this.tot_points =  orig_tot_points;
	}

	private double getSumCompletes() {
		return sumCompletes;
	}

	/**
	 * also used for calculation of portions (for example portions <= 1)score
	 * @return
	 */
	public double getOrigAvgScore() {

		return orig_tot_points/quantity;
	}
	public double getAdjustedAvgScore() {

		return tot_points/quantity;
	}


	public String getName() {
		return name;
	}
	public double getQuantity() {
		return quantity;
	}
	double getOrig_tot_points() {
		return orig_tot_points;
	}
	double getTotPoints() {
		return tot_points;
	}
	void setName(String name) {
		this.name = name;
	}
	void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	void setTot_points(double tot_points) {
		this.tot_points = tot_points;
	}
	public void addTotalPoints(double nr) {
		tot_points += nr;

	}
	
	//changed
	public void addCompleteness(int complete) {
		sumCompletes += complete;

	}
	/**
	 *
	 * @param nr should only be a positive integer.
	 */
	public void addBM(int nr) {
		nrOfBMs+=nr;

	}
	public void addToBristol(double sumBristol2) {
		sumBristol += sumBristol2;

	}
	
	public double getAvgBristol() {
		return sumBristol/nrOfBMs;
	}

	/**this is only valid for portions calculatiosn. Se TagPointPortionsTest
	 *
	 * Calculations is made by storing hours of time after HOURS_COHERENT_TIME_FOR_PORTIONS
	 * (all this time portions must have been under ONE_PORTION)
	 * The following time is the one where hours (quantity) are accumaluted as well as score (tot_points).
	 * Stops when end of chunk or portionsize >  ONE_PORTION
	 *
	 */
	public double getPortionsAvgScore() {
		return tot_points/quantity;
	}
	public double getBlueZonesQuant() {
		return blueZonesQuant;
	}
	public TagPoint addBlueZonesQuant(double nr) {
		blueZonesQuant+=nr;
		return this;
	}
	public double getCompleteAvg() {
		return (double)sumCompletes/(double)nrOfBMs;
	}




}
