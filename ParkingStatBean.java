public class ParkingStatBean {

    private String sourceElementKey;

    private String occupancyDateTime;

    private String paidOccupancy;

    private String parkingSpaceCount;

    private String parkingTimeLimitCategory;

    private Integer sum;

    private Integer count;

    public String getKey() {
        return sourceElementKey + ' ' + occupancyDateTime;
    }

    public String getSourceElementKey() {
        return sourceElementKey;
    }

    public ParkingStatBean sourceElementKey(String sourceElementKey) {
        this.sourceElementKey = sourceElementKey;
        return this;
    }

    public String getOccupancyDateTime() {
        return occupancyDateTime;
    }

    public ParkingStatBean occupancyDateTime(String occupancyDateTime) {
        this.occupancyDateTime = occupancyDateTime;
        return this;
    }

    public String getPaidOccupancy() {
        return paidOccupancy;
    }

    public ParkingStatBean paidOccupancy(String paidOccupancy) {
        this.paidOccupancy = paidOccupancy;
        return this;
    }

    public String getParkingSpaceCount() {
        return parkingSpaceCount;
    }

    public ParkingStatBean parkingSpaceCount(String parkingSpaceCount) {
        this.parkingSpaceCount = parkingSpaceCount;
        return this;
    }

    public String getParkingTimeLimitCategory() {
        return parkingTimeLimitCategory;
    }

    public ParkingStatBean parkingTimeLimitCategory(String parkingTimeLimitCategory) {
        this.parkingTimeLimitCategory = parkingTimeLimitCategory;
        return this;
    }

    public Integer getSum() {
        return sum;
    }

    public ParkingStatBean sum(Integer sum) {
        this.sum = sum;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public ParkingStatBean count(Integer count) {
        this.count = count;
        return this;
    }

}
