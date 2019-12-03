import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Li Zhen
 * @create 2019/11/20
 * @since 1.0.0
 */

public class CollectedFunctions {

    /**
     * 将秒数格式化成HH:mm:ss的格式
     */
    public static String secToTime(long time) {
        String timeStr = null;
        long hour;
        long minute;
        long second;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(long i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    /**
     * 计算2个时间点的时间间隔，要求计算时间间隔时不计0点-6点的时间段
     */
    public static long calculateHours(){
        LocalDateTime s = null;
        LocalDateTime e = null;
        LocalDateTime startTime = LocalDateTime.of(2019,11,18,3,0,0);
        LocalDateTime endTime = LocalDateTime.of(2019,11,18,4,0,0);

        long l = ChronoUnit.DAYS.between(startTime.toLocalDate(), endTime.toLocalDate());

        /**
         * 开始时间在当天6点之前，起始时间点按6点算
         */
        if (startTime.isBefore(LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(6,0)))){
            s = LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(6,0));
            /**
             *  开始时间在当天6点之后，起始时间点不变
             */
        }else{
            s = startTime;
        }

        /**
         * 结束时间在当天6点之前，起始时间点按0点算
         */
        if (endTime.isBefore(LocalDateTime.of(endTime.toLocalDate(), LocalTime.of(6,0)))){
            e = LocalDateTime.of(endTime.toLocalDate(), LocalTime.of(0,0));
            /**
             *  结束时间在当天6点之后，起始时间点不变
             */
        }else{
            e = endTime;
        }

        /**
         * 时间间隔在同一天内
         */
        if (l<1 && endTime.isBefore(LocalDateTime.of(startTime.toLocalDate().plusDays(1), LocalTime.MIN))){
            if (s.isAfter(e)){
                return 0;
            }else {
                return ChronoUnit.HOURS.between(s, e);
            }
        }
        /**
         * 时间间隔在一天以上
         */
        else if (l>=1 && endTime.isAfter(LocalDateTime.of(startTime.toLocalDate().plusDays(1), LocalTime.MIN))){

            long l3 = 18 * (l - 1);

            if (startTime.isBefore(LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(6,0))) && endTime.isBefore(LocalDateTime.of(endTime.toLocalDate(), LocalTime.of(6,0)))){
                return 18 + l3;
            }else if(startTime.isBefore(LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(6,0))) && endTime.isAfter(LocalDateTime.of(endTime.toLocalDate(), LocalTime.of(6,0)))){
                long l1 = ChronoUnit.HOURS.between(LocalDateTime.of(endTime.toLocalDate(), LocalTime.of(6, 0)), endTime);
                return l1+18 + l3;
            }else if (startTime.isAfter(LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(6,0))) && endTime.isBefore(LocalDateTime.of(endTime.toLocalDate(), LocalTime.of(6,0)))){
                return ChronoUnit.HOURS.between(s, LocalDateTime.of(startTime.toLocalDate().plusDays(1), LocalTime.MIN)) + l3;
            }else {
                long l1 = ChronoUnit.HOURS.between(LocalDateTime.of(endTime.toLocalDate(), LocalTime.of(6, 0)), endTime);
                long l2 = ChronoUnit.HOURS.between(s, LocalDateTime.of(startTime.toLocalDate().plusDays(1), LocalTime.MIN));
                return l1+l2 + l3;
            }
        }
        return 0;
    }

    /**
     * 用二进制来存储标志位，将每一位0/1来标识是否拥有某个标志位，将每一位1的十进制值放入List
     * 即用一个数字来存储它拥有多少选项
     */
    public static void tagGet(Integer warning1){
        List<Integer> warning1s = new ArrayList<>();
        String s = Integer.toBinaryString(warning1);
        for (int i = s.length()-1;i>=0;i--){
            if (1 == Integer.parseInt(s.charAt(s.length()-1-i)+"")){
                warning1s.add((int) Math.pow(2,i));
            }
        }
    }

}