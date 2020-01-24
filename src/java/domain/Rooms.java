/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author 2eM yadah
 */
@Entity
public class Rooms implements Serializable {
   @Id
   private String id=UUID.randomUUID().toString();
   private String roomCategory;
   private String roomNumber;
   private String floor;
   private String status="available";
   private String description;
   private String price;
  
   @OneToMany(mappedBy = "rooms",cascade =CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
   private List<RoomImage> roomImage;
   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(String roomCategory) {
        this.roomCategory = roomCategory;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

  

    public List<RoomImage> getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(List<RoomImage> roomImage) {
        this.roomImage = roomImage;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
   
}
