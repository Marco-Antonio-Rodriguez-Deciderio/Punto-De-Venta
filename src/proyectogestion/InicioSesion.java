package proyectogestion;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.AbstractCellEditor;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InicioSesion extends javax.swing.JFrame {

    private Connection con;

    public InicioSesion() {
        initComponents();
        conectar();

        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cierra la aplicación
            }
        });

        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cierra la ventana actual (InicioSesion)
                dispose();
                // Abre la ventana RegistrarUSUYADM
                RegistrarUSUYADM registrar = new RegistrarUSUYADM();
                registrar.setVisible(true);
            }
        });

        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = jTextField1.getText().trim();
                String contraseña = jTextField2.getText().trim();

                if (usuario.isEmpty() || contraseña.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingresa usuario y contraseña");
                    return;
                }

                try {
                    // Consulta para verificar el usuario en registro_empleado
                    String sqlEmpleado = "SELECT * FROM REGISTRO_EMPLEADO WHERE USUARIO = ? AND CONTRASEÑA = ?";
                    PreparedStatement psEmpleado = con.prepareStatement(sqlEmpleado);
                    psEmpleado.setString(1, usuario);
                    psEmpleado.setString(2, contraseña);
                    ResultSet rsEmpleado = psEmpleado.executeQuery();

                    // Si se encontró el usuario en registro_empleado
                    if (rsEmpleado.next()) {
                        JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso como empleado");
                        // Abre la ventana principal pasando el tipo de usuario
                        Menu menu = new Menu("Empleado");
                        menu.setVisible(true);
                        dispose(); // Cierra la ventana actual de inicio de sesión
                    } else {
                        // Si no se encontró en registro_empleado, verificar en registro_administrador
                        String sqlAdministrador = "SELECT * FROM REGISTRO_ADMINISTRADOR WHERE USUARIO = ? AND CONTRASEÑA = ?";
                        PreparedStatement psAdministrador = con.prepareStatement(sqlAdministrador);
                        psAdministrador.setString(1, usuario);
                        psAdministrador.setString(2, contraseña);
                        ResultSet rsAdministrador = psAdministrador.executeQuery();

                        // Si se encontró el usuario en registro_administrador
                        if (rsAdministrador.next()) {
                            JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso como administrador");
                            // Abre la ventana principal pasando el tipo de usuario
                            Menu menu = new Menu("Administrador");
                            menu.setVisible(true);
                            dispose(); // Cierra la ventana actual de inicio de sesión
                        } else {
                            // Si no se encontró en ninguna tabla
                            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                        }
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al verificar usuario: " + ex.getMessage());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("USUARIO");

        jLabel2.setText("CONTRASEÑA");

        jButton1.setText("REGISTRAR NUEVO USUARIO");

        jButton2.setText("INGRESAR");

        jButton3.setText("SALIR");

        jButton4.setText("Manual");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(46, 46, 46))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(236, 236, 236)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(192, 192, 192)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(221, 221, 221)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(jButton1)))
                .addContainerGap(194, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(43, 43, 43)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(91, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        

    String rutaPDF = "C:\\Users\\marco\\OneDrive\\Documentos\\1.pdf";
    
  
    File file = new File(rutaPDF);
    
    try {
        
        if (Desktop.isDesktopSupported()) {
            
            Desktop.getDesktop().open(file);
        } else {
            System.out.println("Apertura de archivos no soportada en este sistema.");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    }//GEN-LAST:event_jButton4ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InicioSesion().setVisible(true);
            }
        });
    }

    private void conectar() {
        try {
            // Establece la conexión a la base de datos MySQL
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/proyectogestionedith", "root", "12345");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
