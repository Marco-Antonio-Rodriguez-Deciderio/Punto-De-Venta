
package proyectogestion;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.print.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cobrar extends javax.swing.JFrame {

    private Ventas ventas;
    
    public Cobrar(Ventas ventas) {
        initComponents();
        this.ventas = ventas;
        actualizarValores();
        
     
    }
    
    
    public void calcularTotalProductos() {
        javax.swing.JTable tabla = getTablaCobrar();
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            double importe = Double.parseDouble(model.getValueAt(i, 3).toString());
            total += importe;
        }
        LBSub.setText(String.format("Subtotal: $%.2f", total)); ;
    }
    
   public void actualizarValores() {
        LBSubtotal.setText(ventas.getSubtotalText());
        LBDescuento.setText(ventas.getDescuentoText());
    }
   
    public javax.swing.JTable getTablaCobrar() {
        return jTable1;
    }
   public void generarTicketPDF() throws PrinterException, IOException {
        javax.swing.JTable tabla = getTablaCobrar();
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();

        // Crear archivo PDF
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setJobName("Ticket");

        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                int y = 20; // Posición vertical inicial

                // Encabezado con el nombre de la empresa
                graphics.drawString("CENTRAL STORE", 150, y);
                y += 20;

                // Dirección
                graphics.drawString("Del Molino 80A, Jardines de la Hacienda", 100, y);
                y += 20;
                graphics.drawString("Cuautitlán Izcalli, 54720 México, Méx", 100, y);
                y += 20;

                // Hora de impresión
                String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                graphics.drawString("Hora de impresión: " + fechaHora, 100, y);
                y += 20;
                   
                graphics.drawString("-----------------------------------------------------------", 100, y);
                y += 20;

                // Detalles de los productos
                for (int i = 0; i < model.getRowCount(); i++) {
                    String producto = model.getValueAt(i, 0).toString();
                    String cantidad = model.getValueAt(i, 1).toString();
                    String precio = model.getValueAt(i, 2).toString();
                    String linea = producto + " - Cantidad: " + cantidad + " - Precio: $" + precio;
                    graphics.drawString(linea, 100, y);
                    y += 20;
                }

                // Separador
                graphics.drawString("-----------------------------------------------------------", 100, y);
                y += 20;

                // Subtotal, descuento y total
                String subtotal = LBSub.getText();
                String descuento = LBDescuento.getText();
                String total = LBSubtotal.getText();
                graphics.drawString(subtotal, 100, y);
                y += 20;
                graphics.drawString(descuento, 100, y);
                y += 20;
                graphics.drawString(total, 100, y);
                y += 20;
                
                graphics.drawString("-----------------------------------------------------------", 100, y);
                y += 20;
                
                String texto = "Guarde El Ticket Para Cualquior Cambio O Devolucion";
                graphics.drawString(texto, 100, y);
                
                
                

                return Printable.PAGE_EXISTS;
            }
        });
        if (printerJob.printDialog()) {
            printerJob.print();

            // Guardar el archivo PDF
            FileOutputStream fos = new FileOutputStream(new File("ticket.pdf"));
            fos.write("Ticket generado mediante Java Print API.".getBytes());
            fos.close();

            JOptionPane.showMessageDialog(null, "Ticket guardado como PDF.");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        LBDescuento = new javax.swing.JLabel();
        LBSubtotal = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        LBSub = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabel1.setText("Cobrar");

        jLabel3.setText("PRODUCTOS");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Precio", "Importe", "Descuento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        LBDescuento.setText("0.00");

        LBSubtotal.setText("0.00");

        jButton1.setText("TERMINAR COBRO");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        LBSub.setText("0.00");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(222, 222, 222))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(232, 232, 232)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LBSub, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LBDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LBSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(LBSub)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LBDescuento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LBSubtotal)
                .addGap(83, 83, 83)
                .addComponent(jButton1)
                .addGap(117, 117, 117))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
           
   actualizarValores();
   calcularTotalProductos();     

            
    }//GEN-LAST:event_formWindowActivated

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         try {
            generarTicketPDF();
        } catch (PrinterException | IOException e) {
            JOptionPane.showMessageDialog(this, "Error al generar el ticket PDF: " + e.getMessage());
        }
         
         this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

  
    public static void main(String args[]) {
    
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Ventas ventas = new Ventas("Administrador");
                ventas.setVisible(true);
                new Cobrar(ventas).setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LBDescuento;
    private javax.swing.JLabel LBSub;
    private javax.swing.JLabel LBSubtotal;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
