import csv
import pandas as pd
from datetime import datetime

# Customize settings
filePath = ''
year = 2019
batch_size = 2000

# column names
columns_dict = {0: 'OccupancyDateTime', 1: 'PaidOccupancy', 2: 'BlockfaceName', 3: 'SideOfStreet',
                4: "SourceElementKey", 5: 'ParkingTimeLimitCategory', 6: 'ParkingSpaceCount', 7: 'PaidParkingArea',
                8: 'PaidParkingSubArea', 9: 'PaidParkingRate', 10: 'ParkingCategory', 11: 'Location'}

parking_stat_key = ['OccupancyDateTime', 'PaidOccupancy', "SourceElementKey"]
parking_lot_key = ['SourceElementKey', 'BlockfaceName', 'SideOfStreet', 'ParkingTimeLimitCategory',
                   'ParkingSpaceCount', 'PaidParkingArea', 'PaidParkingSubArea', 'PaidParkingRate',
                   'ParkingCategory', 'Location']

source_element_list = []

now = datetime.now()
current_time = now.strftime("%H:%M:%S")
print("Start Time =", current_time)

df_stat = pd.DataFrame(columns=parking_stat_key)
df_lot = pd.DataFrame(columns=parking_lot_key)

df_stat.to_csv(str(year) + '_parking_stat.csv', mode='w', index=False, header=True)
df_lot.to_csv(str(year) + '_parking_lot.csv', mode='w', index=False, header=True)
count = 0

with open(filePath + '/' + str(year) + '_Paid_Parking_Occupancy__Year-to-date_.csv', 'r') as csvfile:
    reader = csv.reader(csvfile)
    header = next(reader)  # skip header

    for row in reader:
        count += 1

        # 1. split dataframe
        df_stat.loc[count % batch_size] = [row[0], row[1], row[4]]

        source_element_key = row[4]
        if source_element_key not in source_element_list:
            source_element_list.append(source_element_key)
            df_lot.loc[len(df_lot.index)] = [row[4], row[2], row[3], row[5],
                                             row[6], row[7], row[8], row[9],
                                             row[10], row[11]]

        if count % batch_size == 0:
            now = datetime.now()
            current_time = now.strftime("%H:%M:%S")
            print(count, "Current Time:", current_time)

            # 2. save csv Data -> include index... (because duplicated...)
            df_stat.to_csv(str(year) + '_parking_stat.csv', mode='a', index=False, header=False)
            df_lot.to_csv(str(year) + '_parking_lot.csv', mode='a', index=False, header=False)

            # 3. Initialize temp dataframe
            df_stat = pd.DataFrame(columns=parking_stat_key)
            df_lot = pd.DataFrame(columns=parking_lot_key)

now = datetime.now()
current_time = now.strftime("%H:%M:%S")
print("End Time =", current_time)
