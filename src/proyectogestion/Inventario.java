
package proyectogestion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



public class Inventario extends javax.swing.JFrame {
    private Connection con;
    private String tipoUsuario;
    
    public Inventario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        initComponents();
        conectar();
        cargarTablaInvenatrios(jTable2);
        jComboBox1.setSelectedIndex(-1);
        jComboBox2.setSelectedIndex(-1);
        configurarPermisos();
      

    }
    
     private void configurarPermisos() {
        if ("Empleado".equals(tipoUsuario)) {
            btnEditar.setEnabled(false);
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
            String sql = "SELECT * FROM INVENTARIOS";
            // Preparar la consulta
            PreparedStatement stmt = con.prepareStatement(sql);
            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();
            // Crear un modelo de tabla para el JTable
            DefaultTableModel modelo = new DefaultTableModel();
            // Agregar columnas al modelo
            modelo.addColumn("ID Producto");
            modelo.addColumn("Nombre Producto");
            modelo.addColumn("Marca");
            modelo.addColumn("Talla");
            modelo.addColumn("Tipo");
            modelo.addColumn("Proveedor");
            modelo.addColumn("Existencia");
            modelo.addColumn("Precio Unitario");
            // Agregar filas al modelo con los datos recuperados de la base de datos
            while (rs.next()) {
                Object[] fila = new Object[8];
                fila[0] = rs.getString("ID_PRODUCTO");
                fila[1] = rs.getString("NOMBRE_PRODUCTO");
                fila[2] = rs.getString("MARCA");
                fila[3] = rs.getString("TALLA");
                fila[4] = rs.getString("TIPO");
                fila[5] = rs.getString("PROVEEDOR");
                fila[6] = rs.getBigDecimal("EXISTENCIA");
                fila[7] = rs.getBigDecimal("PRECIO_UNITARIO");
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
            String idProducto = jTextField1.getText();
            String nombreProducto = jTextField2.getText();
            String marca = jTextField3.getText();
            String talla = (String) jComboBox2.getSelectedItem();
            String tipo = (String) jComboBox1.getSelectedItem();
            String proveedor = jTextField6.getText();
            int existencia = Integer.parseInt(jTextField7.getText());
            double precioUnitario = Double.parseDouble(jTextField8.getText());

            // Lógica para guardar como administrador
            String sql = "INSERT INTO INVENTARIOS (ID_PRODUCTO, NOMBRE_PRODUCTO, MARCA, TALLA, TIPO, PROVEEDOR, EXISTENCIA, PRECIO_UNITARIO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, idProducto); // ID_PRODUCTO
            stmt.setString(2, nombreProducto); // NOMBRE_PRODUCTO
            stmt.setString(3, marca); // MARCA
            stmt.setString(4, talla); // TALLA
            stmt.setString(5, tipo); // TIPO
            stmt.setString(6, proveedor); // PROVEEDOR
            stmt.setInt(7, existencia); // EXISTENCIA
            stmt.setDouble(8, precioUnitario); // PRECIO_UNITARIO   

            // Ejecutar la consulta
            stmt.executeUpdate();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos guardados correctamente.");

            // Recargar los datos en la tabla
            cargarTablaInvenatrios(jTable2);

            limpiarCasillas();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + ex.getMessage());
        }
    }
    
    private void borrarInventario() {
    int selectedRow = jTable2.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecciona un registro para borrar.");
            return;
        }

        String idProducto = jTable2.getValueAt(selectedRow, 0).toString();

        try {
            // Consulta SQL para borrar el registro de la base de datos
            String sql = "DELETE FROM INVENTARIOS WHERE ID_PRODUCTO = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, idProducto);
            // Ejecutar la consulta
            stmt.executeUpdate();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(null, "Registro borrado correctamente.");

            // Recargar los datos en la tabla
            cargarTablaInvenatrios(jTable2);

            limpiarCasillas();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al borrar el registro: " + ex.getMessage());
        }
}
    
   private void EditarInventario() {
  int selectedRow = jTable2.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro para editar.");
            return;
        }

        try {
            // Obtener los valores de los campos desde los JTextField
            String idProducto = jTextField1.getText();
            String nombreProducto = jTextField2.getText();
            String marca = jTextField3.getText();
            String talla = (String) jComboBox2.getSelectedItem();
            String tipo = (String) jComboBox1.getSelectedItem();
            String proveedor = jTextField6.getText();
            int existencia = Integer.parseInt(jTextField7.getText());
            double precioUnitario = Double.parseDouble(jTextField8.getText());

            // Lógica para editar como administrador
            String sql = "UPDATE INVENTARIOS SET NOMBRE_PRODUCTO = ?, MARCA = ?, TALLA = ?, TIPO = ?, PROVEEDOR = ?, EXISTENCIA = ?, PRECIO_UNITARIO = ? WHERE ID_PRODUCTO = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nombreProducto); // NOMBRE_PRODUCTO
            stmt.setString(2, marca); // MARCA
            stmt.setString(3, talla); // TALLA
            stmt.setString(4, tipo); // TIPO
            stmt.setString(5, proveedor); // PROVEEDOR
            stmt.setInt(6, existencia); // EXISTENCIA
            stmt.setDouble(7, precioUnitario); // PRECIO_UNITARIO   
            stmt.setString(8, idProducto); // ID_PRODUCTO

            // Ejecutar la consulta
            stmt.executeUpdate();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(null, "Registro editado correctamente.");

            // Recargar los datos en la tabla
            cargarTablaInvenatrios(jTable2);

            limpiarCasillas();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al editar el registro: " + ex.getMessage());
        }
}
   
   private void limpiarCasillas() {
     
         jTextField1.setText("");
         jTextField2.setText("");
         jTextField3.setText("");
         jComboBox2.setSelectedIndex(-1);
         jComboBox1.setSelectedIndex(-1);
         jTextField6.setText("");
         jTextField7.setText("");
         jTextField8.setText("");
     }
   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inventarios");

        jLabel1.setText("Inventario");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        btnGuardar.setText("guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnEditar.setText("editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnBorrar.setText("borrar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        jLabel2.setText("id producto");

        jLabel3.setText("nombre producto");

        jLabel4.setText("marca");

        jLabel5.setText("talla");

        jLabel6.setText("tipo");

        jLabel7.setText("proveedor");

        jLabel8.setText("existencia");

        jLabel9.setText("precio unitario");
        jLabel9.setToolTipText("");

        jButton1.setText("REGRESAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FEMENINO", "MASCULINO", "UNISEX" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "S", "M", "L", "XL" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addComponent(btnBorrar))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(50, 50, 50)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(28, 28, 28)
                                                    .addComponent(jLabel3)))
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(9, 9, 9))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField6)
                                    .addComponent(jTextField7)
                                    .addComponent(jTextField8)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jButton1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar)
                            .addComponent(btnEditar))
                        .addGap(61, 61, 61)
                        .addComponent(btnBorrar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarInventario();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
    borrarInventario();        
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
       EditarInventario();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        int selectedRow = jTable2.getSelectedRow();
        
         jTextField1.setText(jTable2.getValueAt(selectedRow,0).toString());
         jTextField2.setText(jTable2.getValueAt(selectedRow,1).toString());
         jTextField3.setText(jTable2.getValueAt(selectedRow,2).toString());
         jComboBox2.setSelectedItem(jTable2.getValueAt(selectedRow,3).toString());
         jComboBox1.setSelectedItem(jTable2.getValueAt(selectedRow,4).toString());
         jTextField6.setText(jTable2.getValueAt(selectedRow,5).toString());
         jTextField7.setText(jTable2.getValueAt(selectedRow,6).toString());
         jTextField8.setText(jTable2.getValueAt(selectedRow,7).toString());
         
        
        

   
        
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Menu mn = new Menu(tipoUsuario);
        mn.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed
    
   
    public static void main(String args[]) {
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inventario("Administrador").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
