package com.johanlund.stat_backend.point_classes;

public class TagPoint implements PointBase {
    private String name;
    private double quantity;

    //this one is avg Point hours after tag occurred.
    private double orig_tot_points;

    private int jumps = 0;
    /**
     * adjusted_avg_points is changed once in end of loop
     */
    private double tot_points = 0;

    //+ if other tag exist in map of tagpoints that should effect avg score negatively, vice
    // versa for -
    private int plus_minus = 0;

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
        this.tot_points = orig_tot_points;
    }

    public int getNrOfBMs() {
        return nrOfBMs;
    }

    private double getSumCompletes() {
        return sumCompletes;
    }

    private int getJumps() {
        return jumps;
    }

    // parameter can be +1 or -1
    public void addPlusMinus(int i) {
        plus_minus += i;
    }

    /**
     * also used for calculation of portions (for example portions <= 1)score
     *
     * @return
     */
    public double getOrigAvgScore() {

        return orig_tot_points / quantity;
    }

    public double getAdjAvgScore() {

        return tot_points / quantity;
    }


    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getOrig_tot_points() {
        return orig_tot_points;
    }

    double getTotPoints() {
        return tot_points;
    }

    int getPlus_minus() {
        return plus_minus;
    }

    void setTot_points(double tot_points) {
        this.tot_points = tot_points;
    }

    void setPlusMinus(int plus_minus) {
        this.plus_minus = plus_minus;
    }

    public void addTotalPoints(double nr) {
        tot_points += nr;

    }

    public void addNrOfJumps(int nr) {
        jumps += nr;

    }

    public int getJumpsDiff() {
        return jumps;
    }

    /**
     * @param foundJump can be +1 or 1, nothing else.
     */
    public void addJump(int foundJump) {
        if (foundJump != 1 && foundJump != -1) {
            throw new IllegalArgumentException();
        }
        jumps += foundJump;

    }

    //changed
    public void addCompleteness(int complete) {
        sumCompletes += complete;

    }

    /**
     * @param nr should only be a positive integer.
     */
    public void addBM(int nr) {
        nrOfBMs += nr;

    }

    //both bristol and completeness use this
    public void addToBMScore(double sumBristol2) {
        sumBristol += sumBristol2;

    }

    public double getJumpQuotient() {
        return jumps / quantity;
    }

    public double getAvgBristol() {
        return sumBristol / nrOfBMs;
    }

    /**
     * this is only valid for portions calculatiosn. Se TagPointPortionsTest
     * <p>
     * Calculations is made by storing hours of time after HOURS_COHERENT_TIME_FOR_PORTIONS
     * (all this time portions must have been under ONE_PORTION)
     * The following time is the one where hours (duration) are accumaluted as well as score
     * (tot_points).
     * Stops when end of chunk or portionsize >  ONE_PORTION
     */
    public double getPortionsAvgScore() {
        return tot_points / quantity;
    }

    public double getBlueZonesQuant() {
        return blueZonesQuant;
    }

    public TagPoint addBlueZonesQuant(double nr) {
        blueZonesQuant += nr;
        return this;
    }

    public double getCompleteAvg() {
        return (double) sumCompletes / (double) nrOfBMs;
    }


}