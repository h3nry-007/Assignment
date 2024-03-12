package Assignment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
/**
 * @author henry
 */

public class CustomerMS extends javax.swing.JFrame {
    
    DatabaseConnection db = new DatabaseConnection();
    Connection con = db.dbConnection();
    Object columnNames[]= {"Customer ID", "Customer Name", "Phone Number", "Email","Address", "Gender", "Balance"};
    JTable jTblData = new JTable();
    DefaultTableModel model = new DefaultTableModel();

    /**
     * Creates new form CustomerMS
     */
    public CustomerMS() {
        initComponents();
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new GridLayout(2,1));
        
        idField.setEditable(false);
        add(jPanel1);
        add(jPanel2);
        
        selectData();
        
        JMenuBar bar = new JMenuBar();
        
        jTblData.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Get the selected row index
                    int selectedRow = jTblData.getSelectedRow();

                    if (selectedRow != -1) {
                        idField.setText(jTblData.getValueAt(selectedRow, 0).toString());
                        nameField.setText(jTblData.getValueAt(selectedRow, 1).toString());
                        phoneField.setText(jTblData.getValueAt(selectedRow, 2).toString());
                        mailField.setText(jTblData.getValueAt(selectedRow, 3).toString());
                        addressField.setText(jTblData.getValueAt(selectedRow, 4).toString());
                        if (jTblData.getValueAt(selectedRow, 5).toString().equalsIgnoreCase("Male")) {
                            male_Btn.setSelected(true);
                        } else if (jTblData.getValueAt(selectedRow, 5).toString().equalsIgnoreCase("Female")) {
                            female_Btn.setSelected(true);
                        }
                        balanceField.setText(jTblData.getValueAt(selectedRow,6).toString());

//                        jCboAge.setSelectedIndex(Integer.parseInt(jTblData.getValueAt(selectedRow, 6).toString()) - 17);
                        if (!idField.getText().equalsIgnoreCase("")) {
                            saveUpate_Btn.setText("Update");
                        }
                    }
                }
            }
        });
    }
    
    /* The place that write by author <not generated code> start here */
    
    // Insert Data 
    private void insertData() {
        String name = nameField.getText();
        String mail = mailField.getText();
        String ph = phoneField.getText();
        String add = addressField.getText();
        String balance = balanceField.getText();
        String gender = "";
        if (male_Btn.isSelected()) {
            gender = "Male";
        } else if (female_Btn.isSelected()) {
            gender = "Female";
        }
//        int age = 0;
//        if (jCboAge.getSelectedIndex() != 0) {
//            age = Integer.parseInt(jCboAge.getSelectedItem().toString());
//        }

        try {
            String insertQuery = "INSERT INTO customer (Name, Phone, Mail,"
                    + "Address, Gender, Balance) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, ph);
            preparedStatement.setString(3, mail);
            preparedStatement.setString(4, add);
            preparedStatement.setString(5, gender);
            preparedStatement.setString(6, balance);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
                JOptionPane.showMessageDialog(null, "Data inserted successfully!", "Notic",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Failed to insert data!");
            }
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Get Data Method Here
    private void selectData() {
        try {
            String selectQuery = "SELECT * FROM customer";
            PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
            ResultSet rs = preparedStatement.executeQuery();

            jPanel2.setLayout(new BorderLayout());

            jPanel2.add(jTblData, BorderLayout.CENTER);
            Object row[] = new Object[7];

            model.setColumnIdentifiers(columnNames);
            jTblData.setModel(model);
            jTblData.setAutoResizeMode(jTblData.AUTO_RESIZE_ALL_COLUMNS);
            jTblData.setFillsViewportHeight(true);
            jTblData.setShowGrid(true);
            jTblData.setGridColor(Color.BLACK);

            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            if (defaults.get("Table.alternateRowColor") == null) {
                defaults.put("Table.alternateRowColor", new Color(240, 240, 240));
            }

            jTblData.getTableHeader().setBackground(new Color(240, 240, 240));
            jTblData.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
            ((DefaultTableCellRenderer) jTblData.getTableHeader().getDefaultRenderer())
                    .setHorizontalAlignment(SwingConstants.CENTER);
            jTblData.getTableHeader().setResizingAllowed(false);
            jTblData.getTableHeader().setReorderingAllowed(false);

            JScrollPane scrollPane = new JScrollPane(jTblData);
            jPanel2.add(scrollPane, BorderLayout.CENTER);

            while (rs.next()) {
                row[0] = rs.getInt("id");
                row[1] = rs.getString("Name");
                row[2] = rs.getString("Phone");
                row[3] = rs.getString("Mail");
                row[4] = rs.getString("Address");
                row[5] = rs.getString("Gender");
                row[6] = rs.getInt("Balance");

                model.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Clear Button Method
    private void clearData(){
        jTblData.getSelectionModel().clearSelection();
        jTblData.getColumnModel().getSelectionModel().clearSelection();
        
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        mailField.setText("");
        addressField.setText("");
        balanceField.setText("");
        buttonGroup1.clearSelection();
        nameField.requestFocus();
        
        if(idField.getText().equalsIgnoreCase("")){
            saveUpate_Btn.setText("Save");
        }
    }
    
    // Update Table Modle Method 
    private void updateTableModel() {
        try {
            // Assuming con is your database connection
            String selectQuery = "SELECT * FROM customer";
            PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
            ResultSet rs = preparedStatement.executeQuery();

            // Clear existing data in the model
            model.setRowCount(0);

            // Populate the model with updated data
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID"),
                    rs.getString("Name"),
                    rs.getString("Phone"),
                    rs.getString("Mail"),
                    rs.getString("Address"),
                    rs.getString("Gender"),
                    rs.getString("Balance")
                };
                model.addRow(row);
            }

            // Dispose resources
            rs.close();
            preparedStatement.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    //update Data Method
    private void updateData() {
        int id = Integer.parseInt(idField.getText());

        String name = nameField.getText();
        String mail = mailField.getText();
        String ph = phoneField.getText();
        String add = addressField.getText();
        String balance = balanceField.getText();
        String gender = "";
        
        if (male_Btn.isSelected()) {
            gender = "Male";
        } else if (female_Btn.isSelected()) {
            gender = "Female";
        }
//        int age = 0;
//        if (jCboAge.getSelectedIndex() != 0) {
//            age = Integer.parseInt(jCboAge.getSelectedItem().toString());
//        }

        try {
            String updateQuery = "UPDATE customer SET Name = ?, Phone = ?, Mail = ?,"
                    + "Address = ?, Gender = ?, balance =? WHERE ID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(updateQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, ph);
            preparedStatement.setString(3, mail);
            preparedStatement.setString(4, add);
            preparedStatement.setString(5, gender);
            preparedStatement.setString(6, balance);
            preparedStatement.setInt(7, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data updated successfully!");
                JOptionPane.showMessageDialog(null, "Data Update Successfully", "Notic",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Failed to update data!");
                JOptionPane.showMessageDialog(null, "Failed to update data!", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Delete Data Method
    private void deleteData() {
        int id = Integer.parseInt(idField.getText());
        try {
            String deleteQuery = "DELETE FROM customer WHERE ID=?";
            PreparedStatement preparedStatement = con.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Data deleted successfully!");
                JOptionPane.showMessageDialog(null, "Data Deleted Successfully", "Notic",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Failed to delete data!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Search Data 
    private void searchData() {
        //int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter stud ID you want to search!"));
        String name = JOptionPane.showInputDialog(null, "Enter Customer Name");
        try {
            String selectQuery = "SELECT * FROM customer WHERE Name = ?";
            PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                idField.setText(rs.getInt("ID") + "");
                nameField.setText(rs.getString("Name"));
                phoneField.setText(rs.getString("Phone"));
                mailField.setText(rs.getString("Mail"));
                addressField.setText(rs.getString("Address"));
                if (rs.getString("Gender").equalsIgnoreCase("Male")) {
                    male_Btn.setSelected(true);
                } else if (rs.getString("Gender").equalsIgnoreCase("Female")) {
                    female_Btn.setSelected(true);
                }
                balanceField.setText(rs.getString("Balance"));
                //jCboAge.setSelectedIndex(rs.getInt("stud_age") - 17);
            } else {
                JOptionPane.showMessageDialog(null, "No data found");
            }
            if (!idField.getText().equalsIgnoreCase("")) {
                saveUpate_Btn.setText("Update");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // data validation
    private Boolean dataValidation(){
        if(nameField.getText().equals("")) return false;
        return true;
    }
    
    /* The place that write by author <not generated code> end here */
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        mailField = new javax.swing.JTextField();
        nameField = new javax.swing.JTextField();
        balanceField = new javax.swing.JTextField();
        phoneField = new javax.swing.JTextField();
        addressField = new javax.swing.JTextField();
        male_Btn = new javax.swing.JRadioButton();
        female_Btn = new javax.swing.JRadioButton();
        clear_Btn = new javax.swing.JButton();
        saveUpate_Btn = new javax.swing.JButton();
        search_Btn = new javax.swing.JButton();
        delete_Btn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Customer Management System");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Entry", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("sansserif", 1, 14))); // NOI18N

        jLabel1.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel1.setText("Customer Name");

        jLabel2.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel2.setText("Customer ID");

        jLabel3.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel3.setText("Phone Number");

        jLabel4.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel4.setText("E- Mail");

        jLabel5.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel5.setText("Gender");

        jLabel6.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel6.setText("Balance");

        jLabel7.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel7.setText("Address");

        idField.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        idField.setText("Auto Fill Field");

        mailField.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N

        nameField.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });

        balanceField.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        balanceField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                balanceFieldActionPerformed(evt);
            }
        });

        phoneField.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N

        addressField.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N

        buttonGroup1.add(male_Btn);
        male_Btn.setText("Male");
        male_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                male_BtnActionPerformed(evt);
            }
        });

        buttonGroup1.add(female_Btn);
        female_Btn.setText("Female");

        clear_Btn.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        clear_Btn.setForeground(new java.awt.Color(0, 153, 255));
        clear_Btn.setText("Clear");
        clear_Btn.setBorder(null);
        clear_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_BtnActionPerformed(evt);
            }
        });

        saveUpate_Btn.setBackground(new java.awt.Color(0, 153, 255));
        saveUpate_Btn.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        saveUpate_Btn.setForeground(new java.awt.Color(255, 255, 255));
        saveUpate_Btn.setText("Save");
        saveUpate_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveUpate_BtnActionPerformed(evt);
            }
        });

        search_Btn.setBackground(new java.awt.Color(0, 153, 255));
        search_Btn.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        search_Btn.setForeground(new java.awt.Color(255, 255, 255));
        search_Btn.setText("Search");
        search_Btn.setBorder(null);
        search_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_BtnActionPerformed(evt);
            }
        });

        delete_Btn.setBackground(new java.awt.Color(0, 153, 255));
        delete_Btn.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        delete_Btn.setForeground(new java.awt.Color(255, 255, 255));
        delete_Btn.setText("Delete");
        delete_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_BtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addressField)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(idField, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                            .addComponent(mailField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(male_Btn)
                        .addGap(18, 18, 18)
                        .addComponent(female_Btn))
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(balanceField, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                    .addComponent(phoneField))
                .addGap(14, 14, 14))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(clear_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(saveUpate_Btn)
                .addGap(18, 18, 18)
                .addComponent(delete_Btn)
                .addGap(18, 18, 18)
                .addComponent(search_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(36, 36, 36)
                    .addComponent(jLabel2)
                    .addContainerGap(875, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(mailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(male_Btn)
                    .addComponent(female_Btn)
                    .addComponent(balanceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clear_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveUpate_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delete_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(jLabel2)
                    .addContainerGap(192, Short.MAX_VALUE)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("sansserif", 1, 14))); // NOI18N
        jPanel2.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 329, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void male_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_male_BtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_male_BtnActionPerformed

    private void clear_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_BtnActionPerformed
        // TODO add your handling code here:
        clearData();
    }//GEN-LAST:event_clear_BtnActionPerformed

    private void saveUpate_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveUpate_BtnActionPerformed
      
        if(saveUpate_Btn.getText().equalsIgnoreCase("Save")){
            //Save Method to insert into database
            if(dataValidation()) insertData();
            else{
                JOptionPane.showMessageDialog(null, "You need to fill data");
            }
        }else if (saveUpate_Btn.getText().equalsIgnoreCase("Update")){
            //somethings
            updateData();
        }
        
        updateTableModel();
        
        SwingUtilities.invokeLater(()->{
            model.fireTableDataChanged();
        });
        clearData();
        
    }//GEN-LAST:event_saveUpate_BtnActionPerformed

    private void search_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_BtnActionPerformed
        searchData();
    }//GEN-LAST:event_search_BtnActionPerformed

    private void delete_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_BtnActionPerformed
        int i = JOptionPane.showConfirmDialog(null, "Are you sure?");
        if(i == JOptionPane.YES_OPTION){
            deleteData();
            updateTableModel();
            
            SwingUtilities.invokeLater(() -> {
                model.fireTableDataChanged();
            });
            clearData();
        }
        
    }//GEN-LAST:event_delete_BtnActionPerformed

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameFieldActionPerformed

    private void balanceFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_balanceFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_balanceFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CustomerMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerMS().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressField;
    private javax.swing.JTextField balanceField;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton clear_Btn;
    private javax.swing.JButton delete_Btn;
    private javax.swing.JRadioButton female_Btn;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField mailField;
    private javax.swing.JRadioButton male_Btn;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField phoneField;
    private javax.swing.JButton saveUpate_Btn;
    private javax.swing.JButton search_Btn;
    // End of variables declaration//GEN-END:variables
}
