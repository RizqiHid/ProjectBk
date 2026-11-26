/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package BK_SMKN1Dlanggu;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.ButtonGroup;
import java.text.ParseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author 16_Devin_X-RPL-3#
 */
public class siswa extends javax.swing.JFrame {
    private DefaultTableModel model;
    
      private void tampilData() {

    model.setRowCount(0);

    String sql = "SELECT * FROM siswa";
    try (Connection conn = Koneksi.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            Object[] data = {
                rs.getString("nisn"),
                rs.getString("nama"),
                rs.getString("jurusan"),
                rs.getString("tingkat") + " " +
                    rs.getString("rombel"),
                rs.getDate("tanggal_lahir"),
                rs.getString("kota_kab"),
                rs.getString("kecamatan"),
                rs.getString("desa"),
                rs.getString("dusun_jalan")
            };

            model.addRow(data);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Gagal menampilkan data: " + e.getMessage());
    }
}
      private void tambahData() {

  String nisn = id_siswa.getText().trim();

if (!nisn.matches("[0-9]{10}")) {
    JOptionPane.showMessageDialog(this,
            "NISN harus terdiri dari 10 angka!");
    id_siswa.requestFocus();
    return;
}
    String nama = txt_nama.getText().trim();

    String jurusan = cmb_kelas.getSelectedItem().toString();

    String tingkat = "";
    String rombel = "";

    // Mengambil pilihan angka
    if (jRadioButton1.isSelected()) rombel = "1";
    else if (jRadioButton2.isSelected()) rombel = "2";
    else if (jRadioButton3.isSelected()) rombel = "3";

    // Mengambil pilihan tingkat
    if (jRadioButton4.isSelected()) tingkat = "X";
    else if (jRadioButton5.isSelected()) tingkat = "XI";
    else if (jRadioButton6.isSelected()) tingkat = "XII";

    // Validasi input
    if (nisn.isEmpty() || nama.isEmpty()
            || tingkat.isEmpty() || rombel.isEmpty()
            || jDateChooser1.getDate() == null) {

        JOptionPane.showMessageDialog(this,
                "Lengkapi semua data terlebih dahulu!");
        return;
    }

    String sql = "INSERT INTO siswa "
            + "(nisn, nama, jurusan, tingkat, rombel, "
            + "tanggal_lahir, kota_kab, kecamatan, desa, dusun_jalan) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = Koneksi.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database tidak terhubung!");
            return;
        }

        ps.setString(1, nisn);
        ps.setString(2, nama);
        ps.setString(3, jurusan);
        ps.setString(4, tingkat);
        ps.setInt(5, Integer.parseInt(rombel));

        Date tanggal = jDateChooser1.getDate();

        ps.setDate(6, new java.sql.Date(tanggal.getTime()));

        ps.setString(7, jComboBox1.getSelectedItem().toString());
        ps.setString(8, jComboBox2.getSelectedItem().toString());
        ps.setString(9, jComboBox3.getSelectedItem().toString());
        ps.setString(10, jTextField2.getText());

        ps.executeUpdate();

        JOptionPane.showMessageDialog(this,
                "Data siswa berhasil ditambahkan!");

        tampilData();
        kosongkanForm();

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(this,
                "Gagal menambahkan data: " + e.getMessage());
    }
}
      
      private void kosongkanForm() {

    id_siswa.setText("");
    txt_nama.setText("");

    cmb_kelas.setSelectedIndex(0);

    jDateChooser1.setDate(null);

    jComboBox1.setSelectedIndex(0);
    jComboBox2.setSelectedIndex(0);
    jComboBox3.setSelectedIndex(0);

    jTextField2.setText("");

    grupResetAngka();
}
      
      private void grupResetAngka() {

    jRadioButton1.setSelected(false);
    jRadioButton2.setSelected(false);
    jRadioButton3.setSelected(false);

    jRadioButton4.setSelected(false);
    jRadioButton5.setSelected(false);
    jRadioButton6.setSelected(false);
}
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(siswa.class.getName());

     
   
    public siswa() {
    initComponents();

    // Group tingkat kelas
    ButtonGroup groupTingkat = new ButtonGroup();
    groupTingkat.add(jRadioButton1);
    groupTingkat.add(jRadioButton2);
    groupTingkat.add(jRadioButton3);

    // Group nomor kelas
    ButtonGroup groupNomor = new ButtonGroup();
    groupNomor.add(jRadioButton4);
    groupNomor.add(jRadioButton5);
    groupNomor.add(jRadioButton6);

    model = new DefaultTableModel();

    model.addColumn("NISN");
    model.addColumn("Nama");
    model.addColumn("Jurusan");
    model.addColumn("Kelas");
    model.addColumn("Tanggal Lahir");
    model.addColumn("Kota/Kab");
    model.addColumn("Kecamatan");
    model.addColumn("Desa");
    model.addColumn("Dsn/Jl");

    tabel_siswa.setModel(model);

    // Mengatur tombol berdasarkan jurusan
    cmb_kelas.addActionListener(e -> updateKelas());
    updateKelas();

    // Menampilkan data database
    tampilData();
}
    private void updateKelas() {

    Object pilihan = cmb_kelas.getSelectedItem();

    if (pilihan == null) {
        return;
    }

    String jurusan = pilihan.toString();

    if (jurusan.equals("ANM") ||
        jurusan.equals("PHT") ||
        jurusan.equals("DKV")) {

        jRadioButton6.setEnabled(false); // Tombol 3
        jRadioButton6.setSelected(false);

    } else if (jurusan.equals("RPL") ||
               jurusan.equals("TKJ") ||
               jurusan.equals("KL")) {

        jRadioButton6.setEnabled(true); // Tombol 3 bisa diklik
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton7 = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_siswa = new javax.swing.JTable();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        txt_nama = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmb_kelas = new javax.swing.JComboBox<>();
        id_siswa = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        jRadioButton7.setText("jRadioButton7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(700, 500));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Data Siswa");

        tabel_siswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NISN", "Nama", "Jurusan", "Kelas", "Tanggal Lahir", "Kota/Kab", "Kecamatan", "Desa", "Dsn/Jln"
            }
        ));
        tabel_siswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_siswaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_siswa);

        btn_tambah.setText("Tambah");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_hapus.setText("Hapus");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        jLabel2.setText("Nama ");

        jLabel3.setText("Jurusan");

        cmb_kelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RPL", "TKJ", "DKV", "PHT", "KL", "ANM" }));
        cmb_kelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_kelasActionPerformed(evt);
            }
        });

        id_siswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id_siswaActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 51, 102));
        jButton1.setText("Exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Search");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel5.setText("NISN");

        jRadioButton1.setText("X");

        jRadioButton2.setText("XI");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jRadioButton3.setText("XII");

        jRadioButton4.setText("1");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jRadioButton5.setText("2");

        jRadioButton6.setText("3");
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton6ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kota Mojokerto", "Kabupaten Mojokerto", "Kabupaten Sidoarjo" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Kota/Kab");

        jLabel7.setText("Kec.");

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel8.setText("Desa");

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel9.setText("Dsn/Jl");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel4.setText("Tanggal Lahir");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2)))
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(cmb_kelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(23, 23, 23)
                                                        .addComponent(jRadioButton4))
                                                    .addComponent(jRadioButton1))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(10, 10, 10)
                                                        .addComponent(jRadioButton5)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(jRadioButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(jRadioButton2)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jRadioButton3))))
                                            .addComponent(jTextField2)
                                            .addComponent(txt_nama)
                                            .addComponent(id_siswa)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(153, 153, 153)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(351, 351, 351)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(btn_tambah))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_hapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addGap(0, 14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(id_siswa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5))
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cmb_kelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton4)
                            .addComponent(jRadioButton5)
                            .addComponent(jRadioButton6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2)
                            .addComponent(jRadioButton3))
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_tambah)
                        .addComponent(btn_hapus)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jComboBox1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void updateKecamatan() {

    jComboBox2.removeAllItems();

    Object pilihan = jComboBox1.getSelectedItem();

    if (pilihan == null) {
        return;
    }

    String kabupaten = pilihan.toString();

    if (kabupaten.equals("Kabupaten Mojokerto")) {

        jComboBox2.addItem("Puri");
        jComboBox2.addItem("Trawas");
        jComboBox2.addItem("Dlanggu");

    } else if (kabupaten.equals("Kota Mojokerto")) {

        jComboBox2.addItem("Magersari");
        jComboBox2.addItem("Prajurit Kulon");
        jComboBox2.addItem("Kranggan");
        
    } else if (kabupaten.equals("Kabupaten Sidoarjo")) {

        jComboBox2.addItem("Tarik");
        jComboBox2.addItem("Prambon");
        jComboBox2.addItem("Krian");
    }
}
    
    private void updateDesa() {

    jComboBox3.removeAllItems();

    Object pilihan = jComboBox2.getSelectedItem();

    if (pilihan == null) {
        return;
    }

    String kecamatan = pilihan.toString();

    // KABUPATEN MOJOKERTO
    if (kecamatan.equals("Puri")) {

        jComboBox3.addItem("Mlaten");
        jComboBox3.addItem("Puri");
        jComboBox3.addItem("Tangunan");

    } else if (kecamatan.equals("Trawas")) {

        jComboBox3.addItem("Sukosari");
        jComboBox3.addItem("Tamiajeng");
        jComboBox3.addItem("Trawas");

    } else if (kecamatan.equals("Dlanggu")) {

        jComboBox3.addItem("Dlanggu");
        jComboBox3.addItem("Randegan");
        jComboBox3.addItem("Pandean");

    }

    // KOTA MOJOKERTO
    else if (kecamatan.equals("Magersari")) {

        jComboBox3.addItem("Gunung Gedangan");
        jComboBox3.addItem("Kedundung");
        jComboBox3.addItem("Wates");

    } else if (kecamatan.equals("Prajurit Kulon")) {

        jComboBox3.addItem("Surodinawan");
        jComboBox3.addItem("Prajurit Kulon");
        jComboBox3.addItem("Blooto");
        
    } else if (kecamatan.equals("Kranggan")) {

        jComboBox3.addItem("Kranggan");
        jComboBox3.addItem("Meri");
        jComboBox3.addItem("Jagalan");
        jComboBox3.addItem("Miji");
        jComboBox3.addItem("Sentanan");
        jComboBox3.addItem("Purwotengah");

    }

    // KABUPATEN SIDOARJO
    else if (kecamatan.equals("Tarik")) {

        jComboBox3.addItem("Balongmacekan");
        jComboBox3.addItem("Banjarwungu");
        jComboBox3.addItem("Gampingrowo");

    } else if (kecamatan.equals("Prambon")) {

        jComboBox3.addItem("Bendotretek");
        jComboBox3.addItem("Bulang");
        jComboBox3.addItem("Cangkringturi");

    } else if (kecamatan.equals("Krian")) {

        jComboBox3.addItem("Barengkrajan");
        jComboBox3.addItem("Jatikalang");
        jComboBox3.addItem("Kater");
    }
}
    private void cmb_kelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_kelasActionPerformed
       updateKelas();  // TODO add your handling code here:
    }//GEN-LAST:event_cmb_kelasActionPerformed

    private void id_siswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_siswaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id_siswaActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed

        tambahData();
      String nisn = id_siswa.getText().trim();

if (!nisn.matches("[0-9]{10}")) {
    JOptionPane.showMessageDialog(this,
            "NISN harus terdiri dari 10 angka!");
    id_siswa.requestFocus();
    return;
}
    String nama = txt_nama.getText().trim();
    String jurusan = cmb_kelas.getSelectedItem().toString();
    

    String kota = jComboBox1.getSelectedItem().toString();
    String kecamatan = jComboBox2.getSelectedItem().toString();
    String desa = jComboBox3.getSelectedItem().toString();
    String alamat = jTextField2.getText().trim();

    // Mengambil tingkat kelas (X, XI, XII)
    String tingkat = "";

    if (jRadioButton1.isSelected()) {
        tingkat = "X";
    } else if (jRadioButton2.isSelected()) {
        tingkat = "XI";
    } else if (jRadioButton3.isSelected()) {
        tingkat = "XII";
    }

    // Mengambil nomor kelas (1, 2, 3)
    String nomorKelas = "";

    if (jRadioButton4.isSelected()) {
        nomorKelas = "1";
    } else if (jRadioButton5.isSelected()) {
        nomorKelas = "2";
    } else if (jRadioButton6.isSelected()) {
        nomorKelas = "3";
    }

    // Validasi input
    if (nisn.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "NISN, Nama, dan Dsn/Jl harus diisi!");
        return;
    }

    if (tingkat.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Pilih tingkat kelas terlebih dahulu!");
        return;
    }

    if (nomorKelas.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Pilih nomor kelas terlebih dahulu!");
        return;
    }

    // Menggabungkan tingkat dan nomor kelas
    String kelas = tingkat + "-" + nomorKelas;

    // Mengambil tanggal lahir dari kalender
Date tanggalLahir = jDateChooser1.getDate();

if (tanggalLahir == null) {
    JOptionPane.showMessageDialog(this,
            "Tanggal lahir harus diisi!");
    return;
}
    String tanggalLahirFormat = new SimpleDateFormat("dd-MM-yyyy")
        .format(tanggalLahir);

    // Memasukkan data ke tabel
    model.addRow(new Object[]{
        nisn,
        nama,
        jurusan,
        kelas,
        tanggalLahirFormat,
        kota,
        kecamatan,
        desa,
        alamat
    });

    // Reset form setelah data berhasil ditambahkan
    id_siswa.setText("");
    txt_nama.setText("");
    jTextField2.setText("");

    jRadioButton1.setSelected(false);
    jRadioButton2.setSelected(false);
    jRadioButton3.setSelected(false);

    jRadioButton4.setSelected(false);
    jRadioButton5.setSelected(false);
    jRadioButton6.setSelected(false);

    cmb_kelas.setSelectedIndex(0);
    jComboBox1.setSelectedIndex(0);

    updateKecamatan();
    updateDesa();

    JOptionPane.showMessageDialog(this,
            "Data siswa berhasil ditambahkan!");

       // TODO add your handling code here:
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void tabel_siswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_siswaMouseClicked


    int baris = tabel_siswa.getSelectedRow();

    if (baris == -1) {
        return;
    }

    // Mengambil data dari tabel
    id_siswa.setText(model.getValueAt(baris, 0).toString());
    txt_nama.setText(model.getValueAt(baris, 1).toString());

    cmb_kelas.setSelectedItem(model.getValueAt(baris, 2).toString());

    String kelas = model.getValueAt(baris, 3).toString();

    String kota = model.getValueAt(baris, 5).toString();
    String kecamatan = model.getValueAt(baris, 6).toString();
    String desa = model.getValueAt(baris, 7).toString();
    String alamat = model.getValueAt(baris, 8).toString();

    jTextField2.setText(alamat);

    // Memisahkan tingkat dan nomor kelas
    String[] bagianKelas = kelas.split("-");

    if (bagianKelas.length == 2) {

        String tingkat = bagianKelas[0];
        String nomor = bagianKelas[1];

        // Memilih tingkat
        if (tingkat.equals("X")) {
            jRadioButton1.setSelected(true);
        } else if (tingkat.equals("XI")) {
            jRadioButton2.setSelected(true);
        } else if (tingkat.equals("XII")) {
            jRadioButton3.setSelected(true);
        }

        // Memilih nomor kelas
        if (nomor.equals("1")) {
            jRadioButton4.setSelected(true);
        } else if (nomor.equals("2")) {
            jRadioButton5.setSelected(true);
        } else if (nomor.equals("3")) {
            jRadioButton6.setSelected(true);
        }
    }

    // Mengisi ComboBox Kabupaten
    jComboBox1.setSelectedItem(kota);

    // Memperbarui kecamatan
    updateKecamatan();

    // Mengisi ComboBox Kecamatan
    jComboBox2.setSelectedItem(kecamatan);

    // Memperbarui desa
    updateDesa();

    // Mengisi ComboBox Desa
    jComboBox3.setSelectedItem(desa);
    // TODO add your handling code here:
    }//GEN-LAST:event_tabel_siswaMouseClicked

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
int baris = tabel_siswa.getSelectedRow();

if (baris == -1) {
    JOptionPane.showMessageDialog(this,
            "Pilih data yang ingin dihapus!");
    return;
}

int konfirmasi = JOptionPane.showConfirmDialog(
        this,
        "Apakah yakin ingin menghapus data ini?",
        "Konfirmasi",
        JOptionPane.YES_NO_OPTION
);

if (konfirmasi == JOptionPane.YES_OPTION) {

    String nisn = model.getValueAt(baris, 0).toString();

    String sql = "DELETE FROM siswa WHERE nisn = ?";

    try {
        Connection conn = Koneksi.getConnection();

        if (conn == null) {
            JOptionPane.showMessageDialog(this,
                    "Database tidak terhubung!");
            return;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nisn);

            int hasil = ps.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(this,
                        "Data berhasil dihapus!");

                tampilData();
                kosongkanForm();
            }
        }

        conn.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
                "Gagal menghapus data: " + e.getMessage());
    }
}
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_hapusActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        updateDesa();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
 updateKecamatan();        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

    String nisn = id_siswa.getText().trim();

    if (nisn.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Masukkan NISN yang ingin dicari!");
        return;
    }

    boolean ditemukan = false;

    for (int i = 0; i < tabel_siswa.getRowCount(); i++) {

        String nisnTabel = tabel_siswa.getValueAt(i, 0).toString();

        if (nisnTabel.equals(nisn)) {

            tabel_siswa.setRowSelectionInterval(i, i);

            tabel_siswa.scrollRectToVisible(
                    tabel_siswa.getCellRect(i, 0, true)
            );

            ditemukan = true;
            break;
        }
    }

    if (ditemukan) {

        JOptionPane.showMessageDialog(this,
                "Data siswa ditemukan!");

    } else {

        JOptionPane.showMessageDialog(this,
                "Data siswa tidak ditemukan!");
    }// TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
int baris = tabel_siswa.getSelectedRow();

if (baris == -1) {
    JOptionPane.showMessageDialog(this,
            "Pilih data siswa yang ingin diedit!");
    return;
}

String nisnLama = model.getValueAt(baris, 0).toString();

String nisn = id_siswa.getText().trim();
String nama = txt_nama.getText().trim();
String jurusan = cmb_kelas.getSelectedItem().toString();

String tingkat = "";
if (jRadioButton1.isSelected()) tingkat = "X";
else if (jRadioButton2.isSelected()) tingkat = "XI";
else if (jRadioButton3.isSelected()) tingkat = "XII";

String rombel = "";
if (jRadioButton4.isSelected()) rombel = "1";
else if (jRadioButton5.isSelected()) rombel = "2";
else if (jRadioButton6.isSelected()) rombel = "3";

if (nisn.isEmpty() || nama.isEmpty()
        || tingkat.isEmpty() || rombel.isEmpty()
        || jDateChooser1.getDate() == null) {

    JOptionPane.showMessageDialog(this,
            "Lengkapi semua data terlebih dahulu!");
    return;
}

// Validasi jumlah kelas berdasarkan jurusan
if ((jurusan.equals("DKV") || jurusan.equals("PHT")
        || jurusan.equals("ANM")) && rombel.equals("3")) {

    JOptionPane.showMessageDialog(this,
            "Jurusan tersebut hanya memiliki kelas 1 dan 2!");
    return;
}

String sql = "UPDATE siswa SET "
        + "nisn=?, nama=?, jurusan=?, tingkat=?, rombel=?, "
        + "tanggal_lahir=?, kota_kab=?, kecamatan=?, desa=?, dusun_jalan=? "
        + "WHERE nisn=?";

try {
    Connection conn = Koneksi.getConnection();

    if (conn == null) {
        JOptionPane.showMessageDialog(this,
                "Database tidak terhubung!");
        return;
    }

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nisn);
        ps.setString(2, nama);
        ps.setString(3, jurusan);
        ps.setString(4, tingkat);
        ps.setInt(5, Integer.parseInt(rombel));

        ps.setDate(6, new java.sql.Date(
                jDateChooser1.getDate().getTime()));

        ps.setString(7, jComboBox1.getSelectedItem().toString());
        ps.setString(8, jComboBox2.getSelectedItem().toString());
        ps.setString(9, jComboBox3.getSelectedItem().toString());
        ps.setString(10, jTextField2.getText().trim());

        ps.setString(11, nisnLama);

        int hasil = ps.executeUpdate();

        if (hasil > 0) {
            JOptionPane.showMessageDialog(this,
                    "Data siswa berhasil diperbarui!");

            tampilData();
            kosongkanForm();
        }
    }

    conn.close();

} catch (SQLException e) {
    JOptionPane.showMessageDialog(this,
            "Gagal mengedit data: " + e.getMessage());
}
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new siswa().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JComboBox<String> cmb_kelas;
    private javax.swing.JTextField id_siswa;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
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
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTable tabel_siswa;
    private javax.swing.JTextField txt_nama;
    // End of variables declaration//GEN-END:variables
}
