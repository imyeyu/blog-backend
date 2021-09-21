package net.imyeyu.blogapi.handler;

import net.imyeyu.blogapi.bean.ImageRenderingType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 图像缩放渲染算法枚举映射
 *
 * <p>夜雨 创建于 2021-09-20 11:52
 */
public class ImageRenderingTypeHandler extends BaseTypeHandler<ImageRenderingType> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, ImageRenderingType parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.toString());
	}

	@Override
	public ImageRenderingType getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String type = rs.getString(columnName);
		if (rs.wasNull() || type == null) {
			return null;
		} else {
			return ImageRenderingType.valueOf(type);
		}
	}

	@Override
	public ImageRenderingType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String type = rs.getString(columnIndex);
		if (rs.wasNull() || type == null) {
			return null;
		} else {
			return ImageRenderingType.valueOf(type);
		}
	}

	@Override
	public ImageRenderingType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String type = cs.getString(columnIndex);
		if (cs.wasNull() || type == null) {
			return null;
		} else {
			return ImageRenderingType.valueOf(type);
		}
	}
}