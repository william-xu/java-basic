package com.xwl41.common.basic.util;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 核心工具类，用来保存常用的工具方法
 * <p>
 * 1）将常用方法放在核心类，是为了简化调用，如果一个类有无数方法，那么有时连选择都要选择很久，实际上使用频率最高的方法无非就那几个
 * 2）使用内部类，主要是为了对同种类型的工具方法进行分类，也是为了更容易查看选择
 */
public abstract class CoreUtil {

    /**
     * 日期时间相关工具类
     */
    public static class DATETIME {
        public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";  //默认日期格式
        public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; //默认日期时间格式
        public static final String DEFAULT_DATE_FORMAT_COMPACT = "yyyyMMdd";    //默认日期紧凑格式
        public static final String DEFAULT_DATETIME_FORMAT_COMPACT = "yyyyMMddHHmmss";  //默认日期时间紧凑格式
        public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);

        //中国时区ZoneOffset对象
        public static final ZoneOffset ZONE_OFFSET_8 = ZoneOffset.ofHours(8);

        ////////////////////////////////////////////////////////////////////////////
        //  时间、日期时间转字符串（格式化）
        ////////////////////////////////////////////////////////////////////////////

        public static String toString(Date date) {
            return new SimpleDateFormat(DEFAULT_DATETIME_FORMAT).format(date);
        }

        public static String toString(Date date, String datetimeFmt) {
            return new SimpleDateFormat(datetimeFmt).format(date);
        }

        public static String toString(LocalDateTime localDateTime) {
            return localDateTime.format(DEFAULT_DATETIME_FORMATTER);
        }

        public static String toString(LocalDateTime localDateTime, String datetimeFmt) {
            return localDateTime.format(DateTimeFormatter.ofPattern(datetimeFmt));
        }

        public static String toCompactDateStr(LocalDateTime localDateTime) {
            return toString(localDateTime, DEFAULT_DATE_FORMAT_COMPACT);
        }

        public static String toCompactDateTimeStr(LocalDateTime localDateTime) {
            return toString(localDateTime, DEFAULT_DATETIME_FORMAT_COMPACT);
        }

        ////////////////////////////////////////////////////////////////////////////
        //  字符串值转日期、日期时间对象
        ////////////////////////////////////////////////////////////////////////////
        public static Date toDate(LocalDateTime localDateTime) {
            return new Date(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
        }

        public static LocalDate toLocalDate(String dateStr) {
            return LocalDate.parse(dateStr, DEFAULT_DATE_FORMATTER);
        }

        public static LocalDateTime toLocalDateTime(String dateTimeStr) {
            return LocalDateTime.parse(dateTimeStr, DEFAULT_DATETIME_FORMATTER);
        }

        public static LocalDateTime dateStrToLocalDateTime(String dateStr) {
            return LocalDateTime.of(toLocalDate(dateStr), LocalTime.MIN);
        }


        ////////////////////////////////////////////////////////////////////////////
        //  日期、日期时间获取和日期、日期时间互相转换（不同时区）
        ////////////////////////////////////////////////////////////////////////////
        public static LocalDateTime utcNow() {
            return LocalDateTime.now(ZoneOffset.UTC);
        }

        public static LocalDateTime utcToCn(LocalDateTime utcDateTime) {
            return LocalDateTime.ofInstant(utcDateTime.toInstant(ZoneOffset.UTC), ZONE_OFFSET_8);
        }

        public static LocalDateTime cnToUtc(LocalDateTime cnDateTime) {
            return LocalDateTime.ofInstant(cnDateTime.toInstant(ZONE_OFFSET_8), ZoneOffset.UTC);
        }

    }

    /**
     * 数值相关工具类
     */
    public static class NUMERIC {
        ////////////////////////////////////////////////////////////////////////////
        //  BigDecimal常用操作
        ////////////////////////////////////////////////////////////////////////////

        public static final int DEFAULT_SCALE = 4;
        public static final int DEFAULT_SCALE_SHORT = 2;

        public static BigDecimal toDecimal(double value) {
            return new BigDecimal(String.valueOf(value));
        }

        public static BigDecimal toDecimal(float value) {
            return new BigDecimal(String.valueOf(value));
        }

        //null转为0的处理
        public static BigDecimal nullToZero(BigDecimal value) {
            return value == null ? new BigDecimal("0") : value;
        }

        public static BigDecimal divide(BigDecimal bd1, BigDecimal bd2) {
            return bd1.divide(bd2, DEFAULT_SCALE, RoundingMode.HALF_UP);
        }

        public static BigDecimal divide(BigDecimal bd1, BigDecimal bd2, int scale) {
            return bd1.divide(bd2, scale, RoundingMode.HALF_UP);
        }

        public static BigDecimal divide(BigDecimal bd1, BigDecimal bd2, RoundingMode roundingMode) {
            return bd1.divide(bd2, DEFAULT_SCALE, roundingMode);
        }

    }

    /**
     * 文件相关工具类
     */
    public static class FILE {
        /**
         * get text content from file that exists in classpath
         * 从类路径读取文件文本内容
         *
         * @param path where file located under classpath, path with filename
         * @return file content in text
         */
        public static String getTextFromResource(String path) {
            //先尝试读取类路径
            try (InputStream in = FILE.class.getResourceAsStream(path)) {
                if (in != null) {
                    try (BufferedInputStream bis = new BufferedInputStream(in)) {
                        return new String(toByteArray(bis));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * get text content from exists file in specified path
         * 从指定路径读取文件文本内容
         *
         * @param path where file located, path with filename
         * @return file content in text
         */
        public static String getTextFromFile(String path) {
            File f = new File(path);
            if (!f.exists()) {
                return null;
            }
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {
                return new String(toByteArray(bis));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        /**
         * read bytes from InputStream to a byte array
         * 将输入流转换为字节数组
         *
         * @param in input stream
         * @return byte array
         * @throws IOException reading exception
         */
        private static byte[] toByteArray(InputStream in) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        }

        /**
         * 将输入流保存到指定文件中
         *
         * @param inputStream 输入流
         * @param fileName    要保存的目标文件
         */
        public static void saveToFile(InputStream inputStream, String fileName) {
            OutputStream os = null;
            try {
                byte[] bs = new byte[1024];
                int len;
                File tempFile = new File(fileName);
                if (tempFile.exists() || tempFile.createNewFile()) {
                    os = new FileOutputStream(tempFile);
                    while ((len = inputStream.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //  其他工具类引用和扩展
    ////////////////////////////////////////////////////////////////////////////
    public static class STRING extends StringUtil {
    }

    public static class COLLECTION extends CollectionUtil {
    }

    public static class MAP extends MapUtil {
    }

    public static class REFLECT extends ReflectUtil {
    }

    ////////////////////////////////////////////////////////////////////////////
    //  常用工具类方法 -- 是否为空
    ////////////////////////////////////////////////////////////////////////////

    public static boolean isEmpty(String str) {
        return STRING.isEmpty(str);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return COLLECTION.isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return MAP.isEmpty(map);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

}
