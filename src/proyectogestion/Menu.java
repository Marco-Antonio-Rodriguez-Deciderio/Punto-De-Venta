
package proyectogestion;


public class Menu extends javax.swing.JFrame {

private String tipoUsuario;

    public Menu(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        initComponents();
     
       
        
    }
  
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnMercancia = new javax.swing.JButton();
        btnVenta = new javax.swing.JButton();
        btnInventario = new javax.swing.JButton();
        btnCorteC = new javax.swing.JButton();
        btnEmpleados = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Principal");

        btnMercancia.setText("Mercancia");
        btnMercancia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMercanciaActionPerformed(evt);
            }
        });

        btnVenta.setText("Ventas");
        btnVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaActionPerformed(evt);
            }
        });

        btnInventario.setText("Inventario");
        btnInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioActionPerformed(evt);
            }
        });

        btnCorteC.setText("Corte De Caja");
        btnCorteC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorteCActionPerformed(evt);
            }
        });

        btnEmpleados.setText("Empleados");
        btnEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpleadosActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(216, 216, 216)
                        .addComponent(btnMercancia)
                        .addGap(32, 32, 32)
                        .addComponent(btnVenta)
                        .addGap(61, 61, 61)
                        .addComponent(btnInventario))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(224, 224, 224)
                        .addComponent(btnCorteC)
                        .addGap(38, 38, 38)
                        .addComponent(btnEmpleados)
                        .addGap(66, 66, 66)
                        .addComponent(btnSalir)))
                .addContainerGap(327, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(130, 130, 130)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMercancia)
                    .addComponent(btnVenta)
                    .addComponent(btnInventario))
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCorteC)
                    .addComponent(btnEmpleados)
                    .addComponent(btnSalir))
                .addContainerGap(244, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMercanciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMercanciaActionPerformed
    Mercancia_Compra mc = new Mercancia_Compra(tipoUsuario);
    mc.setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_btnMercanciaActionPerformed

    private void btnVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaActionPerformed
     Ventas vt = new Ventas(tipoUsuario);
     vt.setVisible(true);
     this.setVisible(false);
    }//GEN-LAST:event_btnVentaActionPerformed

    private void btnInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioActionPerformed
       Inventario in = new Inventario(tipoUsuario);
       in.setVisible(true);
       this.setVisible(false);
    }//GEN-LAST:event_btnInventarioActionPerformed

    private void btnCorteCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorteCActionPerformed
        CorteDeCaja cc = new CorteDeCaja(tipoUsuario);
        cc.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnCorteCActionPerformed

    private void btnEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpleadosActionPerformed
        Empleados em = new Empleados(tipoUsuario);
        em.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnEmpleadosActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        InicioSesion ine = new InicioSesion();
        this.setVisible(false);
        ine.setVisible(true);
        
    }//GEN-LAST:event_btnSalirActionPerformed

   
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
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu("Administrador").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCorteC;
    private javax.swing.JButton btnEmpleados;
    private javax.swing.JButton btnInventario;
    private javax.swing.JButton btnMercancia;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnVenta;
    // End of variables declaration//GEN-END:variables
}
