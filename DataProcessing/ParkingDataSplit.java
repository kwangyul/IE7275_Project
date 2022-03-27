import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParkingDataSplit {

    private final static Logger logger = LoggerFactory.getLogger(ParkingDataSplit.class);

    private final static Pattern p = Pattern.compile("(\\d+\\.?\\d+)");

    private final List<String> parking_lot_list = new ArrayList<>();

    private final Map<String, BufferedWriter> outFiles = new ConcurrentHashMap<>();

    public void run(String dataPath, int year) throws IOException {
        // The orginal dataset
        File file = new File(dataPath + year + "_Paid_Parking_Occupancy__Year-to-date_.csv");
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");

        // Output files: 1) Parking Stat, 2) Parking Lot
        FileWriter writer_lot = new FileWriter(year + "_ParkingLot.csv");
        BufferedWriter bw_lot = new BufferedWriter(writer_lot);

        int cnt = 0;
        int batch_size = 1000000;
        String header = "";
        try {
            while (it.hasNext()) {

                String line = it.nextLine();
                String[] elements = line.split(",");

                String sourceElementKey = elements[4];
                String parkingTimeLimitCategory = elements[5];
                String parkingSpaceCount = elements[6];
                String paidParkingArea = elements[7];

                if (!parking_lot_list.contains(sourceElementKey)) {
                    List<String> point = getPoint(elements[11]);

                    String longitude = cnt == 0 ? "Longitude" : point.get(0);
                    String latitude = cnt == 0 ? "Latitude" : point.get(1);

                    parking_lot_list.add(sourceElementKey);

                    String lot = String.join(",",
                            sourceElementKey, elements[2], elements[3], parkingTimeLimitCategory,
                            parkingSpaceCount, elements[7], elements[8], elements[9],
                            elements[10], longitude, latitude);

                    bw_lot.write(lot);
                    bw_lot.newLine();

                    if (cnt != 0 && !outFiles.containsKey(paidParkingArea)) {
                        String outFileName = year + "_ParkingStat_" + paidParkingArea + ".csv";
                        outFileName = outFileName.replace('/', '_');
                        outFileName = outFileName.replace(" ", "");
                        FileWriter writer_stat = new FileWriter(outFileName);
                        BufferedWriter bw_stat = new BufferedWriter(writer_stat);
                        bw_stat.write(header);
                        bw_stat.newLine();

                        outFiles.put(paidParkingArea, bw_stat);
                    }
                }
                String stat = String.join(",", sourceElementKey, elements[0], elements[1],
                        parkingSpaceCount, parkingTimeLimitCategory);

                if (cnt == 0) {
                    header = stat;
                } else {
                    outFiles.get(paidParkingArea).write(stat);
                    outFiles.get(paidParkingArea).newLine();
                }

                if (cnt % batch_size == 0) {
                    logger.info("Count: {}", cnt);
                }
                ++cnt;
            }
        } finally {
            IOUtils.closeQuietly(bw_lot);
            for (BufferedWriter bw_stat : outFiles.values()) {
                IOUtils.closeQuietly(bw_stat);
            }

            LineIterator.closeQuietly(it);
        }

        logger.info("Total Count: {}", cnt);
    }


    private List<String> getPoint(String location) {
        List<String> result = new ArrayList<>();
        Matcher matcher = p.matcher(location);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
}
