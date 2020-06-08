import DAO.BaseDAO;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        List<Object[]> flight1,flight2;
        Object[] flight3;
        flight1 = BaseDAO.getAllFlight();
        flight2 = BaseDAO.getFlightByRoute("上海","长沙","20200531","fare","asc");
        flight3 = BaseDAO.getFlightById(1);
        for(Object o[]:flight1) {
            System.out.println(Arrays.toString(o));
        }
        for(Object o[]:flight2) {
            System.out.println(Arrays.toString(o));
        }
        System.out.println(Arrays.toString(flight3));

        System.out.println(BaseDAO.login("admin","admin"));
        System.out.println(BaseDAO.login("admin","admin1"));

        BaseDAO.bookFlight(1);

        BaseDAO.createPayment(123456789,1, (float) 1999.99);
        if(BaseDAO.payMoney(8,123456789))
            BaseDAO.paySuccess(8,123456789);
        else
            System.out.println("支付失败");
        System.out.println(BaseDAO.verifyOrder(8,123456789));


    }
}
