import java.sql.*;

//4-DB ye bağlanacak olan sınıf:Connection,Statement,PreparedStatement
public class StudentRepository {

    private Connection conn;
    private Statement st;
    private PreparedStatement prst;


    //5-connection oluşturma için metod
    private void setConnection(){
        try {
            this.conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/jdbc_db","dev_user","password");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //6-statement oluşturma için metod
    private void setStatement(){
        try {
            this.st=conn.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //7-preparedstatement oluşturma için metod
    private void setPreparedStatement(String sql){
        try {
            this.prst= conn.prepareStatement(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //8-tablo oluşturma
    public void createTable(){
        setConnection();
        setStatement();
        try {
            st.execute("CREATE TABLE IF NOT EXISTS t_student(" +
                    "id SERIAL UNIQUE," +
                    "name VARCHAR(50) NOT NULL CHECK(LENGTH(name)>0)," +//empty ""
                    "lastname VARCHAR(50) NOT NULL CHECK(LENGTH(lastname)>0)," +
                    "city VARCHAR(50) NOT NULL CHECK(LENGTH(city)>0)," +
                    "age INT NOT NULL CHECK(age>0)" +
                    ")");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    //12-tabloya kayıt ekleme
    public void save(Student student) {
        String sql="INSERT INTO t_student(name,lastname,city,age) VALUES(?,?,?,?)";
        setConnection();
        setPreparedStatement(sql);
        try {
            prst.setString(1,student.getName());
            prst.setString(2,student.getLastname());
            prst.setString(3,student.getCity());
            prst.setInt(4,student.getAge());
            prst.executeUpdate();
            System.out.println("Saved successfully...");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                prst.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //14-tablodaki tüm kayıtları tüm fieldlarıyla getirip yazdırma
    public void findAll() {
        setConnection();
        setStatement();
        String query="SELECT * FROM t_student";
        System.out.println("=============ALL STUDENTS========================");
        try {
            ResultSet resultSet=st.executeQuery(query);
            while (resultSet.next()){
                System.out.print("id :"+resultSet.getInt("id"));
                System.out.print("  -ad :"+resultSet.getString("name"));
                System.out.print("     -soyad :"+resultSet.getString("lastname"));
                System.out.print("     -şehir :"+resultSet.getString("city"));
                System.out.print("     -yaş :"+resultSet.getInt("age"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }


    //16-tablodan idsi verilen kaydı silme
    public void delete(int id) {
        setConnection();
//        String query="DELETE FROM t_student WHERE id="+id;
//        st.executeUpdate(query);
        String query="DELETE FROM t_student WHERE id=?";
        setPreparedStatement(query);
        try {
            prst.setInt(1,id);
            int deleted=prst.executeUpdate();
            //kayıt bulunursa deleted=1 olur
            if(deleted>0){
                System.out.println("Student is deleted successfully by id:"+id);
            }else{//böyle bir kayıt yoksa deleted=0 olur
                System.out.println("Student could not found by id:"+id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                prst.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    //18-id si verilen kaydı tablodan getirme
    public Student findById(int id) {
        Student student=null;
        setConnection();
        String sql="SELECT * FROM t_student WHERE id=?";
        setPreparedStatement(sql);
        try {
            prst.setInt(1,id);
            ResultSet resultSet=prst.executeQuery();
            if(resultSet.next()){
                student=new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setLastname(resultSet.getString("lastname"));
                student.setCity(resultSet.getString("city"));
                student.setAge(resultSet.getInt("age"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                prst.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return student;
    }

    //20-tablodaki id si verilen kaydı güncelleme
    public void update(Student foundStudent) {
        setConnection();
        String query="UPDATE t_student SET name=?, lastname=?, city=?, age=? WHERE id=?";
        setPreparedStatement(query);
        try {
            prst.setString(1,foundStudent.getName());
            prst.setString(2,foundStudent.getLastname());
            prst.setString(3,foundStudent.getCity());
            prst.setInt(4,foundStudent.getAge());
            prst.setInt(5,foundStudent.getId());
            prst.executeUpdate();
            if(prst.executeUpdate()>0){
                System.out.println("updated successfully...");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                prst.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


}