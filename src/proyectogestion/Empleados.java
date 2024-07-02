
package proyectogestion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Empleados extends javax.swing.JFrame {

    private Connection con;
    private String tipoUsuario;
    
    public Empleados(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        initComponents();
         conectar();
        // Carga los datos de la tabla de ventas en el JTable
        cargarTablaEmpleados(jTable1);
        jComboBox1.setSelectedIndex(-1);
        configurarpermisos();
    }
    
    private void configurarpermisos(){
    if("Empleado".equals(tipoUsuario)){
    btnBorrar.setEnabled(false);
    btnEditar.setEnabled(false);
    }
    
    }
    private boolean verificarContraseñaAdmin(String contraseña) {
        try {
            String sql = "SELECT * FROM registro_administrador WHERE contraseña = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, contraseña);
            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al verificar la contraseña: " + ex.getMessage());
            return false;
        }
    }
    
  private void conectar() {
        try {
            // Establece la conexión a la base de datos MySQL
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/proyectogestionedith", "root", "12345");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
        }
    }
    
    private void cargarTablaEmpleados(JTable tabla) {
        try {
            // Consulta SQL para obtener los datos de la tabla de ventas
            String sql = "SELECT * FROM EMPLEADOS";
            // Preparar la consulta
            PreparedStatement stmt = con.prepareStatement(sql);
            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();
            // Crear un modelo de tabla para el JTable
            DefaultTableModel modelo = new DefaultTableModel();
            // Agregar columnas al modelo
            modelo.addColumn("ID Empleado");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellido");
            modelo.addColumn("Edad");
            modelo.addColumn("Puesto");
            modelo.addColumn("Sueldo Mensual");
            modelo.addColumn("Fecha Ingreso");
            modelo.addColumn("Telefono");
            // Agregar filas al modelo con los datos recuperados de la base de datos
            while (rs.next()) {
                Object[] fila = new Object[8];
                fila[0] = rs.getString("ID_EMPLEADO");
                fila[1] = rs.getString("NOMBRE");
                fila[2] = rs.getString("APELLIDO");
                fila[3] = rs.getBigDecimal("EDAD");
                fila[4] = rs.getString("PUESTO");
                fila[5] = rs.getDouble("SUELDO_MENSUAL");
                fila[6] = rs.getDate("FECHA_INGRESO");
                fila[7] = rs.getBigDecimal("TELEFONO");
                modelo.addRow(fila);
            }
            // Establecer el modelo de tabla en el JTable
            tabla.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla de ventas: " + ex.getMessage());
        }
    }
    
     private void guardarInventario() {
        try {
            
            // Obtener los datos de los JTextFields
            String idempleado = jTextField1.getText();
            String nombre = jTextField2.getText();
            String apellido = jTextField3.getText();
            int edad = Integer.parseInt(jTextField4.getText());
            String puesto = (String) jComboBox1.getSelectedItem();
            Double sueldo = Double.parseDouble(jTextField6.getText()); 
            
            Date fechaDate = jDateChooser1.getDate();
             // Convertir la fecha a un objeto java.sql.Date
            java.sql.Date fechaSQL = new java.sql.Date(fechaDate.getTime());  
            
            String  telefono= jTextField8.getText();
            

            // Consulta SQL para insertar los datos en la base de datos
            String sql = "INSERT INTO EMPLEADOS (ID_EMPLEADO, NOMBRE, APELLIDO, EDAD, PUESTO, SUELDO_MENSUAL, FECHA_INGRESO, TELEFONO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            // Asignar los valores a la consulta
            stmt.setString(1, idempleado);
            stmt.setString(2, nombre);
            stmt.setString(3, apellido);
            stmt.setInt(4, edad);
            stmt.setString(5, puesto);
            stmt.setDouble(6, sueldo);
            stmt.setDate(7, fechaSQL);
            stmt.setString(8, telefono);
            // Ejecutar la consulta
            stmt.executeUpdate();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos guardados correctamente.");
            
            // Recargar los datos en la tabla
            cargarTablaEmpleados(jTable1);
            
            limpiarCasillas();

        } catch (SQLException ex) {
            System.out.println("Error al guardar los datos: " + ex.getMessage());
            
            
        }
    }
     
     private void borrarInventario() {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Por favor, selecciona un registro para borrar.");
        return;
    }

    String idempleado = jTable1.getValueAt(selectedRow, 0).toString();

    try {
        // Consulta SQL para borrar el registro de la base de datos
        String sql = "DELETE FROM EMPLEADOS WHERE ID_EMPLEADO = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, idempleado);
        // Ejecutar la consulta
        stmt.executeUpdate();

        // Mostrar mensaje de éxito
        JOptionPane.showMessageDialog(null, "Registro borrado correctamente.");
        
        // Recargar los datos en la tabla
        cargarTablaEmpleados(jTable1);
        limpiarCasillas();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al borrar el registro: " + ex.getMessage());
    }
}
     
     private void EditarRegistro(){
     int selectedRow = jTable1.getSelectedRow();
         if(selectedRow == -1){
         JOptionPane.showMessageDialog(null, "Seleccione un registro para editar");
         return;
         }
         
         try{
        String idempleado = jTextField1.getText();
        String nombre = jTextField2.getText();
        String apellido = jTextField3.getText();
        String edad = jTextField4.getText();
        String puesto = (String) jComboBox1.getSelectedItem();
        String sueldo = jTextField6.getText();
        
        Date fechaDate = jDateChooser1.getDate();
        java.sql.Date fechaSQL = new java.sql.Date(fechaDate.getTime()); 
        
        String telefono = jTextField8.getText();
        
        String sql = "UPDATE EMPLEADOS SET ID_EMPLEADO = ?, NOMBRE = ?, APELLIDO = ?, EDAD = ?, PUESTO = ?, SUELDO_MENSUAL = ?, FECHA_INGRESO = ?, TELEFONO = ? WHERE ID_EMPLEADO = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        
        stmt.setString(1, idempleado);
        stmt.setString(2, nombre);
        stmt.setString(3, apellido);
        stmt.setString(4, edad);
        stmt.setString(5, puesto);
        stmt.setString(6, sueldo);
        stmt.setDate(7, fechaSQL);
        stmt.setString(8, telefono);
        stmt.setString(9, jTable1.getValueAt(selectedRow, 0).toString());
     
        stmt.executeUpdate();
        
        JOptionPane.showMessageDialog(null, "Datos actualizados correctamente.");
        limpiarCasillas();
        
        cargarTablaEmpleados(jTable1);
     }catch (SQLException ex){
     JOptionPane.showMessageDialog(null, "Error al actualizar los datos: " + ex.getMessage());
     }
     
     }
     
     private void limpiarCasillas() {
     
         jTextField1.setText("");
         jTextField2.setText("");
         jTextField3.setText("");
         jTextField4.setText("");
         jComboBox1.setSelectedIndex(-1);
         jTextField6.setText("");
         jDateChooser1.setDate(null);
         jTextField8.setText("");
     }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Empleados");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Empleados");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(287, 9, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 35, -1, -1));
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(142, 69, 106, -1));
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 97, 106, -1));
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 125, 106, -1));

        jTextField4.setToolTipText("");
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(137, 152, 106, -1));
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(143, 214, 106, -1));
        getContentPane().add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(143, 270, 106, -1));

        jLabel2.setText("id empleado");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 69, 73, -1));

        jLabel3.setText("nombre");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 100, -1, -1));

        jLabel4.setText("apellido");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 128, 48, -1));

        jLabel5.setText("edad");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 155, 37, -1));

        jLabel6.setText("puesto");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 189, 37, -1));

        jLabel7.setText("sueldo mensual");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 214, 87, -1));

        jLabel8.setText("fecha ingreso");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 242, 81, -1));

        jLabel9.setText("Telefono");
        jLabel9.setToolTipText("");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, -1, -1));

        jButton2.setText("Guardar");
        jButton2.setToolTipText("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 326, -1, -1));

        btnBorrar.setText("borrar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 326, -1, -1));

        btnEditar.setText("editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 367, -1, -1));

        jButton1.setText("Regresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 6, -1, -1));
        getContentPane().add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(143, 242, 106, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Vendedor", "Subgerente", "Gerente" }));
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 110, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        guardarInventario();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        borrarInventario();
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        EditarRegistro();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int selectedRow = jTable1.getSelectedRow();

         jTextField1.setText(jTable1.getValueAt(selectedRow,0).toString());
         jTextField2.setText(jTable1.getValueAt(selectedRow,1).toString());
         jTextField3.setText(jTable1.getValueAt(selectedRow,2).toString());
         jTextField4.setText(jTable1.getValueAt(selectedRow,3).toString());
         jComboBox1.setSelectedItem(jTable1.getValueAt(selectedRow,4).toString());
         jTextField6.setText(jTable1.getValueAt(selectedRow,5).toString());
         jTextField8.setText(jTable1.getValueAt(selectedRow,7).toString());
        
         
         String fechaString = jTable1.getValueAt(selectedRow, 6).toString();
    try {
    // Convertir la cadena de texto a un objeto Date
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date fechaDate = dateFormat.parse(fechaString);
    
    // Establecer la fecha en el JDateChooser
    jDateChooser1.setDate(fechaDate);
    } catch (ParseException ex) {
    JOptionPane.showMessageDialog(null, "Error al convertir la fecha: " + ex.getMessage());
    }
    
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Menu mn = new Menu(tipoUsuario);
        mn.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Empleados("Empleado").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
