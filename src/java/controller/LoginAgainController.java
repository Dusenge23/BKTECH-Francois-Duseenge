/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mchange.v1.cachedstore.CachedStore.Manager;
import static controller.DbConstant.USERTYPE;

import dao.generic.RoomDao;
import dao.impl.ClientImpl;
import dao.impl.LoginImpl;

import dao.impl.RoomImageImpl;

import dao.impl.RoomsImpl;
import dao.impl.UserImpl;
import domain.Client;



import domain.RoomImage;
import domain.Rooms;
import domain.User;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Edmond
 */
@ManagedBean
@SessionScoped
public class LoginAgainController {

    private User user = new User();
    private String whereFrom = "login";
    private User loggedInUser;
    private boolean isLoggedIn;
    private User changePass = new User();
    private String retypePassword;
    private String recentPassword;
    private String secondPassword;
    private Client loggedInCompany;
    private Client comp = new Client();
    private Rooms room = new Rooms();
    private List<String> image = new ArrayList<>();
    private List<Rooms> nrooms = new RoomsImpl().getAll();
    private List<Client> cust = new ClientImpl().getAll();
    private List<RoomImage> custt = new RoomImageImpl().getAll();
    private List<Rooms> availableRoom = new RoomDao().getAvailableRoom();
    private Rooms choosenRooms;
 
    private double totalPrice = 0;
    private String inYear;
    private Client loggedInCustomer;
    private Date todayDate = new Date();
    
    
    
    private User users = new User();
    private String roomStatus = "save";

    public String chooseAction() {
        if (this.roomStatus.equalsIgnoreCase("save")) {
            this.regiterRooms();
            return null;
        } else {
            this.updateRoomz();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Has been successfull updated", null));
            return "roomdetails.xhtml";
        }
    }

    public RoomImage roomsImage(Rooms rumz) throws Exception {
        List<RoomImage> rImage = new RoomImageImpl().getGenericListWithHQLParameter(new String[]{"rooms_id"}, new Object[]{rumz.getId()}, "RoomImage");
        return rImage.get(0);
    }



    public void initRooms(Rooms rooms) {
        this.choosenRooms = rooms;
        System.out.println("-----------------------------------------------"+rooms.getId());
                
    }

    public String login() throws NoSuchAlgorithmException, Exception {
        try {
            UserImpl userImpl = new UserImpl();
            User userLoggedIn = new User();
            userLoggedIn = userImpl.getModelWithMyHQL(new String[]{"userName", "password"}, new Object[]{
                user.getUsername(), new LoginImpl().criptPassword(user.getPassword())}, "from User");
            FacesContext context = FacesContext.getCurrentInstance();
            String location = "login";
            this.whereFrom = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginFrom");
            if (userLoggedIn == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Invalid username or Password", null));
            } else {
                if (userLoggedIn.getStatus().equalsIgnoreCase("active")) {
                    this.isLoggedIn = true;
                    this.loggedInUser = userLoggedIn;
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userLoggedIn", loggedInUser);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userType", loggedInUser.getUserType());
                    if (userLoggedIn.getUserType().equalsIgnoreCase(USERTYPE[0])) {
                        location = "admin/index.xhtml?faces-redirect=true";
                    } else if (userLoggedIn.getUserType().equalsIgnoreCase(USERTYPE[1])) {
                        this.loggedInCustomer = new ClientImpl().getModelWithMyHQL(new String[]{"user_userId"}, new Object[]{this.loggedInUser.getUserId()}, "From Client");

                        if (this.whereFrom != null && this.whereFrom.equalsIgnoreCase("continueToPayment")) {
                            location = "continueToPayment.xhtml?faces-redirect=true";
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginFrom", null);
                        }
                        location = "customer/index.xhtml?faces-redirect=true";
                    } else {
//                      
                    }
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Your Account have problem please contact System Administrator", null));
                }
            }
            this.init();
            return location;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        this.user = null;
        context.getExternalContext().invalidateSession();
        this.isLoggedIn = true;
        loggedInUser = null;
        return "login.xhtml?faces-redirect=true";
    }

    public String changePassword() throws Exception {
        try {

            String location = null;
            changePass.setUsername(user.getUsername());
            User u = new UserImpl().getModelWithMyHQL(new String[]{"userName", "password"}, new Object[]{
                changePass.getUsername(), new LoginImpl().criptPassword(recentPassword)}, "from User");
            FacesContext context = FacesContext.getCurrentInstance();
            if (u == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Invalid Recent Password", null));
            } else {
                if (retypePassword.equalsIgnoreCase(changePass.getPassword())) {
                    u.setPassword(new LoginImpl().criptPassword(changePass.getPassword()));
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password has been changed", null));
                    new UserImpl().updateInfo(u);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userLoggedIn", null);
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Your password doesn't match", null));
                }
            }
            return location;

        } catch (Exception ex) {

//            new SendEmail().sendEmail("iyaturemyeclaude@gmail.com", "error", ex.getMessage());
            return null;
        }
    }

    public void regiterCompany() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            if (user.getPassword().equals(secondPassword)) {
                user.setPassword(new LoginImpl().criptPassword(user.getPassword()));
                user.setUserType("customer");
                user.setStatus("active");
                User users = new UserImpl().create(user);
                comp.setUser(users);
                new ClientImpl().create(comp);
                comp = new Client();
                user = new User();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "registration done successfully", null));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "password mismatch try again or contact administrator", null));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String initRoomz(Rooms room) {
        this.room = room;
        this.roomStatus="Update";
        
        return "roomReg.xhtml?faces-redirect=true";
    }

    public void updateRoomz() {
        new RoomsImpl().updateInfo(room);
        this.roomStatus = "save";
        this.room=new Rooms();
        this.nrooms = new RoomsImpl().getAll();
    }

    public void updateRooms(Rooms rumz) {
        rumz.setStatus((rumz.getStatus().equalsIgnoreCase("available")) ? "occupied" : "available");
        new RoomsImpl().updateInfo(rumz);
        nrooms = new RoomsImpl().getAll();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Has been successull updated", null));
    }

    public void regiterRooms() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Rooms roomd = new RoomsImpl().getModelWithMyHQL(new String[]{"roomNumber"},
                    new Object[]{this.room.getRoomNumber()}, "from Rooms");
            if (roomd == null) {
                Rooms r = new RoomsImpl().create(room);
                System.out.println("--------------------" + image.size());
                for (String x : this.image) {
                    RoomImage rimage = new RoomImage();
                    rimage.setRooms(r);
                    rimage.setImageName(x);
                    new RoomImageImpl().create(rimage);
                }
                room = new Rooms();

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " regitered successfully", null));

            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, " arlead exist", null));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Upload(FileUploadEvent event) {
        String newName = new FileUpload().Upload(event, "C:\\Users\\God win\\Documents\\NetBeansProjects\\\\web\\suites\\images\\roomImage\\");
        this.image.add(newName);
    }

    private void init() {
        if (this.loggedInUser.getUserType().equalsIgnoreCase("customer")) {
           
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWhereFrom() {
        return whereFrom;
    }

    public void setWhereFrom(String whereFrom) {
        this.whereFrom = whereFrom;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public User getChangePass() {
        return changePass;
    }

    public void setChangePass(User changePass) {
        this.changePass = changePass;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }

    public String getRecentPassword() {
        return recentPassword;
    }

    public void setRecentPassword(String recentPassword) {
        this.recentPassword = recentPassword;
    }

    public String getSecondPassword() {
        return secondPassword;
    }

    public void setSecondPassword(String secondPassword) {
        this.secondPassword = secondPassword;
    }

    public Client getLoggedInCompany() {
        return loggedInCompany;
    }

    public void setLoggedInCompany(Client loggedInCompany) {
        this.loggedInCompany = loggedInCompany;
    }

    public Client getComp() {
        return comp;
    }

    public void setComp(Client comp) {
        this.comp = comp;
    }

    public Rooms getRoom() {
        return room;
    }

    public void setRoom(Rooms room) {
        this.room = room;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<Rooms> getNrooms() {
        return nrooms;
    }

    public void setNrooms(List<Rooms> nrooms) {
        this.nrooms = nrooms;
    }

    public List<Client> getCust() {
        return cust;
    }

    public void setCust(List<Client> cust) {
        this.cust = cust;
    }

    public List<RoomImage> getCustt() {
        return custt;
    }

    public void setCustt(List<RoomImage> custt) {
        this.custt = custt;
    }

    public List<Rooms> getAvailableRoom() {
        return availableRoom;
    }

    public void setAvailableRoom(List<Rooms> availableRoom) {
        this.availableRoom = availableRoom;
    }

    public Rooms getChoosenRooms() {
        return choosenRooms;
    }

    public void setChoosenRooms(Rooms choosenRooms) {
        this.choosenRooms = choosenRooms;
    }

    

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getInYear() {
        return inYear;
    }

    public void setInYear(String inYear) {
        this.inYear = inYear;
    }

    public Client getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public void setLoggedInCustomer(Client loggedInCustomer) {
        this.loggedInCustomer = loggedInCustomer;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }




  

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

//    

   

     
}
