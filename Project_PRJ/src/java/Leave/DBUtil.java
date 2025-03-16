/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Leave;

/**
 *
 * @author LTSon
 */
public class DBUtil {
    private static final UserDBContext userDB = new UserDBContext();
    private static final LeaveDBContext leaveDB = new LeaveDBContext();

    public static UserDBContext getUserDB() {
        return userDB;
    }

    public static LeaveDBContext getLeaveDB() {
        return leaveDB;
    }
}
