
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class stdCourseGUI extends JFrame implements ActionListener {
    // GUI组件
    private JLabel lblQuery, lblResult;
    private JTextField txtQuery, txtResult;
    private JButton btnTime, btnName;

    // 数据库连接
    private String jdbcUrl = "jdbc:sqlserver://localhost:1433;DatabaseName=文素课表";
    private String user = "sa";
    private String password = "123456";
    private Connection conn;

    // 构造函数
    public stdCourseGUI() {
        // 初始化GUI组件
        lblQuery = new JLabel("查询条件：");
        lblResult = new JLabel("查询结果：");
        txtQuery = new JTextField(20);
        txtResult = new JTextField(30);
        btnTime = new JButton("按时间查询");
        btnName = new JButton("按姓名/学号查询");

        // 添加GUI组件
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(lblQuery);
        panel.add(txtQuery);
        panel.add(lblResult);
        panel.add(txtResult);
        panel.add(btnTime);
        panel.add(btnName);
        add(panel);

        // 添加事件监听器
        btnTime.addActionListener(this);
        btnName.addActionListener(this);

        // 连接数据库
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 设置GUI界面
        setTitle("学生课程查询");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // 实现事件处理方法
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTime) {
            // 按时间查询
            String time = txtQuery.getText();
            String sql = "SELECT s.name, c.course FROM stdmsg s INNER JOIN stdCourse c ON s.student_id = c.student_id WHERE c.time = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, time);
                ResultSet rs = ps.executeQuery();
                String result = "";
                while (rs.next()) {
                    result += rs.getString("name") + "： " + rs.getString("course") + "\n";
                }
                txtResult.setText(result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == btnName) {
            // 按姓名/学号查询
            String query = txtQuery.getText();
            String sql = "SELECT course FROM stdCourse WHERE student_id = (SELECT student_id FROM stdmsg WHERE name = ? OR student_id = ?)";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, query);
                ps.setString(2, query);
                ResultSet rs = ps.executeQuery();
                String result = "";
                while (rs.next()) {
                    result += rs.getString("course") + "\n";
                }
                txtResult.setText(result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 入口方法
    public static void main(String[] args) {
        new stdCourseGUI();
    }
}

//在不用数据库的前提下，实现一个可以通过可视化查询进行操作的程序，具体来说：
