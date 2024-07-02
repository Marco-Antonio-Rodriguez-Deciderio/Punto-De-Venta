
package proyectogestion;

import com.toedter.calendar.JDateChooser;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

    public class CorteDeCaja extends javax.swing.JFrame {

        private Connection con;
        private String tipoUsuario;

        public CorteDeCaja(String tipoUsuario) {
            this.tipoUsuario = tipoUsuario;
            initComponents();
            conectar();
            cargarTablaVenta(jTable1);
            cargarTablaDetalleVenta(jTable2);
            DATE.setVisible(false);
            DATE2.setVisible(false);


        }

    private void conectar() {
            try {
                // Establece la conexión a la base de datos MySQL
                con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/proyectogestionedith", "root", "12345");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
            }
        }

    private void cargarTablaVenta(javax.swing.JTable tabla) {
             try {
                if (con == null) {
                    JOptionPane.showMessageDialog(null, "Conexión a la base de datos no establecida.");
                    return;
                }
                // Consulta SQL para obtener los datos de la tabla de ventas
                String sql = "SELECT * FROM venta";
                // Preparar la consulta
                PreparedStatement stmt = con.prepareStatement(sql);
                // Ejecutar la consulta
                ResultSet rs = stmt.executeQuery();
                // Crear un modelo de tabla para el JTable
                DefaultTableModel modelo = new DefaultTableModel();
                // Agregar columnas al modelo
                modelo.addColumn("ID Venta");
                modelo.addColumn("Correo Cliente");
                modelo.addColumn("Total");
                modelo.addColumn("Fecha");
                // Agregar filas al modelo con los datos recuperados de la base de datos
                while (rs.next()) {
                    Object[] fila = new Object[4];
                    fila[0] = rs.getInt("ID_VENTA");
                    fila[1] = rs.getString("CORREO_CLIENTE");
                    fila[2] = rs.getBigDecimal("TOTAL");
                    fila[3] = rs.getDate("FECHA");
                    modelo.addRow(fila);
                }
                // Establecer el modelo de tabla en el JTable
                tabla.setModel(modelo);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla de ventas: " + ex.getMessage());
            }
        }
    
    private void cargarTablaDetalleVenta(javax.swing.JTable tabla) {
             try {
                if (con == null) {
                    JOptionPane.showMessageDialog(null, "Conexión a la base de datos no establecida.");
                    return;
                }
                // Consulta SQL para obtener los datos de la tabla de ventas
                String sql = "SELECT * FROM DETALLE_VENTA";
                // Preparar la consulta
                PreparedStatement stmt = con.prepareStatement(sql);
                // Ejecutar la consulta
                ResultSet rs = stmt.executeQuery();
                // Crear un modelo de tabla para el JTable
                DefaultTableModel modelo = new DefaultTableModel();
                // Agregar columnas al modelo
                modelo.addColumn("ID Detalle");
                modelo.addColumn("ID Venta");
                modelo.addColumn("ID Producto");
                modelo.addColumn("Nombre Producto");
                modelo.addColumn("Cantidad");
                modelo.addColumn("Precio");
                modelo.addColumn("Total");
                modelo.addColumn("Descuento");
                // Agregar filas al modelo con los datos recuperados de la base de datos
                while (rs.next()) {
                    Object[] fila = new Object[8];
                    fila[0] = rs.getInt("ID_DETALLE_VENTA");
                    fila[1] = rs.getInt("ID_VENTA");
                    fila[2] = rs.getInt("ID_PRODUCTO");
                    fila[3] = rs.getString("NOMBRE_PRODUCTO");
                    fila[4] = rs.getInt("CANTIDAD");
                    fila[5] = rs.getBigDecimal("PRECIO");
                    fila[6] = rs.getBigDecimal("TOTAL");
                    fila[7] = rs.getInt("DESCUENTO");
                    modelo.addRow(fila);
                }
                // Establecer el modelo de tabla en el JTable
                tabla.setModel(modelo);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla de ventas: " + ex.getMessage());
            }
        }
    
     private void buscarVentasPorFecha() {
        try {
            if (con == null) {
                JOptionPane.showMessageDialog(null, "Conexión a la base de datos no establecida.");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaInicio = sdf.format(DATE.getDate());
            String fechaFin = sdf.format(DATE2.getDate());
            String sql = "SELECT * FROM venta WHERE FECHA BETWEEN ? AND ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID Venta");
            modelo.addColumn("Correo Cliente");
            modelo.addColumn("Total");
            modelo.addColumn("Fecha");
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt("ID_VENTA");
                fila[1] = rs.getString("CORREO_CLIENTE");
                fila[2] = rs.getBigDecimal("TOTAL");
                fila[3] = rs.getDate("FECHA");
                modelo.addRow(fila);
            }
            jTable1.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar los datos de la tabla de ventas: " + ex.getMessage());
        }
    }

     private void guardarPDF() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                int y = 20;
                g2d.drawString("Corte de Caja", 0, y);
                y += 15;
                graphics.drawString("-----------------------------------------------------------------------------------------------------------------", 0, y);
                y += 15;
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                double totalVentas = 0.0;
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    String linea = "";
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        if (j == 2) { // Columna "Total"
                            totalVentas += Double.parseDouble(modelo.getValueAt(i, j).toString());
                        }
                        linea += modelo.getColumnName(j) + ": " + modelo.getValueAt(i, j).toString() + " ";
                    }
                    g2d.drawString(linea, 10, y);
                    y += 15;
                }
                graphics.drawString("-----------------------------------------------------------------------------------------------------------------", 0, y);
                y += 15;
                g2d.drawString("Total de ventas:$ " + totalVentas, 250, y);
                return PAGE_EXISTS;
            }
        });

        try {
            printerJob.print();
            JOptionPane.showMessageDialog(null, "PDF generado exitosamente.");
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + ex.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        check = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        DATE = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        DATE2 = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Corte De Caja");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jButton1.setText("Regresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

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
        jScrollPane1.setViewportView(jTable1);

        check.setText("Seleccionar Por Fecha");
        check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkActionPerformed(evt);
            }
        });

        jButton2.setText("Imprimir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

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
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(check))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DATE2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DATE, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jButton1)
                        .addGap(32, 32, 32)
                        .addComponent(check)
                        .addGap(29, 29, 29)
                        .addComponent(DATE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(DATE2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Menu mn = new Menu(tipoUsuario);
        mn.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
 
        
    }//GEN-LAST:event_formWindowActivated

    private void checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkActionPerformed
     if (check.isSelected()) {
     DATE.setVisible(true);
     DATE2.setVisible(true);
    } else {
        DATE.setVisible(false);
        DATE2.setVisible(false);
    }
    }//GEN-LAST:event_checkActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        buscarVentasPorFecha();
        guardarPDF();
            
    }//GEN-LAST:event_jButton2ActionPerformed


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CorteDeCaja("Administrador").setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser DATE;
    private com.toedter.calendar.JDateChooser DATE2;
    private javax.swing.JCheckBox check;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
