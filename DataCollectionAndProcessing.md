## Data Collection

### Table: 1. Data Set

| Data                        | Rows        | Size | Source       |
|-----------------------------|-------------|------|--------------|
| 2018 Paid Parking Occupancy | 289,433,487 | 40GB | [Link][2018] |
| 2019 Paid Parking Occupancy | 286,106,444 | 40GB | [Link][2019] |
| 2020 Paid Parking Occupancy | 153,006,451 | 22GB | [Link][2020] |


[2018]: https://data.seattle.gov/Transportation/2018-Paid-Parking-Occupancy-Year-to-date-/6yaw-2m8q
[2019]: https://data.seattle.gov/Transportation/2019-Paid-Parking-Occupancy-Year-to-date-/qktt-2bsy
[2020]: https://data.seattle.gov/Transportation/2020-Paid-Parking-Occupancy-Year-to-date-/wtpb-jp8d


### Table: 2. Attributes

| No  | Column                       | Description                                                                      | Type     |
|-----|------------------------------|----------------------------------------------------------------------------------|----------|
| 1   | OccupancyDateTime            | The date and time (minute) of the transaction as recorded.                       | DateTime |
| 2   | PaidOccupancy                | The numerator of the paid occupancy percentage calculation.                      | Number   |
| 3   | BlockfaceName                | Street segment, name of street.                                                  | Text     |
| 4   | SideOfStreet                 | Options: E, S, N, W, NE, SW, SE, NW.                                             | Text     |
| 5   | SourceElementKey             | Unique identifier for the city street segment where the pay station is located.  | Number   |
| 6   | ParkingTimeLimitCategory     | In minutes. For example, 120 (2-hour parking).                                   | Number   |
| 7   | ParkingSpaceCount            | Number of paid spaces on the blockface at the given date and time.               | Number   |
| 8   | PaidParkingArea              | Primary name of a paid parking neighborhood.                                     | Text     |
| 9   | PaidParkingSubArea           | Subset of a paid parking area.                                                   | Text     |
| 10  | PaidParkingRate              | Parking rate charged at date and time.                                           | Number   |
| 11  | ParkingCategory              | An overall description of the type of parking allowed on a blockface.            | Text     |
| 12  | Point (Latitude, Longitude)  | Location of a pay station along the same blockface.                              | Text     |


## Data Processing


### Table: 3. Parking Occupancy Statistics

| No  | Column                       | Comment                  |
|-----|------------------------------|--------------------------|
| 5   | SourceElementKey             | Primary Key, Foreign key |
| 1   | OccupancyDateTime            | Primary Key              |
| 2   | PaidOccupancy                |                          |
| 6   | ParkingTimeLimitCategory     |                          |
| 7   | ParkingSpaceCount            |                          |


### Table: 4. Parking Lot Information

| No  | Column                   | Comment                    |
|-----|--------------------------|----------------------------|
| 5   | SourceElementKey         | Primary key                |
| 3   | BlockfaceName            |                            |
| 4   | SideOfStreet             |                            |
| 6   | ParkingTimeLimitCategory |                            |
| 7   | ParkingSpaceCount        |                            |
| 8   | PaidParkingArea          |                            |
| 9   | PaidParkingSubArea       |                            |
| 10  | PaidParkingRate          |                            |
| 11  | ParkingCategory          |                            |
| 12  | Longitude                | Parsing from `Point` value |
| 12  | Latitude                 | Parsing from `Point` value |


### Table: 5. Size Comparison Before and After Processing

| Data              | Size (Before) | Size (After) |
|-------------------|---------------|--------------|
| 2018 Paid Parking | 40 GB         | 10.0 GB      |
| 2019 Paid Parking | 40 GB         |  9.8 GB      |
| 2020 Paid Parking | 22 GB         |  5.4 GB      |


### Table: 6. Aggregated data in 10-minutes 

| Data                       | Size   | Row        |
|----------------------------|--------|------------|
| 2018 Paid Parking (10-Min) | 778 MB | 28,958,233 |
| 2019 Paid Parking (10-Min) | 769 MB | 28,634,647 |
| 2020 Paid Parking (10-Min) | 426 MB | 15,311,841 |




### Source Code

 - [Data Splitor using Python](parking_data_split.py)
   > At first, I programmed in Python, but it would take more than 6 days to process each year data.
 
 - [Data Splitor using Java](ParkingDataSplit.java)
   > It took about 30 minutes to process each year data using Java program.

 - [Aggregator in 10 minutes using Java](ParkingDataAggregate.java)
 
