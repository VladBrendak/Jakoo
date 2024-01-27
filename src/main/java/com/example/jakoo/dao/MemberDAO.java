package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.Members;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    private final DatabaseConnector connector;

    public MemberDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public List<Members> getAllGroupMembers(String groupId) {
        String sql = "SELECT * FROM Members WHERE employee_group = " + groupId + ";";
        List<Members> membersList = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Members members = new Members();
                members.setMembersId(resultSet.getLong("members_id"));
                members.setEmployeeGroup(resultSet.getLong("employee_group"));
                members.setEmployee(resultSet.getLong("employee"));
                membersList.add(members);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return membersList;
    }

    public void addMember(Members members) {
        String sql = "INSERT INTO Members (employee_group, employee) VALUES (?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, members.getEmployeeGroup());
            preparedStatement.setLong(2, members.getEmployee());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMember(Members members) {
        String sql = "UPDATE Members SET employee_group = ?, employee = ? WHERE members_id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, members.getEmployeeGroup());
            preparedStatement.setLong(2, members.getEmployee());
            preparedStatement.setLong(3, members.getMembersId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMember(Long membersId) {
        String sql = "DELETE FROM Members WHERE members_id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, membersId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMembersByIds(String membersIds) {
        if(membersIds.isBlank() || membersIds.isEmpty()) {
            return;
        }
        String sql = "DELETE FROM Members WHERE employee IN (" + membersIds + ")";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMembersFromGroupByIds(String employeeIds, long employeeGroupId) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return;
        }
        String sql = "DELETE FROM Members WHERE employee IN (" + employeeIds + ") AND employee_group = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, employeeGroupId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addMembersByIds(long groupId, List<Long> employeeIds) {
        if(employeeIds.size() < 1)
        {
            return;
        }
        String sql = "INSERT INTO Members (employee_group, employee) VALUES (?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Long employeeId : employeeIds) {
                preparedStatement.setLong(1, groupId);
                preparedStatement.setLong(2, employeeId);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
