package LoadTextFile;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
class HOCVIEN{
	String MaHV, TenHV, Gender;
	String NS;
	Double Diem;
	HOCVIEN(String ma, String ten, String ns, String gt, Double d){
		MaHV = ma;
		TenHV = ten;
		NS = ns;
		Gender = gt;
		Diem = d;
	}
}

public class LoadtxtFile {
	public static void main(String[] args) {
		List<HOCVIEN> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("input.txt")))
        {
            String s;
            while ((s = br.readLine()) != null) {
            	HOCVIEN t = new HOCVIEN(s.substring(0, 10),s.substring(10, 35),s.substring(61, 71),s.substring(71, 74),Double.parseDouble(s.substring(74, 77)));
                list.add(t);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		// SHOW LIST HOCVIEN
//		for(HOCVIEN hv:list){  
//			    System.out.println(hv.MaHV+" "+hv.TenHV+" "+hv.NS+" "+hv.Gender+" "+hv.Diem);  
//		} 
		
		String selectAll = "SELECT * FROM HOCVIEN";
		String sqlInsert = "INSERT INTO HOCVIEN VALUES (?,?,?,?,?)";
		
		DateFormat from = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat to = new SimpleDateFormat("yyyy-MM-dd");
		from.setLenient(false);
		int i = 0;
		try {
			String url = "jdbc:mysql://localhost:3306/javadata";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement prepStmt = con.prepareStatement(sqlInsert);
			Statement stmt = con.createStatement();
			// INSERT INTO HOCVIEN
			Iterator<HOCVIEN> it = list.iterator();
			boolean append = false;
			while(it.hasNext()){
				++i;
				try {
				    HOCVIEN hv = it.next();
				    prepStmt.setString(1,hv.MaHV);            
				    prepStmt.setString(2,hv.TenHV);
					java.sql.Date sqlDate = java.sql.Date.valueOf(to.format(from.parse(hv.NS)));
					prepStmt.setDate(3, sqlDate);
				    prepStmt.setString(4,hv.Gender);
				    prepStmt.setDouble(5, hv.Diem);
				    prepStmt.addBatch();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// Xuất dữ liệu bị lỗi ở từng dòng ra file error.txt
					String content = "Dong "+i+": Sai dinh dang ngay sinh";
					try (FileWriter fw = new FileWriter("error.txt", append);
						 BufferedWriter bw = new BufferedWriter(fw)){
					    	bw.write(content);
					    	bw.newLine();
					}catch (IOException ioe) {
			            System.err.format("IOException: %s%n", ioe);
			        }
					append = true;
				}
			  }
			prepStmt.executeBatch();
			// SELECT ALL HOCVIEN
//			ResultSet rs = stmt.executeQuery(selectAll);
//				while (rs.next()) {
//					++i;
//					String mahv = rs.getString("MaHocVien");
//					String name = rs.getString("TenHocVien");
//					Date ns = new Date(0);
//					try {
//						ns = rs.getDate("NgaySinh");
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						// Xuất dữ liệu bị lỗi ở từng dòng ra file error.txt
//						System.out.println("Dong "+i+": Sai dinh dang ngay sinh");
//					}
//					String gt = rs.getString("GioiTinh");
//					double diem = rs.getDouble("DiemThi");
//					//System.out.println("MaHV = " +mahv+ " TenHV = " + name + " NgaySinh = " + ns + " GioiTinh = " + gt + " DiemThi = " + diem);
//				}
//			rs.close();
//			stmt.close();
//			con.close();
		} catch(Exception e) {System.out.println("Error " + e);}
	}
}
