
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

public class Mercancia_Compra extends javax.swing.JFrame {

 private Connection con;
 private String tipoUsuario;
 
    public Mercancia_Compra(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        initComponents();
        conectar();
        cargarTablaInvenatrios(jTable1);
        configurarPermisos();
    }
    
       private void configurarPermisos() {
        if ("Empleado".equals(tipoUsuario)) {
            btnBorrar.setEnabled(false);
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
    
    private void cargarTablaInvenatrios(JTable tabla) {
        try {
            // Consulta SQL para obtener los datos de la tabla de ventas
            String sql = "SELECT * FROM COMPRA_MERCANCIA";
            // Preparar la consulta
            PreparedStatement stmt = con.prepareStatement(sql);
            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();
            // Crear un modelo de tabla para el JTable
            DefaultTableModel modelo = new DefaultTableModel();
            // Agregar columnas al modelo
            modelo.addColumn("Numero Caja");
            modelo.addColumn("ID Caja");
            modelo.addColumn("Piezas");
            modelo.addColumn("Fecha");
            // Agregar filas al modelo con los datos recuperados de la base de datos
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt("NUMERO_CAJA");
                fila[1] = rs.getString("ID_CAJA");
                fila[2] = rs.getString("PIEZAS");
                fila[3] = rs.getString("FECHA");
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
            String numeroCaja = jTextField1.getText();
            String idcaja = jTextField2.getText();
            String piezas = jTextField3.getText();
            
             // Obtener la fecha del jDateChooser1
        Date fechaDate = jDateChooser1.getDate();
        // Convertir la fecha a un objeto java.sql.Date
        java.sql.Date fechaSQL = new java.sql.Date(fechaDate.getTime());

            // Consulta SQL para insertar los datos en la base de datos
            String sql = "INSERT INTO COMPRA_MERCANCIA (NUMERO_CAJA, ID_CAJA, PIEZAS, FECHA) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            // Asignar los valores a la consulta
            stmt.setString(1, numeroCaja);
            stmt.setString(2, idcaja);
            stmt.setString(3, piezas);
            stmt.setDate(4, fechaSQL);
            // Ejecutar la consulta
            stmt.executeUpdate();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos guardados correctamente.");
            
            // Recargar los datos en la tabla
            cargarTablaInvenatrios(jTable1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + ex.getMessage());
        }
    }
    
    private void borrarInventario() {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Por favor, selecciona un registro para borrar.");
        return;
    }

    String numeroCaja = jTable1.getValueAt(selectedRow, 0).toString();

    try {
        // Consulta SQL para borrar el registro de la base de datos
        String sql = "DELETE FROM COMPRA_MERCANCIA WHERE NUMERO_CAJA = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, numeroCaja);
        // Ejecutar la consulta
        stmt.executeUpdate();

        // Mostrar mensaje de éxito
        JOptionPane.showMessageDialog(null, "Registro borrado correctamente.");
        
        // Recargar los datos en la tabla
        cargarTablaInvenatrios(jTable1);

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al borrar el registro: " + ex.getMessage());
    }
}
    
    private void EditarRegistro(){
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un registro para editar.");
        return;
    }

    try {
        // Obtener los datos modificados de los campos
        String numeroCaja = jTextField1.getText();
        String idcaja = jTextField2.getText();
        String piezas = jTextField3.getText();
        
        // Obtener la fecha del JDateChooser
        Date fechaDate = jDateChooser1.getDate();
        java.sql.Date fechaSQL = new java.sql.Date(fechaDate.getTime());
        
        // Consulta SQL para actualizar los datos en la base de datos
        String sql = "UPDATE COMPRA_MERCANCIA SET NUMERO_CAJA = ?, ID_CAJA = ?, PIEZAS = ?, FECHA = ? WHERE NUMERO_CAJA = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        // Asignar los valores a la consulta
        stmt.setString(1, numeroCaja);
        stmt.setString(2, idcaja);
        stmt.setString(3, piezas);
        stmt.setDate(4, fechaSQL);
        stmt.setString(5, jTable1.getValueAt(selectedRow, 0).toString()); // Suponiendo que el ID está en la columna 4
        // Ejecutar la consulta
        stmt.executeUpdate();

        // Mostrar mensaje de éxito
        JOptionPane.showMessageDialog(null, "Datos actualizados correctamente.");
        
        // Recargar los datos en la tabla
        cargarTablaInvenatrios(jTable1);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al actualizar los datos: " + ex.getMessage());
    }
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mercansia");

        jLabel1.setText("Mercancia");

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

        jLabel2.setText("Numero De Caja");

        jLabel3.setText("ID Caja");

        jLabel4.setText("Piezas");

        jLabel5.setText("Fecha");

        jButton1.setText("REGRESAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Guardar");
        jButton2.setToolTipText("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnBorrar.setText("borrar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnEditar.setText("editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(262, 262, 262)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnEditar)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField2)
                                        .addComponent(jTextField3)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(12, 12, 12)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGap(101, 101, 101))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBorrar)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(38, 38, 38)
                                .addComponent(jButton2)
                                .addGap(4, 4, 4)
                                .addComponent(btnEditar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBorrar))
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Menu mn = new Menu(tipoUsuario);
        mn.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       guardarInventario();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        borrarInventario();
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int selectedRow = jTable1.getSelectedRow();

         jTextField1.setText(jTable1.getValueAt(selectedRow,0).toString());
         jTextField2.setText(jTable1.getValueAt(selectedRow,1).toString());
         jTextField3.setText(jTable1.getValueAt(selectedRow,2).toString());
         String fechaString = jTable1.getValueAt(selectedRow, 3).toString();
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

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
       EditarRegistro();
    }//GEN-LAST:event_btnEditarActionPerformed

    
    
    
    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mercancia_Compra("Administrador").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
