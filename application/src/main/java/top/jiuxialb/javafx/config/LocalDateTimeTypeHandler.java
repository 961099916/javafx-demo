package top.jiuxialb.javafx.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@MappedTypes(LocalDateTime.class)
@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, Timestamp.valueOf(parameter));
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseTimestamp(rs.getObject(columnName));
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseTimestamp(rs.getObject(columnIndex));
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseTimestamp(cs.getObject(columnIndex));
    }

    private LocalDateTime parseTimestamp(Object timestampObj) {
        if (timestampObj == null) {
            return null;
        }

        try {
            if (timestampObj instanceof Timestamp) {
                return ((Timestamp) timestampObj).toLocalDateTime();
            } else if (timestampObj instanceof String) {
                String timestampStr = (String) timestampObj;
                // 移除毫秒部分（如果存在）
                if (timestampStr.contains(".")) {
                    timestampStr = timestampStr.substring(0, timestampStr.indexOf("."));
                }
                // 尝试解析
                return LocalDateTime.parse(timestampStr, FORMATTER);
            } else if (timestampObj instanceof Date) {
                return ((Date) timestampObj).toLocalDate().atStartOfDay();
            }
        } catch (DateTimeParseException | ClassCastException e) {
            // 如果解析失败，尝试其他格式
            try {
                if (timestampObj instanceof String) {
                    String timestampStr = (String) timestampObj;
                    // 尝试ISO格式
                    return LocalDateTime.parse(timestampStr);
                }
            } catch (Exception ex) {
                // 忽略异常，返回null
            }
        }
        return null;
    }
}