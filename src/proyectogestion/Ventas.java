package proyectogestion;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
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

public class Ventas extends javax.swing.JFrame {

    private Connection con;
    private String tipoUsuario;

    public Ventas(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        initComponents();
        conectar();
        // Carga los datos de la tabla de ventas en el JTable
        cargarTablaVentas(jTable1);
        configurarTablaEliminar(jTable2);

    }

    public String getSubtotalText() {
        return LBSubtotal.getText();
    }

    public String getDescuentoText() {
        return LBDescuento.getText();
    }

    private void conectar() {
        try {
            // Establece la conexión a la base de datos MySQL
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/proyectogestionedith", "root", "12345");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
        }
    }

    private void cargarTablaVentas(JTable tabla) {
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
            modelo.addColumn("ID Poducto");
            modelo.addColumn("Nombre producto");
            modelo.addColumn("Marca");
            modelo.addColumn("Talla");
            modelo.addColumn("Tipo");
            modelo.addColumn("Existencia");
            modelo.addColumn("Precio Unitario");
            // Agregar filas al modelo con los datos recuperados de la base de datos
            while (rs.next()) {
                Object[] fila = new Object[7];
                fila[0] = rs.getString("ID_PRODUCTO");
                fila[1] = rs.getString("NOMBRE_PRODUCTO");
                fila[2] = rs.getString("MARCA");
                fila[3] = rs.getString("TALLA");
                fila[4] = rs.getString("TIPO");
                fila[5] = rs.getBigDecimal("EXISTENCIA");
                fila[6] = rs.getDouble("PRECIO_UNITARIO");
                modelo.addRow(fila);
            }
            // Establecer el modelo de tabla en el JTable
            tabla.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla de ventas: " + ex.getMessage());
        }
    }

    private void configurarTablaEliminar(JTable tabla) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Solo la columna "Eliminar" es editable
            }
        };

        modelo.addColumn("Eliminar");
        modelo.addColumn("Producto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio");
        modelo.addColumn("Importe");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("Descuento");

        tabla.setModel(modelo);
        tabla.getColumnModel().getColumn(0).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(0).setCellEditor(new ButtonEditor(new JButton("Eliminar")));
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Eliminar" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;

        public ButtonEditor(JButton button) {
            this.button = new JButton();
            this.button.setOpaque(true);
            this.button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Eliminar" : value.toString();
            this.button.setText(label);
            isPushed = true;
            selectedRow = row;
            return this.button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Acción al presionar el botón
                int response = JOptionPane.showConfirmDialog(null, "¿Seguro que desea eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    ((DefaultTableModel) jTable2.getModel()).removeRow(selectedRow);
                    actualizarSubtotal();
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
        }
    }

    private void limpiarCasillas() {

        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField6.setText("");
        jTextField7.setText("");

    }

    public void actualizarSubtotal() {
        double subtotal = 0.0;
        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            double precio = (double) modelo.getValueAt(i, 3); // Obtener el precio original
            double cantidad = (int) modelo.getValueAt(i, 2); // Obtener la cantidad
            double importe = cantidad * precio; // Calcular el importe sin descuento
            double porcentajeDescuento = 0.0; // Inicializar el porcentaje de descuento en 0
            if (modelo.getValueAt(i, 7) != null) { // Verificar si hay un porcentaje de descuento aplicado
                porcentajeDescuento = (double) modelo.getValueAt(i, 7); // Obtener el porcentaje de descuento
            }
            if (porcentajeDescuento > 0) {
                double descuento = importe * (porcentajeDescuento / 100); // Calcular el descuento
                importe -= descuento; // Restar el descuento al importe
            }
            subtotal += importe; // Sumar el importe al subtotal
        }
        LBSubtotal.setText(String.format("Total: $%.2f", subtotal)); // Actualizar el JLabel
    }

    public void actualizarDescuento() {
        double descuentoTotal = 0.0;
        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            double precio = (double) modelo.getValueAt(i, 3); // Obtener el precio original
            double cantidad = (int) modelo.getValueAt(i, 2); // Obtener la cantidad
            double importe = cantidad * precio; // Calcular el importe sin descuento
            double porcentajeDescuento = 0.0; // Inicializar el porcentaje de descuento en 0
            if (modelo.getValueAt(i, 7) != null) { // Verificar si hay un porcentaje de descuento aplicado
                porcentajeDescuento = (double) modelo.getValueAt(i, 7); // Obtener el porcentaje de descuento
            }
            if (porcentajeDescuento > 0) {
                double descuento = importe * (porcentajeDescuento / 100); // Calcular el descuento
                descuentoTotal += descuento; // Sumar el descuento al total de descuentos
            }
        }
        LBDescuento.setText(String.format("Descuento: $%.2f", descuentoTotal)); // Actualizar el JLabel de descuento
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
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnRestar = new javax.swing.JButton();
        btnSumar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        btnGuardarVenta = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        LBDescuento = new javax.swing.JLabel();
        btnCobrar = new javax.swing.JButton();
        LBSubtotal = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ventas");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Ventas");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 134, -1));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 550, 160));
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 150, -1));
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 150, -1));
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 150, -1));

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 150, -1));
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 150, -1));
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 150, -1));
        getContentPane().add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 150, -1));

        jButton1.setText("Regresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel2.setText("id producto");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, -1, -1));

        jLabel3.setText("nombre producto");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, 20));

        jLabel4.setText("marca");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, -1, 20));

        jLabel5.setText("talla");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, -1, -1));

        jLabel6.setText("tipo");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, -1));

        jLabel7.setText("existencia");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, -1, -1));

        jLabel8.setText("Correo");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, -1, -1));

        btnRestar.setText("-");
        btnRestar.setToolTipText("");
        btnRestar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestarActionPerformed(evt);
            }
        });
        getContentPane().add(btnRestar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 370, 60, -1));

        btnSumar.setText("+");
        btnSumar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSumarActionPerformed(evt);
            }
        });
        getContentPane().add(btnSumar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, 60, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ELIMINAR", "PRODUCTO", "CANTIDAD", "PRECIO", "IMPORTE", "DESCUENTO", "FECHA", "HORA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, 610, 180));

        btnGuardarVenta.setText("Guardar");
        btnGuardarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarVentaActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 370, -1, -1));
        getContentPane().add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, 150, -1));

        jLabel9.setText("precio unitario");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, -1, -1));

        LBDescuento.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        LBDescuento.setText("0.00");
        getContentPane().add(LBDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 440, -1, -1));
        LBDescuento.getAccessibleContext().setAccessibleName("LBSubtotal");
        LBDescuento.getAccessibleContext().setAccessibleDescription("");

        btnCobrar.setText("Cobrar");
        btnCobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCobrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCobrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 410, -1, -1));

        LBSubtotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        LBSubtotal.setText("0.00");
        getContentPane().add(LBSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 410, -1, -1));

        jButton2.setText("Enviar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 410, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Menu mn = new Menu(tipoUsuario);
        mn.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void btnRestarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestarActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto de la tabla.");
            return;
        }

        String producto = jTable1.getValueAt(selectedRow, 1).toString();
        double precio = Double.parseDouble(jTable1.getValueAt(selectedRow, 6).toString());

        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 1).equals(producto)) {
                int cantidad = (int) modelo.getValueAt(i, 2);
                if (cantidad > 1) {
                    cantidad--;
                    double importe = cantidad * precio;
                    modelo.setValueAt(cantidad, i, 2);
                    modelo.setValueAt(importe, i, 4);
                    modelo.setValueAt(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime().toLocalDate(), i, 5);
                    modelo.setValueAt(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime().toLocalTime(), i, 6);
                } else {
                    modelo.removeRow(i);
                }
                break;
            }
        }
        actualizarSubtotal();
        actualizarDescuento();
    }//GEN-LAST:event_btnRestarActionPerformed

    Set<String> productosConDescuento = new HashSet<>();

    private void btnSumarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSumarActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto de la tabla.");
            return;
        }

        String producto = jTable1.getValueAt(selectedRow, 1).toString();
        double precio = Double.parseDouble(jTable1.getValueAt(selectedRow, 6).toString());

        // Verificar si el producto ya tiene descuento aplicado
        if (productosConDescuento.contains(producto)) {
            agregarProductoSinDescuento(producto, precio);
        } else {
            preguntarDescuento(producto, precio);
        }

        actualizarSubtotal();
        actualizarDescuento();
    }//GEN-LAST:event_btnSumarActionPerformed

    private void preguntarDescuento(String producto, double precio) {
        // Preguntar al usuario si desea agregar descuento al producto
        int opcion = JOptionPane.showConfirmDialog(null, "¿Desea agregar descuento al producto?", "Agregar Descuento", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            // Abrir una ventana emergente para agregar el descuento
            String descuentoStr = JOptionPane.showInputDialog(null, "Ingrese el descuento:", "Descuento", JOptionPane.QUESTION_MESSAGE);
            if (descuentoStr != null && !descuentoStr.isEmpty()) {
                double descuento = Double.parseDouble(descuentoStr);

                // Agregar descuento a la columna 7 (Descuento)
                DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
                Object[] nuevaFila = new Object[8];
                nuevaFila[0] = "Eliminar";
                nuevaFila[1] = producto;
                nuevaFila[2] = 1; // Cantidad inicial
                nuevaFila[3] = precio;
                nuevaFila[4] = precio; // Importe inicial
                nuevaFila[5] = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime().toLocalDate();
                nuevaFila[6] = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime().toLocalTime();
                nuevaFila[7] = descuento; // Agregar descuento a la columna 7
                modelo.addRow(nuevaFila);

                // Agregar el producto a la lista de productos con descuento aplicado
                productosConDescuento.add(producto);
            }
        } else {
            agregarProductoSinDescuento(producto, precio);
            // Agregar el producto a la lista de productos sin descuento
            productosConDescuento.add(producto);
        }
    }

    private void agregarProductoSinDescuento(String producto, double precio) {
        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        boolean found = false;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 1).equals(producto)) {
                int cantidad = (int) modelo.getValueAt(i, 2);
                cantidad++;
                double importe = cantidad * precio;
                modelo.setValueAt(cantidad, i, 2);
                modelo.setValueAt(importe, i, 4);
                modelo.setValueAt(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime().toLocalDate(), i, 5);
                modelo.setValueAt(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime().toLocalTime(), i, 6);
                found = true;
                break;
            }
        }
        if (!found) {
            Object[] nuevaFila = new Object[7];
            nuevaFila[0] = "Eliminar";
            nuevaFila[1] = producto;
            nuevaFila[2] = 1; // Cantidad inicial
            nuevaFila[3] = precio;
            nuevaFila[4] = precio; // Importe inicial
            nuevaFila[5] = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime().toLocalDate();
            nuevaFila[6] = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime().toLocalTime();
            modelo.addRow(nuevaFila);
        }
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int selectedRow = jTable1.getSelectedRow();

        jTextField1.setText(jTable1.getValueAt(selectedRow, 0).toString());
        jTextField2.setText(jTable1.getValueAt(selectedRow, 1).toString());
        jTextField3.setText(jTable1.getValueAt(selectedRow, 2).toString());
        jTextField4.setText(jTable1.getValueAt(selectedRow, 3).toString());
        jTextField5.setText(jTable1.getValueAt(selectedRow, 4).toString());
        jTextField6.setText(jTable1.getValueAt(selectedRow, 5).toString());
        jTextField7.setText(jTable1.getValueAt(selectedRow, 6).toString());


    }//GEN-LAST:event_jTable1MouseClicked

    private void btnGuardarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarVentaActionPerformed
        String correoCliente = jTextField8.getText();
        if (correoCliente.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un correo de cliente.");
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No hay productos en la tabla de ventas.");
            return;
        }

        double totalPrecio = 0.0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            double precio = (double) modelo.getValueAt(i, 4);
            Double descuentoObj = (Double) modelo.getValueAt(i, 7); // Suponiendo que el descuento está en la columna 7
            double descuento = (descuentoObj != null) ? descuentoObj : 0.0; // Si el descuento es null, usar 0.0
            double precioConDescuento = precio - (precio * descuento / 100); // Aplicar el descuento
            totalPrecio += precioConDescuento; // Sumar el precio con descuento al total
        }

        try {
            // Insertar la venta en la tabla VENTA
            String sqlVenta = "INSERT INTO VENTA (CORREO_CLIENTE, TOTAL, FECHA) VALUES (?, ?, ?)";
            PreparedStatement stmtVenta = con.prepareStatement(sqlVenta, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtVenta.setString(1, correoCliente);
            stmtVenta.setDouble(2, totalPrecio);
            stmtVenta.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmtVenta.executeUpdate();

            // Obtener el ID de la venta recién insertada
            ResultSet rsVenta = stmtVenta.getGeneratedKeys();
            int idVenta = 0;
            if (rsVenta.next()) {
                idVenta = rsVenta.getInt(1);
            }

            // Insertar los detalles de la venta en la tabla DETALLE_VENTA
            String sqlDetalle = "INSERT INTO DETALLE_VENTA (ID_VENTA, ID_PRODUCTO, NOMBRE_PRODUCTO, CANTIDAD, PRECIO, TOTAL, DESCUENTO) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtDetalle = con.prepareStatement(sqlDetalle);

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String nombreProducto = modelo.getValueAt(i, 1).toString();
                int cantidad = (int) modelo.getValueAt(i, 2);
                double precio = (double) modelo.getValueAt(i, 3);
                double total = (double) modelo.getValueAt(i, 4);
                Double descuentoObj = (Double) modelo.getValueAt(i, 7);
                double descuento = (descuentoObj != null) ? descuentoObj : 0.0; // Si el descuento es null, usar 0.0

                // Calcular el total con descuento aplicado
                double totalConDescuento = total - (total * descuento / 100);

                // Buscar el ID del producto en la tabla de inventarios
                String sqlIdProducto = "SELECT ID_PRODUCTO FROM INVENTARIOS WHERE NOMBRE_PRODUCTO = ?";
                PreparedStatement stmtIdProducto = con.prepareStatement(sqlIdProducto);
                stmtIdProducto.setString(1, nombreProducto);
                ResultSet rs = stmtIdProducto.executeQuery();
                if (rs.next()) {
                    int idProducto = rs.getInt("ID_PRODUCTO");

                    stmtDetalle.setInt(1, idVenta);
                    stmtDetalle.setInt(2, idProducto);
                    stmtDetalle.setString(3, nombreProducto);
                    stmtDetalle.setInt(4, cantidad);
                    stmtDetalle.setDouble(5, precio);
                    stmtDetalle.setDouble(6, totalConDescuento); // Insertar el total con descuento
                    stmtDetalle.setDouble(7, descuento);
                    stmtDetalle.addBatch();
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el ID del producto para " + nombreProducto);
                }
            }

            stmtDetalle.executeBatch();
            JOptionPane.showMessageDialog(null, "Venta guardada correctamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar la venta: " + ex.getMessage());
        }

    }//GEN-LAST:event_btnGuardarVentaActionPerformed

    private void btnCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCobrarActionPerformed
        Cobrar cb = new Cobrar(this);
        cb.setVisible(true);

        JTable tablaCobrar = cb.getTablaCobrar();

        transferirDatos(jTable2, tablaCobrar);
    }//GEN-LAST:event_btnCobrarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

    }//GEN-LAST:event_formWindowActivated

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
       
        String url = "https://mail.google.com/";
        
       
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            
            desktop.browse(new URI(url));
        } else {
           
            JOptionPane.showMessageDialog(this, "No se puede abrir el navegador web.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        
        JOptionPane.showMessageDialog(this, "Error al abrir la URL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    public void transferirDatos(JTable origen, JTable destino) {
        DefaultTableModel modeloOrigen = (DefaultTableModel) origen.getModel();
        DefaultTableModel modeloDestino = (DefaultTableModel) destino.getModel();

        // Limpiar la tabla destino antes de transferir los datos
        modeloDestino.setRowCount(0);

        for (int i = 0; i < modeloOrigen.getRowCount(); i++) {
            Object[] fila = new Object[5];
            fila[0] = modeloOrigen.getValueAt(i, 1); // Producto
            fila[1] = modeloOrigen.getValueAt(i, 2); // Cantidad
            fila[2] = modeloOrigen.getValueAt(i, 3); // Precio Unitario (se asume que la columna 6 contiene el precio)
            fila[3] = modeloOrigen.getValueAt(i, 4); //importe
            fila[4] = modeloOrigen.getValueAt(i, 7); //descuento
            modeloDestino.addRow(fila);
        }
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new Ventas("Administrador").setVisible(true);

            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LBDescuento;
    private javax.swing.JLabel LBSubtotal;
    private javax.swing.JButton btnCobrar;
    private javax.swing.JButton btnGuardarVenta;
    private javax.swing.JButton btnRestar;
    private javax.swing.JButton btnSumar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
