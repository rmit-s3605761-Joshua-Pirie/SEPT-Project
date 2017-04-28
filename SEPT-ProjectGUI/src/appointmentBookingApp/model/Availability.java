package appointmentBookingApp.model;

import appointmentBookingApp.util.DbUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import static appointmentBookingApp.util.DbUtil.*;

/**
 * Created by Joshua on 19/04/2017.
 */
public class Availability {
    HashMap<Day, ArrayList<Integer>> availability;

    public Availability() {
        availability = new HashMap<Day, ArrayList<Integer>>();
        for(Day d : Day.values())
            availability.put(d, new ArrayList<Integer>());
    }

    public void setAvailability(Day day, int time) {
        if(!availability.get(day).contains(time))
            availability.get(day).add(time);
        availability.get(day).sort(Integer::compareTo);
    }

    public void resetDay(Day day) {
        availability.get(day).clear();
    }

    public ArrayList<Integer> getAvailability(Day day) {
        return availability.get(day);
    }

    public boolean addAvailabilityToDB(String staffID) {
        boolean success = deleteStaffAvailability(staffID);
        for(Day d : Day.values()) {
            Integer startTime = null;
            Integer endTime = null;
            for(int t : availability.get(d)) {
                if(startTime == null) {
                    startTime = t;
                    endTime = t + 1;
                }
                else if(t - endTime == 0) {
                    endTime = t + 1;
                }
                else {
                    success = addToDB(staffID, startTime, endTime, d);
                    startTime = t;
                    endTime = t + 1;
                }
            }
            if(startTime != null)
                success = addToDB(staffID, startTime, endTime, d);
        }
        return success;
    }

    private boolean deleteStaffAvailability(String staffID) {
        String sql = "DELETE FROM availability WHERE staffID='" + staffID + "'";
        try {
            getNewStatment().executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean addToDB(String staffID, int startTime, int endTime, Day day) {
        String sql = "INSERT INTO availability (staffID, dayOfWeek, startTime, endTime) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, staffID);
            ps.setByte(2, (byte)day.ordinal());
            ps.setString(3, (String.format("%02d", startTime) + ":00:00"));
            ps.setString(4, (String.format("%02d", endTime) + ":00:00"));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
