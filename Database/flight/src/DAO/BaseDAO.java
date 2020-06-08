package DAO;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

// import connectionUtil.getConnection;

public class BaseDAO {

    public static void bookFlight(int flightId) {// 按路线查找航班信息
        Connection conn = null;
        try {
            conn = connectionUtil.getConnection();
            String sql = "update flight set remaining = remaining - 1 where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, flightId);
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionUtil.closeAll(null, conn);
        }
    }

    //filter取值必须为 departure_time或fare或remaining
    public static List<Object[]> getFlightByRoute(String departureCity,String destinationCity,String departureDate,String filter,String order) {// 按路线查找航班信息
        Connection conn = null;
        List<Object[]> list = new LinkedList<>();
        try {
            conn = connectionUtil.getConnection();
            String sql;
            if(order.equals("order")) {
                sql = "select * from flight where departure_city = ? and destination_city = ? and DATE_FORMAT(departure_time,'%Y%m%d') = ? order by ? asc";
            }else{
                sql = "select * from flight where departure_city = ? and destination_city = ? and DATE_FORMAT(departure_time,'%Y%m%d') = ? order by ? desc";
            }

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, departureCity);
            preparedStatement.setString(2, destinationCity);
            preparedStatement.setString(3, departureDate);
            preparedStatement.setString(4, filter);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Object[] objects = new Object[]{
                        "id:",resultSet.getInt("id"),
                        "number:",resultSet.getString("number"),
                        "departurePort:",resultSet.getString("departure_port"),
                        "departureCity:",resultSet.getString("departure_city"),
                        "destinationPort:",resultSet.getString("destination_port"),
                        "destinationCity:",resultSet.getString("destination_city"),
                        "destinationCity:",resultSet.getString("destination_city"),
                        "destinationCity:",resultSet.getString("destination_city"),
                        "airline:",resultSet.getInt("airline"),
                        "departureTime:",resultSet.getDate("departure_time"),
                        "fare:",resultSet.getFloat("fare"),
                        "remaining:",resultSet.getInt("remaining"),
                };
                list.add(objects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionUtil.closeAll(null, conn);
        }
        return list;
    }

    public static Object[] getFlightById(int flightId) {// 按路线查找航班信息
        Connection conn = null;
        Object[] object = new Object[0];
        try {
            conn = connectionUtil.getConnection();
            String sql = "select * from flight where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                        Object[] objects = new Object[]{
                        "id:",resultSet.getInt("id"),
                        "number:",resultSet.getString("number"),
                        "departurePort:",resultSet.getString("departure_port"),
                        "departureCity:",resultSet.getString("departure_city"),
                        "destinationPort:",resultSet.getString("destination_port"),
                        "destinationCity:",resultSet.getString("destination_city"),
                        "destinationCity:",resultSet.getString("destination_city"),
                        "destinationCity:",resultSet.getString("destination_city"),
                        "airline:",resultSet.getInt("airline"),
                        "departureTime:",resultSet.getDate("departure_time"),
                        "fare:",resultSet.getFloat("fare"),
                        "remaining:",resultSet.getInt("remaining"),
                        };
             object = objects;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionUtil.closeAll(null, conn);
        }
        return object;
    }

    public static List<Object[]> getAllFlight() {// 获取全部航班信息

        Connection conn = null;
        List<Object[]> list = new LinkedList<>();
        //String[] list1 = new String[100];
        try {
            conn = connectionUtil.getConnection();
            String sql = "select * from flight";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Object[] objects = new Object[]{
                        "id:",resultSet.getInt("id"),
                        "number:",resultSet.getString("number"),
                        "departurePort:",resultSet.getString("departure_port"),
                        "departureCity:",resultSet.getString("departure_city"),
                        "destinationPort:",resultSet.getString("destination_port"),
                        "destinationCity:",resultSet.getString("destination_city"),
                        "destinationCity:",resultSet.getString("destination_city"),
                        "destinationCity:",resultSet.getString("destination_city"),
                        "airline:",resultSet.getInt("airline"),
                        "departureTime:",resultSet.getDate("departure_time"),
                        "fare:",resultSet.getFloat("fare"),
                        "remaining:",resultSet.getInt("remaining"),
                };
                list.add(objects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionUtil.closeAll(null, conn);
        }
        return list;
    }


    public static boolean login(String username, String password) {// 登录
        Connection conn = null;
        String user_password = null;
        try {
            conn = connectionUtil.getConnection();
            String sql = "select password from user where username= ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user_password = resultSet.getString("password");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            connectionUtil.closeAll(null, conn);
        }
        return password.equals(user_password);
    }

    public static void createPayment(int userId, int flightId, float flightPrice) {// 登录
        Connection conn = null;
        try {
            conn = connectionUtil.getConnection();
            String sql = "insert into `order`(user_id, flight_id, order_price) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,flightId);
            preparedStatement.setFloat(3,flightPrice);
            preparedStatement.execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            connectionUtil.closeAll(null, conn);
        }
    }

    public static float getUserMoney(int userId) {// 查余额
        Connection conn = null;
        float money = 0;
        try {
            conn = connectionUtil.getConnection();
            String sql = "select remaing from user where id= ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                money = resultSet.getFloat("remaing");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            connectionUtil.closeAll(null, conn);
        }
        return money;
    }

    public static float getOrderPrice(int orderId,int userId) {// 查余额
        Connection conn = null;
        float price = 0;
        try {
            conn = connectionUtil.getConnection();
            String sql = "select order_price from `order` where id= ? and user_id= ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,orderId);
            preparedStatement.setInt(2,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                price = resultSet.getFloat("order_price");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            connectionUtil.closeAll(null, conn);
        }
        return price;
    }

    public static boolean payMoney(int orderId, int userId) {// 取钱
        Connection conn = null;
        float money_after = 0;
        float order_money = getOrderPrice(orderId,userId);
        float money_before = getUserMoney(userId);
        money_after = money_before - order_money;
        if (money_after < 0) {
            // 需要判断
            return false;
        }else {
            try {
                conn = connectionUtil.getConnection();
                String sql = "update user set remaing = ? where id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setFloat(1,money_after);
                preparedStatement.setInt(2,userId);
                preparedStatement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connectionUtil.closeAll(null, conn);
            }
        }
        return true;
    }

    public static void paySuccess(int orderId, int userId) {// 取钱
        Connection conn = null;
        try {
            conn = connectionUtil.getConnection();
            String sql = "update `order` set is_paid = 1 where id = ? and user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setFloat(1,orderId);
            preparedStatement.setInt(2,userId);
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionUtil.closeAll(null, conn);
        }
    }

    public static boolean verifyOrder(int orderId, int userId) {// 取钱
        Connection conn = null;
        int isPaid = 0;
        try {
            conn = connectionUtil.getConnection();
            String sql = "select is_paid from `order` where id = ? and user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setFloat(1,orderId);
            preparedStatement.setInt(2,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isPaid = resultSet.getInt("is_paid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionUtil.closeAll(null, conn);
        }
        return isPaid == 1;
    }

}
