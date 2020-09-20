package db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBConnect {

	private Connection con;
	private PreparedStatement checkLogin, inserUser, changePassword, updatePassword, 
								editProfile, updatePhoto, getPhoto, searchPeople, 
								getPeopleByEmail, sendMessage, getMessages, getFile;
	
	public DBConnect() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/ptalk","root","Harshit@123");
		con.createStatement();
		checkLogin = con.prepareStatement(
				"select * from userinfo where email = ? and pass = ?");
		inserUser = con.prepareStatement(
				"insert into userinfo values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		changePassword = con.prepareStatement(
                "select name from userinfo where email=? and pass=?");
        updatePassword = con.prepareStatement(
                "update userinfo set pass = ? where email = ?");
        editProfile = con.prepareStatement(
        		"update userinfo set name = ?, phone = ?, gender = ?, dob = ?, "
        		+ "state = ?, city = ?, area = ? where email = ?");
        updatePhoto = con.prepareStatement(
        		"update userinfo set photo = ? where email = ?");
        getPhoto = con.prepareStatement(
                "select photo from userinfo where email=? ");
        searchPeople = con.prepareStatement(
        		"select name, phone, email, dob, gender from userinfo where state = ? and "
        		+ "city = ? and email != ? and area like ?");
        getPeopleByEmail = con.prepareStatement(
        		"select * from userinfo where email = ?");
        sendMessage = con.prepareStatement(
        		"insert into peoplemsg  (sid,rid,msg,filename,ufile,udate) "
        		+ "values (?,?,?,?,?,now())");
        getMessages = con.prepareStatement(
        		"select * from peoplemsg where sid=? and rid=? ");
        getFile = con.prepareStatement(
                "select ufile from peoplemsg where pid=? ");
	}
	
	public HashMap<String, Object> checkLogin(String e, String p) throws SQLException {
		checkLogin.setString(1, e);
		checkLogin.setString(2, p);
		ResultSet rs = checkLogin.executeQuery();
		if(rs.next()) {
			HashMap<String, Object> userDetails = new HashMap<>();
			userDetails.put("email", e);
			userDetails.put("name", rs.getString("name"));
			userDetails.put("phone", rs.getString("phone"));
			userDetails.put("gender", rs.getString("gender"));
			userDetails.put("dob", rs.getDate("dob"));
			userDetails.put("state", rs.getString("state"));
			userDetails.put("city", rs.getString("city"));
			userDetails.put("area", rs.getString("area"));
			return userDetails;
			
		}else {
			return null;
		}
		
	}
	public String inserUser(HashMap<?, ?> userDetails) throws SQLException {
		try {
			inserUser.setString(1, (String) userDetails.get("email"));
			inserUser.setString(2, (String) userDetails.get("pass"));
			inserUser.setString(3, (String) userDetails.get("name"));
			inserUser.setString(4, (String) userDetails.get("phone"));
			inserUser.setString(5, (String) userDetails.get("gender"));
			inserUser.setDate(6, (java.sql.Date) userDetails.get("dob"));
			inserUser.setString(7, (String) userDetails.get("state"));
			inserUser.setString(8, (String) userDetails.get("city"));
			inserUser.setString(9, (String) userDetails.get("area"));
			inserUser.setBinaryStream(10,  (InputStream) userDetails.get("photo"));
			inserUser.setInt(11, 1);
			int x = inserUser.executeUpdate();
			if(x != 0) {
				return "Success";
			}else {
				return "Failed";
			}
		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
			return "Already";
		}
		
	}
	
	public String changePassword(String o, String p, String c, String e) throws SQLException {
		try {
			changePassword.setString(1, e);
			changePassword.setString(2, o);
			ResultSet rs = changePassword.executeQuery();
			if(rs.next()!=false) {
				if(p.equals(c)) {
					updatePassword.setString(1, p);
					updatePassword.setString(2, e);
					int x = updatePassword.executeUpdate();
					if(x!=0) {
						return "Done";
					}else {
						return null;
					}
				}else{
                    return "matchFailed";
                }
			}else {
                return "wrongPass";
            }
			
		}catch(java.sql.SQLIntegrityConstraintViolationException ex) {
			return null;
		}
	}
	
	public String editProfile(String ph, String n, String g, java.sql.Date d, String s, String c, String a, String e) throws SQLException {
		try {
			editProfile.setString(1, n);
			editProfile.setString(2, ph);
			editProfile.setString(3, g);
			editProfile.setDate(4, d);
			editProfile.setString(5, s);
			editProfile.setString(6, c);
			editProfile.setString(7, a);
			editProfile.setString(8, e);
			int x = editProfile.executeUpdate();
			if(x != 0) {
				return "Done";
			}else
				return "Failed";
			
		}catch(Exception ex) {
			ex.printStackTrace();
			return "FailedCatch";
		}
	}
	
	public String updatePhoto(String e, InputStream is) throws SQLException{
		try {
			updatePhoto.setBinaryStream(1, is);
			updatePhoto.setString(2, e);
			int x = updatePhoto.executeUpdate();
			if(x!=0) {
				return "Done";
			}else {
				return "Failed";
			}
		}catch(Exception ex) {
			return "FailedEx";
		}
	}
	
	public byte[] getPhoto(String e) {
        try {
            getPhoto.setString(1, e);
            ResultSet rs = getPhoto.executeQuery();
            if (rs.next()) {
                byte[] b = rs.getBytes("photo");
                if (b.length != 0) {
                    return b;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
	
	public ArrayList<HashMap<String, Object>> searchPeople(String s, String c, String a, String e) throws SQLException{
		searchPeople.setString(1, s);
		searchPeople.setString(2, c);
		searchPeople.setString(3, e);
		searchPeople.setString(4, "%"+a+"%");
		ResultSet rs = searchPeople.executeQuery();
		ArrayList<HashMap<String, Object>> allUserDetails = new ArrayList<HashMap<String, Object>>();
		while(rs.next()) {
			HashMap<String, Object> userDetails = new HashMap<>();
			userDetails.put("email", rs.getString("email"));
			userDetails.put("name", rs.getString("name"));
			userDetails.put("phone", rs.getString("phone"));
			userDetails.put("gender", rs.getString("gender"));
			userDetails.put("dob", rs.getDate("dob"));
			allUserDetails.add(userDetails);
		}
		return allUserDetails;
		
	}
	
	public HashMap<String, Object> getPeopleByEmail(String e) {
		try {
			getPeopleByEmail.setString(1, e);
			ResultSet rs = getPeopleByEmail.executeQuery();
			if(rs.next()) {
				HashMap<String, Object> userDetails = new HashMap<>();
				userDetails.put("email", e);
				userDetails.put("name", rs.getString("name"));
				userDetails.put("phone", rs.getString("phone"));
				userDetails.put("gender", rs.getString("gender"));
				userDetails.put("dob", rs.getDate("dob"));
				userDetails.put("state", rs.getString("state"));
				userDetails.put("city", rs.getString("city"));
				userDetails.put("area", rs.getString("area"));
				return userDetails;
				
			}else {
				return null;
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	public String sendMessage(String temail, String semail, String message, String fname, InputStream is) throws SQLException{
		sendMessage.setString(1, semail);
		sendMessage.setString(2, temail);
		sendMessage.setString(3, message);
		sendMessage.setString(4, fname);
		sendMessage.setBinaryStream(5, is);
		int x = sendMessage.executeUpdate();
		if(x!=0) {
			return "Done";
		}else {
			return "Failed";
		}
		
	}
	
	public ArrayList<HashMap<String, Object>> getMessages(String s, String r) {
		try {
			getMessages.setString(1, s);
			getMessages.setString(2, r);
			ResultSet rs = getMessages.executeQuery();
			ArrayList<HashMap<String, Object>> allMessages = new ArrayList<HashMap<String, Object>>();
			while(rs.next()) {
				HashMap<String, Object> messages = new HashMap<>();
				messages.put("message", rs.getString("msg") );
				messages.put("datetime", rs.getString("udate"));
				String chk = rs.getString("filename");
				if(chk.isEmpty()) {
					chk=null;
				}
				messages.put("fname", chk);
				messages.put("pid", rs.getString("pid"));
				messages.put("ufile", rs.getBytes("ufile"));
				allMessages.add(messages);
			}
			return allMessages;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public byte[] getFile(int e) {
        try {
            getFile.setInt(1, e);
            ResultSet rs = getFile.executeQuery();
            if (rs.next()) {
                byte[] b = rs.getBytes("ufile");
                if (b.length != 0) {
                    return b;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
}
