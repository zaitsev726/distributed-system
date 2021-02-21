package dao.impl;

import dao.TagDao;
import database.DBUtil;
import model.entity.NodeEntity;
import model.entity.TagEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDaoImpl implements TagDao {

    private static final String SQL_INSERT = "" +
            "insert into tags(node_id, key, value) " +
            "values (?, ?, ?)";

    private static void prepareStatement(PreparedStatement statement, TagEntity tag) throws SQLException {
        statement.setLong(1, tag.getNodeId());
        statement.setString(2, tag.getKey());
        statement.setString(3, tag.getValue());
    }

    @Override
    public void insertTag(TagEntity tag) throws SQLException {
        Connection connection = DBUtil.getConnection();
        Statement statement = connection.createStatement();
        String sql = "insert into tags(node_id, key, value) " +
                "values (" + tag.getNodeId() + ", '" + tag.getKey() +
                "', '" + tag.getValue().replaceAll("'", "''") + "')";
        statement.execute(sql);
    }

    @Override
    public void insertPreparedTag(TagEntity tag) throws SQLException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
        prepareStatement(statement, tag);
        statement.execute();
    }

    @Override
    public void batchInsertTags(List<TagEntity> tags) throws SQLException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
        for (TagEntity tag : tags) {
            prepareStatement(statement, tag);
            statement.addBatch();
        }
        statement.executeBatch();
    }

}
