import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingDataAggregate {

    private final static Logger logger = LoggerFactory.getLogger(ParkingDataAggregate.class);

    private static final String FROM_DATE_FORMAT = "MM/dd/yyyy hh:mm:ss aa";
    private static final String TO_DATE_FORMAT = "MM/dd HH:mm";

    public void run(String fileName) throws IOException, ParseException {
        Map<String, ParkingStatBean> map = new ConcurrentHashMap<>();
        logger.info("START: {}", fileName);
        File file = new File(fileName);
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");

        FileWriter writer_lot = new FileWriter(fileName + "_10min.csv");
        BufferedWriter bw_lot = new BufferedWriter(writer_lot);

        int cnt = 0;
        int batch_size = 1000000;

        while (it.hasNext()) {
            String line = it.nextLine();

            if (cnt == 0) {
                bw_lot.write(line);
                bw_lot.newLine();
                bw_lot.flush();
                ++cnt;
                continue;
            }

            try {
                String[] elements = line.split(",");

                String sourceElementKey = elements[0];
                String occupancyDateTime = elements[1];
                Integer paidOccupancy = Integer.parseInt(elements[2]);
                String parkingSpaceCount = elements[3];
                String parkingTimeLimitCategory;
                try {
                    parkingTimeLimitCategory = elements[4];
                } catch (RuntimeException e) {
                    parkingTimeLimitCategory = "";
                }

                occupancyDateTime = convertDateTime(occupancyDateTime);

                ParkingStatBean bean = new ParkingStatBean();
                 bean.sourceElementKey(sourceElementKey)
                        .occupancyDateTime(occupancyDateTime)
                        //.paidOccupancy(paidOccupancy)
                        .parkingSpaceCount(parkingSpaceCount)
                        .parkingTimeLimitCategory(parkingTimeLimitCategory);

                String key = bean.getKey();

                ParkingStatBean ori;
                if (map.containsKey(key)) {
                    ori = map.get(key);
                } else {
                    ori = new ParkingStatBean();
                    ori.sum(0).count(0);
                }

                int sum = ori.getSum() + paidOccupancy;
                int count = ori.getCount() + 1;

                if (count == 10) {
                    // write outputfile
                    String data = String.join(",",
                            sourceElementKey, occupancyDateTime, Float.toString((float) sum / count), parkingSpaceCount,
                            parkingTimeLimitCategory);

                    bw_lot.write(data);
                    bw_lot.newLine();

                    map.remove(key);
                } else {
                    bean.sum(sum).count(count);
                    map.put(key, bean);
                }


                if (cnt % batch_size == 0) {
                    logger.info("Count: {}", cnt);
                }

                ++cnt;
            } catch (RuntimeException e) {
                System.out.println(line);
            }
        }

        for (ParkingStatBean bean : map.values()) {
            // write outputfile
            String data = String.join(",",
                    bean.getSourceElementKey(), bean.getOccupancyDateTime(), Float.toString((float) bean.getSum() / bean.getCount()),
                    bean.getParkingSpaceCount(),
                    bean.getParkingTimeLimitCategory());

            bw_lot.write(data);
            bw_lot.newLine();
        }
        bw_lot.close();
    }


    /**
     * convert datetime string.
     * <p>
     * ex) 09/03/2019 04:50:00 PM -> 09/03 16:50
     *
     * @param fromDate original datetime string
     * @return converted datetime string
     * @throws ParseException if fromDate is exceptional string
     */
    public static String convertDateTime(String fromDate) throws ParseException {
        Date date = DateUtils.parseDate(fromDate, FROM_DATE_FORMAT);

        int minute = date.getMinutes();
        minute = minute / 10 * 10;
        date.setMinutes(minute);

        DateFormat formatter = new SimpleDateFormat(TO_DATE_FORMAT);

        return formatter.format(date);
    }
}

