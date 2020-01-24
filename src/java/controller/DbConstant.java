/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author RTAP4
 */
public interface DbConstant {

    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    public static final String SELECT_USERS = "from User";
    public static final String SELECT_APPROVER = "from Approver";
    public static final String USERS = "users";
    public static final String OFFLINE = "offline";
    public static final String ONLINE = "online";
    public static final String ACTIVE = "active";
    public static final String DESACTIVE = "desactive";
    public static final String USERCATID = "userCatid";
    public static final String SELECT_USERCATEGORY = "from UserCategory";
    public static final String AVAILABLE = "Available";
    public static final String BOOKED = "Booked";
    public static final String SELECT_APPROVERCATEGORY = "from ApproverCategory";
    public static final String SELECT_APPROVERCATEGORYID = "approverCategoryId";
    public static final String USERID = "userId";
    public static final String DEPID = "departementId";
    public static final String SELECT_DEPARTMENT = "from Departement";
    /*Requisition Status*/
    public static final String INPROGRESS = "In Progress";
    public static final String NOTYET = "Not Yet";
    public static final String APPROVED = "Approved";
    public static final String APPROVEDBYDIRECTOR = "ApprovedByDirector";
    public static final String REJECT = "Rejected";
    //Journey

    public static final String DEPARTURE = "departure";
    public static final String RETURN = "return";
   
    public static final String[] USERTYPE = {"admin","customer"};
    public static final Object NOTAPPROVED = "not approved";

}
